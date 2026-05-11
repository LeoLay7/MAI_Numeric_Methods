package NonlinearSystems;

public class NonlinearSystemsMain {
    public static void main(String[] args) {
        double eps = 1e-3;
        NonlinearSystem system = new NonlinearSystem();
        
        System.out.println("Решение системы нелинейных уравнений:");
        System.out.println("(x₁² + 4) * x₂ - 8 = 0");
        System.out.println("(x₁ - 2)² + (x₂ - 1)² - 4 = 0");
        System.out.println("Точность: " + eps);
        System.out.println();
        
        // Графический анализ для определения начальных приближений
        System.out.println("=== ГРАФИЧЕСКИЙ АНАЛИЗ ===");
        NonlinearSystemUtils.graphicalAnalysis(system);
        System.out.println();
        
        // Найденные начальные приближения
        double[][] initialGuesses = {
            {0.5, 1.5},  // Первая точка пересечения
            {3.5, 1.5}   // Вторая точка пересечения
        };
        
        for (int i = 0; i < initialGuesses.length; i++) {
            double x1_0 = initialGuesses[i][0];
            double x2_0 = initialGuesses[i][1];
            
            System.out.println("=== РЕШЕНИЕ " + (i + 1) + " (начальное приближение: x₁=" + x1_0 + ", x₂=" + x2_0 + ") ===");
            
            // Метод Ньютона
            try {
                SystemResult newtonResult = NonlinearSystemUtils.newtonMethod(system, x1_0, x2_0, eps);
                System.out.println("Метод Ньютона:");
                System.out.println("Решение: x₁ = " + newtonResult.getX1() + ", x₂ = " + newtonResult.getX2());
                System.out.println("Количество итераций: " + newtonResult.getIterations());
                System.out.println("Невязка: " + newtonResult.getResidual());
                System.out.println("Условие сходимости выполнено: " + newtonResult.isConvergenceConditionMet());
            } catch (Exception e) {
                System.out.println("Метод Ньютона: " + e.getMessage());
            }
            System.out.println();
            
            // Метод простой итерации
            try {
                SystemResult iterationResult = NonlinearSystemUtils.simpleIterationMethod(system, x1_0, x2_0, eps);
                System.out.println("Метод простой итерации:");
                System.out.println("Решение: x₁ = " + iterationResult.getX1() + ", x₂ = " + iterationResult.getX2());
                System.out.println("Количество итераций: " + iterationResult.getIterations());
                System.out.println("Невязка: " + iterationResult.getResidual());
                System.out.println("Условие сходимости выполнено: " + iterationResult.isConvergenceConditionMet());
            } catch (Exception e) {
                System.out.println("Метод простой итерации: " + e.getMessage());
            }
            System.out.println();
            
            // Метод Зейделя
            try {
                SystemResult seidelResult = NonlinearSystemUtils.seidelMethod(system, x1_0, x2_0, eps);
                System.out.println("Метод Зейделя:");
                System.out.println("Решение: x₁ = " + seidelResult.getX1() + ", x₂ = " + seidelResult.getX2());
                System.out.println("Количество итераций: " + seidelResult.getIterations());
                System.out.println("Невязка: " + seidelResult.getResidual());
                System.out.println("Условие сходимости выполнено: " + seidelResult.isConvergenceConditionMet());
            } catch (Exception e) {
                System.out.println("Метод Зейделя: " + e.getMessage());
            }
            System.out.println();
        }
    }
}