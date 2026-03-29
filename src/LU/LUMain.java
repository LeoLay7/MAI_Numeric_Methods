package LU;

import common.equation.Equation;
import common.equation.EquationUtils;
import common.matrix.Column;
import common.matrix.Matrix;
import common.matrix.MatrixUtils;

public class LUMain {
    public static void main(String[] args) {
//        String matrixPath = "C:\\Code\\JavaCode\\fosp\\MAI_Numeric_Methods\\resources\\matrix\\matrix1";
//        Matrix m = MatrixUtils.fromFile(matrixPath);
//        MatrixUtils.printMatrix(m);
//
//        LU lu = LUUtil.getLU(m);
//        MatrixUtils.printMatrix(lu.getU(), "\nU:");
//        MatrixUtils.printMatrix(lu.getL(), "\nL:");
//
        String eqpath = "C:\\Code\\self\\MAI_Numeric_Methods\\resources\\equations\\lab.txt";
        Equation eq = EquationUtils.fromFile(eqpath);
//        MatrixUtils.printMatrix(eq.getCoefficients());
//        MatrixUtils.printMatrix(eq.getValuesCol());

        Column ans = LUEquationSolver.solveLU(eq);

        System.out.println("Определитель: " + LUUtil.det(eq.getCoefficients()));
        MatrixUtils.printMatrix(LUUtil.inverse(eq.getCoefficients()), "Обратная матрица:");
    }
}
