package OLS;

import common.table.Table;
import common.table.TableUtils;

public class OLSMain {
    public static void main(String[] args) {
        one();
        System.out.println("\n" + "=".repeat(80) + "\n");
        two();
        System.out.println("\n" + "=".repeat(80) + "\n");
        three();
    }

    public static void one() {
        Table table = TableUtils.fromFile("C:\\Code\\self\\MAI_Numeric_Methods\\resources\\table\\table1.txt");
        
        System.out.println("=== ИНТЕРПОЛЯЦИОННЫЕ МНОГОЧЛЕНЫ ===");
        // Многочлен Лагранжа 2-й степени
        System.out.println("=== МНОГОЧЛЕН ЛАГРАНЖА 2-Й СТЕПЕНИ ===");
        int[] indices2 = OLSUtilsOne.getSelectedPoints(table, 2);
        System.out.print("Выбранные точки: ");
        for (int i = 0; i < indices2.length; i++) {
            System.out.printf("(%.2f, %.4f) ", table.getFromX(indices2[i]), table.getFromY(indices2[i]));
        }
        System.out.println();
        double lagrange2 = OLSUtilsOne.lagrangeInterpolation(table, 2);
        System.out.printf("Значение в точке %.3f = %.6f\n", table.getDot(), lagrange2);
        double errorLagrange2 = OLSUtilsOne.estimateError(table, 2, "lagrange");
        System.out.printf("Оценка погрешности: %.6f\n\n", errorLagrange2);

        System.out.println("=== МНОГОЧЛЕН ЛАГРАНЖА 3-Й СТЕПЕНИ ===");
        int[] indices3 = OLSUtilsOne.getSelectedPoints(table, 3);
        System.out.print("Выбранные точки: ");
        for (int i = 0; i < indices3.length; i++) {
            System.out.printf("(%.2f, %.4f) ", table.getFromX(indices3[i]), table.getFromY(indices3[i]));
        }
        System.out.println();
        double lagrange3 = OLSUtilsOne.lagrangeInterpolation(table, 3);
        System.out.printf("Значние в точке %.3f = %.6f\n", table.getDot(), lagrange3);
        double errorLagrange3 = OLSUtilsOne.estimateError(table, 3, "lagrange");
        System.out.printf("Оценка погрешности: %.6f\n\n", errorLagrange3);
        
        System.out.println("=== МНОГОЧЛЕН НЬЮТОНА 2-Й СТЕПЕНИ ===");
        System.out.print("Выбранные точки: ");
        for (int i = 0; i < indices2.length; i++) {
            System.out.printf("(%.2f, %.4f) ", table.getFromX(indices2[i]), table.getFromY(indices2[i]));
        }
        System.out.println();
        double newton2 = OLSUtilsOne.newtonInterpolation(table, 2);
        System.out.printf("N2(%.3f) = %.6f\n", table.getDot(), newton2);
        double errorNewton2 = OLSUtilsOne.estimateError(table, 2, "newton");
        System.out.printf("Оценка погрешности: %.6f\n\n", errorNewton2);
        
        System.out.println("=== МНОГОЧЛЕН НЬЮТОНА 3-Й СТЕПЕНИ ===");
        System.out.print("Выбранные точки: ");
        for (int i = 0; i < indices3.length; i++) {
            System.out.printf("(%.2f, %.4f) ", table.getFromX(indices3[i]), table.getFromY(indices3[i]));
        }
        System.out.println();
        double newton3 = OLSUtilsOne.newtonInterpolation(table, 3);
        System.out.printf("N3(%.3f) = %.6f\n", table.getDot(), newton3);
        double errorNewton3 = OLSUtilsOne.estimateError(table, 3, "newton");
        System.out.printf("Оценка погрешности: %.6f\n", errorNewton3);
    }

    public static void two() {
        Table table = TableUtils.fromFile("C:\\Code\\self\\MAI_Numeric_Methods\\resources\\table\\table2.txt");
        
        System.out.println("=== ЕСТЕСТВЕННЫЙ КУБИЧЕСКИЙ СПЛАЙН ===");
        
        // Построение кубического сплайна
        OLSUtilsTwo.CubicSpline spline = OLSUtilsTwo.buildCubicSpline(table);
        
        // Вывод коэффициентов сплайна
        System.out.println("Коэффициенты кубического сплайна:");
        OLSUtilsTwo.printSplineCoefficients(spline, table);
        
        // Вычисление значения в точке x*
        double result = OLSUtilsTwo.evaluateSpline(spline, table, table.getDot());
        System.out.printf("\nЗначение сплайна в точке x* = %.3f: S(%.3f) = %.6f\n", 
                         table.getDot(), table.getDot(), result);
    }

    public static void three() {
        Table table = TableUtils.fromFile("C:\\Code\\self\\MAI_Numeric_Methods\\resources\\table\\table3.txt");
        
        System.out.println("=== МЕТОД НАИМЕНЬШИХ КВАДРАТОВ ===");
        
        // Построение приближающих многочленов 1, 2 и 3 степени
        OLSUtilsThree.Polynomial[] polynomials = new OLSUtilsThree.Polynomial[3];
        double[] errors = new double[3];
        
        for (int degree = 1; degree <= 3; degree++) {
            polynomials[degree - 1] = OLSUtilsThree.buildLeastSquaresPolynomial(table, degree);
            errors[degree - 1] = OLSUtilsThree.calculateSumOfSquaredErrors(table, polynomials[degree - 1]);
            
            System.out.printf("=== МНОГОЧЛЕН %d-Й СТЕПЕНИ ===\n", degree);
            System.out.println(polynomials[degree - 1].toString());
            System.out.printf("Сумма квадратов ошибок: %.6f\n", errors[degree - 1]);
            System.out.printf("P%d(%.3f) = %.6f\n\n", degree, table.getDot(), 
                             polynomials[degree - 1].evaluate(table.getDot()));
        }

        System.out.println();
        OLSUtilsThree.printGraphData(table, polynomials);
    }
}