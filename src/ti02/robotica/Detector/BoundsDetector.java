package ti02.robotica.Detector;

import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.Bounds;
import ti02.robotica.Models.DetectorResult;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.Models.Object;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

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

    public void setSource(BufferedImage source) {
        this.source = source;
    }

    public ArrayList<ti02.robotica.Models.Object> LocateObjects() {
        ArrayList objects = new ArrayList<ti02.robotica.Models.Object>();

        if (source == null) {
            CurrentLogger.Logger.Error("Source image is null!");
            return null;
        }

        int blockSize = 20;     // Grootte van blur in pixels

        MMPicture mmPicture = new MMPicture(source.getWidth(), source.getHeight());
        Boolean[][] objectFound = new Boolean[source.getWidth() / blockSize][source.getHeight() / blockSize];

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

//                // Ga door alle pixels heen in een blok
//                for (int Xlocal = Xblock; Xlocal < Xblock + blockSize; Xlocal++)
//                {
//                    for (int Ylocal = Yblock; Ylocal < Yblock + blockSize; Ylocal++)
//                    {
//                        // Bereken gemiddelde kleur
//                        color = new Color(source.getRGB(Xlocal, Ylocal));
////
////                        averageRed = incrementalAverage(averageRed, (double)color.getRed());
////                        averageGreen = incrementalAverage(averageGreen, (double)color.getGreen());
////                        averageBlue = incrementalAverage(averageBlue, (double)color.getBlue());
//                    }
//                }

                // Bepaal of er een object aanwezig is in dit blok
                if (averageRed >= 5 && averageGreen >= 5 && averageBlue >= 5) {
                    objectFound[Xblock / blockSize][Yblock / blockSize] = true;
                } else {
                    objectFound[Xblock / blockSize][Yblock / blockSize] = false;
                }
            }
        }

        // Iiterate through blocks and check if neighboring blocks also contain an object
        for (int x = 0; x < objectFound.length; x++) {
            for (int y = 0; y < objectFound[x].length; y++) {
//                CurrentLogger.Logger.Info("X = "+x+", Y="+y);
                if (objectFound[x][y]) {
                    // Create new M&M
                    Bounds bounds = new Bounds(y, x, y, x);
                    ti02.robotica.Models.Object mm = CheckNeighboringBlocks(blockSize, new ti02.robotica.Models.Object(source.getWidth(), source.getHeight()), objectFound, x, y, bounds);

                    // TODO:
                    //  - discard smaller objects (opposite bounds are closer)
                    //  - discard objects with an almost black or white average color

                    objects.add(mm);    // Add object to list of found objects
                }
            }
        }

        CurrentLogger.Logger.Info("==> Found " + objects.size() + " objects.");
        return objects;  // Return the found objects
    }

    // Check if the neighboring blocks also contain an object
    private ti02.robotica.Models.Object CheckNeighboringBlocks(int blockSize, ti02.robotica.Models.Object mm, Boolean[][] objectFound, int x, int y, Bounds bounds) {
        // Mark this block as 'not an object' so this block will be ignored in future checks
        objectFound[x][y] = false;

        // Add pixel to object
        mm.setPixel(new Color(source.getRGB(x * blockSize, y * blockSize)), x, y);

        // North neighbour
        if ((bounds.getNorth() - 1)*blockSize >= 0) {                   // Check if new bound would be inside range of source
            if (objectFound[x][y - 1]) {                                // Check if there is an object in the block north of current block
                bounds.setNorth(bounds.getNorth() - 1);                 // Increment the bound
                CheckNeighboringBlocks(blockSize, mm, objectFound, x, y - 1, bounds);   // Do the same checks for the neighboring blocks
            }
        }

        // East neighbour
        if ((bounds.getEast() + 1)*blockSize < source.getWidth()) {
            if (objectFound[x + 1][y]) {
                bounds.setEast(bounds.getEast() + 1);
                CheckNeighboringBlocks(blockSize, mm, objectFound, x + 1, y, bounds);
            }
        }

        // South neightbour
        if ((bounds.getSouth() + 1)*blockSize < source.getHeight()) {
            if (objectFound[x][y + 1]) {
                bounds.setSouth(bounds.getSouth() + 1);
                CheckNeighboringBlocks(blockSize, mm, objectFound, x, y + 1, bounds);
            }
        }

        // West neighbour
        if ((bounds.getWest() - 1)*blockSize >= 0) {
            if (objectFound[x - 1][y]) {
                bounds.setWest(bounds.getWest() - 1);
                CheckNeighboringBlocks(blockSize, mm, objectFound, x - 1, y, bounds);
            }
        }

        mm.setBounds(bounds);
        return mm;
    }
}
