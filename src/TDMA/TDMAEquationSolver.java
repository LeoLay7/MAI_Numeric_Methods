package TDMA;

import common.equation.Equation;
import common.matrix.Column;
import common.matrix.Matrix;

public class TDMAEquationSolver {
    public static Column solveTDMA(Equation equation) {
        Matrix A = equation.getCoefficients();
        Column f = equation.getValuesCol();
        int n = A.getSize().getRows();

        double[][] a = new double[n][n];
        double[] b = new double[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = A.getDouble(i, j);
            }
            b[i] = f.getDouble(i);
        }

        for (int k = 0; k < n - 1; k++) {
            int maxRow = k;
            for (int i = k + 1; i < Math.min(k + 2, n); i++) {
                if (Math.abs(a[i][k]) > Math.abs(a[maxRow][k])) {
                    maxRow = i;
                }
            }

            if (maxRow != k) {
                double[] temp = a[k];
                a[k] = a[maxRow];
                a[maxRow] = temp;

                double tempB = b[k];
                b[k] = b[maxRow];
                b[maxRow] = tempB;
            }

            for (int i = k + 1; i < Math.min(k + 2, n); i++) {
                double factor = a[i][k] / a[k][k];
                for (int j = k; j < Math.min(k + 3, n); j++) {
                    a[i][j] -= factor * a[k][j];
                }
                b[i] -= factor * b[k];
            }
        }

        Column x = new Column(n);
        x.set(n - 1, b[n - 1] / a[n - 1][n - 1]);

        for (int i = n - 2; i >= 0; i--) {
            double sum = b[i];
            for (int j = i + 1; j < Math.min(i + 3, n); j++) {
                sum -= a[i][j] * x.getDouble(j);
            }
            x.set(i, sum / a[i][i]);
        }

        return x;
    }
}
