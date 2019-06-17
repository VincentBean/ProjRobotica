package ti02.robotica.PhotoDetection;

import jssc.SerialPort;
import jssc.SerialPortList;
import ti02.robotica.Detector.BoundsDetector;
import ti02.robotica.Detector.ColorDetector;
import ti02.robotica.Detector.HardwareController;
import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.Bounds;
import ti02.robotica.Models.MMPicture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class PictureProcessor {
    private int nullCount = 0;              // Amount of 'null' average colors
    private HardwareController Controller;
    private ArrayList<ti02.robotica.Enums.Color> colorArray = new ArrayList<>();

    public PictureProcessor() {
        String[] serialPorts = SerialPortList.getPortNames();
        if (serialPorts.length != 0) {
            Controller = new HardwareController(SerialPort.BAUDRATE_115200, serialPorts[0]);
            Controller.Connect();
        } else {
            Controller = new HardwareController(0, "");
        }

        resetFoundColors();
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

        Color average = null;
        // Go through all found objects
        for (ti02.robotica.Models.Object object : objects) {
            Color colorResult = colorDetector.detectColor(object);  // Get average color of object
            object.setAverageColor(colorResult);

            // Set color for first time
            if (average == null) {
                average = colorResult;
                continue;
            }

            // Calculate average of all objects
            average = new java.awt.Color(
                    (int) incrementalAverage(average.getRed(), colorResult.getRed()),
                    (int) incrementalAverage(average.getGreen(), colorResult.getGreen()),
                    (int) incrementalAverage(average.getBlue(), colorResult.getBlue()));
        }

        // Draw bounds (for debugging)
        for (ti02.robotica.Models.Object object : objects) {

            java.awt.Color[][] pixels = object.getPixels();
            Bounds bounds = object.getBounds();
            Color averageColor = object.getAverageColor();

            CurrentLogger.Logger.Debug("north=" + bounds.getNorth() + ", east=" + bounds.getEast() + ", south=" + bounds.getSouth() + ", west=" + bounds.getWest());

            // TODO: replace hardcoded value for 'blockSize'
            // Draw west and east bounds
            for (int x = bounds.getWest() * 20; x < bounds.getEast() * 20; x++) {
                mmPicture.setPixel(x, bounds.getSouth() * 20, averageColor.getRGB());
                mmPicture.setPixel(x, bounds.getNorth() * 20, averageColor.getRGB());
            }

            // Draw north and south bounds
            for (int y = bounds.getNorth() * 20; y < bounds.getSouth() * 20; y++) {
                mmPicture.setPixel(bounds.getWest() * 20, y, averageColor.getRGB());
                mmPicture.setPixel(bounds.getEast() * 20, y, averageColor.getRGB());
            }
        }


        // Find matching Enum color
        ti02.robotica.Enums.Color convertedColor = null;

        if (average != null) {
            convertedColor = colorDetector.convertColor(average, 50);
        }


        // Open matching gate
        if (convertedColor != null) {
            System.out.println(convertedColor + ", " + nullCount);
            nullCount = 0;          // Color found, reset null counter

            Controller.OpenGate(convertedColor.getGateNumber());

        } else {
            nullCount++;            // 'Null' found, increment count

            if (nullCount >= 25) {  // If no color has been found after 25 frames
                nullCount = 0;
                Controller.Step();
                Controller.setDoStep(true);
            }
        }

        return mmPicture;
    }

    // Calculate average - https://stackoverflow.com/a/16757630
    double incrementalAverage(double average, double new_value) {
        average -= average;
        average += new_value;

        return average;
    }

    void resetFoundColors() {
        colorArray.clear();
    }
}
