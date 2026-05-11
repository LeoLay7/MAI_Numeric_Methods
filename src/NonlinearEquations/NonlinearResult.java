package NonlinearEquations;

public class NonlinearResult {
    private final double root;
    private final int iterations;
    private final boolean convergenceConditionMet;
    private final String method;
    
    public NonlinearResult(double root, int iterations, boolean convergenceConditionMet, String method) {
        this.root = root;
        this.iterations = iterations;
        this.convergenceConditionMet = convergenceConditionMet;
        this.method = method;
    }
    
    public double getRoot() {
        return root;
    }
    
    public int getIterations() {
        return iterations;
    }
    
    public boolean isConvergenceConditionMet() {
        return convergenceConditionMet;
    }
    
    public String getMethod() {
        return method;
    }
}