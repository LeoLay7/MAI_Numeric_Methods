package QR;

import Rotate.RotateMain;
import Rotate.RotateUtils;
import common.matrix.Column;
import common.matrix.Matrix;
import common.matrix.MatrixUtils;

import java.util.List;

public class QRMain {
    public static void main(String[] args) {
        String path = "C:\\Code\\self\\MAI_Numeric_Methods\\resources\\matrix\\RotateMatrix";
        Matrix m = MatrixUtils.fromFile(path);

        List<String> eigenvalues = QRUtils.findEigenvalues(m, 1e-4);
//        MatrixUtils.printMatrix(eigenvalues, "QR:");

//        Column rotateEigenvalues = RotateUtils.findEigenvalues(m, 1e-4);
//        MatrixUtils.printMatrix(rotateEigenvalues, "\nЯкоби:");
    }
}
