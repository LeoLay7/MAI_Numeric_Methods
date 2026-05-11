package ODE;

public class ODEFunction {
    
    // Дифференциальное уравнение: y' + y - e^x = 0
    // Приводим к виду: y' = f(x, y) = e^x - y
    public double f(double x, double y) {
        return Math.exp(x) - y;
    }
    
    // Аналитическое решение
    // Для уравнения y' + y - e^x = 0, или y' = e^x - y
    // Это линейное ОДУ первого порядка
    // Решение: y = e^x * (x + C)
    // Из начального условия y(-2) = 7.456:
    // 7.456 = e^(-2) * (-2 + C)
    // C = 7.456 / e^(-2) + 2 = 7.456 * e^2 + 2
    public double analyticalSolution(double x) {
        double C = 7.456 * Math.exp(2) + 2;
        return Math.exp(x) * (x + C);
    }
    
    // Проверка начального условия
    public boolean checkInitialCondition(double x0, double y0) {
        double expected = analyticalSolution(x0);
        return Math.abs(y0 - expected) < 1e-10;
    }
}