package QR;

import common.matrix.Column;
import common.matrix.Matrix;
import java.util.ArrayList;
import java.util.List;

public class QRUtils {

    public static List<String> findEigenvalues(Matrix A, double eps) {
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

        // Переменные для хранения параметров предыдущей итерации
        // Храним "спектральные сигнатуры": для блоков - дискриминант, для одиночек - значение
        double[] prevSignatures = new double[n];
        boolean firstRun = true;
        double maxChange = Double.MAX_VALUE;

        while (iterations < MAX_ITER) {
            // Сохраняем сигнатуры перед итерацией
            if (!firstRun) {
                for (int i = 0; i < n; i++) {
                    prevSignatures[i] = getCurrentSignature(current, i, eps);
                }
            }

            // 1. QR-разложение
            Matrix[] qr = qrDecomposeGramSchmidt(current);
            Matrix Q = qr[0];
            Matrix R = qr[1];

            // 2. Обратное умножение
            current = multiply(R, Q);

            iterations++;

            // 3. Проверка сходимости через изменение дискриминантов/значений
            if (!firstRun) {
                maxChange = 0.0;
                for (int i = 0; i < n; i++) {
                    // Пропускаем индексы, которые являются вторыми частями блоков (i-1 уже обработал блок)
                    // Нам нужно проверять только начала блоков.
                    // Но для простоты оценки изменения пройдемся по всем потенциальным стартам.

                    // Определяем, является ли индекс i началом блока
                    boolean isBlockStart = (i < n - 1) && (Math.abs(current.getDouble(i + 1, i)) >= eps);
                    boolean isPrevBlockEnd = (i > 0) && (Math.abs(current.getDouble(i, i - 1)) >= eps);

                    if (isPrevBlockEnd) {
                        continue; // Этот индекс часть предыдущего блока, его сигнатура учтена там
                    }

                    double currentSig = getCurrentSignature(current, i, eps);
                    double diff = Math.abs(currentSig - prevSignatures[i]);

                    // Нормализуем разницу, чтобы eps работал для разных масштабов
                    double scale = Math.abs(currentSig) + 1e-10;
                    double relDiff = diff / scale;

                    if (relDiff > maxChange) {
                        maxChange = relDiff;
                    }
                }

                if (maxChange < eps) {
                    System.out.println("Сходимость достигнута по стабилизации дискриминантов/значений.");
                    break;
                }
            }
            firstRun = false;
        }

        // Извлечение собственных значений
        List<String> eigenvaluesStr = extractEigenvaluesFormatted(current, eps);

        // Вывод отчёта
        System.out.println("=== QR Алгоритм ===");
        System.out.println("Итераций: " + iterations);
        System.out.println("Максимальное изменение на последнем шаге: " + maxChange);
        System.out.println("\nФинальная квазидиагональная матрица:");
        printMatrix(current);

        System.out.println("\nСобственные значения:");
        for (int i = 0; i < eigenvaluesStr.size(); i++) {
            System.out.println("λ" + (i + 1) + " = " + eigenvaluesStr.get(i));
        }

        return eigenvaluesStr;
    }

    /**
     * Вычисляет "сигнатуру" для проверки сходимости.
     * Для блока 2x2 (начинающегося в i) возвращает Дискриминант.
     * Для одиночного элемента возвращает само значение (как аналог корня).
     */
    private static double getCurrentSignature(Matrix m, int i, double eps) {
        int n = m.getSize().getRows();
        // Проверяем, блок ли это
        if (i < n - 1 && Math.abs(m.getDouble(i + 1, i)) >= eps) {
            double a = m.getDouble(i, i);
            double b = m.getDouble(i, i + 1);
            double c = m.getDouble(i + 1, i);
            double d = m.getDouble(i + 1, i + 1);

            double trace = a + d;
            double det = a * d - b * c;
            return trace * trace - 4 * det; // Дискриминант
        } else {
            // Одиночный элемент
            return m.getDouble(i, i);
        }
    }

    private static List<String> extractEigenvaluesFormatted(Matrix A, double eps) {
        int n = A.getSize().getRows();
        List<String> result = new ArrayList<>();

        int i = 0;
        while (i < n) {
            if (i < n - 1 && Math.abs(A.getDouble(i + 1, i)) >= eps) {
                double a = A.getDouble(i, i);
                double b = A.getDouble(i, i + 1);
                double c = A.getDouble(i + 1, i);
                double d = A.getDouble(i + 1, i + 1);

                double trace = a + d;
                double det = a * d - b * c;
                double disc = trace * trace - 4 * det;

                if (disc >= 0) {
                    double sqrtDisc = Math.sqrt(disc);
                    double x1 = (trace + sqrtDisc) / 2;
                    double x2 = (trace - sqrtDisc) / 2;
                    result.add(String.format("%.6f", x1));
                    result.add(String.format("%.6f", x2));
                } else {
                    double re = trace / 2.0;
                    double im = Math.sqrt(Math.abs(disc)) / 2.0;
                    result.add(String.format("%.6f + %.6f*i", re, im));
                    result.add(String.format("%.6f - %.6f*i", re, im));
                }
                i += 2;
            } else {
                result.add(String.format("%.6f", A.getDouble(i, i)));
                i++;
            }
        }
        return result;
    }

    private static Matrix[] qrDecomposeGramSchmidt(Matrix A) {
        int n = A.getSize().getRows();
        Matrix Q = new Matrix(n, n);
        Matrix R = new Matrix(n, n);

        for (int j = 0; j < n; j++) {
            double[] v = new double[n];
            for (int i = 0; i < n; i++) {
                v[i] = A.getDouble(i, j);
            }

            for (int i = 0; i < j; i++) {
                double r_ij = 0;
                for (int k = 0; k < n; k++) {
                    r_ij += Q.getDouble(k, i) * A.getDouble(k, j);
                }
                R.set(i, j, r_ij);
                for (int k = 0; k < n; k++) {
                    v[k] -= r_ij * Q.getDouble(k, i);
                }
            }

            double r_jj = 0;
            for (double val : v) {
                r_jj += val * val;
            }
            r_jj = Math.sqrt(r_jj);
            R.set(j, j, r_jj);

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