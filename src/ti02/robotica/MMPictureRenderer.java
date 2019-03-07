package ti02.robotica;

import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.Models.Pixel;
import ti02.robotica.Util.ImageUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Date;

public class MMPictureRenderer {

    final int colorEmpty = 0x00000000;  // AARRGGBB
    final int colorMM = 0x804b004b;

    public final int pixelsPerThread = 150;


    public BufferedImage render(MMPicture source)
    {
        BufferedImage outputImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
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

        Date end = new Date();
        long seconds = (end.getTime()-start.getTime());
        CurrentLogger.Logger.Info("Took " + seconds + "ms to complete");

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
            outputImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
            int[] outputImagePixelData = ((DataBufferInt) outputImage.getRaster().getDataBuffer()).getData();

            for (Pixel p : source.getColorPixels()) {
                outputImagePixelData[p.getY() * source.getWidth() + p.getX()] = colorMM;
            }
        }
    }
}
