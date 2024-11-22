package rasterizers;

import objects.Line;
import objects.Polygon;
import raster.Raster;

import java.awt.*;
import java.util.ArrayList;

public class Cliper {
    public Cliper() {
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

            if (dotProduct1 >= 0) {
                cutResult.add(new Line(p1, p2));
            }

            Point v2 = new Point(p2.x - cP1.x, p2.y - cP1.y);
            int dotProduct2 = v2.x * n.x + v2.y * n.y;

            if ((dotProduct1 >= 0 && dotProduct2 < 0) || (dotProduct1 < 0 && dotProduct2 >= 0)) {
                float k1 = ((float) (cP2.y - cP1.y) / (cP2.x - cP1.x));
                float k2 = ((float) (p2.y - p1.y) / (p2.x - p1.x));
                float q1 = cP1.y - k1 * cP1.x;
                float q2 = p1.y - k2 * p1.x;

                int x = Math.round((q2 - q1) / (k1 - k2));
                int y = Math.round(k1 * x + q1);
                Point intersection = new Point(x, y);
                cutResult.add(new Line(p1, intersection));
                cutResult.add(new Line(intersection, p2));
            }
        }
    }


}



