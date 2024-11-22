package rasterizers;

import objects.Line;

import java.util.ArrayList;

public class PolygonRasterizer {
    private final LineRasterizerTrivial lineRasterizer;
    public PolygonRasterizer(LineRasterizerTrivial lineRasterizer){
        this.lineRasterizer = lineRasterizer;
    }

//    public void drawPolygon(ArrayList<Line> lines){
//        System.out.println(lines);
//        for(Line line : lines){
//            lineRasterizer.drawLine(line.start, line.end);
//        }
//
//        if(lines.size() > 1){
//            Line firstLine = lines.get(0);
//            Line lastLine = lines.get(lines.size()-1);
//
//            lineRasterizer.drawLine(firstLine.start, lastLine.end);
//        }
//    }
    public void drawPolygon(ArrayList<Line> lines) {
        // Draw each line in the list
        for (Line line : lines) {
            lineRasterizer.drawLine(line.start, line.end);
        }

        // Add the missing line to close the polygon if not already closed
//        if (lines.size() > 1) {
//            Line firstLine = lines.get(0);
//            Line lastLine = lines.get(lines.size() - 1);
//
//            // Check if the polygon is already closed
//            if (!lastLine.end.equals(firstLine.start)) {
//                // Draw the closing line but do not modify the original list
//                lineRasterizer.drawLine(lastLine.end, firstLine.start);
//            }
//        }
    }

}
