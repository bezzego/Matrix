package view.animation;

import java.awt.*;
import javax.swing.*;

public class MatrixAnimator {
    private static final int ANIMATION_DURATION = 500; // миллисекунды

    public static void animateComputation(JPanel panel) {
        Timer timer = new Timer(50, null);
        final int steps = 10;
        final int[] currentStep = {0};

        timer.addActionListener(e -> {
            float progress = (float) currentStep[0] / steps;
            panel.setBackground(new Color(
                255,
                (int) (255 * (1 - progress)),
                (int) (255 * (1 - progress))
            ));
            
            currentStep[0]++;
            if (currentStep[0] > steps) {
                timer.stop();
                panel.setBackground(Color.WHITE);
            }
        });

        timer.start();
    }

    public static void animateResult(JPanel panel) {
        panel.setVisible(false);
        Timer timer = new Timer(ANIMATION_DURATION, e -> {
            panel.setVisible(true);
            SwingUtilities.invokeLater(() -> {
                panel.setBackground(new Color(200, 255, 200));
                new Timer(500, ev -> panel.setBackground(Color.WHITE)).start();
            });
        });
        timer.setRepeats(false);
        timer.start();
    }
}