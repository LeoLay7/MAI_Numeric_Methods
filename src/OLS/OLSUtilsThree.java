package OLS;

import common.table.Table;
import common.equation.Equation;
import common.matrix.Column;
import common.matrix.Matrix;
import common.matrix.MatrixSize;
import LU.LUEquationSolver;
import java.util.List;

public class OLSUtilsThree {

    public static class Polynomial {
        public double[] coefficients;
        public int degree;
        
        public Polynomial(int degree) {
            this.degree = degree;
            this.coefficients = new double[degree + 1];
        }

        public double evaluate(double x) {
            double result = 0.0;
            double xPower = 1.0;
            
            for (int i = 0; i <= degree; i++) {
                result += coefficients[i] * xPower;
                xPower *= x;
            }
            
            return result;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("P(x) = ");
            
            for (int i = 0; i <= degree; i++) {
                if (i > 0 && coefficients[i] >= 0) {
                    sb.append(" + ");
                } else if (i > 0) {
                    sb.append(" ");
                }
                
                if (i == 0) {
                    sb.append(String.format("%.6f", coefficients[i]));
                } else if (i == 1) {
                    sb.append(String.format("%.6f*x", coefficients[i]));
                } else {
                    sb.append(String.format("%.6f*x^%d", coefficients[i], i));
                }
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Построение приближающего многочлена
     */
    public static Polynomial buildLeastSquaresPolynomial(Table table, int degree) {
        List<Double> x = table.getX();
        List<Double> y = table.getY();
        int n = x.size();
        
        // Создаем систему нормальных уравнений A*c = b
        double[][] A = new double[degree + 1][degree + 1];
        double[] b = new double[degree + 1];
        
        // Заполняем матрицу A и вектор b
        for (int i = 0; i <= degree; i++) {
            for (int j = 0; j <= degree; j++) {
                A[i][j] = 0.0;
                for (int k = 0; k < n; k++) {
                    A[i][j] += Math.pow(x.get(k), i + j);
                }
            }
            
            b[i] = 0.0;
            for (int k = 0; k < n; k++) {
                b[i] += y.get(k) * Math.pow(x.get(k), i);
            }
        }
        
        // Решаем систему используя готовый LU решатель
        double[] coefficients = solveLUSystem(A, b);
        
        Polynomial polynomial = new Polynomial(degree);
        polynomial.coefficients = coefficients;
        
        return polynomial;
    }

    private static double[] solveLUSystem(double[][] A, double[] b) {
        int n = b.length;

        Matrix matrix = new Matrix(new MatrixSize(n, n));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix.set(i, j, A[i][j]);
            }
        }

        Column rightSide = new Column(n);
        for (int i = 0; i < n; i++) {
            rightSide.set(i, b[i]);
        }

        Equation equation = new Equation(matrix, rightSide);
        Column solution = LUEquationSolver.solveLU(equation);

        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = solution.getDouble(i);
        }
        
        return result;
    }
    
    /**
     * сумма квадратов ошибок
     */
    public static double calculateSumOfSquaredErrors(Table table, Polynomial polynomial) {
        List<Double> x = table.getX();
        List<Double> y = table.getY();
        double sum = 0.0;
        
        for (int i = 0; i < x.size(); i++) {
            double predicted = polynomial.evaluate(x.get(i));
            double error = y.get(i) - predicted;
            sum += error * error;
        }
        
        return sum;
    }

    public static void printGraphData(Table table, Polynomial[] polynomials) {
        List<Double> x = table.getX();
        List<Double> y = table.getY();
        
        System.out.println("Данные для построения графика:");
        System.out.println("x\t\tf(x)\t\tP1(x)\t\tP2(x)\t\tP3(x)");
        System.out.println("-".repeat(80));
        
        // Сохраняем данные в файл
        try (java.io.PrintWriter writer = new java.io.PrintWriter("C:\\Code\\self\\MAI_Numeric_Methods\\resources\\graph\\olsres.txt")) {
            writer.println("# Данные для построения графика МНК");
            writer.println("# x f(x) P1(x) P2(x) P3(x)");
            
            for (int i = 0; i < x.size(); i++) {
                double xi = x.get(i);
                double yi = y.get(i);
                double p1 = polynomials[0].evaluate(xi);
                double p2 = polynomials[1].evaluate(xi);
                double p3 = polynomials[2].evaluate(xi);
                
                System.out.printf("%.2f\t\t%.4f\t\t%.4f\t\t%.4f\t\t%.4f\n", xi, yi, p1, p2, p3);
                writer.printf("%.6f %.6f %.6f %.6f %.6f\n", xi, yi, p1, p2, p3);
            }
            
            // Добавляем расширенные данные для гладкого графика
            writer.println("# Расширенные данные для гладкого графика");
            writer.println("# x P1(x) P2(x) P3(x)");
            
            double xMin = x.get(0);
            double xMax = x.get(x.size() - 1);
            double step = (xMax - xMin) / 100; // 100 точек для гладкого графика
            
            for (double xi = xMin; xi <= xMax; xi += step) {
                double p1 = polynomials[0].evaluate(xi);
                double p2 = polynomials[1].evaluate(xi);
                double p3 = polynomials[2].evaluate(xi);
                writer.printf("%.6f %.6f %.6f %.6f\n", xi, p1, p2, p3);
            }
            
        } catch (java.io.FileNotFoundException e) {
            System.err.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }
}