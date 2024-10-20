import java.awt.*;
import java.awt.image.BufferedImage;

public class FilledLineRasterizer extends LineRasterizer {

    private int thickness = 15;

    public FilledLineRasterizer(BufferedImage img) {
        super(img);
    }

    public FilledLineRasterizer(BufferedImage img, int x1, int y1, int x2, int y2) {
        super(img);
        super.x1 = x1;
        super.y1 = y1;
        super.x2 = x2;
        super.y2 = y2;
        System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
        draw();
    }

    public void draw() {
        super.color = 0xFF0000;

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if(x1 >= img.getWidth() || x1 < 0 || y1 >= img.getHeight() || y1 < 0) break;

            drawThickPixel(x1, y1);

            if (x1 == x2 && y1 == y2) break;

            int err2 = err * 2;
            if (err2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (err2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    @Override
    public void drawGhostLine(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(Color.green);
        g2d.drawLine(super.x1, super.y1, super.x2, super.y2);
    }

    private void drawThickPixel(int x, int y) {
        int halfThickness = thickness / 2;
        // Dvojitá smyčka pro iteraci přes okolní pixely
        for (int i = -halfThickness; i <= halfThickness; i++) { // Pro každý offset v ose X
            for (int j = -halfThickness; j <= halfThickness; j++) { // Pro každý offset v ose Y
                int newX = x + i; // Nová X souřadnice posunutá o offset i
                int newY = y + j; // Nová Y souřadnice posunutá o offset j

                // Volání metody pro vykreslení kruhu na pozici (newX, newY) s poloměrem 1
                drawCircle(newX, newY, halfThickness); // Můžete změnit poloměr podle potřeby
            }
        }
    }

    // Metoda pro vykreslení kruhu
    private void drawCircle(int centerX, int centerY, int radius) {
        for (int angle = 0; angle < 360; angle++) {
            double rad = Math.toRadians(angle);
            int circleX = (int) (centerX + radius * Math.cos(rad));
            int circleY = (int) (centerY + radius * Math.sin(rad));

            // Check bounds to avoid ArrayIndexOutOfBoundsException
            img.setRGB(circleX, circleY, super.color);
        }
    }

}