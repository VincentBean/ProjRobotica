package ti02.robotica.PhotoDetection;

import com.sun.org.apache.xpath.internal.operations.Bool;
import jssc.SerialPort;
import ti02.robotica.Detector.BoundsDetector;
import ti02.robotica.Detector.ColorDetector;
import ti02.robotica.Detector.HardwareController;
import ti02.robotica.Logging.ConsoleLogger;
import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.Bounds;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.Models.Pixel;
import ti02.robotica.Models.Object;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class PictureProcessor {
    private int nullCount = 0;
    private HardwareController Controller;

    public PictureProcessor() {
        Controller = new HardwareController(SerialPort.BAUDRATE_115200,"/dev/ttyACM0");
        Controller.Connect();
    }

    public MMPicture findMM(BufferedImage source) {
        MMPicture mmPicture = new MMPicture(source.getWidth(), source.getHeight());

        if (source == null) {
            CurrentLogger.Logger.Error("Source image is null!");
            return null;
        }

        BoundsDetector boundsDetector = new BoundsDetector();
        ColorDetector colorDetector = new ColorDetector();
        boundsDetector.setSource(source);
        ArrayList<ti02.robotica.Models.Object> objects = boundsDetector.LocateObjects();

        // Show arrays for debugging
        for (ti02.robotica.Models.Object object : objects) {
            java.awt.Color[][] pixels = object.getPixels();
            Bounds bounds = object.getBounds();
//
//            CurrentLogger.Logger.Debug("north="+bounds.getNorth() + ", east="+bounds.getEast() + ", south="+bounds.getSouth()+", west="+bounds.getWest());
//
//            for (int x = bounds.getWest(); x < bounds.getEast(); x++) {
//                mmPicture.setPixel(x*10, bounds.getNorth()*10, Color.red.getRGB());
////                mmPicture.setPixel(x*20, bounds.getSouth()*20, Color.green.getRGB());
//            }
//            for (int y = bounds.getNorth(); y < bounds.getSouth(); y++) {
////                mmPicture.setPixel(bounds.getWest()*20, y*20, Color.red.getRGB());
////                mmPicture.setPixel(bounds.getEast()*20, y*20, Color.green.getRGB());
//            }

//            for (int x = 0; x < pixels.length; x++) {
//                for (int y = 0; y < pixels[x].length; y++) {
//                    if (pixels[x][y] != null) {
////                        System.out.print("....");
//
//                    } else {
////                        System.out.print(pixels[x][y].getRGB());
//                    }
//
////                    System.out.print("\t");
//                }
////                System.out.print("\n");
//            }
////            System.out.print("\n\n\n");
        }


        Color average = null;
        for (ti02.robotica.Models.Object object : objects) {
            Color colorResult = colorDetector.detectColor(object);

            // Set color for first time
            if (average == null) {
                average = colorResult;
                continue;
            }

            // Calculate average
            average = new java.awt.Color(
                    (int)incrementalAverage(average.getRed(), colorResult.getRed()),
                    (int)incrementalAverage(average.getGreen(), colorResult.getGreen()),
                    (int)incrementalAverage(average.getBlue(), colorResult.getBlue()));
        }

//        CurrentLogger.Logger.Debug(average.toString());


        ti02.robotica.Enums.Color converted = colorDetector.convertColor(average, 15);


        CurrentLogger.Logger.Info(converted + ", "+nullCount);
        if (converted != null) {
            nullCount = 0;
            System.out.printf("Opening gate %d\n", converted.ordinal());
            Controller.OpenGate(converted.ordinal());
        } else {
            nullCount++;
            if (nullCount >= 25) {
                nullCount = 0;
                Controller.Feed();
                try {
                    Thread.sleep(1000);
                } catch( InterruptedException e){}
            }
        }

        return mmPicture;
    }

    // Bereken gemiddelde - https://stackoverflow.com/a/16757630
    double incrementalAverage(double average, double new_value) {
        average -= average;
        average += new_value;

        return average;
    }
}
