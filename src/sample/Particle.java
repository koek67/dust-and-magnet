package sample;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by koushikkrishnan on 1/17/15.
 */
public class Particle {
    PVector loc, vel, accel;
    /**
     * <code>data</code> is a map of all the attributes of the Particle
     * the key (a string) is the name of the attribute and the
     * value is a normalized double (floating point number) between
     * 0.0 and 1.0 inclusive
     */
    Map<String, Double> data;
    String cat;
    String name;
    // highlight color
    PVector hc;

    boolean drawName;

    public Particle(int x, int y) {
        this(x, y, new HashMap<String, Double>(), "Mustang", "Ford");
    }

    public Particle(int x, int y, Map<String, Double> data, String name, String cat) {
        loc = new PVector(x, y);
        vel = new PVector();
        accel = new PVector();
        this.data = data;
        this.name = name;
        this.cat = cat;
        drawName = false;
    }

    public void draw(DustAndMagnet p) {
        p.fill(66, 255, 35, 100);
        p.stroke(66, 255, 35, 100);
        if (hc != null) {
            p.fill(hc.x, hc.y, hc.z, 100);
            p.stroke(hc.x, hc.y, hc.z, 100);
        }
        p.ellipse(loc.x, loc.y, 20, 20);
    }

    public void updateLocation() {
        loc.x += vel.x;
        loc.y += vel.y;
    }

    public void attract(DustAndMagnet p) {
        Iterable<Magnet> ms = (Iterable<Magnet>) p.getMagnets();
        vel = new PVector();
        for (Magnet m : ms) {
            String attrName = m.getName();
            double value = data.get(attrName);
            vel.x += m.getValue() * value * (m.getLoc().x - loc.x) / 50;
            vel.y += m.getValue() * value * (m.getLoc().y - loc.y) / 50;
        }
    }

    public boolean contains (int x, int y) {
        return ((loc.x - x) * (loc.x - x) + (loc.y - y) * (loc.y - y)) <= 115;
    }

}
