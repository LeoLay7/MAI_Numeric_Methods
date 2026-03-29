package Iterations;

import common.equation.Equation;
import common.equation.EquationUtils;
import common.matrix.Column;
import common.matrix.MatrixUtils;

public class IterationsMain {
    public static void main(String[] args) {
        Equation equation = EquationUtils.fromFile("C:\\Code\\self\\MAI_Numeric_Methods\\resources\\equations\\IterationsEquation");

        System.out.println("МЕТОД ПРОСТЫХ ИТЕРАЦИЙ:");
        Column res1 = IterationsEquationsSolver.solveIteration(equation);
        MatrixUtils.printMatrix(res1, "Решение:");

        System.out.println("Решение верно: " + EquationUtils.doCheck(equation, res1));

        System.out.println("МЕТОД ЗЕЙДЕЛЯ:");
        Column res2 = IterationsEquationsSolver.solveZeidel(equation);
        MatrixUtils.printMatrix(res2, "Решение:");

        System.out.println("Решение верно: " + EquationUtils.doCheck(equation, res2));
    }
}
