package ti02.robotica;

import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.Models.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MMPictureRenderer {

    public BufferedImage render(MMPicture source)
    {
        BufferedImage outputImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);

        final Color colorEmpty = new Color(0, 0, 0, 0);
        final Color colorMM = new Color(75, 0, 75, 128);
        final Color colorEdge = new Color(255, 0, 255, 128);
        final Color colorBounds = new Color(0, 255, 255);

        int bounds[] = {-1, -1, outputImage.getHeight(), 0};    // left, right, top, down

        int totalPixelCount = outputImage.getWidth() * outputImage.getHeight();
        int pixelCount = 1;

        for (int x = 0; x < source.getWidth(); x++)
        {

            for (int y = 0; y < source.getHeight(); y++)
            {
                CurrentLogger.Logger.Debug((float)((int)(((float)pixelCount++ / (float)totalPixelCount) * 10000.0))/100.0 + "%");

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

        for (int x = 0; x < source.getWidth(); x++)
        {
            for (int y = 0; y < source.getHeight(); y++)
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
