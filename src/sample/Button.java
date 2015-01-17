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

    public boolean contains(int x, int y) { return r.contains(x, y); }

}
