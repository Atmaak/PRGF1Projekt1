package rasterizers;

import objects.Line;
import objects.Polygon;
import raster.Raster;
import rasterizers.LineRasterizerTrivial;
import rasterizers.PolygonRasterizer;
import window.Panel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ScanLine {
    private Raster raster;
    private PolygonRasterizer polygonRasterizer;

    public ScanLine(Raster raster, PolygonRasterizer polygonRasterizer) {
        this.polygonRasterizer = polygonRasterizer;
        this.raster = raster;
    }

    public void fill(Polygon polygon) {
        //Pomocný seznam hran - array list ->Edge
        ArrayList<Line> edges = new ArrayList<>();
        for (int i = 0; i < polygon.size(); i++) {
            //projdu pointy polygonu a pro každé 2 pointy vytvořím hranu
            Point p1 = polygon.get(i).start;
            Point p2 = polygon.get((i+1) % polygon.size()).start;

            Line line = new Line(p1, p2);
            //hranu uložím do seznamu
            if(!line.isHorizontal()){
                line.oriented();
                edges.add(line);
            }
        }
        //najit y min, y max
        double yMin = polygon.get(0).start.y;
        double yMax = yMin;
        for (int i = 1; i < polygon.size(); i++) {
            double py1 = polygon.get(i).start.y;
            if (py1 < yMin) {
                yMin = py1;
            }else if (py1 > yMax) {
                yMax = py1;
            }
        }

        for (double y = yMin; y <= yMax; y++) {
            ArrayList<Integer> intersections = new ArrayList<>();
            for (Line line : edges) {
                if(line.hasInterection((int)y)){
                    //pokud ano,tak ho spocitam
                    int intersectionx = line.intersection((int) y);
                    intersections.add(intersectionx);
                    //vysledkem je souradnice x ulozim si ji do seznamu pruseciku
                }
            }
            //seřadit průsečíky z leva do prava popdle x
            bubbleSort(intersections);
            // vykreslím úsečku mezi každým lichým a sudým průsečíkem třeba přes line rasterizer
            // pomoci modula se dá udělat pattern - pokud ale nepoužijeme Linerasterizer
            for (int i = 0; i < intersections.size()-1; i++) {
                if (i % 2 == 0) {
                    for (double j = intersections.get(i); j < intersections.get(i+1); j++) {
                        raster.setRGB((int) j, (int) y, 0xffff00);
                    }
                }
            }
            intersections.clear();
        }
        polygonRasterizer.drawPolygon(polygon, false);
        // vykreslím hranici polygonu
    }

    static void bubbleSort(ArrayList<Integer> arr){
        int i, j, temp;
        boolean swapped;
        for (i = 0; i < arr.size() - 1; i++) {
            swapped = false;
            for (j = 0; j < arr.size() - i - 1; j++) {
                if (arr.get(j) > arr.get(j + 1)) {

                    // Swap arr[j] and arr[j+1]
                    temp = arr.get(j);
                    arr.set(j,arr.get(j + 1));
                    arr.set(j + 1,temp);
                    swapped = true;
                }
            }
            // If no two elements were
            // swapped by inner loop, then break
            if (!swapped)
                break;
        }
    }

}