package ti02.robotica.Models;

import java.util.ArrayList;
import java.util.List;

public class MMPicture {

    private int width;
    private int height;

    private List<Pixel> colorPixels;

    public MMPicture(int width, int height) {
        this.width = width;
        this.height = height;

        colorPixels = new ArrayList<>();
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
