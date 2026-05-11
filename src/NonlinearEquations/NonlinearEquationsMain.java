package NonlinearEquations;

public class NonlinearEquationsMain {
    public static void main(String[] args) {
        double eps = 0.001;
        NonlinearFunction function = new NonlinearFunction();
        
        System.out.println("Решение нелинейного уравнения: 2^x - x² - 0.5 = 0");
        System.out.println("Точность: " + eps);
        System.out.println();
        
        // Графический анализ для определения начальных приближений
        System.out.println("=== ГРАФИЧЕСКИЙ АНАЛИЗ ===");
        NonlinearUtils.graphicalAnalysis(function);
        System.out.println();
        
        // Найденные интервалы: [-1, 0] и [1, 2]
        double[] intervals = {-1.0, 0.0, 1.0, 2.0};
        
        for (int i = 0; i < intervals.length - 1; i += 2) {
            double a = intervals[i];
            double b = intervals[i + 1];
            
            System.out.println("=== КОРЕНЬ НА ИНТЕРВАЛЕ [" + a + ", " + b + "] ===");
            
            // Метод дихотомии
            NonlinearResult bisectionResult = NonlinearUtils.bisectionMethod(function, a, b, eps);
            System.out.println("Метод дихотомии:");
            System.out.println("Корень: " + bisectionResult.getRoot());
            System.out.println("Количество итераций: " + bisectionResult.getIterations());
            System.out.println("Значение функции: " + function.evaluate(bisectionResult.getRoot()));
            System.out.println();
            
            // Метод простой итерации
            double x0 = (a + b) / 2; // начальное приближение
            boolean isPositiveRoot = x0 > 0; // определяем тип корня
            try {
                NonlinearResult iterationResult = NonlinearUtils.simpleIterationMethod(function, x0, eps, isPositiveRoot);
                System.out.println("Метод простой итерации:");
                System.out.println("Корень: " + iterationResult.getRoot());
                System.out.println("Количество итераций: " + iterationResult.getIterations());
                System.out.println("Значение функции: " + function.evaluate(iterationResult.getRoot()));
                System.out.println("Условие сходимости выполнено: " + iterationResult.isConvergenceConditionMet());
            } catch (Exception e) {
                System.out.println("Метод простой итерации: " + e.getMessage());
            }
            System.out.println();
            
            // Метод Ньютона
            try {
                NonlinearResult newtonResult = NonlinearUtils.newtonMethod(function, x0, eps);
                System.out.println("Метод Ньютона:");
                System.out.println("Корень: " + newtonResult.getRoot());
                System.out.println("Количество итераций: " + newtonResult.getIterations());
                System.out.println("Значение функции: " + function.evaluate(newtonResult.getRoot()));
                System.out.println("Условие сходимости выполнено: " + newtonResult.isConvergenceConditionMet());
            } catch (Exception e) {
                System.out.println("Метод Ньютона: " + e.getMessage());
            }
            System.out.println();
        }
    }
}