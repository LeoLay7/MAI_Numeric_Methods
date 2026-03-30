package common.table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TableUtils {
    public static Table fromFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            List<String> lines = Files.readAllLines(path);
            List<Double> x = Arrays.stream(lines.get(0).split(" ")).map(Double::parseDouble).toList();
            List<Double> y = Arrays.stream(lines.get(1).split(" ")).map(Double::parseDouble).toList();
            Double dot = Double.valueOf(lines.get(2));
            return new Table(x,y,dot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
