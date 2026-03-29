package Iterations;

import common.equation.Equation;
import common.matrix.Column;
import common.matrix.Matrix;

public class IterationsEquationsSolver {
    public static Column solveZeidel(Equation eq) {
        int n = eq.getCoefficients().getSize().getRows();
        Matrix A = eq.getCoefficients();
        Column b = eq.getValuesCol();
        
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;
        
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i; k < n; k++) {
                if (Math.abs(A.getDouble(perm[k], i)) > Math.abs(A.getDouble(perm[maxRow], i))) {
                    maxRow = k;
                }
            }
            int temp = perm[i];
            perm[i] = perm[maxRow];
            perm[maxRow] = temp;
        }
        
        Column x = new Column(n);
        Column xPrev = new Column(n);
        int iterations = 0;
        final int MAX_ITERATIONS = 10000;
        do {
            for (int i = 0; i < n; i++) {
                xPrev.set(i, x.getDouble(i));
            }
            
            for (int i = 0; i < n; i++) {
                int row = perm[i];
                double sum = b.getDouble(row);
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        sum -= A.getDouble(row, j) * x.getDouble(j);
                    }
                }
                x.set(i, sum / A.getDouble(row, i));
            }

            iterations++;

            if (iterations > MAX_ITERATIONS) {
                System.err.println("Превышено кол-во итераций");
                break;
            }
        } while (maxDiff(x, xPrev) >= 0.0001);
        System.out.println("Кол-во итераций ЗЕЙДЕЛЬ: " + iterations);
        return x;
    }

    public static Column solveIteration(Equation eq) {
        int n = eq.getCoefficients().getSize().getRows();
        Matrix A = eq.getCoefficients();
        Column b = eq.getValuesCol();

        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;

        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i; k < n; k++) {
                if (Math.abs(A.getDouble(perm[k], i)) > Math.abs(A.getDouble(perm[maxRow], i))) {
                    maxRow = k;
                }
            }
            int temp = perm[i];
            perm[i] = perm[maxRow];
            perm[maxRow] = temp;
        }

        Column x = new Column(n);       // Текущее приближение
        Column xPrev = new Column(n);   // Предыдущее приближение

        for(int i=0; i<n; i++) xPrev.set(i, 0.0);

        int iterations = 0;
        final int MAX_ITERATIONS = 10000;

        do {
            // Копируем текущее x в xPrev
            for (int i = 0; i < n; i++) {
                xPrev.set(i, x.getDouble(i));
            }

            // Вычисляем новые значения
            for (int i = 0; i < n; i++) {
                int row = perm[i];
                double sum = b.getDouble(row);

                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        // Используем xPrev
                        sum -= A.getDouble(row, j) * xPrev.getDouble(j);
                    }
                }
                x.set(i, sum / A.getDouble(row, i));
            }
            iterations++;

            if (iterations > MAX_ITERATIONS) {
                System.err.println("Превышено кол-во итераций");
                break;
            }
        } while (maxDiff(x, xPrev) >= 0.0001);
        System.out.println("Кол-во итераций ПРОСТЫЕ ИТЕРАЦИИ: " + iterations);
        return x;
    }
    
    private static double maxDiff(Column x, Column xPrev) {
        double max = 0;
        for (int i = 0; i < x.getSize().getRows(); i++) {
            max = Math.max(max, Math.abs(x.getDouble(i) - xPrev.getDouble(i)));
        }
        return max;
    }
}
