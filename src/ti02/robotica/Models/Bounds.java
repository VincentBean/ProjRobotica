package ti02.robotica.Models;

public class Bounds {
    int north;
    int east;
    int south;
    int west;

    public Bounds(int north, int east, int south, int west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public Bounds() {
        this(0, 0, 0, 0);
    }

    public int getNorth() {
        return north;
    }

    public int getEast() {
        return east;
    }

    public int getSouth() {
        return south;
    }

    public int getWest() {
        return west;
    }
}
