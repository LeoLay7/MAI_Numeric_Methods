package TDMA;

import common.equation.Equation;
import common.equation.EquationUtils;
import common.matrix.Column;
import common.matrix.MatrixUtils;

public class TDMAMain {
    public static void main(String[] args) {
        Equation equation = EquationUtils.fromFile("C:\\Code\\self\\MAI_Numeric_Methods\\resources\\equations\\TDMAEquation");

        Column res = TDMAEquationSolver.solveTDMA(equation);
        MatrixUtils.printMatrix(res, "Решение:");
        System.out.println();

        System.out.println("Решение верно: " + EquationUtils.doCheck(equation, res));

        System.out.println("\nОпределитель: " + TDMAUtils.det(equation.getCoefficients()));
    }
}
