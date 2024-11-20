package window;

import objects.Line;
import objects.Polygon;
import raster.Raster;
import rasterizers.LineRasterizerTrivial;
import rasterizers.PolygonRasterizer;

import rasterizers.SeedFill;
import utils.TypeOfLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Canvas extends JFrame {
    int width, height;
    Raster raster;
    Panel panel;
    LineRasterizerTrivial lineRasterizerTrivial;
    PolygonRasterizer polygonRasterizer;
    Polygon polygon;
    SeedFill seedFill;
    boolean shiftPressed = false;
    boolean ctrlPressed = false;
    boolean gPressed = false;
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

        panel = new Panel(raster);
        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);

        polygon = new Polygon();
    }

    private void initListeners(){
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                start = new Point(e.getX(), e.getY());

                if(gPressed){
                    fillStart = new Point(e.getX(), e.getY());
                    seedFill.fill(fillStart, 0xff0000, (currentValue, point) -> currentValue != 0xff0000);
                    panel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(gPressed) return;
                if(!shiftPressed){
                    end = new Point(e.getX(), e.getY());
                }

                raster.clear();

                if(!polygon.isEmpty()){
                    Line ll = polygon.get(polygon.size()-1);
                    start = new Point(ll.end.x, ll.end.y);
                }

                polygon.add(new Line(start, end));
                polygonRasterizer.drawPolygon(polygon, polygon.isBold());
                if(fillStart != null) seedFill.fill(fillStart, 0xff0000, (currentValue, point) -> currentValue != 0xff0000);

                panel.repaint();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                end = new Point(e.getX(), e.getY());

                raster.clear();

                if(shiftPressed){
                    switch (getClosestLineType(start, end)){
                        case TypeOfLine.DIAGONAL: switchPoints(); break;
                        case TypeOfLine.VERTICAL : start.y = end.y; break;
                        case TypeOfLine.HORIZONTAL : start.x = end.x; break;
                    }
                }

                if(!polygon.isEmpty()){
                    lineRasterizerTrivial.setColor(0xff0000);
                    Line fl = polygon.get(0);
                    start = fl.start;
                    lineRasterizerTrivial.drawLine(start, end);

                    Line ll = polygon.get(polygon.size()-1);
                    start = ll.end;
                }

                lineRasterizerTrivial.drawLine(start, end);

                lineRasterizerTrivial.setColor(0x00ff00);

                polygonRasterizer.drawPolygon(polygon, polygon.isBold());
                panel.repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if(keyCode == KeyEvent.VK_C){
                    polygon = new Polygon();
                    raster.clear();
                    panel.repaint();
                }

                if(keyCode == KeyEvent.VK_SHIFT){
                    shiftPressed = true;
                }

                if(keyCode == 71){
                    gPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if(keyCode == KeyEvent.VK_SHIFT){
                    shiftPressed = false;
                }
                if(keyCode == 17){
                    ctrlPressed = !ctrlPressed;
                }
                if(keyCode == 71){
                    gPressed = false;
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

    public void switchPoints(){
        end = calculateDiagonalEndPoint(start, end);
    }

    public void start(){
        raster.clear();
    }
}
