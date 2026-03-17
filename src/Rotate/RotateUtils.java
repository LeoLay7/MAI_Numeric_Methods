package Rotate;

import common.matrix.Column;
import common.matrix.Matrix;

public class RotateUtils {

    public static Column findEigenvalues(Matrix A, double eps) {
        int n = A.getSize().getRows();

        // Рабочая копия матрицы с копией верхнего треугольника
        Matrix current = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j >= i) {
                    double val = A.getDouble(i, j);
                    current.set(i, j, val);
                    current.set(j, i, val);
                }
            }
        }

        System.out.println("Метод вращения");

        // Матрица собственных векторов
        Matrix V = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            V.set(i, i, 1.0);
        }

        int iterations = 0;
        final int MAX_ITER = 10000;

        while (true) {
            // Поиск максимального по модулю внедиагонального элемента
            double maxVal = 0;
            int p = 0, q = 1;

            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    double val = Math.abs(current.getDouble(i, j));
                    if (val > maxVal) {
                        maxVal = val;
                        p = i;
                        q = j;
                    }
                }
            }

            // Проверка критерия остановки
            if (maxVal < eps) {
                break;
            }

            // Вычисление параметров вращения
            double[] cs = computeCS(current.getDouble(p, p), current.getDouble(q, q), current.getDouble(p, q));
            double c = cs[0]; // cos(θ)
            double s = cs[1]; // sin(θ)

            // Применение вращения: A' = J^T * A * J
            applyRotation(current, p, q, c, s);

            // Обновление матрицы собственных векторов: V' = V * J
            applyRotationToVectors(V, p, q, c, s);

            iterations++;

            if (iterations > MAX_ITER) {
                System.err.println("Превышение кол-ва итераций");
                break;
            }
        }

        // Извлечение собственных значений из диагонали
        Column eigenvalues = new Column(n);
        for (int i = 0; i < n; i++) {
            eigenvalues.set(i, current.getDouble(i, i));
        }

        System.out.println("Якоби");
        System.out.println("Итерации: " + iterations);
        // System.out.println("Max off-diagonal element: " + String.format("%.2e", maxVal));
        System.out.println("\nДиагональная матрица:");
        printMatrix(current);
        System.out.println();

        return eigenvalues;
    }

    private static double[] computeCS(double app, double aqq, double apq) {
        double[] result = new double[2];

        if (Math.abs(apq) < 1e-12) {
            // Элемент уже почти нулевой
            result[0] = 1.0; // c
            result[1] = 0.0; // s
            return result;
        }

        // tan(2θ) = 2*a[p][q] / (a[q][q] - a[p][p])
        double tau = (aqq - app) / (2.0 * apq);

        // Вычисляем t = tan(θ)
        double t;
        if (tau >= 0) {
            t = 1.0 / (tau + Math.sqrt(1.0 + tau * tau));
        } else {
            t = -1.0 / (-tau + Math.sqrt(1.0 + tau * tau));
        }

        // Вычисляем c и s
        double c = 1.0 / Math.sqrt(1.0 + t * t);
        double s = c * t;

        result[0] = c;
        result[1] = s;
        return result;
    }


    private static void applyRotation(Matrix A, int p, int q, double c, double s) {
        int n = A.getSize().getRows();

        // Сохраняем старые значения диагональных элементов
        double app = A.getDouble(p, p);
        double aqq = A.getDouble(q, q);
        double apq = A.getDouble(p, q);

        // Обновляем диагональные элементы по формулам
        A.set(p, p, c * c * app + s * s * aqq - 2.0 * s * c * apq);
        A.set(q, q, s * s * app + c * c * aqq + 2.0 * s * c * apq);
        A.set(p, q, 0.0);
        A.set(q, p, 0.0);

        // Обновляем остальные элементы строк/столбцов p и q
        for (int k = 0; k < n; k++) {
            if (k != p && k != q) {
                // Элементы в позиции (p, k) и (k, p)
                double apk = A.getDouble(p, k);
                double aqk = A.getDouble(q, k);

                A.set(p, k, c * apk - s * aqk);
                A.set(k, p, A.getDouble(p, k)); // симметрия

                A.set(q, k, s * apk + c * aqk);
                A.set(k, q, A.getDouble(q, k)); // симметрия
            }
        }
    }


    private static void applyRotationToVectors(Matrix V, int p, int q, double c, double s) {
        int n = V.getSize().getRows();

        for (int k = 0; k < n; k++) {
            double vkp = V.getDouble(k, p);
            double vkq = V.getDouble(k, q);

            V.set(k, p, c * vkp - s * vkq);
            V.set(k, q, s * vkp + c * vkq);
        }
    }

    private static void printMatrix(Matrix m) {
        int n = m.getSize().getRows();
        for (int i = 0; i < n; i++) {
            System.out.print("[");
            for (int j = 0; j < n; j++) {
                System.out.printf("%10.6f", m.getDouble(i, j));
                if (j < n - 1) System.out.print("  ");
            }
            System.out.println("]");
        }
    }
}
