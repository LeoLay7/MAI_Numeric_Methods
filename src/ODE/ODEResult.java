package ODE;

public class ODEResult {
    private final double[] xValues;
    private final double[] yValues;
    private final String method;
    private final double step;
    
    public ODEResult(double[] xValues, double[] yValues, String method, double step) {
        this.xValues = xValues.clone();
        this.yValues = yValues.clone();
        this.method = method;
        this.step = step;
    }
    
    public double[] getXValues() {
        return xValues.clone();
    }
    
    public double[] getYValues() {
        return yValues.clone();
    }
    
    public String getMethod() {
        return method;
    }
    
    public double getStep() {
        return step;
    }
    
    public int getPointsCount() {
        return xValues.length;
    }
}