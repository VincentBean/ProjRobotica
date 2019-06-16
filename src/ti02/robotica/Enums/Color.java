package ti02.robotica.Enums;

public enum Color {

    BLUE(1),
    BROWN(2),
    GREEN(3),
    ORANGE(4),
    RED(5),
    YELLOW(6);

    int gateNumber;

    Color(int gateNumber) {
        this.gateNumber = gateNumber;
    }

    public int getGateNumber() {
        return gateNumber;
    }
}
