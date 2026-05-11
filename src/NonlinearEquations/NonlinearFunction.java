package NonlinearEquations;

public class NonlinearFunction {
    
    // Функция f(x) = 2^x - x² - 0.5
    public double evaluate(double x) {
        return Math.pow(2, x) - x * x - 0.5;
    }
    
    // Производная f'(x) = 2^x * ln(2) - 2x
    public double derivative(double x) {
        return Math.pow(2, x) * Math.log(2) - 2 * x;
    }
    
    // Функция для метода простой итерации: x = g(x)
    // Для разных интервалов используем разные преобразования
    public double iterationFunction(double x, boolean isPositiveRoot) {
        if (isPositiveRoot) {
            // Для положительного корня: x = sqrt(2^x - 0.5)
            double value = Math.pow(2, x) - 0.5;
            if (value < 0) {
                throw new RuntimeException("Отрицательное значение под корнем в функции итерации");
            }
            return Math.sqrt(value);
        } else {
            // Для отрицательного корня: x = -sqrt(2^x - 0.5)
            double value = Math.pow(2, x) - 0.5;
            if (value < 0) {
                throw new RuntimeException("Отрицательное значение под корнем в функции итерации");
            }
            return -Math.sqrt(value);
        }
    }
    
    // Производная функции итерации g'(x) для проверки условия сходимости
    public double iterationDerivative(double x, boolean isPositiveRoot) {
        double value = Math.pow(2, x) - 0.5;
        if (value <= 0) {
            return Double.MAX_VALUE; // Условие сходимости не выполнено
        }
        
        double derivative = (Math.pow(2, x) * Math.log(2)) / (2 * Math.sqrt(value));
        return isPositiveRoot ? derivative : -derivative;
    }
}