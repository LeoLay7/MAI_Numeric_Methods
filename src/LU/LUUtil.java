package LU;

import common.equation.Equation;
import common.matrix.Column;
import common.matrix.Matrix;

public class LUUtil {

    // Метод Дулитла
    public static LU getLU(Matrix m) {
        int n = m.getSize().getRows();
        Matrix l = new Matrix(m.getSize());
        Matrix u = new Matrix(m.getSize());

        for (int i = 0; i < n; i++) {
            // Заполняем U
            for (int j = i; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < i; k++) {
                    sum += l.getDouble(i, k) * u.getDouble(k, j);
                }
                u.set(i, j, m.getDouble(i, j) - sum);
            }

            // Заполняем L
            for (int j = i; j < n; j++) {
                if (i == j) {
                    l.set(i, i, 1);
                } else {
                    double sum = 0;
                    for (int k = 0; k < i; k++) {
                        sum += l.getDouble(j, k) * u.getDouble(k, i);
                    }
                    l.set(j, i, (m.getDouble(j, i) - sum) / u.getDouble(i, i));
                }
            }
        }

        return new LU(l, u);
    }

    public static double det(Matrix m) {
        LU lu = getLU(m);
        Matrix u = lu.getU();
        double sum = 1;
        for (int i = u.getSize().getRows() - 1; i >= 0; i--) {
            sum *= u.getDouble(i, i);
        }
        return sum;
    }

    public static Matrix inverse(Matrix m) {
        int n = m.getSize().getRows();
        LU lu = LUUtil.getLU(m);
        Matrix l = lu.getL();
        Matrix u = lu.getU();
        Matrix inv = new Matrix(m.getSize());

        // Для каждого столбца единичной матрицы
        for (int i = 0; i < n; i++) {
            // Создаем i-й столбец единичной матрицы
            Column e = new Column(n);
            e.set(i, 1);

            // Решаем систему A*x = e
            Column y = LUEquationSolver.solveL(new Equation(l, e));
            Column x = LUEquationSolver.solveU(new Equation(u, y));

            // x - это i-й столбец обратной матрицы
            for (int j = 0; j < n; j++) {
                inv.set(j, i, x.getFloat(j));
            }
        }

        return inv;
    }

}
