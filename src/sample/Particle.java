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
    private PVector loc, vel, accel;
    /**
     * <code>data</code> is a map of all the attributes of the Particle
     * the key (a string) is the name of the attribute and the
     * value is a normalized double (floating point number) between
     * 0.0 and 1.0 inclusive
     */
    private Map<String, Double> data;
    private DustAndMagnet p;

    public Particle(int x, int y, DustAndMagnet p) {
        this(x, y, p, new HashMap<String, Double>());
    }

    public Particle(int x, int y, DustAndMagnet p, Map<String, Double> data) {
        loc = new PVector(x, y);
        vel = new PVector();
        accel = new PVector();
        this.p = p;
        this.data = data;
    }

    public void draw() {
        p.ellipse(loc.x, loc.y, 10, 10);
    }

    public void updateLocation() {
        vel.x += accel.x;
        vel.y += accel.y;
        loc.x += vel.x;
        loc.y += vel.y;
    }

    public void attract() {
        ArrayList<Magnet> ms = p.getMagnets();
        accel = new PVector();
        for (Magnet m : ms) {
            String attrName = m.getName();
            double value = data.get(attrName);
            accel.x += m.getValue() * value * (m.getLoc().x - loc.x) / 70000 * .99;
            accel.y += m.getValue() * value * (m.getLoc().y - loc.y) / 70000 * .99;
        }
    }

}
