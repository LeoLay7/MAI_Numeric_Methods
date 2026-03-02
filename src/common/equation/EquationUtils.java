package common.equation;

import common.matrix.Column;
import common.matrix.Matrix;
import common.matrix.MatrixElement;

import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class EquationUtils {
    public static Equation fromFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            List<String> lines = Files.readAllLines(path);
            return fromLines(lines, 0, lines.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Equation fromLines(List<String> lines, int start, int end) {
        int rows = end - start;
        int cols = lines.getFirst().split(" ").length;

        Matrix m = new Matrix(rows, cols - 1);
        Column c = new Column(rows);
        for (int i = 0; i < rows; i++) {
            MatrixElement[] row = new MatrixElement[cols];
            String[] values = lines.get(i).split(" ");
            for (int j = 0; j < cols - 1; j++) {
                row[j] = MatrixElement.floatOfString(values[j]);
            }
            m.setRow(i, row);
            c.set(i, MatrixElement.floatOfString(values[cols - 1]));
        }
        return new Equation(m, c);
    }
}
