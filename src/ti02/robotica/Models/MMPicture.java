package ti02.robotica.Models;

import ti02.robotica.Logging.CurrentLogger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MMPicture {

    private int width;
    private int height;

    private Pixel bounds[]; // 0: top left, 1: bottom right

    private List<Pixel> colorPixels;

    public MMPicture(int width, int height) {
        this.width = width;
        this.height = height;

        colorPixels = new ArrayList<>();

        bounds = new Pixel[2];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Pixel> getColorPixels() {
        return colorPixels;
    }

    // List omzetten naar image
    public BufferedImage toBufferedImage() {
        BufferedImage outputImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        final Color colorEmpty = new Color(0, 0, 0, 0);
        final Color colorMM = new Color(75, 0, 75, 128);
        final Color colorEdge = new Color(255, 0, 255, 128);
        final Color colorBounds = new Color(0, 255, 255);

        int bounds[] = {-1, -1, outputImage.getHeight(), 0};    // left, right, top, down

        int totalPixelCount = outputImage.getWidth() * outputImage.getHeight();
        int pixelCount = 1;

        for (int x = 0; x < getWidth(); x++)
        {

            for (int y = 0; y < getHeight(); y++)
            {
                CurrentLogger.Logger.Debug((float)((int)(((float)pixelCount++ / (float)totalPixelCount) * 10000.0))/100.0 + "%");

                final int finalX = x;
                final int finalY = y;

                Pixel pixel = getColorPixels().stream().filter((p) -> p.getX() == finalX)
                        .filter((Pixel) -> Pixel.getY() == finalY)
                        .findFirst().orElse(null);

                Pixel pixelNorth = getColorPixels().stream().filter((p) -> p.getX() == finalX)
                        .filter((Pixel) -> Pixel.getY() == finalY - 1)
                        .findFirst().orElse(null);
                Pixel pixelSouth = getColorPixels().stream().filter((p) -> p.getX() == finalX)
                        .filter((Pixel) -> Pixel.getY() == finalY + 1)
                        .findFirst().orElse(null);
                Pixel pixelWest = getColorPixels().stream().filter((p) -> p.getX() == finalX - 1)
                        .filter((Pixel) -> Pixel.getY() == finalY)
                        .findFirst().orElse(null);
                Pixel pixelEast = getColorPixels().stream().filter((p) -> p.getX() == finalX + 1)
                        .filter((Pixel) -> Pixel.getY() == finalY)
                        .findFirst().orElse(null);

                if (pixel == null) {
                    outputImage.setRGB(x, y, colorEmpty.getRGB());

                    if (pixelNorth != null || pixelSouth != null || pixelWest != null || pixelEast != null)
                        outputImage.setRGB(x, y, colorEdge.getRGB());
                }
                else {
                    outputImage.setRGB(x, y, colorMM.getRGB());



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
        }

        for (int x = 0; x < getWidth(); x++)
        {
            for (int y = 0; y < getHeight(); y++)
            {
                // left bound
                if (x == bounds[0] && y >= bounds[2] && y < bounds[3])
                    outputImage.setRGB(x, y, colorBounds.getRGB());

                // right bound
                if (x == bounds[1] && y >= bounds[2] && y < bounds[3])
                    outputImage.setRGB(x, y, colorBounds.getRGB());

                // top bound
                if (y == bounds[2] && x >= bounds[0] && x <= bounds[1])
                    outputImage.setRGB(x, y, colorBounds.getRGB());

                // bottom bound
                if (y == bounds[3] && x >= bounds[0] && x <= bounds[1])
                    outputImage.setRGB(x, y, colorBounds.getRGB());
            }
        }

        return outputImage;
    }
}
