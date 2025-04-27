package view;

import controller.MatrixController;
import model.User;
import util.FileHandler;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Вход в систему");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Войти");
        JButton registerButton = new JButton("Регистрация");

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> {
            RegisterFrame registerFrame = new RegisterFrame();
            registerFrame.setVisible(true);
            this.dispose();
        });

        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
    }

    private void login() {
        try {
            List<User> users = FileHandler.loadUsers();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            for (User user : users) {
                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                    MatrixController.setCurrentUser(user);
                    MatrixGUI gui = new MatrixGUI();
                    gui.setVisible(true);
                    new MatrixController(gui);
                    this.dispose();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Неверный email или пароль");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
        }
    }
}