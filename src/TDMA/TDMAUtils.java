package TDMA;

import common.matrix.Matrix;

public class TDMAUtils {
    public static double det(Matrix matrix) {
        int n = matrix.getSize().getRows();
        double[][] a = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = matrix.getDouble(i, j);
            }
        }

        double det = 1.0;
        int swaps = 0;

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
                swaps++;
            }

            if (Math.abs(a[k][k]) < 1e-12) {
                return 0.0;
            }

            for (int i = k + 1; i < Math.min(k + 2, n); i++) {
                double factor = a[i][k] / a[k][k];
                for (int j = k; j < Math.min(k + 3, n); j++) {
                    a[i][j] -= factor * a[k][j];
                }
            }
        }

        if (Math.abs(a[n - 1][n - 1]) < 1e-12) {
            return 0.0;
        }

        for (int i = 0; i < n; i++) {
            det *= a[i][i];
        }

        return (swaps % 2 == 0) ? det : -det;
    }
}
