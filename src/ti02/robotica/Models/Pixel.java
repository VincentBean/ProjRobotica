package ti02.robotica.Models;

public class Pixel {
    private int x;
    private int y;

    private int rgb[];

    public Pixel(int x, int y, int[] rgb) {
        this.x = x;
        this.y = y;
        this.rgb = rgb;
    }

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pixel(int x, int y, int r, int g, int b) {
        this(x, y, new int[] {r, g, b});
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getRgb() {
        return rgb;
    }
}
