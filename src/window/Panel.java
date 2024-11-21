package window;

import raster.Raster;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    Raster raster;

    public Panel(Raster raster){
        this.raster = raster;
        setPreferredSize(new Dimension(raster.getWidth(), raster.getHeight()));
//        raster.getGraphics().drawString("ALT + MOUSE1 = thick line", 5, raster.getHeight() - 5);
//        raster.getGraphics().drawString("CTRL + MOUSE1 = normal line", 5, raster.getHeight() - 20);
//        raster.getGraphics().drawString("SHIFT + MOUSE1 = straight line", 5, raster.getHeight() - 35);
//        raster.getGraphics().drawString("MOUSE2 = Polygon", 5, raster.getHeight() - 50);
//        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.preset(g);
    }
}
