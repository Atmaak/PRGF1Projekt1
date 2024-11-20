import java.awt.*;
import java.awt.image.BufferedImage;

public class OldLineRasterizer {
    public int x1, x2, y1, y2 = 0;
    public BufferedImage img;
    public int color = 0xFF0000;

    public OldLineRasterizer(BufferedImage img) {
        this.img = img;
    }

    public OldLineRasterizer(BufferedImage img, int x1, int y1, int x2, int y2) {
        this.img = img;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
        drawLine();
    }

    public void setStart(int x1, int y1){
        this.x1 = x1;
        this.y1 = y1;
    }

    public void setEnd(int x2, int y2){
        this.x2 = x2;
        this.y2 = y2;
    }

    public void draw() {
        if(x1 == -1 || x2 == -1 || y1 == -1 || y2 == -1){
            return;
        }

        drawLine();

        setStart(-1, -1);
        setEnd(-1, -1);
    }

    public void drawLine() {
        // Výpočet rozdílů v souřadnicích mezi počátečním a koncovým bodem
        int dx = Math.abs(x2 - x1); // Vzdálenost mezi body v ose X
        int dy = Math.abs(y2 - y1); // Vzdálenost mezi body v ose Y

        // Určení směru, kterým se bude kreslit přímka v obou osách
        int sx = (x1 < x2) ? 1 : -1; // Směr pohybu v ose X (1 doprava, -1 doleva)
        int sy = (y1 < y2) ? 1 : -1; // Směr pohybu v ose Y (1 dolů, -1 nahoru)

        // Inicializace chyby, která se bude v průběhu algoritmu upravovat
        int err = dx - dy; // Chyba při výběru následujícího bodu

        // Hlavní smyčka pro vykreslení přímky
        while (true) {
            // Kontrola, jestli je bod mimo hranice obrázku (pokud ano, algoritmus končí)
            if(x1 >= img.getWidth() || x1 < 0 || y1 >= img.getHeight() || y1 < 0) break;

            // Nastavení barvy pixelu na aktuální souřadnici
            img.setRGB(x1, y1, color);

            // Pokud jsme dosáhli koncového bodu, přímka je hotová, ukončíme smyčku
            if (x1 == x2 && y1 == y2) break;

            // Dvojnásobek chyby, který určuje, zda se posunout více v ose X nebo Y
            int err2 = err * 2;

            // Pokud chyba překročí záporný rozdíl dy, opravíme chybu a posuneme se v ose X
            if (err2 > -dy) {
                err -= dy; // Aktualizace chyby
                x1 += sx;  // Posun v ose X podle směru
            }

            // Pokud chyba je menší než rozdíl dx, opravíme chybu a posuneme se v ose Y
            if (err2 < dx) {
                err += dx; // Aktualizace chyby
                y1 += sy;  // Posun v ose Y podle směru
            }
        }
    }


    public void drawGhostLine(Graphics g) {
        g.setColor(Color.green);
        g.drawLine(x1, y1, x2, y2);
    }

    public void reset(){
        x1 = 0;
        x2 = 0;
        y1 = 0;
        y2 = 0;
    }
}
