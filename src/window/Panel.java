package window;

import objects.FancyPolygon;
import raster.Raster;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    Raster raster;
    FancyPolygon fancyPolygon;
    public Panel(Raster raster, FancyPolygon fancyPolygon) {
        this.raster = raster;
        this.fancyPolygon = fancyPolygon;
        setPreferredSize(new Dimension(raster.getWidth(), raster.getHeight()));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.preset(g);
        if(fancyPolygon != null) {
            if(fancyPolygon.drawing){
                fancyPolygon.drawRadius(g);
            }
        }

        this.repaint();

    }
}
