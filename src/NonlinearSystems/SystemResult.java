package NonlinearSystems;

public class SystemResult {
    private final double x1;
    private final double x2;
    private final int iterations;
    private final double residual;
    private final boolean convergenceConditionMet;
    private final String method;
    
    public SystemResult(double x1, double x2, int iterations, double residual, 
                       boolean convergenceConditionMet, String method) {
        this.x1 = x1;
        this.x2 = x2;
        this.iterations = iterations;
        this.residual = residual;
        this.convergenceConditionMet = convergenceConditionMet;
        this.method = method;
    }
    
    public double getX1() {
        return x1;
    }
    
    public double getX2() {
        return x2;
    }
    
    public int getIterations() {
        return iterations;
    }
    
    public double getResidual() {
        return residual;
    }
    
    public boolean isConvergenceConditionMet() {
        return convergenceConditionMet;
    }
    
    public String getMethod() {
        return method;
    }
}