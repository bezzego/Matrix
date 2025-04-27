package controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.*;
import model.Matrix;
import model.MatrixOperations;
import model.User;
import view.MatrixGUI;
import view.LoginFrame;
import java.util.List;

public class MatrixController {
    private final MatrixGUI view;
    private static User currentUser;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MatrixController(MatrixGUI view) {
        this.view = view;
        this.view.setRandomizeListener(e -> view.randomizeMatrices());
        this.view.setComputeListener(e -> compute());
        
        if (currentUser != null) {
            view.setTitle("Matrix Calculator - " + currentUser.getFirstName() + " " + currentUser.getLastName());
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdown));
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    private void compute() {
        Future<?> future = executor.submit(() -> {
            try {
                List<Matrix> mats = view.readMatrices();
                String op = view.getSelectedOperation();
                Matrix result = null;
                double scalarResult = 0;
                boolean isScalar = false;

                switch (op) {
                    case "Add":
                        result = MatrixOperations.add(mats.get(0), mats.get(1));
                        break;
                    case "Multiply":
                        result = MatrixOperations.multiply(mats.get(0), mats.get(1));
                        break;
                    case "Sum All":
                        result = MatrixOperations.sumAll(mats);
                        break;
                    case "Chain Multiply":
                        result = MatrixOperations.chainMultiply(mats);
                        break;
                    case "Transpose":
                        result = MatrixOperations.transpose(mats.get(0));
                        break;
                    case "Scalar Multiply":
                        result = MatrixOperations.scalarMultiply(mats.get(0), view.getScalarValue());
                        break;
                    case "Determinant":
                        scalarResult = MatrixOperations.determinant(mats.get(0));
                        isScalar = true;
                        break;
                    case "Inverse":
                        result = MatrixOperations.inverse(mats.get(0));
                        break;
                    default:
                        throw new IllegalStateException("Unknown operation: " + op);
                }

                Matrix finalResult = result;
                double finalScalarResult = scalarResult;
                boolean finalIsScalar = isScalar;

                SwingUtilities.invokeLater(() -> {
                    if (finalIsScalar) {
                        view.showScalarResult(finalScalarResult);
                    } else {
                        view.showResult(finalResult);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage()));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}