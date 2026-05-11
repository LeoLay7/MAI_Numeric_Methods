package ODE;

public class ODEMain {
    public static void main(String[] args) {
        // Параметры задачи
        double x0 = -2.0;
        double y0 = 7.456;
        double xEnd = 3.0;
        double h = 0.2;
        
        ODEFunction function = new ODEFunction();
        
        System.out.println("Решение задачи Коши для ОДУ: y' + y - e^x = 0");
        System.out.println("Начальные условия: y(-2) = 7.456");
        System.out.println("Интервал: [-2, 3], шаг h = " + h);
        System.out.println();
        
        // Аналитическое решение
        System.out.println("Аналитическое решение: y = e^x * (x + C), где C определяется из начального условия");
        System.out.println();
        
        // Решение методом Эйлера-Коши
        System.out.println("=== МЕТОД ЭЙЛЕРА-КОШИ ===");
        ODEResult eulerResult = ODEUtils.eulerCauchyMethod(function, x0, y0, xEnd, h);
        printResults(eulerResult, function);
        System.out.println();
        
        // Решение методом Рунге-Кутты 4-го порядка
        System.out.println("=== МЕТОД РУНГЕ-КУТТЫ 4-ГО ПОРЯДКА ===");
        ODEResult rungeKuttaResult = ODEUtils.rungeKutta4Method(function, x0, y0, xEnd, h);
        printResults(rungeKuttaResult, function);
        System.out.println();
        
        // Исследование влияния шага
        System.out.println("=== ИССЛЕДОВАНИЕ ВЛИЯНИЯ ШАГА ===");
        double[] steps = {0.4, 0.2, 0.1, 0.05};
        double xTest = 1.0; // Тестовая точка
        
        System.out.println("Сравнение в точке x = " + xTest + ":");
        System.out.println("Аналитическое решение: " + function.analyticalSolution(xTest));
        System.out.println();
        System.out.println("Шаг\t\tЭйлер-Коши\t\tПогрешность\t\tРунге-Кутта\t\tПогрешность");
        System.out.println("--------------------------------------------------------------------------------");
        
        for (double step : steps) {
            ODEResult eulerRes = ODEUtils.eulerCauchyMethod(function, x0, y0, xTest, step);
            ODEResult rkRes = ODEUtils.rungeKutta4Method(function, x0, y0, xTest, step);
            
            double analytical = function.analyticalSolution(xTest);
            double eulerValue = eulerRes.getYValues()[eulerRes.getYValues().length - 1];
            double rkValue = rkRes.getYValues()[rkRes.getYValues().length - 1];
            
            double eulerError = Math.abs(eulerValue - analytical);
            double rkError = Math.abs(rkValue - analytical);
            
            System.out.printf("%.2f\t\t%.6f\t\t%.6f\t\t%.6f\t\t%.6f\n", 
                            step, eulerValue, eulerError, rkValue, rkError);
        }
    }
    
    private static void printResults(ODEResult result, ODEFunction function) {
        double[] xValues = result.getXValues();
        double[] yValues = result.getYValues();
        
        System.out.println("x\t\ty_численное\t\ty_аналитическое\t\tПогрешность");
        System.out.println("----------------------------------------------------------------");
        
        for (int i = 0; i < xValues.length; i++) {
            double analytical = function.analyticalSolution(xValues[i]);
            double error = Math.abs(yValues[i] - analytical);
            System.out.printf("%.1f\t\t%.6f\t\t%.6f\t\t%.6f\n", 
                            xValues[i], yValues[i], analytical, error);
        }
        
        // Максимальная погрешность
        double maxError = 0;
        for (int i = 0; i < xValues.length; i++) {
            double analytical = function.analyticalSolution(xValues[i]);
            double error = Math.abs(yValues[i] - analytical);
            maxError = Math.max(maxError, error);
        }
        System.out.println("Максимальная погрешность: " + maxError);
    }
}