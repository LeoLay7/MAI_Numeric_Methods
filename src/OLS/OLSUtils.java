package OLS;

import common.table.Table;
import java.util.List;

public class OLSUtils {

    public static int[] getSelectedPoints(Table table, int degree) {
        return selectClosestPoints(table.getX(), table.getDot(), degree + 1);
    }

    /**
     * Лагранж
     */
    public static double lagrangeInterpolation(Table table, int degree) {
        List<Double> x = table.getX();
        List<Double> y = table.getY();
        double xStar = table.getDot();
        
        // Выбираем ближайшие точки к x*
        int[] indices = selectClosestPoints(x, xStar, degree + 1);
        
        double result = 0.0;
        
        for (int i = 0; i < indices.length; i++) {
            double li = 1.0;
            int idx_i = indices[i];
            
            for (int j = 0; j < indices.length; j++) {
                if (i != j) {
                    int idx_j = indices[j];
                    li *= (xStar - x.get(idx_j)) / (x.get(idx_i) - x.get(idx_j));
                }
            }
            
            result += y.get(idx_i) * li;
        }
        
        return result;
    }
    
    /**
     * Ньютон
     */
    public static double newtonInterpolation(Table table, int degree) {
        List<Double> x = table.getX();
        List<Double> y = table.getY();
        double xStar = table.getDot();
        
        // Выбираем ближайшие точки к x*
        int[] indices = selectClosestPoints(x, xStar, degree + 1);
        
        // Строим таблицу разделенных разностей
        double[][] dividedDiff = new double[indices.length][indices.length];
        
        // Заполняем первый столбец значениями y
        for (int i = 0; i < indices.length; i++) {
            dividedDiff[i][0] = y.get(indices[i]);
        }
        
        // Вычисляем разделенные разности
        for (int j = 1; j < indices.length; j++) {
            for (int i = 0; i < indices.length - j; i++) {
                dividedDiff[i][j] = (dividedDiff[i + 1][j - 1] - dividedDiff[i][j - 1]) / 
                                   (x.get(indices[i + j]) - x.get(indices[i]));
            }
        }
        
        // Вычисляем значение многочлена Ньютона
        double result = dividedDiff[0][0];
        double product = 1.0;
        
        for (int i = 1; i < indices.length; i++) {
            product *= (xStar - x.get(indices[i - 1]));
            result += dividedDiff[0][i] * product;
        }
        
        return result;
    }
    
    /**
     * Выбор ближайших к x* точек
     */
    private static int[] selectClosestPoints(List<Double> x, double xStar, int count) {
        int n = x.size();
        int[] indices = new int[count];
        double[] distances = new double[n];
        
        // Вычисляем расстояния от x* до всех точек
        for (int i = 0; i < n; i++) {
            distances[i] = Math.abs(x.get(i) - xStar);
        }
        
        // Находим индексы ближайших точек
        for (int i = 0; i < count; i++) {
            int minIndex = 0;
            double minDistance = Double.MAX_VALUE;
            
            for (int j = 0; j < n; j++) {
                if (distances[j] < minDistance) {
                    boolean alreadySelected = false;
                    for (int k = 0; k < i; k++) {
                        if (indices[k] == j) {
                            alreadySelected = true;
                            break;
                        }
                    }
                    if (!alreadySelected) {
                        minDistance = distances[j];
                        minIndex = j;
                    }
                }
            }
            
            indices[i] = minIndex;
        }
        
        // Сортируем индексы по возрастанию x
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (x.get(indices[i]) > x.get(indices[j])) {
                    int temp = indices[i];
                    indices[i] = indices[j];
                    indices[j] = temp;
                }
            }
        }
        
        return indices;
    }
    
    /**
     * Погрешность
     */
    public static double estimateError(Table table, int degree, String method) {
        List<Double> x = table.getX();
        List<Double> y = table.getY();
        double xStar = table.getDot();
        
        // Находим максимальную разность между соседними значениями y
        double maxDiff = 0.0;
        for (int i = 0; i < y.size() - 1; i++) {
            double diff = Math.abs(y.get(i + 1) - y.get(i));
            if (diff > maxDiff) {
                maxDiff = diff;
            }
        }
        
        // Находим минимальный шаг по x
        double minStep = Double.MAX_VALUE;
        for (int i = 0; i < x.size() - 1; i++) {
            double step = Math.abs(x.get(i + 1) - x.get(i));
            if (step < minStep) {
                minStep = step;
            }
        }
        
        // Оценка погрешности как функция от степени многочлена
        double factorial = 1.0;
        for (int i = 1; i <= degree + 1; i++) {
            factorial *= i;
        }
        
        // Приближенная оценка погрешности
        double error = (maxDiff / factorial) * Math.pow(minStep, degree + 1);
        
        return Math.abs(error);
    }
}
