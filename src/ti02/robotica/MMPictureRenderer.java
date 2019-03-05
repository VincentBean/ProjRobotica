package ti02.robotica;

import org.omg.CORBA.Current;
import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.Models.Pixel;
import ti02.robotica.Util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.Date;

public class MMPictureRenderer {

    final int colorEmpty = 0x00000000;
    final int colorMM = 0x804b004b;     // AARRGGBB
    final int colorEdge = 0x80FF00FF;
    final int ColorBounds = 0xFF00FFFF;

    public final int pixelsPerThread = 100;


    public BufferedImage render(MMPicture source)
    {
        BufferedImage outputImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);

        int bounds[] = {-1, -1, outputImage.getHeight(), 0};    // left, right, top, down

        int totalPixelCount = outputImage.getWidth() * outputImage.getHeight();
        int pixelCount = 1;

        // Create n workers to render each part of the output image
        int threadCountX = source.getWidth() / pixelsPerThread;
        int threadCountY = source.getHeight() / pixelsPerThread;
        int totalThreads = threadCountX * threadCountY;

        CurrentLogger.Logger.Debug("Starting " + totalThreads + " worker threads to render the overlay");
        Date start = new Date();


        ArrayList<PartRenderer> renderers = new ArrayList<PartRenderer>();
        ArrayList<Thread> rendererThreads = new ArrayList<Thread>();

        // Build list of part renderers
        for (int xThread = 0; xThread < threadCountX; xThread++) {
            for (int yThread = 0; yThread < threadCountY; yThread++) {

                int startX = xThread * pixelsPerThread;
                int endX = startX + pixelsPerThread;

                int startY = yThread * pixelsPerThread;
                int endY = startY + pixelsPerThread;


                PartRenderer renderer = new PartRenderer(startX, endX, startY, endY, source);

                Thread t = new Thread(renderer);

                renderers.add(renderer);
                rendererThreads.add(t);

            }
        }

        // Start threads
        CurrentLogger.Logger.Debug("Starting threads");
        for (int i = 0; i < rendererThreads.size(); i++) {
            rendererThreads.get(i).start();
        }

        // Wait for threads to finish
        CurrentLogger.Logger.Debug("Waiting for threads to finish");
        for (Thread t : rendererThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CurrentLogger.Logger.Debug("Threads finished");



        // Combine
        CurrentLogger.Logger.Debug("Combining results");
        for(PartRenderer r : renderers) {
            outputImage = ImageUtil.Combine(r.getOutputImage(), outputImage);
        }
        CurrentLogger.Logger.Debug("Done");



        // Calculate bounds
        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                // set left bound
                if (bounds[0] == -1)
                    bounds[0] = x;

                // set right bound
                bounds[1] = x;

                // set top bound
                if (y <= bounds[2])
                    bounds[2] = y;

                // set bottom bound
                if (y >= bounds[3])
                    bounds[3] = y;
            }
        }

//        // Draw bounds
//        for (int x = 0; x < source.getWidth(); x++)
//        {
//            for (int y = 0; y < source.getHeight(); y++)
//            {
//                // left bound
//                if (x == bounds[0] && y >= bounds[2] && y < bounds[3])
//                    outputImage.setRGB(x, y, colorBounds.getRGB());
//
//                // right bound
//                if (x == bounds[1] && y >= bounds[2] && y < bounds[3])
//                    outputImage.setRGB(x, y, colorBounds.getRGB());
//
//                // top bound
//                if (y == bounds[2] && x >= bounds[0] && x <= bounds[1])
//                    outputImage.setRGB(x, y, colorBounds.getRGB());
//
//                // bottom bound
//                if (y == bounds[3] && x >= bounds[0] && x <= bounds[1])
//                    outputImage.setRGB(x, y, colorBounds.getRGB());
//            }
//        }

        Date end = new Date();
        long seconds = (end.getTime()-start.getTime())/1000;
        CurrentLogger.Logger.Info("Took " + seconds + "s to complete");

        return outputImage;
    }



    private class PartRenderer implements Runnable
    {
        private int startX, endX;
        private int startY, endY;
        private MMPicture source;

        private BufferedImage outputImage;

        public PartRenderer(int startX, int endX, int startY, int endY, MMPicture source) {
            this.startX = startX;
            this.endX = endX;
            this.startY = startY;
            this.endY = endY;
            this.source = source;
        }

        @Override
        public void run() {
            renderPart();

        }

        public BufferedImage getOutputImage() {
            return outputImage;
        }

        private void renderPart()
        {
            int pixelCount = 0;
            int[] pixels = new int[source.getWidth() * source.getHeight()]; // 0xAARRGGBB

            for (int x = startX; x < endX; x++)
            {
                for (int y = startY; y < endY; y++)
                {
                    //CurrentLogger.Logger.Debug((float)((int)(((float)pixelCount++ / (float)totalPixelCount) * 10000.0))/100.0 + "%");

                    final int finalX = x;
                    final int finalY = y;

                    Pixel pixel = source.getColorPixels().stream().filter((p) -> p.getX() == finalX)
                            .filter((Pixel) -> Pixel.getY() == finalY)
                            .findFirst().orElse(null);

                    Pixel pixelNorth = source.getColorPixels().stream().filter((p) -> p.getX() == finalX)
                            .filter((Pixel) -> Pixel.getY() == finalY - 1)
                            .findFirst().orElse(null);
                    Pixel pixelSouth = source.getColorPixels().stream().filter((p) -> p.getX() == finalX)
                            .filter((Pixel) -> Pixel.getY() == finalY + 1)
                            .findFirst().orElse(null);
                    Pixel pixelWest = source.getColorPixels().stream().filter((p) -> p.getX() == finalX - 1)
                            .filter((Pixel) -> Pixel.getY() == finalY)
                            .findFirst().orElse(null);
                    Pixel pixelEast = source.getColorPixels().stream().filter((p) -> p.getX() == finalX + 1)
                            .filter((Pixel) -> Pixel.getY() == finalY)
                            .findFirst().orElse(null);

                    if (pixel == null) {
                        pixels[pixelCount] = colorEmpty;


                        if (pixelNorth != null || pixelSouth != null || pixelWest != null || pixelEast != null)
                            pixels[pixelCount] = colorEdge;
                    }
                    else {
                        pixels[pixelCount] = colorMM;
                    }

                    pixelCount++;
                }
            }

            MemoryImageSource AHHWHHAH = new MemoryImageSource(source.getWidth(), source.getHeight(), pixels, 0, source.getWidth());
            outputImage = Toolkit.getDefaultToolkit().createImage(AHHWHHAH);
        }
    }
}
