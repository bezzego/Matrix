package view;

import controller.MatrixController;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.User;
import util.FileHandler;

public class RegisterFrame extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel passwordStrengthLabel;

    public RegisterFrame() {
        setTitle("Регистрация");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Поля ввода
        panel.add(new JLabel("Имя:"));
        firstNameField = new JTextField();
        panel.add(firstNameField);

        panel.add(new JLabel("Фамилия:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Телефон:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Подтвердите пароль:"));
        confirmPasswordField = new JPasswordField();
        panel.add(confirmPasswordField);

        // Индикатор сложности пароля
        passwordStrengthLabel = new JLabel("Сложность пароля: ");

        // Кнопки
        JButton registerButton = new JButton("Зарегистрироваться");
        JButton backButton = new JButton("Назад");

        // Слушатели
        registerButton.addActionListener(e -> register());
        backButton.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        });

        // Слушатель изменения пароля
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updatePasswordStrength(); }
            public void removeUpdate(DocumentEvent e) { updatePasswordStrength(); }
            public void changedUpdate(DocumentEvent e) { updatePasswordStrength(); }
        });

        // Форматирование телефона
        phoneField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                String phone = phoneField.getText();
                if (phone.length() >= 12 && e.getKeyChar() != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
                if (!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != '+') {
                    e.consume();
                }
            }
        });

        // Валидация email
        emailField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (!isValidEmail(emailField.getText())) {
                    emailField.setBackground(new Color(255, 200, 200));
                } else {
                    emailField.setBackground(Color.WHITE);
                }
            }
        });

        // Panel for action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        add(panel, BorderLayout.CENTER);

        // Panel combining password strength label and action buttons
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        // Center the strength label under the password fields
        passwordStrengthLabel.setHorizontalAlignment(JLabel.CENTER);
        southPanel.add(passwordStrengthLabel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void updatePasswordStrength() {
        String password = new String(passwordField.getPassword());
        int strength = 0;

        if (password.length() >= 8) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[0-9].*")) strength++;
        if (password.matches(".*[!@#$%^&*()].*")) strength++;

        String strengthText = switch (strength) {
            case 0, 1 -> "Очень слабый";
            case 2 -> "Слабый";
            case 3 -> "Средний";
            case 4 -> "Хороший";
            case 5 -> "Отличный";
            default -> "Неизвестно";
        };

        passwordStrengthLabel.setText("Сложность пароля: " + strengthText);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void register() {
        // Валидация полей
        if (!isValidEmail(emailField.getText())) {
            JOptionPane.showMessageDialog(this, "Некорректный email!");
            return;
        }

        if (!new String(passwordField.getPassword())
                .equals(new String(confirmPasswordField.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Пароли не совпадают!");
            return;
        }

        try {
            User user = new User(
                firstNameField.getText(),
                lastNameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                new String(passwordField.getPassword())
            );
            FileHandler.saveUser(user);
            MatrixController.setCurrentUser(user);

            MatrixGUI gui = new MatrixGUI();
            gui.setVisible(true);
            new MatrixController(gui);
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Ошибка при регистрации: " + ex.getMessage());
        }
    }
}