import java.awt.*;
import java.awt.image.BufferedImage;

public class StraightLine extends OldLineRasterizer {
    public StraightLine(BufferedImage img) {
        super(img);
        super.color = 0xFF00FF;
    }

    @Override
    public void draw() {
        Point dir = calculateDirection(new Point(x1, y1), new Point(x2, y2));

        int iterations = (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));

        for (int i = 0; i < iterations; i++) {
            x1 += dir.x;
            y1 += dir.y;
            img.setRGB(x1, y1, color);
        }
    }

    public Point calculateDirection(Point A, Point B) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        double distance = Math.sqrt(dx * dx + dy * dy);

        double dirX = (distance == 0) ? 0 : dx / distance;
        double dirY = (distance == 0) ? 0 : dy / distance;

        return new Point((int)Math.round(dirX), (int)Math.round(dirY));
    }

}
