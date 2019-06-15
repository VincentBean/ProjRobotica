package ti02.robotica.Detector;

import ti02.robotica.Enums.Color;
import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.DetectorResult;
import ti02.robotica.Models.Object;

import java.util.ArrayList;

public class ColorDetector implements IDetector {
    // Colors to detect
    final java.awt.Color colorBlue = new java.awt.Color(9, 74, 177);
    final java.awt.Color colorBrown = new java.awt.Color(33, 30, 34);
    final java.awt.Color colorGreen = new java.awt.Color(84, 160, 8);
    final java.awt.Color colorOrange = new java.awt.Color(10, 8, 6);
    final java.awt.Color colorRed = new java.awt.Color(196, 40, 73);
    final java.awt.Color colorYellow = new java.awt.Color(196, 167, 6);
    final java.awt.Color colorBlack = new java.awt.Color(0, 0, 0);

    @Override
    public DetectorResult<Color> detect() {
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

    // Calculate average color
    public java.awt.Color detectColor(ti02.robotica.Models.Object object) {
        java.awt.Color average = null;

        for (java.awt.Color[] xResult : object.getPixels()) {
            for (java.awt.Color yResult : xResult) {
                if (yResult != null) {
                    if (average == null) {
                        average = yResult;
                    }

                    average = new java.awt.Color(
                            (int)incrementalAverage(average.getRed(), yResult.getRed()),
                            (int)incrementalAverage(average.getGreen(), yResult.getGreen()),
                            (int)incrementalAverage(average.getBlue(), yResult.getBlue()));
                }
            }
        }

//        CurrentLogger.Logger.Info(average + "");

        return average;
    }

    public ti02.robotica.Enums.Color convertColor(java.awt.Color inputColor, int margin) {
        // Compare colors
        if (compareColors(inputColor, colorBlue, margin)) {
            return Color.BLUE;
        }
        else if (compareColors(inputColor, colorBrown, margin)) {
            return Color.BROWN;
        }
        else if (compareColors(inputColor, colorGreen, margin)) {
            return Color.GREEN;
        }
//        else if (compareColors(inputColor, colorOrange, margin)) {
//            return Color.ORANGE;
//        }
        else if (compareColors(inputColor, colorRed, margin)) {
            return Color.RED;
        }
        else if (compareColors(inputColor, colorYellow, margin)) {
            return Color.YELLOW;
        }

        return null;
    }

    // TODO: maak utility class
    // Bereken gemiddelde - https://stackoverflow.com/a/16757630
    private double incrementalAverage(double average, double new_value) {
        average -= average;
        average += new_value;

        return average;
    }

    private Boolean compareColors(java.awt.Color color, java.awt.Color color2, int margin) {
        if ((color.getRed() >= color2.getRed() && color.getGreen() >= color2.getGreen() && color.getBlue() >= color2.getBlue()) && (color.getRed() < color2.getRed()+margin && color.getGreen() < color2.getGreen()+margin && color.getBlue() < color2.getBlue()+margin)) {
            return true;
        }

        return false;
    }
}
