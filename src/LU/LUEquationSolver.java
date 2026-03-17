package LU;

import common.equation.Equation;
import common.matrix.Column;
import common.matrix.Matrix;
import common.matrix.MatrixUtils;

public class LUEquationSolver {
    public static Column solveLU(Equation eq) {
        // Разбиваем матрицу A = LU
        LU lu = LUUtil.getLU(eq.getCoefficients());
        Matrix l = lu.getL();
        Matrix u = lu.getU();
        MatrixUtils.printMatrix(l, "\nL:");
        MatrixUtils.printMatrix(u, "\nU:");
        MatrixUtils.printMatrix(eq.getValuesCol(), "\nb:");
        // y = Ux
        // Решаем Ly = b
        Column y = solveL(new Equation(l, eq.getValuesCol()));
        MatrixUtils.printMatrix(y, "\ny:");
        // решаем Ux = y
        Column x = solveU(new Equation(u, y));
        MatrixUtils.printMatrix(x, "\nx:");
        return x;
    }

    public static Column solveL(Equation eq) {
        Column res = new Column(eq.getCoefficients().getSize().getCols());

        for (int i = 0; i < eq.getCoefficients().getSize().getCols(); i++) {
            double diag = eq.getCoefficients().getDouble(i, i);
            if (Math.abs(diag) < 1e-9) {
                res.set(i, 0);
            }
            double sum = 0;
            double value = eq.getValuesCol().getDouble(i);
            for (int j = 0; j < i; j++) {
                sum += eq.getCoefficients().getDouble(i, j) * res.getDouble(j);
            }

            res.set(i, (value - sum) / diag);
        }

        return res;
    }

    public static Column solveU(Equation eq) {
        Matrix u = eq.getCoefficients();
        Column y = eq.getValuesCol();
        int n = u.getSize().getCols();
        Column x = new Column(n);


        for (int i = n - 1; i >= 0; i--) {

            double diag = u.getDouble(i, i);


            if (Math.abs(diag) < 1e-9) {
                throw new ArithmeticException("Деление на ноль в U[" + i + "][" + i + "]. Матрица вырождена.");
            }


            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += u.getDouble(i, j) * x.getDouble(j);
            }

            double value = y.getDouble(i);
            x.set(i, (value - sum) / diag);
        }

        return x;
    }
}
