package rasterizers;

import objects.Line;
import objects.Polygon;
import raster.Raster;

import java.awt.*;
import java.util.ArrayList;

public class SutherHodgman {
    private SeedFill seedFill;
    private Raster raster;
    public SutherHodgman(SeedFill seedFill, Raster raster) {
        this.seedFill = seedFill;
        this.raster = raster;
    }

//    public ArrayList<Line> cutPolygons(ArrayList<Polygon> polygons, Polygon cutter) {
//
//    }

    public void cutPolygonsCrack(Polygon cutter) {
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


//    private ArrayList<Line> clipPolygon(Polygon polygon, Polygon cutter) {
//        ArrayList<Line> clippedLines = new ArrayList<>();
//
//        // Get the list of lines for the polygon
//        ArrayList<Line> polygonLines = polygon;
//
//        // Clip the polygon against each edge of the cutter polygon
//        for (int i = 0; i < cutter.size(); i++) {
//            Line cutterEdge = cutter.get(i);
//            ArrayList<Line> newClippedPolygon = new ArrayList<>();
//
//            // Process each line of the polygon
//            for (int j = 0; j < polygonLines.size(); j++) {
//                Line polygonEdge = polygonLines.get(j);
//                ArrayList<Point> intersectionPoints = clipEdge(polygonEdge, cutterEdge);
//
//                if (!intersectionPoints.isEmpty()) {
//                    // Add the intersection points as lines to the new clipped polygon
//                    for (Point intersection : intersectionPoints) {
//                        newClippedPolygon.add(new Line(polygonEdge.start, intersection));
//                        newClippedPolygon.add(new Line(intersection, polygonEdge.end));
//                    }
//                }
//            }
//
//            polygonLines = newClippedPolygon;
//        }
//
//        clippedLines.addAll(polygonLines);
//        return clippedLines;
//    }
//
//    private ArrayList<Point> clipEdge(Line polygonEdge, Line cutterEdge) {
//        ArrayList<Point> intersectionPoints = new ArrayList<>();
//
//        // Get the intersection points between the polygon edge and the cutter edge
//        // For now, we need to implement a basic line intersection test
//
//        // Example, assume you implement a function getIntersection which returns intersection point(s)
//        Point intersection = getIntersection(polygonEdge, cutterEdge);
//        if (intersection != null) {
//            intersectionPoints.add(intersection);
//        }
//
//        return intersectionPoints;
//    }
//
//    private Point getIntersection(Line polygonEdge, Line cutterEdge) {
//        // Calculate the intersection point of two lines (polygonEdge and cutterEdge)
//        // This is a placeholder for actual intersection calculation
//        // You can implement line intersection calculation here (e.g., using determinants)
//        return null; // Implement intersection logic
//    }
}
