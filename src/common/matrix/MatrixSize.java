package common.matrix;

public class MatrixSize {
    private int rows;
    private int cols;

    public MatrixSize(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public void setSize(MatrixSize size) {
        this.rows = size.getRows();
        this.cols = size.getCols();
    }
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public int getCols() {
        return cols;
    }
    public void setCols(int cols) {
        this.cols = cols;
    }
}
