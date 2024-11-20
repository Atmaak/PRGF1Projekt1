package window;

import raster.Raster;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    Raster raster;

    public Panel(Raster raster){
        this.raster = raster;
        setPreferredSize(new Dimension(raster.getWidth(), raster.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.preset(g);
    }
}
