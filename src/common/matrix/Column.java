package common.matrix;

public class Column extends Matrix {
    public Column(MatrixElement[] elements) {
        super(elements.length, 1);
        for (int i = 0; i < elements.length; i++) {
            set(i, 0, elements[i]);
        }
    }

    public Column(int rows) {
        super(rows, 1);
    }

    public void set(int row, MatrixElement element) {
        set(row, 0, element);
    }

    public void set(int row, double value) {
        set(row, 0, value);
    }

    public double getFloat(int row) {
        return getDouble(row, 0);
    }
}
