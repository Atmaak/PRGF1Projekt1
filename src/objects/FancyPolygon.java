package objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FancyPolygon {
    BufferedImage img;

    public FancyPolygon(BufferedImage img) {
        this.img = img;
    }

    public boolean drawing = false;
    public boolean drawingPolygons = false;
    private Point center;
    private Point mousePosition;
    private Point outerEdgeMousePosition;
    private int radius;
    private int corners = 3;

    public void draw() {
        int[] xPoints = new int[corners];
        int[] yPoints = new int[corners];

        
        double angleStep = 2 * Math.PI / corners;
        for (int i = 0; i < corners; i++) {
            double angle = i * angleStep;
            xPoints[i] = (int) (center.x + radius * Math.cos(angle));
            yPoints[i] = (int) (center.y + radius * Math.sin(angle));
        }

        
        for (int i = 0; i < corners; i++) {
            int x1 = xPoints[i];
            int y1 = yPoints[i];
            int x2 = xPoints[(i + 1) % corners];
            int y2 = yPoints[(i + 1) % corners];
            drawLine(x1, y1, x2, y2);
        }

        drawingPolygons = false;
        drawing = false;
    }

    public void setCenter(Point center) {
        this.center = center;
        this.drawing = true;
    }

    public void setMousePosition(Point mousePosition) {
        this.mousePosition = mousePosition;
    }

    public void setRadius() {
        this.drawingPolygons = true;
    }

    public void setCorners(Point mousePosition) {
        if(this.mousePosition == null) return;
        int corners = (int) Math.sqrt(Math.pow((this.mousePosition.x - mousePosition.x), 2) + Math.pow((this.mousePosition.y - mousePosition.y), 2)) / 10;
        this.corners = Math.max(corners, 3);
    }

    public void drawRadius(Graphics g) {
        if (mousePosition == null) return;
        if (center == null) return;
        radius = (int) Math.sqrt(Math.pow((center.x - mousePosition.x), 2) + Math.pow((center.y - mousePosition.y), 2));
        g.setColor(Color.WHITE);

        int[] xPoints = new int[corners];
        int[] yPoints = new int[corners];

        for (int i = 0; i < corners; i++) {
            double angle = 2 * Math.PI / corners * i;
            xPoints[i] = center.x + (int) (radius * Math.cos(angle));
            yPoints[i] = center.y + (int) (radius * Math.sin(angle));
        }

        g.drawPolygon(xPoints, yPoints, corners);
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (x1 >= 0 && x1 < img.getWidth() && y1 >= 0 && y1 < img.getHeight()) {
                img.setRGB(x1, y1, 0xff0000);
            }

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
}
