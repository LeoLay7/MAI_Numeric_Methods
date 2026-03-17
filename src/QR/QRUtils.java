package QR;

import common.matrix.Column;
import common.matrix.Matrix;

public class QRUtils {
    
    public static Column findEigenvalues(Matrix A, double eps) {
        int n = A.getSize().getRows();

        // Копируем исходную матрицу
        Matrix current = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                current.set(i, j, A.getDouble(i, j));
            }
        }

        int iterations = 0;
        final int MAX_ITER = 2000;
        double maxSubdiag = Double.MAX_VALUE;

        while (iterations < MAX_ITER) {
            // 1. QR-разложение (Грам-Шмидт)
            Matrix[] qr = qrDecomposeGramSchmidt(current);
            Matrix Q = qr[0];
            Matrix R = qr[1];

            // 2. Обратное умножение: A_new = R * Q
            current = multiply(R, Q);

            iterations++;

            // 3. Проверка сходимости: максимум поддиагонали
            maxSubdiag = 0;
            for (int i = 1; i < n; i++) {
                double val = Math.abs(current.getDouble(i, i - 1));
                if (val > maxSubdiag) maxSubdiag = val;
            }

            if (maxSubdiag < eps) {
                break;
            }
        }

        // Извлечение собственных значений
        Column eigenvalues = extractEigenvalues(current, eps);

        // Вывод отчёта
        System.out.println("=== QR Алгоритм ===");
        System.out.println("Итераций: " + iterations);
        System.out.println("\nФинальная квазидиагональная матрица:");
        printMatrix(current);
        
        // Создаём и выводим диагональную матрицу с собственными значениями
        Matrix diagonal = new Matrix(n, n);
        for (int i = 0; i < eigenvalues.getSize().getRows(); i++) {
            diagonal.set(i, i, eigenvalues.getDouble(i));
        }
        System.out.println("\nДиагональная матрица собственных значений:");
        printMatrix(diagonal);

        return eigenvalues;
    }

    // QR-разложение через Грам-Шмидт
    private static Matrix[] qrDecomposeGramSchmidt(Matrix A) {
        int n = A.getSize().getRows();
        Matrix Q = new Matrix(n, n);
        Matrix R = new Matrix(n, n);

        for (int j = 0; j < n; j++) {
            // Копируем j-й столбец A во временный вектор
            double[] v = new double[n];
            for (int i = 0; i < n; i++) {
                v[i] = A.getDouble(i, j);
            }

            // Вычитаем проекции на предыдущие ортогональные векторы
            for (int i = 0; i < j; i++) {
                // R[i][j] = q_i^T * a_j
                double r_ij = 0;
                for (int k = 0; k < n; k++) {
                    r_ij += Q.getDouble(k, i) * A.getDouble(k, j);
                }
                R.set(i, j, r_ij);

                // v = v - R[i][j] * q_i
                for (int k = 0; k < n; k++) {
                    v[k] -= r_ij * Q.getDouble(k, i);
                }
            }

            // R[j][j] = ||v||
            double r_jj = 0;
            for (double val : v) {
                r_jj += val * val;
            }
            r_jj = Math.sqrt(r_jj);
            R.set(j, j, r_jj);

            // q_j = v / ||v||
            if (r_jj > 1e-10) {
                for (int i = 0; i < n; i++) {
                    Q.set(i, j, v[i] / r_jj);
                }
            }
        }

        return new Matrix[]{Q, R};
    }

    private static Matrix multiply(Matrix A, Matrix B) {
        int n = A.getSize().getRows();
        Matrix C = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += A.getDouble(i, k) * B.getDouble(k, j);
                }
                C.set(i, j, sum);
            }
        }
        return C;
    }

    private static Column extractEigenvalues(Matrix A, double eps) {
        int n = A.getSize().getRows();
        double[] values = new double[n];
        int count = 0;

        int i = 0;
        while (i < n) {
            if (i < n - 1 && Math.abs(A.getDouble(i + 1, i)) >= eps) {
                // Блок 2×2
                double a = A.getDouble(i, i);
                double b = A.getDouble(i, i + 1);
                double c = A.getDouble(i + 1, i);
                double d = A.getDouble(i + 1, i + 1);

                double trace = a + d;
                double det = a * d - b * c;
                double disc = trace * trace - 4 * det;

                if (disc >= 0) {
                    values[count++] = (trace + Math.sqrt(disc)) / 2;
                    values[count++] = (trace - Math.sqrt(disc)) / 2;
                } else {
                    values[count++] = trace / 2;
                    values[count++] = trace / 2;
                }
                i += 2;
            } else {
                values[count++] = A.getDouble(i, i);
                i++;
            }
        }

        Column result = new Column(count);
        for (int j = 0; j < count; j++) {
            result.set(j, values[j]);
        }
        return result;
    }

    private static void printMatrix(Matrix m) {
        int n = m.getSize().getRows();
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder("[");
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%10.6f", m.getDouble(i, j)));
                if (j < n - 1) sb.append("  ");
            }
            sb.append("]");
            System.out.println(sb.toString());
        }
    }
}