package objects;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Optional;

public class Line {
    public Point start, end;
    public Line(Point start, Point end){
        this.start = start;
        this.end = end;
    }

    public void oriented() {
        if (start.y > end.y) {
            Point temp = start;
            start = end;
            end = temp;
        }
    }

    public Optional<Float> yIntercept(int y) {
        if (y < start.y || y > end.y || start.y == end.y) {
            return Optional.empty();
        }
        float slope = (float) (end.x - start.x) / (end.y - start.y);
        float interceptX = start.x + (y - start.y) * slope;
        return Optional.of(interceptX);
    }

    public boolean hasInterection(final int y){
        return (y >= start.y && y <= end.y || y >= end.y && y <= start.y );
    }


    public int intersection(final int y){
        if (start.y != end.y) {
            final double k = (end.x- start.x) / (double) (end.y - start.y);
            final double q = start.x - k * start.y;
            return (int) Math.round(k * y + q);
        }
        return start.y;
    }

    public boolean isHorizontal(){
        return (start.y == end.y);
    }

    public Point getIntersection(Line other) {
        Line2D line1 = new Line2D.Double(this.start.x, this.start.y, this.end.x, this.end.y);
        Line2D line2 = new Line2D.Double(other.start.x, other.start.y, other.end.x, other.end.y);

        if (!line1.intersectsLine(line2)) {
            return null;
        }

        double a1 = this.end.y - this.start.y;
        double b1 = this.start.x - this.end.x;
        double c1 = a1 * this.start.x + b1 * this.start.y;

        double a2 = other.end.y - other.start.y;
        double b2 = other.start.x - other.end.x;
        double c2 = a2 * other.start.x + b2 * other.start.y;

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            return null; // Parallel lines
        }

        double x = (b2 * c1 - b1 * c2) / determinant;
        double y = (a1 * c2 - a2 * c1) / determinant;
        return new Point((int)x, (int)y);
    }

    @Override
    public String toString() {
        return "Line [start=" + start + ", end=" + end + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Line line = (Line) obj;
        return start.equals(line.start) && end.equals(line.end);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }
}
