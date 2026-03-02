package common.matrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MatrixUtils {

    public static void printMatrix(Matrix m, String msg) {
        System.out.println(msg);
        printMatrix(m);
    }

    public static void printMatrix(Matrix m) {
        for (int i = 0; i < m.getSize().getRows(); i++) {
            for (int j = 0; j < m.getSize().getCols(); j++) {
                MatrixElement elem = m.get(i, j);
                if (elem == null) System.out.print("nul\t");
                else System.out.print(m.get(i, j).toStr() + "\t");
            }
            System.out.println();
        }
    }

    public static Matrix fromFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            List<String> lines = Files.readAllLines(path);
            return fromLines(lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Matrix fromLines(List<String> lines) {
        return fromLines(lines, 0, lines.size());
    }

    public static Matrix fromLines(List<String> lines, int start, int end) {
        int rows = end - start;
        int cols = lines.getFirst().split(" ").length;

        Matrix m = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
//            MatrixElement[] row = (MatrixElement[]) Arrays.stream(
//                    lines.get(i).split(" ")
//            ).map(MatrixElement::intOfString).toArray();
            MatrixElement[] row = new MatrixElement[cols];
            String[] values = lines.get(i).split(" ");
            for (int j = 0; j < cols; j++) {
                row[j] = MatrixElement.floatOfString(values[j]);
            }

            // if (row.length != cols) throw new RuntimeException("Разное количество столбцов в разных строках");
            m.setRow(i, row);
        }
        return m;
    }
}
