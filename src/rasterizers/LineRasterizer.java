package rasterizers;

import raster.Raster;

import java.awt.*;

public abstract class LineRasterizer {
    protected final Raster raster;
    protected int color;
    public LineRasterizer(Raster raster){
        this.raster = raster;
    }
    public void drawLine(Point start, Point end){
        // empty, as each lineRasterizer class will have each unige algorithm
    }

    public void drawGhostLine(Graphics g, Point start, Point end){

    }

    public void setColor(int color){
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
