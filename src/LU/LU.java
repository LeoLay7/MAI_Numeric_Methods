package LU;

import common.matrix.Matrix;

public class LU {
    private Matrix l;
    private Matrix u;

    public LU(Matrix l, Matrix u) {
        this.l = l;
        this.u = u;
    }

    public Matrix getL() {
        return l;
    }

    public Matrix getU() {
        return u;
    }
}
