package ODE;

import java.util.ArrayList;
import java.util.List;

public class ODEUtils {
    
    // Метод Эйлера-Коши (улучшенный метод Эйлера)
    public static ODEResult eulerCauchyMethod(ODEFunction function, double x0, double y0, double xEnd, double h) {
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        
        double x = x0;
        double y = y0;
        
        xList.add(x);
        yList.add(y);
        
        while (x < xEnd) {
            // Ограничиваем шаг, чтобы не выйти за границу
            if (x + h > xEnd) {
                h = xEnd - x;
            }
            
            // Метод Эйлера-Коши (предиктор-корректор)
            // Предиктор: y_{n+1}^{(0)} = y_n + h * f(x_n, y_n)
            double yPredictor = y + h * function.f(x, y);
            
            // Корректор: y_{n+1} = y_n + h/2 * [f(x_n, y_n) + f(x_{n+1}, y_{n+1}^{(0)})]
            double yCorrector = y + h / 2.0 * (function.f(x, y) + function.f(x + h, yPredictor));
            
            x += h;
            y = yCorrector;
            
            xList.add(x);
            yList.add(y);
        }
        
        return new ODEResult(
            xList.stream().mapToDouble(Double::doubleValue).toArray(),
            yList.stream().mapToDouble(Double::doubleValue).toArray(),
            "Euler-Cauchy",
            h
        );
    }
    
    // Метод Рунге-Кутты 4-го порядка
    public static ODEResult rungeKutta4Method(ODEFunction function, double x0, double y0, double xEnd, double h) {
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        
        double x = x0;
        double y = y0;
        
        xList.add(x);
        yList.add(y);
        
        while (x < xEnd) {
            // Ограничиваем шаг, чтобы не выйти за границу
            if (x + h > xEnd) {
                h = xEnd - x;
            }
            
            // Коэффициенты Рунге-Кутты
            double k1 = h * function.f(x, y);
            double k2 = h * function.f(x + h/2.0, y + k1/2.0);
            double k3 = h * function.f(x + h/2.0, y + k2/2.0);
            double k4 = h * function.f(x + h, y + k3);
            
            // Формула Рунге-Кутты 4-го порядка
            y = y + (k1 + 2*k2 + 2*k3 + k4) / 6.0;
            x += h;
            
            xList.add(x);
            yList.add(y);
        }
        
        return new ODEResult(
            xList.stream().mapToDouble(Double::doubleValue).toArray(),
            yList.stream().mapToDouble(Double::doubleValue).toArray(),
            "Runge-Kutta 4",
            h
        );
    }
    
    // Простой метод Эйлера (для сравнения)
    public static ODEResult simpleEulerMethod(ODEFunction function, double x0, double y0, double xEnd, double h) {
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        
        double x = x0;
        double y = y0;
        
        xList.add(x);
        yList.add(y);
        
        while (x < xEnd) {
            if (x + h > xEnd) {
                h = xEnd - x;
            }
            
            // Простая формула Эйлера: y_{n+1} = y_n + h * f(x_n, y_n)
            y = y + h * function.f(x, y);
            x += h;
            
            xList.add(x);
            yList.add(y);
        }
        
        return new ODEResult(
            xList.stream().mapToDouble(Double::doubleValue).toArray(),
            yList.stream().mapToDouble(Double::doubleValue).toArray(),
            "Simple Euler",
            h
        );
    }
    
    // Вычисление максимальной погрешности
    public static double calculateMaxError(ODEResult result, ODEFunction function) {
        double[] xValues = result.getXValues();
        double[] yValues = result.getYValues();
        double maxError = 0;
        
        for (int i = 0; i < xValues.length; i++) {
            double analytical = function.analyticalSolution(xValues[i]);
            double error = Math.abs(yValues[i] - analytical);
            maxError = Math.max(maxError, error);
        }
        
        return maxError;
    }
    
    // Вычисление среднеквадратичной погрешности
    public static double calculateRMSError(ODEResult result, ODEFunction function) {
        double[] xValues = result.getXValues();
        double[] yValues = result.getYValues();
        double sumSquaredErrors = 0;
        
        for (int i = 0; i < xValues.length; i++) {
            double analytical = function.analyticalSolution(xValues[i]);
            double error = yValues[i] - analytical;
            sumSquaredErrors += error * error;
        }
        
        return Math.sqrt(sumSquaredErrors / xValues.length);
    }
}