package view;

import controller.MatrixController;
import model.User;
import util.FileHandler;
import javax.swing.*;
import java.awt.*;

public class ProfileFrame extends JFrame {
    private final User user;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmNewPasswordField;
    
    public ProfileFrame(User user) {
        this.user = user;
        setTitle("Профиль пользователя");
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Информация о пользователе
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        infoPanel.add(new JLabel("Имя:"));
        infoPanel.add(new JLabel(user.getFirstName()));
        infoPanel.add(new JLabel("Фамилия:"));
        infoPanel.add(new JLabel(user.getLastName()));
        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(new JLabel(user.getEmail()));
        infoPanel.add(new JLabel("Телефон:"));
        infoPanel.add(new JLabel(user.getPhone()));
        
        // Панель смены пароля
        JPanel passwordPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Изменить пароль"));
        
        currentPasswordField = new JPasswordField();
        newPasswordField = new JPasswordField();
        confirmNewPasswordField = new JPasswordField();
        
        passwordPanel.add(new JLabel("Текущий пароль:"));
        passwordPanel.add(currentPasswordField);
        passwordPanel.add(new JLabel("Новый пароль:"));
        passwordPanel.add(newPasswordField);
        passwordPanel.add(new JLabel("Подтвердите пароль:"));
        passwordPanel.add(confirmNewPasswordField);
        
        JButton changePasswordButton = new JButton("Изменить пароль");
        changePasswordButton.addActionListener(e -> changePassword());
        
        JButton backButton = new JButton("Вернуться к калькулятору");
        backButton.addActionListener(e -> this.dispose());
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(passwordPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void changePassword() {
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmNewPasswordField.getPassword());
        
        if (!user.getPassword().equals(currentPassword)) {
            JOptionPane.showMessageDialog(this, "Неверный текущий пароль!");
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Новые пароли не совпадают!");
            return;
        }
        
        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "Пароль должен быть не менее 6 символов!");
            return;
        }
        
        try {
            user.setPassword(newPassword);
            FileHandler.updateUserPassword(user);
            JOptionPane.showMessageDialog(this, "Пароль успешно изменен!");
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка при изменении пароля: " + ex.getMessage());
        }
    }
}