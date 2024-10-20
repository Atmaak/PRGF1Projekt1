
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.SQLOutput;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * trida pro kresleni na platno: zobrazeni pixelu, ovladani mysi
 *
 * @author PGRF FIM UHK
 * @version 2020
 */

public class CanvasMouse {

    private JPanel panel;
    private BufferedImage img;

    private boolean drawing = false;

    private MouseEvent mouseEvent;

    public CanvasMouse(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        LineRasterizer line = new LineRasterizer(img);
        FilledLineRasterizer fillLine = new FilledLineRasterizer(img);
        Polygon polygon = new Polygon(img);
        StraightLine straightLine = new StraightLine(img);

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
                if (drawing) {
                    if (mouseEvent.isAltDown()) {
                        fillLine.drawGhostLine(g);
                    }
                    if (mouseEvent.isControlDown()) {
                        line.drawGhostLine(g);
                    }
                    if(mouseEvent.isShiftDown()) {
                        straightLine.drawGhostLine(g);
                    }
                }

                if(polygon.drawing){
                    polygon.drawRadius(g);
                }
                panel.repaint();

            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        panel.setFocusable(true);
        panel.requestFocusInWindow();

        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == 67) {
                    start();
                    line.reset();
                    fillLine.reset();
                    straightLine.reset();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if(!polygon.drawing){
                        polygon.setCenter(new Point(x, y));
                    }else if(!polygon.drawingPolygons) {
                        polygon.setRadius();
                    } else {
                        polygon.draw();
                    }
                }
                else if (e.isControlDown()) {
                    line.setStart(x, y);
                    drawing = true;
                }
                else if (e.isAltDown()) {
                    fillLine.setStart(x, y);
                    drawing = true;
                }
                else if (e.isShiftDown()) {
                    straightLine.setStart(x, y);
                    drawing = true;
                }

                panel.repaint();

                mouseEvent = e;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (e.isControlDown()) {
                    line.setEnd(x, y);
                    line.draw();
                    drawing = false;
                }

                if (e.isAltDown()) {
                    fillLine.setEnd(x, y);
                    fillLine.draw();
                    drawing = false;
                }

                if (e.isShiftDown()) {
                    straightLine.setEnd(x, y);
                    straightLine.draw();
                    drawing = false;
                }

                mouseEvent = null;

                panel.repaint();

            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (e.isControlDown()) {
                    line.setEnd(x, y);
                }
                if (e.isAltDown()) {
                    fillLine.setEnd(x, y);
                }
                if (e.isShiftDown()) {
                    straightLine.setEnd(x, y);
                }
                panel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if(polygon.drawing && !polygon.drawingPolygons){
                    polygon.setMousePosition(new Point(x, y));
                }else {
                    polygon.setCorners(new Point(x, y));
                }
            }
        });
    }

    public void clear() {
        Graphics gr = img.getGraphics();
        gr.setColor(new Color(0x2f2f2f));
        gr.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    public void present(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    public void start() {
        clear();
        img.getGraphics().drawString("ALT + MOUSE1 = thick line", 5, img.getHeight() - 5);
        img.getGraphics().drawString("CTRL + MOUSE1 = normal line", 5, img.getHeight() - 20);
        img.getGraphics().drawString("MOUSE2 = Polygon", 5, img.getHeight() - 35);
        panel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CanvasMouse(800, 800).start());
    }
}