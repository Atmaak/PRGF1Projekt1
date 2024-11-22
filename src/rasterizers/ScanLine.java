package rasterizers;

import objects.Line;
import objects.Polygon;
import raster.Raster;
import java.util.ArrayList;

public class ScanLine {
    private Raster raster;
    private PolygonRasterizer polygonRasterizer;

    public ScanLine(Raster raster, PolygonRasterizer polygonRasterizer) {
        this.polygonRasterizer = polygonRasterizer;
        this.raster = raster;
    }

    public void fill(Polygon polygon) {
        // Auxiliary list for edges
        ArrayList<Line> edges = new ArrayList<>();

        Polygon newPolygon = (Polygon)polygon.clone();

        // Populate edges from the polygon
        for (Line line : newPolygon) {
            if (!line.isHorizontal()) {
                line.oriented(); // Ensure consistent orientation
                edges.add(line);
            }
        }

        // Find yMin and yMax
        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;

        for (Line line : newPolygon) {
            // Compare both endpoints of the line
            yMin = Math.min(yMin, Math.min(line.start.y, line.end.y));
            yMax = Math.max(yMax, Math.max(line.start.y, line.end.y));
        }
        
        // Iterate through each scan line
        for (double y = yMin; y <= yMax; y++) {
            ArrayList<Integer> intersections = new ArrayList<>();

            // Calculate intersections with each edge
            for (Line line : edges) {
                if (line.hasInterection((int) y)) {
                    int intersectionX = line.intersection((int) y);
                    intersections.add(intersectionX);
                }
            }

            // Sort intersections from left to right
            intersections.sort(Integer::compareTo);

            // Draw spans between each pair of intersections
            for (int i = 0; i < intersections.size() - 1; i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);

                for (int x = xStart; x < xEnd; x++) {
                    raster.setRGB(x, (int) y, 0xffff00); // Fill with yellow color
                }
            }
        }

        // Draw the boundary of the polygon
        polygonRasterizer.drawPolygon(newPolygon);
    }


}