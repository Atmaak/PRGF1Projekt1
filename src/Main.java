import window.Canvas;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Canvas(800, 800).start());
    }
}