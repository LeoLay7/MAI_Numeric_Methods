package Integration;

public class IntegrationFunction {
    
    public double evaluate(double x) {
        // Функция: (1+2/sqrt(x))/(sqrt(x) + x)^2
        double sqrtX = Math.sqrt(x);
        double numerator = 1 + 2 / sqrtX;
        double denominator = Math.pow(sqrtX + x, 2);
        return numerator / denominator;
    }
}