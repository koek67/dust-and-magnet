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

    public Particle(int x, int y) {
        this(x, y, new HashMap<String, Double>());
    }

    public Particle(int x, int y, Map<String, Double> data) {
        loc = new PVector(x, y);
        vel = new PVector();
        accel = new PVector();
        this.data = data;
    }

    public void draw(DustAndMagnet p) {
        p.fill(256);
        p.ellipse(loc.x, loc.y, 10, 10);
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
            vel.x += m.getValue() * value * (m.getLoc().x - loc.x) / 100;
            vel.y += m.getValue() * value * (m.getLoc().y - loc.y) / 100;
        }
    }

}
