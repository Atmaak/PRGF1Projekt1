package objects;

import raster.Raster;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FancyPolygon {
    Raster raster;

    public FancyPolygon(Raster raster) {
        this.raster = raster;
    }

    public boolean drawing = false;
    public boolean drawingPolygons = false;
    private Point center;
    private Point mousePosition;
    private Point outerEdgeMousePosition;
    private int radius;
    private int corners = 5;

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
            drawLine(new Point(x1, y1), new Point(x2, y2));
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
        this.corners = /* Math.max(corners, 3) */ 5;
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

    private void drawLine(Point start, Point end) {

        int dx = Math.abs(start.x - end.x);
        int dy = Math.abs(end.y - start.y);
        int sx = (start.x < end.x) ? 1 : -1;
        int sy = (start.y < end.y) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (start.x >= 0 && start.x < raster.getWidth() && start.y >= 0 && start.y < raster.getHeight()) {
                raster.setRGB(start.x, start.y, 0x00FF00F);
            }

            if (start.x == end.x && start.y == end.y) break;

            int err2 = err * 2;
            if (err2 > -dy) {
                err -= dy;
                start.x += sx;
            }
            if (err2 < dx) {
                err += dx;
                start.y += sy;
            }
        }
    }
}
