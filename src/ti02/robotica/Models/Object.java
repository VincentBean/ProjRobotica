package ti02.robotica.Models;

import ti02.robotica.Detector.IDetector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class Object {
    private java.awt.Color[][] pixels;
    private int boundNorth, boundSouth, boundWest, boundEast;
    private Bounds bounds;
    private Color averageColor;

    public Bounds getBounds() {
        return bounds;
    }

    public Color getAverageColor() {
        return averageColor;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public void setAverageColor(Color averageColor) {
        this.averageColor = averageColor;
    }

    private Map<String, DetectorResult> Properties;

    public Object(Color[][] pixels) {
        this.pixels = pixels;
    }

    public Object(int width, int height) {
        this.pixels = new java.awt.Color[width][height];
    }

    public void RunDetector(IDetector detector) {
        DetectorResult result = detector.detect();

        Properties.put(detector.toString(), result);
    }

    public void setPixels(Color[][] pixels) {
        this.pixels = pixels;
    }

    public void setPixel(Color pixel, int x, int y) {
        this.pixels[x][y] = pixel;
    }

    public java.awt.Color[][] getPixels() {
        return pixels;
    }
}
