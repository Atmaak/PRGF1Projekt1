package objects;

import java.awt.*;
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

    @Override
    public String toString() {
        return "Line [start=" + start + ", end=" + end + "]";
    }

}
