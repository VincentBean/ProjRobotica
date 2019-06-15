package ti02.robotica.Models;

public class Bounds {
    int North;
    int East;
    int South;
    int West;

    public Bounds(int north, int east, int south, int west) {
        this.North = north;
        this.East = east;
        this.South = south;
        this.West = west;
    }

    public Bounds() {
        this(0, 0, 0, 0);
    }

    public void setNorth(int north) {
        if (north >= 0)
            North = north;
    }

    public void setEast(int east) {
        if (east >= 0)
            East = east;
    }

    public void setSouth(int south) {
        if (south >= 0)
            South = south;
    }

    public void setWest(int west) {
        if (west >= 0)
            West = west;
    }

    public int getNorth() {
        return North;
    }

    public int getEast() {
        return East;
    }

    public int getSouth() {
        return South;
    }

    public int getWest() {
        return West;
    }
}
