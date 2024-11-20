package rasterizers;

import objects.Line;

import java.util.ArrayList;

public class PolygonRasterizer {
    private final LineRasterizerTrivial lineRasterizer;
    public PolygonRasterizer(LineRasterizerTrivial lineRasterizer){
        this.lineRasterizer = lineRasterizer;
    }

    public void drawPolygon(ArrayList<Line> lines, boolean bold){
        for(Line line : lines){
            if(bold) lineRasterizer.drawBoldLine(line.start, line.end);
            else lineRasterizer.drawLine(line.start, line.end);
        }

        if(lines.size() > 1){
            Line firstLine = lines.get(0);
            Line lastLine = lines.get(lines.size()-1);

            if(bold) lineRasterizer.drawBoldLine(firstLine.start, lastLine.end);
            else lineRasterizer.drawLine(firstLine.start, lastLine.end);
        }
    }
}
