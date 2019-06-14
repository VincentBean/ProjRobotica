package ti02.robotica.PhotoDetection;

import com.sun.org.apache.xpath.internal.operations.Bool;
import ti02.robotica.Logging.ConsoleLogger;
import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.Models.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.logging.Logger;

public class PictureProcessor {
    public MMPicture findMM(BufferedImage source) {

        if (source == null) {
            CurrentLogger.Logger.Error("Source image is null!");
            return null;
        }

        int blockSize = 16;     // Grootte van blur in pixels

        MMPicture mmPicture = new MMPicture(source.getWidth(), source.getHeight());
        Boolean[][] objectFound = new Boolean[source.getWidth() / blockSize][source.getHeight() / blockSize];

        final int colorMM = 0x804b004b; // AARRGGBB
        final int colorRed = 0x80FF0000;
        final int colorGreen = 0x8000FF00;
        final int colorBlue = 0x800000FF;
        final int colorBlack = 0xFF000000;

        double averageRed;
        double averageGreen;
        double averageBlue;

        // Deel breedte van beeld op in blokken
        for (int Xblock = 0; Xblock <= source.getWidth() - blockSize; Xblock += blockSize)
        {
            // Deel hoogte van beeld op in blokken
            for (int Yblock = 0; Yblock <= source.getHeight() - blockSize; Yblock += blockSize)
            {
                // Reset gemiddelde kleur bij het aankomen van een nieuwe block
                Color color = new Color(source.getRGB(Xblock, Yblock));
                averageRed = (double)color.getRed();
                averageGreen = (double)color.getGreen();
                averageBlue = (double)color.getBlue();

                // Ga door alle pixels heen in een blok
                for (int Xlocal = Xblock; Xlocal < Xblock + blockSize; Xlocal++)
                {
                    for (int Ylocal = Yblock; Ylocal < Yblock + blockSize; Ylocal++)
                    {
                        // Bereken gemiddelde kleur
                        color = new Color(source.getRGB(Xlocal, Ylocal));

                        averageRed = incrementalAverage(averageRed, (double)color.getRed());
                        averageGreen = incrementalAverage(averageGreen, (double)color.getGreen());
                        averageBlue = incrementalAverage(averageBlue, (double)color.getBlue());
                    }
                }

                // Bepaal of er een object aanwezig is in dit blok
                if (averageRed >= 5 && averageGreen >= 5 && averageBlue >= 5) {
                    objectFound[Xblock / blockSize][Yblock / blockSize] = true;
                } else {
                    objectFound[Xblock / blockSize][Yblock / blockSize] = false;
                }
            }
        }

        // Ga door alle blokken heen
        int x = 0;
        int y;

        for (Boolean[] xResult : objectFound)
        {
            y = 0;
            x++;

            for (Boolean yResult : xResult)
            {
                y++;

//                CurrentLogger.Logger.Info("X="+x+", Y="+y);

//              // Wanneer er een object aanwezig is
                if (yResult)
                {
                    drawBlock(mmPicture, blockSize, x, y, colorBlack);

//                    // North edge
//                    if (!objectFound[x][y-1]) {
//                        drawBlock(mmPicture, blockSize, x, y-1, colorRed);
//                    }
//                    // South edge
//                    if (!objectFound[x][y+1]) {
//                        drawBlock(mmPicture, blockSize, x, y+1, colorBlue);
//                    }
//                    // South edge
//                    if (!objectFound[x-1][y]) {
//                        drawBlock(mmPicture, blockSize, x-1, y, colorGreen);
//                    }
                }

                //                // Check if object is present west
//                if (x > 1) {
//                    if (objectFound[x-1][y-1]) {
//                        drawBlock(mmPicture, blockSize, x-1, y, colorGreen);
//                    }
//                }
            }
        }

        return mmPicture;
    }

    void drawBlock(MMPicture mmPicture, int blockSize, int x, int y, int color) {
        // Kleur hele blok in
        for (int xkwadraat = x * blockSize - blockSize; xkwadraat < (x*blockSize); xkwadraat++)
        {
            for (int ykwadraat = y * blockSize - blockSize; ykwadraat < (y*blockSize); ykwadraat++)
            {
//                            CurrentLogger.Logger.Info("Xkwadraat="+xkwadraat+", Ykwadraat="+ykwadraat);
                mmPicture.setPixel(xkwadraat, ykwadraat, color);

            }
        }
    }

    // Bereken gemiddelde - https://stackoverflow.com/a/16757630
    double incrementalAverage(double average, double new_value) {
        average -= average;
        average += new_value;

        return average;
    }
}
