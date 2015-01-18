package sample;

import java.awt.*;

/**
 * Created by andrew on 1/17/15.
 */
public class Button {

    String name;
    Rectangle r;

    public Button(String name, Rectangle r) {
        this.name = name;
        this.r = r;
    }

    public Button(String name, int x, int y, int width, int height) {
        this(name, new Rectangle(x, y, width, height));
    }

    // FOR TESTING ONLY
    static int i = 1;
    public Button(int x, int y, int width, int height) {
        this("" + i++, x, y, width, height);
    }

    public void draw(DustAndMagnet d, int x, int y) {
        if (contains(x, y)) {
            d.fill(150);
        }
        else {
            d.fill(200);
        }
        d.stroke(256f);
        d.rect(r.x, r.y, r.width, r.height);
        d.fill(0);
        d.textSize(20);
        d.text(name, r.x + r.width / 2 - 6 * name.length(), r.y + r.height / 2 + 6);
    }

    public boolean contains(int x, int y) { return r.contains(x, y); }

}
