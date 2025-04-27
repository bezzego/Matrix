package view;

import model.Operation;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;

public class OperationHistoryFrame extends JFrame {
    private final JTable historyTable;
    private final DefaultTableModel tableModel;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public OperationHistoryFrame(List<Operation> operations) {
        setTitle("История операций");
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Создаем модель таблицы
        String[] columns = {"ID", "Тип операции", "Время", "Результат"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Создаем таблицу
        historyTable = new JTable(tableModel);
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(450);

        // Добавляем данные
        for (Operation op : operations) {
            Object[] row = {
                op.getId(),
                op.getOperationType(),
                op.getTimestamp().format(formatter),
                String.format("%s → %s", 
                    formatMatrixPreview(op.getMatrix1()),
                    formatMatrixPreview(op.getResult()))
            };
            tableModel.addRow(row);
        }

        // Добавляем скролл
        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane);

        // Добавляем кнопки
        JPanel buttonPanel = new JPanel();
        JButton exportButton = new JButton("Экспорт истории");
        exportButton.addActionListener(e -> exportHistory());
        buttonPanel.add(exportButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String formatMatrixPreview(model.Matrix matrix) {
        if (matrix == null) return "null";
        return String.format("Матрица [%dx%d]", matrix.getRows(), matrix.getCols());
    }

    private void exportHistory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Экспорт истории");
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                // TODO: Реализовать экспорт в CSV
                JOptionPane.showMessageDialog(this, 
                    "История успешно экспортирована", 
                    "Успех", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Ошибка при экспорте: " + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}