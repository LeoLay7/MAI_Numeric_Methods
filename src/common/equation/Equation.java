package common.equation;

import common.matrix.Column;
import common.matrix.Matrix;

public class Equation {
    Matrix coefficients;
    Column valuesCol;

    public Equation(Matrix coefficients, Column col) {
        this.coefficients = coefficients;
        this.valuesCol = col;
    }

    public Matrix getCoefficients() {
        return coefficients;
    }

    public Column getValuesCol() {
        return valuesCol;
    }
}
