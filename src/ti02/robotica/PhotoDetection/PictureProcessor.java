package ti02.robotica.PhotoDetection;

import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.Models.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PictureProcessor {
    public MMPicture findMM(BufferedImage source) {

        if (source == null) {
            CurrentLogger.Logger.Error("Source image is null!");
            return null;
        }

        MMPicture mmPicture = new MMPicture(source.getWidth(), source.getHeight());

        final int colorMM = 0x804b004b; // AARRGGBB
        final int colorRed = 0x80FF0000;
        final int colorGreen = 0x8000FF00;
        final int colorBlue = 0x800000FF;

        for (int x = 0; x < source.getWidth(); x++)
        {
            for (int y = 0; y < source.getHeight(); y++)
            {
                Color color = new Color(source.getRGB(x, y));
                int rgb[] = {color.getRed(), color.getGreen(), color.getBlue()};

                if (rgb[0] >= 127 && rgb[1] >= 127 && rgb[2] >= 127) {
                    mmPicture.setPixel(x, y, colorMM);
                }
//
//                if (rgb[0] >= 100 && rgb[0] <= 150 && rgb[1] <= 50 && rgb[2] <= 75) {
//                    mmPicture.setPixel(x, y, colorRED);
//                }


                if (rgb[0] <= 100 && rgb[1] >= 100 && rgb[2] <= 100) {
                    mmPicture.setPixel(x, y, colorGreen);
                }

                // Todo: determine bounds
            }
        }

        return mmPicture;
    }
}
