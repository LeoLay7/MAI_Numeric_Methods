package Integration;

public class IntegrationMain {
    public static void main(String[] args) {
        double a = 0.5;
        double b = 3.0;
        double eps = 1e-3;
        
        IntegrationFunction function = new IntegrationFunction();
        
        System.out.println("Численное интегрирование функции (1+2/sqrt(x))/(sqrt(x) + x)^2");
        System.out.println("Интервал: [" + a + ", " + b + "]");
        System.out.println("Точность: " + eps);
        System.out.println();
        
        // Метод средних прямоугольников
        IntegrationResult midpointResult = IntegrationUtils.midpointRule(function, a, b, eps);
        System.out.println("Метод средних прямоугольников:");
        System.out.println("Значение интеграла: " + midpointResult.getValue());
        System.out.println("Количество разбиений: " + midpointResult.getN());
        System.out.println("Погрешность: " + midpointResult.getError());
        
        // Уточнение методом Рунге-Ромберга
        double midpointRomberg = IntegrationUtils.rungeRombergCorrection(midpointResult, 2);
        System.out.println("Уточненное значение (Рунге-Ромберг): " + midpointRomberg);
        System.out.println();
        
        // Метод трапеций
        IntegrationResult trapezoidResult = IntegrationUtils.trapezoidRule(function, a, b, eps);
        System.out.println("Метод трапеций:");
        System.out.println("Значение интеграла: " + trapezoidResult.getValue());
        System.out.println("Количество разбиений: " + trapezoidResult.getN());
        System.out.println("Погрешность: " + trapezoidResult.getError());
        
        // Уточнение методом Рунге-Ромберга
        double trapezoidRomberg = IntegrationUtils.rungeRombergCorrection(trapezoidResult, 2);
        System.out.println("Уточненное значение (Рунге-Ромберг): " + trapezoidRomberg);
        System.out.println();
        
        // Метод Симпсона
        IntegrationResult simpsonResult = IntegrationUtils.simpsonRule(function, a, b, eps);
        System.out.println("Метод Симпсона:");
        System.out.println("Значение интеграла: " + simpsonResult.getValue());
        System.out.println("Количество разбиений: " + simpsonResult.getN());
        System.out.println("Погрешность: " + simpsonResult.getError());
        
        // Уточнение методом Рунге-Ромберга
        double simpsonRomberg = IntegrationUtils.rungeRombergCorrection(simpsonResult, 4);
        System.out.println("Уточненное значение (Рунге-Ромберг): " + simpsonRomberg);
    }
}