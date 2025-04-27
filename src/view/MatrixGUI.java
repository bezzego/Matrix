package view;

import controller.MatrixController;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import model.Matrix;
import model.Operation; // Изменили с util.Operation на model.Operation
import util.OperationHistory;
import model.MatrixOperations;
import util.NotificationManager;
import view.theme.ThemeManager;
import view.animation.MatrixAnimator;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;

public class MatrixGUI extends JFrame {
    private JSpinner rowsSpinner1, colsSpinner1, rowsSpinner2, colsSpinner2;
    private JPanel resultPanel;
    private JComboBox<String> operationBox;
    private JTextField scalarField;
    private final List<List<JTextField>> resultFields = new ArrayList<>();
    private final List<List<List<JTextField>>> matrixFields = new ArrayList<>();
    // Per-matrix size controls
    private final List<JSpinner> rowsSpinnersList = new ArrayList<>();
    private final List<JSpinner> colsSpinnersList = new ArrayList<>();
    private final JTabbedPane matrixTabs;
    private final JButton addMatrixButton;
    private final JButton removeMatrixButton;
    private int matrixCount = 2;

    private final OperationHistory history = new OperationHistory();
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private final Color ACCENT_COLOR = new Color(66, 165, 245);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Font MAIN_FONT = new Font("Arial", Font.PLAIN, 14);
    private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);

    public MatrixGUI() {
        setTitle("Калькулятор матриц");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set overall frame background and layout
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10,10));

        // Create toolbar and status bar
        JToolBar toolBar = createToolBar();
        add(toolBar, BorderLayout.NORTH);

        // Tabs for input matrices with per-matrix size controls
        matrixTabs = new JTabbedPane();
        for (int i = 0; i < matrixCount; i++) {
            final int index = i;
            // Container for size controls + matrix grid
            JPanel tabContainer = new JPanel(new BorderLayout(5,5));

            // Size control panel
            JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JSpinner rSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
            JSpinner cSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
            rowsSpinnersList.add(rSpinner);
            colsSpinnersList.add(cSpinner);
            sizePanel.add(new JLabel("Rows:"));
            sizePanel.add(rSpinner);
            sizePanel.add(new JLabel("Cols:"));
            sizePanel.add(cSpinner);
            JButton resizeBtn = new JButton("Resize");
            sizePanel.add(resizeBtn);

            // Panel to hold matrix fields
            JPanel gridPanel = new JPanel();
            tabContainer.add(sizePanel, BorderLayout.NORTH);
            tabContainer.add(gridPanel, BorderLayout.CENTER);

            // Initialize fields list and grid
            matrixFields.add(new ArrayList<>());
            int defaultRows = 2, defaultCols = 2;
            resizeBtn.addActionListener(e -> updateMatrixPanel(gridPanel, index, (Integer)rSpinner.getValue(), (Integer)cSpinner.getValue()));
            updateMatrixPanel(gridPanel, i, defaultRows, defaultCols);

            matrixTabs.addTab("Матрица " + (i+1), tabContainer);
        }

        // AddMatrixButton created after matrixTabs initialization
        addMatrixButton = new JButton("Добавить матрицу");
        addMatrixButton.addActionListener(e -> {
            int index = matrixTabs.getTabCount();
            matrixCount = index + 1;
            // Container for size controls + matrix grid
            JPanel tabContainer = new JPanel(new BorderLayout(5,5));
            JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JSpinner rSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
            JSpinner cSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
            rowsSpinnersList.add(rSpinner);
            colsSpinnersList.add(cSpinner);
            sizePanel.add(new JLabel("Rows:"));
            sizePanel.add(rSpinner);
            sizePanel.add(new JLabel("Cols:"));
            sizePanel.add(cSpinner);
            JButton resizeBtn = new JButton("Resize");
            sizePanel.add(resizeBtn);
            JPanel gridPanel = new JPanel();
            tabContainer.add(sizePanel, BorderLayout.NORTH);
            tabContainer.add(gridPanel, BorderLayout.CENTER);
            matrixFields.add(new ArrayList<>());
            int defaultRows = 2, defaultCols = 2;
            resizeBtn.addActionListener(ev -> updateMatrixPanel(gridPanel, index, (Integer)rSpinner.getValue(), (Integer)cSpinner.getValue()));
            updateMatrixPanel(gridPanel, index, defaultRows, defaultCols);
            matrixTabs.addTab("Матрица " + (index + 1), tabContainer);
            revalidate();
            repaint();
        });

        removeMatrixButton = new JButton("Удалить матрицу");
        removeMatrixButton.addActionListener(e -> {
            int idx = matrixTabs.getSelectedIndex();
            if (matrixTabs.getTabCount() > 1 && idx >= 0) {
                matrixTabs.removeTabAt(idx);
                matrixFields.remove(idx);
                rowsSpinnersList.remove(idx);
                colsSpinnersList.remove(idx);
                matrixCount = matrixTabs.getTabCount();
                // Rename remaining tabs
                for (int i = 0; i < matrixTabs.getTabCount(); i++) {
                    matrixTabs.setTitleAt(i, "Матрица " + (i + 1));
                }
            }
        });

        // Main content panel with white background and shadow border
        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new CompoundBorder(
            new EmptyBorder(20,20,20,20),
            new CompoundBorder(
                new LineBorder(new Color(0,0,0,20),1),
                new EmptyBorder(10,10,10,10)
            )
        ));

        // Создаем панель управления
        JPanel controlPanel = createControlPanel(addMatrixButton, removeMatrixButton);

        JPanel matricesPanel = new JPanel(new BorderLayout());
        matricesPanel.add(matrixTabs, BorderLayout.CENTER);

        // Результат
        JPanel resultContainer = new JPanel(new BorderLayout());
        resultContainer.add(new JLabel("Результат:"), BorderLayout.NORTH);
        resultPanel = new JPanel(new GridLayout(2, 2));
        resultContainer.add(resultPanel, BorderLayout.CENTER);
        // Removed: matricesPanel.add(resultContainer, BorderLayout.SOUTH);

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(matricesPanel, BorderLayout.CENTER);
        mainPanel.add(resultContainer, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        // Добавляем кнопку профиля
        JButton profileButton = new JButton("Профиль");
        profileButton.addActionListener(e -> {
            ProfileFrame profileFrame = new ProfileFrame(MatrixController.getCurrentUser());
            profileFrame.setVisible(true);
        });
        controlPanel.add(profileButton);

        // Обновляем матрицы по умолчанию
        updateMatrices();
        updateResultMatrix(2, 2);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createControlPanel(JButton addMatrixButton, JButton removeMatrixButton) {
        JPanel panel = new JPanel(new FlowLayout());

        // Размеры первой матрицы
        panel.add(new JLabel("Размер матриц:"));
        rowsSpinner1 = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        colsSpinner1 = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        panel.add(rowsSpinner1);
        panel.add(new JLabel("x"));
        panel.add(colsSpinner1);

        // Размеры второй матрицы
        rowsSpinner2 = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        colsSpinner2 = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));

        // Выбор операции
        operationBox = new JComboBox<>(new String[]{
            "Add", "Multiply", "Sum All", "Chain Multiply",
            "Transpose", "Scalar Multiply", "Determinant", "Inverse"
        });
        panel.add(operationBox);

        // Поле для скаляра
        panel.add(new JLabel("Скаляр:"));
        scalarField = new JTextField(5);
        panel.add(scalarField);

        // Кнопки
        JButton updateButton = new JButton("Обновить размеры");
        updateButton.addActionListener(e -> updateMatrices());
        panel.add(updateButton);

        JButton randomizeButton = new JButton("Случайные значения");
        panel.add(randomizeButton);
        randomizeButton.addActionListener(e -> randomizeMatrices());

        JButton computeButton = new JButton("Вычислить");
        panel.add(computeButton);
        computeButton.addActionListener(e -> {
            try {
                List<Matrix> matrices = readMatrices();
                String op = getSelectedOperation();
                Matrix result = null;
                switch (op) {
                    case "Add":
                        result = MatrixOperations.add(matrices.get(0), matrices.get(1));
                        break;
                    case "Multiply":
                        result = MatrixOperations.multiply(matrices.get(0), matrices.get(1));
                        break;
                    case "Sum All":
                        result = MatrixOperations.sumAll(matrices);
                        break;
                    case "Chain Multiply":
                        result = MatrixOperations.chainMultiply(matrices);
                        break;
                    case "Transpose":
                        result = MatrixOperations.transpose(matrices.get(0));
                        break;
                    case "Scalar Multiply":
                        result = MatrixOperations.scalarMultiply(matrices.get(0), getScalarValue());
                        break;
                    case "Determinant":
                        showScalarResult(MatrixOperations.determinant(matrices.get(0)));
                        NotificationManager.showSuccess("Операция выполнена");
                        return;
                    case "Inverse":
                        result = MatrixOperations.inverse(matrices.get(0));
                        break;
                    default:
                        throw new IllegalStateException("Неизвестная операция: " + op);
                }
                if (result != null) {
                    // Сохраняем операцию в истории пользователя
                    Operation operation = new Operation(
                        getSelectedOperation(),
                        matrices.get(0),
                        matrices.size() > 1 ? matrices.get(1) : null,
                        result,
                        MatrixController.getCurrentUser().getEmail()
                    );
                    MatrixController.getCurrentUser().addOperation(operation);

                    history.pushOperation(result);
                    showResult(result);
                    NotificationManager.showSuccess("Операция выполнена");
                }
            } catch (Exception ex) {
                NotificationManager.showError("Ошибка: " + ex.getMessage());
            }
        });

        panel.add(addMatrixButton);
        panel.add(removeMatrixButton);

        return panel;
    }

    public void setRandomizeListener(ActionListener listener) {
        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JPanel) {
                for (Component b : ((JPanel) c).getComponents()) {
                    if (b instanceof JButton && ((JButton) b).getText().equals("Случайные значения")) {
                        ((JButton) b).addActionListener(listener);
                    }
                }
            }
        }
    }

    public void setComputeListener(ActionListener listener) {
        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JPanel) {
                for (Component b : ((JPanel) c).getComponents()) {
                    if (b instanceof JButton && ((JButton) b).getText().equals("Вычислить")) {
                        ((JButton) b).addActionListener(listener);
                    }
                }
            }
        }
    }

    private void updateMatrices() {
        int rows = (Integer) rowsSpinner1.getValue();
        int cols = (Integer) colsSpinner1.getValue();
        for (int i = 0; i < matrixTabs.getTabCount(); i++) {
            Component tabComponent = matrixTabs.getComponentAt(i);
            // The grid panel is at BorderLayout.CENTER of tabContainer
            if (tabComponent instanceof JPanel) {
                JPanel tabContainer = (JPanel) tabComponent;
                Component[] children = tabContainer.getComponents();
                JPanel gridPanel = null;
                for (Component child : children) {
                    if (child instanceof JPanel && BorderLayout.CENTER.equals(tabContainer.getLayout() instanceof BorderLayout ? ((BorderLayout)tabContainer.getLayout()).getConstraints(child) : null)) {
                        gridPanel = (JPanel) child;
                        break;
                    }
                }
                // Fallback: find by position
                if (gridPanel == null && children.length > 1 && children[1] instanceof JPanel) {
                    gridPanel = (JPanel)children[1];
                }
                if (gridPanel != null) {
                    // Use spinner values if available
                    int r = rows, c = cols;
                    if (i < rowsSpinnersList.size() && i < colsSpinnersList.size()) {
                        r = (Integer) rowsSpinnersList.get(i).getValue();
                        c = (Integer) colsSpinnersList.get(i).getValue();
                    }
                    updateMatrixPanel(gridPanel, i, r, c);
                }
            }
        }
        updateResultMatrix(rows, cols);
    }

    private void updateMatrixPanel(JPanel panel, int index, int rows, int cols) {
        panel.removeAll();
        List<List<JTextField>> fields;
        if (index < matrixFields.size()) {
            fields = matrixFields.get(index);
            fields.clear();
        } else {
            fields = new ArrayList<>();
            matrixFields.add(fields);
        }
        panel.setLayout(new GridLayout(rows, cols, 2, 2));
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setPreferredSize(new Dimension(150 * cols, 100 * rows));

        for (int i = 0; i < rows; i++) {
            List<JTextField> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                JTextField field = new JTextField(3);
                field.setText("0");
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                panel.add(field);
                row.add(field);
            }
            fields.add(row);
        }

        panel.revalidate();
        panel.repaint();
    }

    private void updateResultMatrix(int rows, int cols) {
        resultPanel.removeAll();
        resultFields.clear();
        resultPanel.setLayout(new GridLayout(rows, cols, 2, 2));

        for (int i = 0; i < rows; i++) {
            List<JTextField> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                JTextField field = new JTextField(3);
                field.setEditable(false);
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                resultPanel.add(field);
                row.add(field);
            }
            resultFields.add(row);
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    public void randomizeMatrices() {
        for (List<List<JTextField>> matrix : matrixFields) {
            randomizeMatrix(matrix);
        }
    }

    private void randomizeMatrix(List<List<JTextField>> fields) {
        for (List<JTextField> row : fields) {
            for (JTextField field : row) {
                field.setText(String.valueOf((int) (Math.random() * 201)));
            }
        }
    }

    public List<Matrix> readMatrices() {
        List<Matrix> matrices = new ArrayList<>();
        for (List<List<JTextField>> fields : matrixFields) {
            matrices.add(readMatrix(fields));
        }
        return matrices;
    }

    public Matrix readMatrix(List<List<JTextField>> fields) {
        int rows = fields.size();
        int cols = fields.get(0).size();
        double[][] data = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                try {
                    data[i][j] = Double.parseDouble(fields.get(i).get(j).getText());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Неверный формат числа в ячейке [" + i + "][" + j + "]");
                }
            }
        }

        return new Matrix(data);
    }

    public String getSelectedOperation() {
        return (String) operationBox.getSelectedItem();
    }

    public double getScalarValue() {
        try {
            return Double.parseDouble(scalarField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат скаляра");
        }
    }

    public void showResult(Matrix result) {
        if (result == null) return;

        int rows = result.getRows();
        int cols = result.getCols();
        updateResultMatrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                resultFields.get(i).get(j).setText(String.format("%.2f", result.get(i, j)));
            }
        }
    }

    public void showScalarResult(double result) {
        updateResultMatrix(1, 1);
        resultFields.get(0).get(0).setText(String.format("%.2f", result));
    }

    /** Создает панель инструментов для undo/redo и экспорт/импорт */
    private JToolBar createToolBar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setBorder(new EmptyBorder(5,10,5,10));
        tb.setBackground(BACKGROUND_COLOR);
        JButton undo = new JButton("↩ Отменить"); styleToolButton(undo); undo.addActionListener(e -> undo());
        JButton redo = new JButton("↪ Вернуть"); styleToolButton(redo); redo.addActionListener(e -> redo());
        JButton exp = new JButton("📤 Экспорт"); styleToolButton(exp); exp.addActionListener(e -> exportMatrix());
        JButton imp = new JButton("📥 Импорт"); styleToolButton(imp); imp.addActionListener(e -> importMatrix());
        tb.add(undo); tb.add(redo); tb.addSeparator(); tb.add(exp); tb.add(imp);
        tb.addSeparator();
        // Кнопка истории операций
        JButton historyButton = new JButton("📋 История");
        styleToolButton(historyButton);
        historyButton.addActionListener(e -> showHistory());
        tb.add(historyButton);
        // Кнопка профиля
        JButton profile = new JButton("👤 Профиль");
        styleToolButton(profile);
        profile.addActionListener(e -> {
            ProfileFrame pf = new ProfileFrame(MatrixController.getCurrentUser());
            pf.setVisible(true);
        });
        tb.add(profile);
        return tb;
    }

    /** Стилизация кнопок тулбара */
    private void styleToolButton(JButton b) {
        b.setFont(MAIN_FONT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBackground(BACKGROUND_COLOR);
    }

    /** Отмена (undo) */
    private void undo() {
        if (history.canUndo()) {
            Matrix m = history.undo();
            showResult(m);
            NotificationManager.showSuccess("Операция отменена");
        }
    }

    /** Переотмена (redo) */
    private void redo() {
        if (history.canRedo()) {
            Matrix m = history.redo();
            showResult(m);
            NotificationManager.showSuccess("Операция возвращена");
        }
    }

    // Экспорт текущей матрицы в CSV
    private void exportMatrix() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Выберите файл для сохранения");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter("CSV файлы", "csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();
            if (!path.endsWith(".csv")) path += ".csv";
            try {
                Matrix m = readMatrix(matrixFields.get(matrixTabs.getSelectedIndex()));
                util.MatrixExporter.exportToCSV(m, path);
                NotificationManager.showSuccess("Матрица успешно экспортирована");
            } catch (Exception e) {
                NotificationManager.showError("Ошибка при экспорте: " + e.getMessage());
            }
        }
    }

    // Импорт матрицы из CSV
    private void importMatrix() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Выберите файл для импорта");
        fc.setFileFilter(new FileNameExtensionFilter("CSV файлы", "csv"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Matrix m = util.MatrixExporter.importFromCSV(fc.getSelectedFile().getPath());
                JPanel tab = (JPanel) matrixTabs.getSelectedComponent();
                JPanel grid = (JPanel) tab.getComponent(1);
                updateMatrixPanel(grid, matrixTabs.getSelectedIndex(), m.getRows(), m.getCols());
                fillMatrixFields(matrixFields.get(matrixTabs.getSelectedIndex()), m);
                NotificationManager.showSuccess("Матрица успешно импортирована");
            } catch (Exception e) {
                NotificationManager.showError("Ошибка при импорте: " + e.getMessage());
            }
        }
    }

    // Заполнить поля значениями из Matrix
    private void fillMatrixFields(List<List<JTextField>> fields, Matrix m) {
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                fields.get(i).get(j).setText(String.valueOf(m.getValue(i, j)));
            }
        }
    }
    /** Открывает окно истории выполненных операций */
    private void showHistory() {
        // Получаем список операций текущего пользователя
        List<Operation> operations = MatrixController.getCurrentUser().getOperations();
        OperationHistoryFrame historyFrame = new OperationHistoryFrame(operations);
        historyFrame.setVisible(true);
    }
}

//mvn clean compile
//mvn exec:java -Dexec.mainClass="controller.MatrixController"