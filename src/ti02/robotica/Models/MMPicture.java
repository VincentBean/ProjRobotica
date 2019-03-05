package ti02.robotica.Models;

import ti02.robotica.Logging.CurrentLogger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MMPicture {

    private int width;
    private int height;

    private Pixel bounds[]; // 0: top left, 1: bottom right

    private List<Pixel> colorPixels;

    public MMPicture(int width, int height) {
        this.width = width;
        this.height = height;

        colorPixels = new ArrayList<>();

        bounds = new Pixel[2];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Pixel> getColorPixels() {
        return colorPixels;
    }
}
