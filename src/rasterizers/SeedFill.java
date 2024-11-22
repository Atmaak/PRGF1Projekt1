package rasterizers;

import raster.Raster;

import java.awt.*;
import java.util.Optional;
import java.util.Stack;
import java.util.function.BiPredicate;

public class SeedFill {
    Raster raster;

    public SeedFill(Raster raster){
        this.raster = raster;
    }

    public void fill(Point start, int pixelValue, BiPredicate<Integer, Point> isInArea){
        if (start.x < 0 || start.x >= raster.getWidth() || start.y < 0 || start.y >= raster.getHeight()) {
            throw new IllegalArgumentException("Starting point is out of bounds");
        }
        int originalValue = raster.getRGB(start.x, start.y);


        Stack<Point> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int currentPixelValue = raster.getRGB(p.x, p.y);
            if (p.x < 0 || p.x >= raster.getWidth() || p.y < 0 || p.y >= raster.getHeight()) {
                continue;
            }

            if (isInArea.test(currentPixelValue, p) && currentPixelValue == originalValue) {
                raster.setRGB(p.x, p.y, pixelValue);

                stack.push(new Point(p.x + 1, p.y));
                stack.push(new Point(p.x - 1, p.y));
                stack.push(new Point(p.x, p.y + 1));
                stack.push(new Point(p.x, p.y - 1));
            }


        }
    }

    public void fillAroundPolygon(Point start, int pixelValue, BiPredicate<Point, Integer> isInArea){
        if (start.x < 0 || start.x >= raster.getWidth() || start.y < 0 || start.y >= raster.getHeight()) {
            throw new IllegalArgumentException("Starting point is out of bounds");
        }

        int originalValue = raster.getRGB(start.x, start.y);

        Stack<Point> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int currentPixelValue = raster.getRGB(p.x, p.y);

            if (p.x < 0 || p.x >= raster.getWidth() || p.y < 0 || p.y >= raster.getHeight()) {
                continue;
            }

            // Check if the current pixel is within the area and is the original color
            if (isInArea.test(new Point(p.x, p.y), 0) && currentPixelValue == originalValue) {
                raster.setRGB(p.x, p.y, pixelValue);

                stack.push(new Point(p.x + 1, p.y));
                stack.push(new Point(p.x - 1, p.y));
                stack.push(new Point(p.x, p.y + 1));
                stack.push(new Point(p.x, p.y - 1));
            }
        }
    }


}
