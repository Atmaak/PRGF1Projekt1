package objects;

import objects.Line;

import java.util.ArrayList;

public class Polygon extends ArrayList<Line> {
    private boolean bold;

    public Polygon(Boolean bold){
        this.bold = bold;
    }

    public Polygon(){
        this.bold = false;
    }
    public boolean isBold() {
        return bold;
    }

}
