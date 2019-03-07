package ti02.robotica.Models;

import ti02.robotica.Logging.CurrentLogger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MMPicture {
    private int width;
    private int height;
    private int[][] pixels;
    private Bounds bounds;

    public MMPicture(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width][height];
        bounds = new Bounds();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getPixels() {
        return pixels;
    }

    public int getPixel(int x, int y) {
        return pixels[x][y];
    }

    public void setPixel(int x, int y, int color) {
        if (x < 0 && y < 0 && x >= width && y >= height) {
            // oops
            CurrentLogger.Logger.Error("Invalid x or y.");
            return;
        }

        pixels[x][y] = color;
    }

    public int getPixelCount() {
        return width * height;
    }
}
