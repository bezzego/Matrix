package view;

import model.Matrix;
import javax.swing.*;
import java.awt.*;

public class MatrixVisualizerFrame extends JFrame {
    private final Matrix matrix;
    private final JPanel heatmapPanel;
    private static final Color MIN_COLOR = new Color(255, 255, 255);
    private static final Color MAX_COLOR = new Color(0, 0, 255);

    public MatrixVisualizerFrame(Matrix matrix) {
        this.matrix = matrix;
        setTitle("Визуализация матрицы");
        setSize(500, 500);
        setLocationRelativeTo(null);

        heatmapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawHeatmap(g);
            }
        };
        add(heatmapPanel);

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);
    }

    private void drawHeatmap(Graphics g) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        // Находим минимальное и максимальное значения
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                double value = matrix.getValue(i, j);
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
        }

        int cellWidth = heatmapPanel.getWidth() / matrix.getCols();
        int cellHeight = heatmapPanel.getHeight() / matrix.getRows();

        // Рисуем ячейки
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                double value = matrix.getValue(i, j);
                double normalized = (value - min) / (max - min);
                Color color = interpolateColor(normalized);
                
                g.setColor(color);
                g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                
                g.setColor(Color.BLACK);
                g.drawRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                
                String text = String.format("%.2f", value);
                g.drawString(text, j * cellWidth + 5, i * cellHeight + cellHeight/2);
            }
        }
    }

    private Color interpolateColor(double factor) {
        int red = (int) (MIN_COLOR.getRed() + factor * (MAX_COLOR.getRed() - MIN_COLOR.getRed()));
        int green = (int) (MIN_COLOR.getGreen() + factor * (MAX_COLOR.getGreen() - MIN_COLOR.getGreen()));
        int blue = (int) (MIN_COLOR.getBlue() + factor * (MAX_COLOR.getBlue() - MIN_COLOR.getBlue()));
        return new Color(red, green, blue);
    }
}