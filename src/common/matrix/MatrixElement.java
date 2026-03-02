package common.matrix;

public class MatrixElement {
    private double value = 0;

    public MatrixElement(int value) {
        this.value = value;
    }

    public MatrixElement(double value) {
        this.value = value;
    }

    public MatrixElement() {}

    public static MatrixElement of(int value) {
        return new MatrixElement(value);
    }

    public static MatrixElement of(double value) {
        return new MatrixElement(value);
    }

    public static MatrixElement floatOfString(String value) {
        return new MatrixElement(Float.parseFloat(value));
    }

    public int toInt() { return (int) value; }

    public double toDouble() { return value; }

    public String toStr() {
        return String.valueOf(value);
    }
}
