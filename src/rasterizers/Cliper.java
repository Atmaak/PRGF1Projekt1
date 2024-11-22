package rasterizers;

import objects.Line;
import objects.Polygon;
import raster.Raster;

import java.awt.*;
import java.util.ArrayList;
public class Cliper {
    private Raster raster;
    public Cliper(Raster raster) {
        this.raster = raster;
    }

    public void cutPolygons(Polygon cutter) {
        for (int x = 0; x < 800; x++) {
            for (int y = 0; y < 800; y++) {
                if(!isPointInsidePolygon(new Point(x, y), cutter)) {
//                    System.out.println("filling" + new Point(x, y));
//                    int color = 0x00ff00;
//                    seedFill.fillAroundPolygon(new Point(x, y), color, (point, c) -> !isPointInsidePolygon(point, cutter));
//                    return ;
                    raster.setRGB(x, y, 0x2f2f2f);
                }
            }
        }
    }

    public boolean isPointInsidePolygon(Point p, Polygon polygon) {
        int intersections = 0;
        ArrayList<Line> edges = polygon;  // Get the lines of the polygon

        for (int i = 0; i < edges.size(); i++) {
            Line edge = edges.get(i);
            if (doesRayIntersectEdge(p, edge)) {
                intersections++;
            }
        }

        // If the number of intersections is odd, the point is inside; otherwise, it's outside
        return (intersections % 2 != 0);
    }

    private boolean doesRayIntersectEdge(Point p, Line edge) {
        // Check if a ray from the point p intersects the edge of the polygon
        Point p1 = edge.start;
        Point p2 = edge.end;

        // Check if the point is on the same level as the edge's y-coordinates (horizontal ray)
        if (p.y < Math.min(p1.y, p2.y) || p.y >= Math.max(p1.y, p2.y)) {
            return false; // The ray does not intersect this edge
        }

        // Find the x-coordinate where the ray intersects the line segment
        double xIntersection = (p.y - p1.y) * (p2.x - p1.x) / (double) (p2.y - p1.y) + p1.x;

        // If the intersection point is to the right of the point (i.e., inside)
        return xIntersection > p.x;
    }

    private ArrayList<Line> cutResult;

    public ArrayList<Line> clipPolygons(ArrayList<Polygon> polygons, Polygon cutter) {
        ArrayList<Line> result = new ArrayList<>();
        for (Polygon p : polygons) {
            result.addAll(p);
        }
        return clip(result, cutter);
    }

    public ArrayList<Line> clip(ArrayList<Line> linesToClip, ArrayList<Line> clipperLines) {
        ArrayList<Line> cutSubresult = linesToClip;

        for (int i = 0; i < clipperLines.size(); i++) {
            cutResult = new ArrayList<>();

            Line clipperLine = clipperLines.get(i);
            Point cP1 = clipperLine.start;
            Point cP2 = clipperLine.end;
            cutEdge(cP1, cP2, cutSubresult);
            cutSubresult = cutResult;
        }
        return cutResult;
    }

    private void cutEdge(Point cP1, Point cP2, ArrayList<Line> linesToCut) {
        Point t = new Point(cP2.x - cP1.x, cP2.y - cP1.y);
        Point n = new Point(-t.y, t.x);

        for (Line line : linesToCut) {
            Point p1 = line.start;
            Point p2 = line.end;

            Point v1 = new Point(p1.x - cP1.x, p1.y - cP1.y);
            int dotProduct1 = v1.x * n.x + v1.y * n.y;

            Point v2 = new Point(p2.x - cP1.x, p2.y - cP1.y);
            int dotProduct2 = v2.x * n.x + v2.y * n.y;

            if (dotProduct1 >= 0 && dotProduct2 >= 0) {
                // Both points inside
                cutResult.add(new Line(p1, p2));
            } else if (dotProduct1 >= 0 && dotProduct2 < 0) {
                // From inside to outside
                Point intersection = calculateIntersection(p1, p2, cP1, cP2);
                cutResult.add(new Line(p1, intersection));
            } else if (dotProduct1 < 0 && dotProduct2 >= 0) {
                // From outside to inside
                Point intersection = calculateIntersection(p1, p2, cP1, cP2);
                cutResult.add(new Line(intersection, p2));
            }
            // Else: Both points outside, do nothing
        }
    }

    private Point calculateIntersection(Point p1, Point p2, Point cP1, Point cP2) {
        float denominator = (p2.x - p1.x) * (cP1.y - cP2.y) - (p2.y - p1.y) * (cP1.x - cP2.x);
        if (denominator == 0) {
            return null; // Lines are parallel
        }
        float t = ((p1.x - cP1.x) * (cP1.y - cP2.y) - (p1.y - cP1.y) * (cP1.x - cP2.x)) / denominator;
        int x = Math.round(p1.x + t * (p2.x - p1.x));
        int y = Math.round(p1.y + t * (p2.y - p1.y));
        return new Point(x, y);
    }
}