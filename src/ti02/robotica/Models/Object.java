package ti02.robotica.Models;

import ti02.robotica.Detector.IDetector;

import java.awt.*;
import java.util.Map;

public class Object {
    private java.awt.Color[][] pixels;

    public java.awt.Color[][] getPixels() {
        return pixels;
    }

    private Map<String, DetectorResult> Properties;

    public void setPixels(Color[][] pixels) {
        this.pixels = pixels;
    }

    public void setPixel(Color pixel, int x, int y) {
        this.pixels[x][y] = pixel;
    }

    public void RunDetector(IDetector detector) {
        DetectorResult result = detector.detect();

        Properties.put(detector.toString(), result);
    }


}
