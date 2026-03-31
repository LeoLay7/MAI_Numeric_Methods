package OLS;

import common.table.Table;
import common.equation.Equation;
import common.matrix.Column;
import common.matrix.Matrix;
import common.matrix.MatrixSize;
import TDMA.TDMAEquationSolver;
import java.util.List;

public class OLSUtilsTwo {

    public static class CubicSpline {
        public double[] a, b, c, d;
        public int n;
        
        public CubicSpline(int n) {
            this.n = n;
            this.a = new double[n];
            this.b = new double[n];
            this.c = new double[n];
            this.d = new double[n];
        }
    }
    
    /**
     * Строим сплайн
     */
    public static CubicSpline buildCubicSpline(Table table) {
        List<Double> x = table.getX();
        List<Double> y = table.getY();
        int n = x.size();
        
        CubicSpline spline = new CubicSpline(n - 1);
        
        // Шаги между узлами
        double[] h = new double[n - 1];
        for (int i = 0; i < n - 1; i++) {
            h[i] = x.get(i + 1) - x.get(i);
        }
        
        // Коэффициенты a_i = y_i
        for (int i = 0; i < n - 1; i++) {
            spline.a[i] = y.get(i);
        }

        double[] A = new double[n];
        double[] B = new double[n];
        double[] C = new double[n];
        double[] F = new double[n];

        A[0] = 0;
        B[0] = 1;
        C[0] = 0;
        F[0] = 0;
        
        // Внутренние уравнения
        for (int i = 1; i < n - 1; i++) {
            A[i] = h[i - 1];
            B[i] = 2 * (h[i - 1] + h[i]);
            C[i] = h[i];
            F[i] = 3 * ((y.get(i + 1) - y.get(i)) / h[i] - (y.get(i) - y.get(i - 1)) / h[i - 1]);
        }
        
        // Последнее уравнение: c_n = 0 (естественное граничное условие)
        A[n - 1] = 0;
        B[n - 1] = 1;
        C[n - 1] = 0;
        F[n - 1] = 0;
        
        // Решаем трёхдиагональную систему
        double[] c = solveTDMASystem(A, B, C, F);
        
        // Вычисляем остальные коэффициенты
        for (int i = 0; i < n - 1; i++) {
            spline.c[i] = c[i];
            spline.b[i] = (y.get(i + 1) - y.get(i)) / h[i] - h[i] * (c[i + 1] + 2 * c[i]) / 3;
            spline.d[i] = (c[i + 1] - c[i]) / (3 * h[i]);
        }
        
        return spline;
    }

    private static double[] solveTDMASystem(double[] a, double[] b, double[] c, double[] f) {
        int n = b.length;
        
        // Создаём матрицу
        Matrix matrix = new Matrix(new MatrixSize(n, n));
        
        // Заполняем трёхдиагональную матрицу
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    matrix.set(i, j, b[i]); // главная диагональ
                } else if (i == j + 1 && i > 0) {
                    matrix.set(i, j, a[i]); // нижняя диагональ
                } else if (i == j - 1 && i < n - 1) {
                    matrix.set(i, j, c[i]); // верхняя диагональ
                } else {
                    matrix.set(i, j, 0.0);
                }
            }
        }
        
        // Создаём вектор правой части
        Column rightSide = new Column(n);
        for (int i = 0; i < n; i++) {
            rightSide.set(i, f[i]);
        }
        
        // Создаём уравнение и решаем его
        Equation equation = new Equation(matrix, rightSide);
        Column solution = TDMAEquationSolver.solveTDMA(equation);
        
        // Преобразуем результат в массив
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = solution.getDouble(i);
        }
        
        return result;
    }
    
    /**
     * Значение сплайна в точке
     */
    public static double evaluateSpline(CubicSpline spline, Table table, double x) {
        List<Double> xNodes = table.getX();
        
        // Находим интервал, содержащий точку x
        int interval = findInterval(xNodes, x);
        if (interval == -1) {
            throw new IllegalArgumentException("Точка x находится вне области определения сплайна");
        }
        
        double dx = x - xNodes.get(interval);
        
        // S_i(x) = a_i + b_i*(x-x_i) + c_i*(x-x_i)^2 + d_i*(x-x_i)^3
        return spline.a[interval] + 
               spline.b[interval] * dx + 
               spline.c[interval] * dx * dx + 
               spline.d[interval] * dx * dx * dx;
    }
    
    /**
     * Поиск интервала, содержащего заданную точку
     */
    private static int findInterval(List<Double> x, double point) {
        for (int i = 0; i < x.size() - 1; i++) {
            if (point >= x.get(i) && point <= x.get(i + 1)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Вывод коэффициентов для всех интервалов
     */
    public static void printSplineCoefficients(CubicSpline spline, Table table) {
        List<Double> x = table.getX();
        
        System.out.println("Интервал\t\ta_i\t\tb_i\t\tc_i\t\td_i");
        System.out.println("-".repeat(70));
        
        for (int i = 0; i < spline.n; i++) {
            System.out.printf("[%.1f, %.1f]\t\t%.6f\t%.6f\t%.6f\t%.6f\n",
                            x.get(i), x.get(i + 1),
                            spline.a[i], spline.b[i], spline.c[i], spline.d[i]);
        }
    }
}