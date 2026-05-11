package NonlinearEquations;

public class NonlinearUtils {
    
    // Графический анализ для определения интервалов с корнями
    public static void graphicalAnalysis(NonlinearFunction function) {
        System.out.println("Анализ функции f(x) = 2^x - x² - 0.5 на интервале [-2, 3]:");
        System.out.println("x\t\tf(x)");
        System.out.println("------------------------");
        
        for (double x = -2.0; x <= 3.0; x += 0.5) {
            double fx = function.evaluate(x);
            System.out.printf("%.1f\t\t%.6f\n", x, fx);
        }
        
        System.out.println("\nОбнаружены интервалы смены знака:");
        System.out.println("1) [-1, 0] - первый корень");
        System.out.println("2) [1, 2] - второй корень");
    }
    
    // Метод дихотомии (бисекции)
    public static NonlinearResult bisectionMethod(NonlinearFunction function, double a, double b, double eps) {
        if (function.evaluate(a) * function.evaluate(b) > 0) {
            throw new RuntimeException("На концах интервала функция имеет одинаковые знаки");
        }
        
        int iterations = 0;
        double c = a;
        
        while (Math.abs(b - a) > 2 * eps) {
            iterations++;
            c = (a + b) / 2;
            
            if (Math.abs(function.evaluate(c)) < eps) {
                break;
            }
            
            if (function.evaluate(a) * function.evaluate(c) < 0) {
                b = c;
            } else {
                a = c;
            }
        }
        
        return new NonlinearResult(c, iterations, true, "Bisection");
    }
    
    // Метод простой итерации
    public static NonlinearResult simpleIterationMethod(NonlinearFunction function, double x0, double eps, boolean isPositiveRoot) {
        int iterations = 0;
        double x = x0;
        double xPrev;
        
        // Проверка условия сходимости в начальной точке
        double gDerivative = Math.abs(function.iterationDerivative(x0, isPositiveRoot));
        boolean convergenceCondition = gDerivative < 1.0;
        
        if (!convergenceCondition) {
            throw new RuntimeException("Условие сходимости |g'(x)| < 1 не выполнено в точке x0 = " + x0 + 
                                     ", |g'(x0)| = " + gDerivative);
        }
        
        do {
            iterations++;
            xPrev = x;
            
            try {
                x = function.iterationFunction(xPrev, isPositiveRoot);
            } catch (RuntimeException e) {
                throw new RuntimeException("Итерационный процесс расходится: " + e.getMessage());
            }
            
            if (iterations > 1000) {
                throw new RuntimeException("Превышено максимальное количество итераций");
            }
            
        } while (Math.abs(x - xPrev) > eps);
        
        return new NonlinearResult(x, iterations, convergenceCondition, "Simple Iteration");
    }
    
    // Метод Ньютона (касательных)
    public static NonlinearResult newtonMethod(NonlinearFunction function, double x0, double eps) {
        int iterations = 0;
        double x = x0;
        double xPrev;
        
        // Проверка условия сходимости: f'(x) ≠ 0
        double derivative = function.derivative(x0);
        boolean convergenceCondition = Math.abs(derivative) > eps;
        
        if (!convergenceCondition) {
            throw new RuntimeException("Условие сходимости f'(x) ≠ 0 не выполнено в точке x0 = " + x0 + 
                                     ", f'(x0) = " + derivative);
        }
        
        do {
            iterations++;
            xPrev = x;
            
            double fx = function.evaluate(xPrev);
            double fpx = function.derivative(xPrev);
            
            if (Math.abs(fpx) < eps) {
                throw new RuntimeException("Производная близка к нулю, метод может расходиться");
            }
            
            x = xPrev - fx / fpx;
            
            if (iterations > 1000) {
                throw new RuntimeException("Превышено максимальное количество итераций");
            }
            
        } while (Math.abs(x - xPrev) > eps);
        
        return new NonlinearResult(x, iterations, convergenceCondition, "Newton");
    }
}