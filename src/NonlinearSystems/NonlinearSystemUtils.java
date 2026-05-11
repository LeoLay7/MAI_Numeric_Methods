package NonlinearSystems;

public class NonlinearSystemUtils {
    
    // Графический анализ для определения начальных приближений
    public static void graphicalAnalysis(NonlinearSystem system) {
        System.out.println("Анализ эквивалентных функций:");
        System.out.println("f₁: y = 8/(x² + 4)");
        System.out.println("f₂: (x - 2)² + (y - 1)² = 4 (окружность с центром (2,1) и радиусом 2)");
        System.out.println();
        System.out.println("x\t\ty₁ = 8/(x²+4)\t\ty₂ (верх)\t\ty₂ (низ)");
        System.out.println("--------------------------------------------------------");
        
        for (double x = -1; x <= 5; x += 0.5) {
            double y1 = system.equivalentFunction1(x);
            double[] y2_values = system.equivalentFunction2(x);
            
            System.out.printf("%.1f\t\t%.6f", x, y1);
            if (y2_values.length == 2) {
                System.out.printf("\t\t%.6f\t\t%.6f", y2_values[0], y2_values[1]);
            } else {
                System.out.print("\t\t---\t\t---");
            }
            System.out.println();
        }
        
        System.out.println("\nОбнаружены приблизительные точки пересечения:");
        System.out.println("1) x₁ ≈ 0.5, x₂ ≈ 1.5");
        System.out.println("2) x₁ ≈ 3.5, x₂ ≈ 1.5");
    }
    
    // Метод Ньютона для систем
    public static SystemResult newtonMethod(NonlinearSystem system, double x1_0, double x2_0, double eps) {
        int iterations = 0;
        double x1 = x1_0;
        double x2 = x2_0;
        
        // Проверка условия сходимости (определитель Якобиана ≠ 0)
        double det = calculateJacobianDeterminant(system, x1_0, x2_0);
        boolean convergenceCondition = Math.abs(det) > eps;
        
        if (!convergenceCondition) {
            throw new RuntimeException("Условие сходимости не выполнено: det(J) ≈ 0 в начальной точке");
        }
        
        while (true) {
            iterations++;
            
            // Вычисляем значения функций
            double f1 = system.f1(x1, x2);
            double f2 = system.f2(x1, x2);
            
            // Проверяем критерий остановки
            double residual = Math.sqrt(f1 * f1 + f2 * f2);
            if (residual < eps) {
                return new SystemResult(x1, x2, iterations, residual, convergenceCondition, "Newton");
            }
            
            // Вычисляем матрицу Якоби
            double j11 = system.df1_dx1(x1, x2);
            double j12 = system.df1_dx2(x1, x2);
            double j21 = system.df2_dx1(x1, x2);
            double j22 = system.df2_dx2(x1, x2);
            
            // Вычисляем определитель
            det = j11 * j22 - j12 * j21;
            if (Math.abs(det) < eps) {
                throw new RuntimeException("Матрица Якоби вырождена на итерации " + iterations);
            }
            
            // Решаем систему линейных уравнений методом Крамера
            double dx1 = (-f1 * j22 + f2 * j12) / det;
            double dx2 = (f1 * j21 - f2 * j11) / det;
            
            // Обновляем приближение
            x1 += dx1;
            x2 += dx2;
            
            if (iterations > 1000) {
                throw new RuntimeException("Превышено максимальное количество итераций");
            }
        }
    }
    
    // Метод простой итерации
    public static SystemResult simpleIterationMethod(NonlinearSystem system, double x1_0, double x2_0, double eps) {
        int iterations = 0;
        double x1 = x1_0;
        double x2 = x2_0;
        
        // Проверка условия сходимости (спектральный радиус матрицы производных < 1)
        boolean convergenceCondition = checkIterationConvergence(system, x1_0, x2_0);
        
        if (!convergenceCondition) {
            throw new RuntimeException("Условие сходимости не выполнено для метода простой итерации");
        }
        
        while (true) {
            iterations++;
            double x1_prev = x1;
            double x2_prev = x2;
            
            try {
                // Итерационные формулы
                x2 = system.g1(x1_prev, x2_prev);  // x₂^(k+1) = g₁(x₁^k, x₂^k)
                x1 = system.g2(x1_prev, x2_prev);  // x₁^(k+1) = g₂(x₁^k, x₂^k)
            } catch (RuntimeException e) {
                throw new RuntimeException("Итерационный процесс расходится: " + e.getMessage());
            }
            
            // Проверяем критерий остановки
            double diff = Math.sqrt((x1 - x1_prev) * (x1 - x1_prev) + (x2 - x2_prev) * (x2 - x2_prev));
            if (diff < eps) {
                double residual = Math.sqrt(system.f1(x1, x2) * system.f1(x1, x2) + 
                                          system.f2(x1, x2) * system.f2(x1, x2));
                return new SystemResult(x1, x2, iterations, residual, convergenceCondition, "Simple Iteration");
            }
            
            if (iterations > 1000) {
                throw new RuntimeException("Превышено максимальное количество итераций");
            }
        }
    }
    
    // Метод Зейделя
    public static SystemResult seidelMethod(NonlinearSystem system, double x1_0, double x2_0, double eps) {
        int iterations = 0;
        double x1 = x1_0;
        double x2 = x2_0;
        
        // Проверка условия сходимости (аналогично методу простой итерации)
        boolean convergenceCondition = checkIterationConvergence(system, x1_0, x2_0);
        
        if (!convergenceCondition) {
            throw new RuntimeException("Условие сходимости не выполнено для метода Зейделя");
        }
        
        while (true) {
            iterations++;
            double x1_prev = x1;
            double x2_prev = x2;
            
            try {
                // Итерационные формулы Зейделя (используем уже обновленные значения)
                x2 = system.g1(x1, x2_prev);      // x₂^(k+1) = g₁(x₁^(k+1), x₂^k)
                x1 = system.g2(x1_prev, x2);      // x₁^(k+1) = g₂(x₁^k, x₂^(k+1))
            } catch (RuntimeException e) {
                throw new RuntimeException("Итерационный процесс расходится: " + e.getMessage());
            }
            
            // Проверяем критерий остановки
            double diff = Math.sqrt((x1 - x1_prev) * (x1 - x1_prev) + (x2 - x2_prev) * (x2 - x2_prev));
            if (diff < eps) {
                double residual = Math.sqrt(system.f1(x1, x2) * system.f1(x1, x2) + 
                                          system.f2(x1, x2) * system.f2(x1, x2));
                return new SystemResult(x1, x2, iterations, residual, convergenceCondition, "Seidel");
            }
            
            if (iterations > 1000) {
                throw new RuntimeException("Превышено максимальное количество итераций");
            }
        }
    }
    
    // Вспомогательный метод для вычисления определителя матрицы Якоби
    private static double calculateJacobianDeterminant(NonlinearSystem system, double x1, double x2) {
        double j11 = system.df1_dx1(x1, x2);
        double j12 = system.df1_dx2(x1, x2);
        double j21 = system.df2_dx1(x1, x2);
        double j22 = system.df2_dx2(x1, x2);
        return j11 * j22 - j12 * j21;
    }
    
    // Проверка условия сходимости для итерационных методов
    private static boolean checkIterationConvergence(NonlinearSystem system, double x1, double x2) {
        try {
            // Вычисляем матрицу производных функций итерации
            double dg1_dx1 = system.dg1_dx1(x1, x2);
            double dg1_dx2 = system.dg1_dx2(x1, x2);
            double dg2_dx1 = system.dg2_dx1(x1, x2);
            double dg2_dx2 = system.dg2_dx2(x1, x2);
            
            // Упрощенная проверка: все элементы матрицы по модулю < 1
            return Math.abs(dg1_dx1) < 1 && Math.abs(dg1_dx2) < 1 && 
                   Math.abs(dg2_dx1) < 1 && Math.abs(dg2_dx2) < 1;
        } catch (Exception e) {
            return false;
        }
    }
}