package common.table;

import java.util.List;

public class Table {
    private List<Double> x;
    private List<Double> y;
    private Double dot;
    private int n;

    public Table(List<Double> x, List<Double> y, Double dot) {
        this.x = x;
        this.y = y;
        this.dot = dot;
        n = this.x.size();
    }

    public List<Double> getX() {
        return x;
    }

    public Double getFromX(int i) {
        return x.get(i);
    }

    public List<Double> getY() {
        return y;
    }

    public Double getFromY(int i) {
        return y.get(i);
    }

    public Double getDot() {
        return dot;
    }
}
