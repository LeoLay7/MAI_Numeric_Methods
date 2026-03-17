package Rotate;

import common.matrix.Column;
import common.matrix.Matrix;
import common.matrix.MatrixUtils;

public class RotateMain {
    public static void main(String[] args) {
        String path = "C:\\Code\\JavaCode\\MAI_Numeric_Methods\\resources\\matrix\\RotateMatrix";
        Matrix m = MatrixUtils.fromFile(path);

        Column eigenvalues = RotateUtils.findEigenvalues(m, 1e-4);
        MatrixUtils.printMatrix(eigenvalues);
    }
}
