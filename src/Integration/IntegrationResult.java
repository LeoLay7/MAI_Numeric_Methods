package Integration;

public class IntegrationResult {
    private final double value;
    private final int n;
    private final double error;
    private final double valueWithHalfStep;
    
    public IntegrationResult(double value, int n, double error, double valueWithHalfStep) {
        this.value = value;
        this.n = n;
        this.error = error;
        this.valueWithHalfStep = valueWithHalfStep;
    }
    
    public double getValue() {
        return value;
    }
    
    public int getN() {
        return n;
    }
    
    public double getError() {
        return error;
    }
    
    public double getValueWithHalfStep() {
        return valueWithHalfStep;
    }
}