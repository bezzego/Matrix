package util;

import java.awt.*;
import javax.swing.*;

public class NotificationManager {
    private static final int NOTIFICATION_DURATION = 3000; // 3 секунды

    public static void showSuccess(String message) {
        showNotification(message, new Color(76, 175, 80));
    }

    public static void showError(String message) {
        showNotification(message, new Color(244, 67, 54));
    }

    private static void showNotification(String message, Color backgroundColor) {
        JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        panel.add(label);

        dialog.add(panel);
        dialog.pack();

        // Размещаем уведомление в правом верхнем углу
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(
            screenSize.width - dialog.getWidth() - 20,
            screenSize.height - dialog.getHeight() - 40
        );

        dialog.setVisible(true);

        // Закрываем уведомление через заданное время
        new Timer(NOTIFICATION_DURATION, e -> {
            dialog.dispose();
        }).start();
    }
}