package ti02.robotica.Detector;

import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.Bounds;
import ti02.robotica.Models.DetectorResult;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.Models.Object;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BoundsDetector implements IDetector {
    private BufferedImage source;

    @Override
    public DetectorResult<Bounds> detect() {
        return null;
    }

    /**
     * Returns the name of this detector
     */
    @Override
    public String toString() {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        String name;

        if (enclosingClass != null) {
            name = enclosingClass.getName();
        } else {
            name = getClass().getName();
        }

        return name.replace("Detector", "");
    }

    public int LocateObjects() {
        ArrayList objects = new ArrayList<Object>();

        if (source == null) {
            CurrentLogger.Logger.Error("Source image is null!");
            return -1;
        }

        int blockSize = 8;     // Grootte van blur in pixels

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
//
//                        averageRed = incrementalAverage(averageRed, (double)color.getRed());
//                        averageGreen = incrementalAverage(averageGreen, (double)color.getGreen());
//                        averageBlue = incrementalAverage(averageBlue, (double)color.getBlue());
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

        // Iterate through all blocks
        // When object is present (objectFound[Xblock / blockSize][Yblock / blockSize] == true), and color is not null
        //     - Add this block to a new object, set color of this block to null
        //     - Check if object is present in all four directions, and color is not null
        //         Add blocks to object, set color of this block to null

        // Deel breedte van beeld op in blokken
        for (int Xblock = 0; Xblock <= source.getWidth() - blockSize; Xblock += blockSize) {

            // Deel hoogte van beeld op in blokken
            for (int Yblock = 0; Yblock <= source.getHeight() - blockSize; Yblock += blockSize) {
                if (objectFound[Xblock / blockSize][Yblock / blockSize] == true) {
                    objectFound[Xblock / blockSize][Yblock / blockSize] = null;
                    
                }
            }
        }



        //    // Wanneer M&M gevonden is:
        //    Object mm = new Object();
        //    mm.setPixels();
        //    objects.add(mm);

        return objects.size();  // Return the amount of found objects
    }
}
