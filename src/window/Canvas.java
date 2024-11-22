package window;

import objects.FancyPolygon;
import objects.Line;
import objects.Polygon;
import raster.Raster;
import rasterizers.*;

import utils.TypeOfLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Canvas extends JFrame {
    int width, height;
    Raster raster;
    Panel panel;
    LineRasterizerTrivial lineRasterizerTrivial;
    PolygonRasterizer polygonRasterizer;
    Polygon polygon;
    ScanLine scanLine;
    ArrayList<Polygon> polygons = new ArrayList<>();
    FancyPolygon fancyPolygon;
    SeedFill seedFill;
    Cliper cliper;
    boolean shiftPressed = false;
    boolean ctrlPressed = false;
    boolean gPressed = false;
    boolean fPressed = false;
    boolean kPressed = false;
    Point start, end, fillStart;

    public Canvas(int width, int height){
        this.width = width;
        this.height = height;
        init();
        initListeners();
    }

    private void init(){
        setLayout(new BorderLayout());
        setTitle("PGRF");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new Raster(width, height);
        lineRasterizerTrivial = new LineRasterizerTrivial(raster);
        lineRasterizerTrivial.setColor(0xff0000);
        polygonRasterizer = new PolygonRasterizer(lineRasterizerTrivial);
        seedFill = new SeedFill(raster);
        scanLine = new ScanLine(raster, polygonRasterizer);
        cliper = new Cliper(raster);
        fancyPolygon = new FancyPolygon(raster);

        panel = new Panel(raster, fancyPolygon);
        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
        polygons.add(new Polygon());
        polygon = polygons.get(polygons.size() - 1);
    }

    private void initListeners(){
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                start = new Point(e.getX(), e.getY());

                if(kPressed){
                    if(!fancyPolygon.drawing){
                        fancyPolygon.setCenter(new Point(e.getX(), e.getY()));
                    }else if(!fancyPolygon.drawingPolygons) {
                        fancyPolygon.setRadius();
                    } else {
                        fancyPolygon.draw();
                    }
                }

                if(gPressed){
                    fillStart = new Point(e.getX(), e.getY());
                    seedFill.fill(fillStart, 0xff0000, (currentValue, point) -> currentValue != 0xff0000);
                    panel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println(kPressed);
                if (gPressed || kPressed) return;

                if (!shiftPressed) {
                    end = new Point(e.getX(), e.getY());
                }

                raster.clear();
                redrawPolygons(polygons);
                if (polygon.isEmpty()) {
                    polygon.add(new Line(start, end));
                } else if(polygon.size() == 1){
                    Line lastLine = polygon.get(polygon.size() - 1);
                    polygon.add(new Line(lastLine.start, end));
                    polygon.add(new Line(lastLine.end, end));
                } else {
                    Line firstLine = polygon.get(0);
                    Line lastLine = polygon.get(polygon.size() - 1);

                    polygon.remove(new Line(firstLine.start, lastLine.end));

                    polygon.add(new Line(firstLine.start, end));
                    polygon.add(new Line(lastLine.end, end));
                }

                polygonRasterizer.drawPolygon(polygon);

                if (fillStart != null) {
                    seedFill.fill(fillStart, 0xff0000, (currentValue, point) -> currentValue != 0xff0000);
                }

                panel.repaint();
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if(fancyPolygon.drawing && !fancyPolygon.drawingPolygons){
                    fancyPolygon.setMousePosition(new Point(x, y));
                }else {
                    fancyPolygon.setCorners(new Point(x, y));
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(kPressed){
                    fancyPolygon.setMousePosition(new Point(e.getX(), e.getY()));
                    return ;
                }

                end = new Point(e.getX(), e.getY());

                raster.clear();
                redrawPolygons(polygons);

                if(shiftPressed){
                    switch (getClosestLineType(start, end)){
                        case DIAGONAL: switchPoints(); break;
                        case VERTICAL : start.y = end.y; break;
                        case HORIZONTAL : start.x = end.x; break;
                    }
                }

                if(!polygon.isEmpty()){
                    lineRasterizerTrivial.setColor(0xff0000);
                    Line fl = polygon.get(0);
                    start = fl.start;
                    lineRasterizerTrivial.drawLine(start, end);

                    Line ll = polygon.get(polygon.size() - 1);
                    start = ll.end;
                }

                lineRasterizerTrivial.drawLine(start, end);

                lineRasterizerTrivial.setColor(0x00ff00);

                polygonRasterizer.drawPolygon(polygon);
                panel.repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if(keyCode == KeyEvent.VK_C){
                    polygons.clear();
                    polygons.add(new Polygon());
                    polygon = polygons.get(polygons.size() - 1);
                    raster.clear();
                    fillStart = null;
                    panel.repaint();
                }

                if(keyCode == KeyEvent.VK_SHIFT){
                    shiftPressed = true;
                }

                if(keyCode == 17){
                    ctrlPressed = true;
                }

                if(keyCode == 70){
                    fPressed = true;
                }

                if(keyCode == 71){
                    gPressed = true;
                }

                if(keyCode == 72){
                    polygons.add(new Polygon());
                    polygon = polygons.get(polygons.size() - 1);
                }

                if(keyCode == 75){
                    kPressed = true;
                }

                if(keyCode == 74){
                    scanLine.fill(polygons.get(polygons.size() - 1));
                    panel.repaint();
                }

                if (keyCode == 76) {
                    Polygon cutter = polygons.getLast();
//                    ArrayList<Line> lines = sutherHodgman.clipPolygons(polygons, cutter);
//
//                    raster.clear();
//
//                    for (Line line : lines) {
//                        lineRasterizerTrivial.drawLine(line.start, line.end);
//                    }
//

                    cliper.cutPolygons(cutter);
                    polygonRasterizer.drawPolygon(cutter);


                    panel.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if(keyCode == KeyEvent.VK_SHIFT){
                    shiftPressed = false;
                }

                if(keyCode == 17){
                    ctrlPressed = false;
                }

                if(keyCode == 70){
                    fPressed = false;
                }

                if(keyCode == 71){
                    gPressed = false;
                }

                if(keyCode == 75){
                    kPressed = false;
                }
            }
        });
    }

    public TypeOfLine getClosestLineType(Point start, Point end) {
        int dx = Math.abs(end.x - start.x);
        int dy = Math.abs(end.y - start.y);

        double threshold = 0.5;

        if (Math.abs(dx - dy) <= threshold * Math.max(dx, dy)) {
            return TypeOfLine.DIAGONAL;
        } else if ((double) dx < (double) dy) {
            return TypeOfLine.HORIZONTAL;
        } else {
            return TypeOfLine.VERTICAL;
        }
    }

    public static Point calculateDiagonalEndPoint(Point start, Point end) {
        int dx = end.x - start.x;
        int dy = end.y - start.y;

        double newX, newY;

        if (Math.abs(dx) > Math.abs(dy)) {
            newX = end.x;
            newY = start.y + Math.signum(dy) * Math.abs(dx);
        } else {
            newX = start.x + Math.signum(dx) * Math.abs(dy);
            newY = end.y;
        }

        return new Point((int)newX, (int)newY);
    }

    public void redrawPolygons(ArrayList<Polygon> polygons){
        for (Polygon polygon: polygons) {
            polygonRasterizer.drawPolygon(polygon);
        }
    }

    public void switchPoints(){
        end = calculateDiagonalEndPoint(start, end);
    }

    public void start(){
        raster.clear();
    }
}