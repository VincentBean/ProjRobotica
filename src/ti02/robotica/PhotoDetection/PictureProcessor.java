package ti02.robotica.PhotoDetection;

import ti02.robotica.Models.MMPicture;
import ti02.robotica.Models.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PictureProcessor {
    public MMPicture findMM(BufferedImage source) {
        MMPicture mmPicture = new MMPicture(source.getWidth(), source.getHeight());

        for (int x = 0; x < source.getWidth(); x++)
        {
            for (int y = 0; y < source.getHeight(); y++)
            {
                Color color = new Color(source.getRGB(x, y));
                int rgb[] = {color.getRed(), color.getGreen(), color.getBlue()};

                if (rgb[0] >= 5 && rgb[1] >= 5 && rgb[2] >= 5) {
                    // Als pixel niet zwart is
                    Pixel pixel = new Pixel(x, y, rgb);
                    mmPicture.getColorPixels().add(pixel);
                }

                // Determine bounds
            }
        }

        return mmPicture;
    }
}
