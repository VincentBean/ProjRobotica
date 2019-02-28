package ti02.robotica.Models;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MMPicture {

    private int width;
    private int height;

    private List<Pixel> colorPixels;

    public MMPicture(int width, int height) {
        this.width = width;
        this.height = height;

        colorPixels = new ArrayList<>();
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
        BufferedImage outputImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        final Color colorEmpty = new Color(0, 0, 0);
        final Color colorMM = new Color(255, 0, 255);

        for (int x = 0; x < getWidth(); x++)
        {

            for (int y = 0; y < getHeight(); y++)
            {
                final int finalX = x;
                final int finalY = y;

                Pixel pixel = getColorPixels().stream().filter((p) -> p.getX() == finalX)
                        .filter((Pixel) -> Pixel.getY() == finalY)
                        .findFirst().orElse(null);

                if (pixel == null) {
                    outputImage.setRGB(x, y, colorEmpty.getRGB());
                }
                else {
                    outputImage.setRGB(x, y, colorMM.getRGB());
                }

            }
        }

        return outputImage;
    }
}
