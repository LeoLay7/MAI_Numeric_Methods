package Integration;

public class IntegrationUtils {
    
    // Метод средних прямоугольников
    public static IntegrationResult midpointRule(IntegrationFunction function, double a, double b, double eps) {
        int n = 4; // Начальное количество разбиений
        double h = (b - a) / n;
        double integral1 = calculateMidpoint(function, a, b, n);
        
        while (true) {
            n *= 2;
            h = (b - a) / n;
            double integral2 = calculateMidpoint(function, a, b, n);
            
            double error = Math.abs(integral2 - integral1) / 3.0; // Для метода 2-го порядка
            
            if (error < eps) {
                return new IntegrationResult(integral2, n, error, integral1);
            }
            
            integral1 = integral2;
        }
    }
    
    private static double calculateMidpoint(IntegrationFunction function, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0.0;
        
        for (int i = 0; i < n; i++) {
            double x = a + (i + 0.5) * h;
            sum += function.evaluate(x);
        }
        
        return sum * h;
    }
    
    // Метод трапеций
    public static IntegrationResult trapezoidRule(IntegrationFunction function, double a, double b, double eps) {
        int n = 4; // Начальное количество разбиений
        double integral1 = calculateTrapezoid(function, a, b, n);
        
        while (true) {
            n *= 2;
            double integral2 = calculateTrapezoid(function, a, b, n);
            
            double error = Math.abs(integral2 - integral1) / 3.0; // Для метода 2-го порядка
            
            if (error < eps) {
                return new IntegrationResult(integral2, n, error, integral1);
            }
            
            integral1 = integral2;
        }
    }
    
    private static double calculateTrapezoid(IntegrationFunction function, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = (function.evaluate(a) + function.evaluate(b)) / 2.0;
        
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += function.evaluate(x);
        }
        
        return sum * h;
    }
    
    // Метод Симпсона
    public static IntegrationResult simpsonRule(IntegrationFunction function, double a, double b, double eps) {
        int n = 4; // Начальное количество разбиений (должно быть четным)
        double integral1 = calculateSimpson(function, a, b, n);
        
        while (true) {
            n *= 2;
            double integral2 = calculateSimpson(function, a, b, n);
            
            double error = Math.abs(integral2 - integral1) / 15.0; // Для метода 4-го порядка
            
            if (error < eps) {
                return new IntegrationResult(integral2, n, error, integral1);
            }
            
            integral1 = integral2;
        }
    }
    
    private static double calculateSimpson(IntegrationFunction function, double a, double b, int n) {
        if (n % 2 != 0) n++; // Убеждаемся, что n четное
        
        double h = (b - a) / n;
        double sum = function.evaluate(a) + function.evaluate(b);
        
        // Нечетные индексы (коэффициент 4)
        for (int i = 1; i < n; i += 2) {
            double x = a + i * h;
            sum += 4 * function.evaluate(x);
        }
        
        // Четные индексы (коэффициент 2)
        for (int i = 2; i < n; i += 2) {
            double x = a + i * h;
            sum += 2 * function.evaluate(x);
        }
        
        return sum * h / 3.0;
    }
    
    // Метод Рунге-Ромберга для уточнения результата
    public static double rungeRombergCorrection(IntegrationResult result, int order) {
        double I_h = result.getValueWithHalfStep();
        double I_h2 = result.getValue();
        
        // Формула Рунге-Ромберга: I_уточн = I_h/2 + (I_h/2 - I_h)/(2^p - 1)
        return I_h2 + (I_h2 - I_h) / (Math.pow(2, order) - 1);
    }
}