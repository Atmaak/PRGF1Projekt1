package rasterizers;

import raster.Raster;

import java.awt.*;

public class LineRasterizerTrivial extends LineRasterizer {
    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }
    @Override
    public void drawLine(Point start, Point end) {
        int dx = end.x - start.x;
        int dy = end.y - start.y;
        int steps = Math.max(Math.abs(dx), Math.abs(dy)); // depending on value of dx/dy we determine how many steps (cycles) we will have in our cycle

        // using variable "steps" we devide the dx/dy for amount we will move them for each step
        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        float x = start.x;
        float y = start.y;

        // simply adding calculated values to each step in to x and y to get our line
        for (int i = 0; i <= steps; i++) {
            raster.setRGB(Math.round(x), Math.round(y), color);
            x += xIncrement;
            y += yIncrement;
        }
    }

    public void drawBoldLine(Point start, Point end){
        int thickness = 10;
        int dx = end.x - start.x;
        int dy = end.y - start.y;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        float x = start.x;
        float y = start.y;

        // Calculate the angle of the line
        double angle = Math.atan2(dy, dx);
        // Calculate offsets for thickness
        float offsetX = (float) (thickness / 2.0 * Math.sin(angle));
        float offsetY = (float) (thickness / 2.0 * -Math.cos(angle)); // Negative to go to the left

        // Draw the bold line by drawing multiple lines at offsets
        for (int i = -thickness / 2; i <= thickness / 2; i++) {
            float offsetXCurrent = offsetX * (i / (float) thickness);
            float offsetYCurrent = offsetY * (i / (float) thickness);

            // Draw the line with the current offset
            for (int j = 0; j <= steps; j++) {
                raster.setRGB(Math.round(x + offsetXCurrent), Math.round(y + offsetYCurrent), color);
                x += xIncrement;
                y += yIncrement;
            }
            // Reset x and y for the next offset
            x = start.x;
            y = start.y;
        }
    }
}
