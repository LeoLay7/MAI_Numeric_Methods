package NonlinearSystems;

public class NonlinearSystem {
    
    // Система уравнений:
    // f₁(x₁, x₂) = (x₁² + 4) * x₂ - 8 = 0
    // f₂(x₁, x₂) = (x₁ - 2)² + (x₂ - 1)² - 4 = 0
    
    public double f1(double x1, double x2) {
        return (x1 * x1 + 4) * x2 - 8;
    }
    
    public double f2(double x1, double x2) {
        return (x1 - 2) * (x1 - 2) + (x2 - 1) * (x2 - 1) - 4;
    }
    
    // Частные производные для метода Ньютона (матрица Якоби)
    // ∂f₁/∂x₁ = 2x₁ * x₂
    public double df1_dx1(double x1, double x2) {
        return 2 * x1 * x2;
    }
    
    // ∂f₁/∂x₂ = x₁² + 4
    public double df1_dx2(double x1, double x2) {
        return x1 * x1 + 4;
    }
    
    // ∂f₂/∂x₁ = 2(x₁ - 2)
    public double df2_dx1(double x1, double x2) {
        return 2 * (x1 - 2);
    }
    
    // ∂f₂/∂x₂ = 2(x₂ - 1)
    public double df2_dx2(double x1, double x2) {
        return 2 * (x2 - 1);
    }
    
    // Функции для метода простой итерации:
    // Из f₁ = 0: x₂ = 8/(x₁² + 4)
    // Из f₂ = 0: x₁ = 2 ± √(4 - (x₂ - 1)²)
    
    public double g1(double x1, double x2) {
        return 8.0 / (x1 * x1 + 4);
    }
    
    public double g2(double x1, double x2) {
        double discriminant = 4 - (x2 - 1) * (x2 - 1);
        if (discriminant < 0) {
            throw new RuntimeException("Отрицательное значение под корнем в функции итерации");
        }
        // Выбираем знак в зависимости от текущего значения x1
        return x1 >= 2 ? 2 + Math.sqrt(discriminant) : 2 - Math.sqrt(discriminant);
    }
    
    // Частные производные функций итерации для проверки условия сходимости
    // ∂g₁/∂x₁ = -16x₁/(x₁² + 4)²
    public double dg1_dx1(double x1, double x2) {
        double denominator = x1 * x1 + 4;
        return -16 * x1 / (denominator * denominator);
    }
    
    // ∂g₁/∂x₂ = 0
    public double dg1_dx2(double x1, double x2) {
        return 0;
    }
    
    // ∂g₂/∂x₁ = 0
    public double dg2_dx1(double x1, double x2) {
        return 0;
    }
    
    // ∂g₂/∂x₂ = ±(x₂ - 1)/√(4 - (x₂ - 1)²)
    public double dg2_dx2(double x1, double x2) {
        double term = x2 - 1;
        double discriminant = 4 - term * term;
        if (discriminant <= 0) {
            return Double.MAX_VALUE;
        }
        double sign = x1 >= 2 ? 1 : -1;
        return sign * term / Math.sqrt(discriminant);
    }
    
    // Эквивалентные функции для графического анализа
    // Из f₁ = 0: y = 8/(x² + 4)
    // Из f₂ = 0: (x - 2)² + (y - 1)² = 4 (окружность)
    
    public double equivalentFunction1(double x) {
        return 8.0 / (x * x + 4);
    }
    
    public double[] equivalentFunction2(double x) {
        // Окружность: (x - 2)² + (y - 1)² = 4
        // y = 1 ± √(4 - (x - 2)²)
        double discriminant = 4 - (x - 2) * (x - 2);
        if (discriminant < 0) {
            return new double[0]; // Нет решений
        }
        double sqrt = Math.sqrt(discriminant);
        return new double[]{1 + sqrt, 1 - sqrt};
    }
}