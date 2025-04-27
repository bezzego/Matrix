package util;

import com.google.gson.Gson;
import model.Matrix;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class MatrixExporter {
    private static final Gson gson = new Gson();

    public static void exportToCSV(Matrix matrix, String filepath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filepath))) {
            for (int i = 0; i < matrix.getRows(); i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < matrix.getCols(); j++) {
                    line.append(matrix.getValue(i, j));
                    if (j < matrix.getCols() - 1) {
                        line.append(",");
                    }
                }
                writer.write(line.toString());
                writer.newLine();
            }
        }
    }

    public static void exportToJSON(Matrix matrix, String filepath) throws IOException {
        String json = gson.toJson(matrix);
        Files.writeString(Paths.get(filepath), json);
    }

    public static Matrix importFromCSV(String filepath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filepath));
        int rows = lines.size();
        int cols = lines.get(0).split(",").length;
        double[][] data = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            String[] values = lines.get(i).split(",");
            for (int j = 0; j < cols; j++) {
                data[i][j] = Double.parseDouble(values[j]);
            }
        }

        return new Matrix(data);
    }
}