package vis;

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
        vel.add(accel);
        if (vel.mag() > 1)
            vel.mult(1/vel.mag());
        loc.add(vel);
    }

    public void attract(DustAndMagnet p) {
        Iterable<Magnet> ms = (Iterable<Magnet>) p.getMagnets();
        accel = new PVector();
        for (Magnet m : ms) {
            String attrName = m.getName();
            float value = (float) (data.get(attrName) * m.getValue() * 100);
            float dist = (float) Math.sqrt((Math.pow((m.getLoc().x - loc.x), 2) + Math.pow((m.getLoc().y - loc.y), 2)));
//            float dist = (float) (Math.pow((m.getLoc().x - loc.x), 2) + Math.pow((m.getLoc().y - loc.y), 2));
            if (dist < 20) { continue; }
            float theta = (float) (Math.atan2(m.getLoc().y - loc.y, m.getLoc().x - loc.x));
            accel.x += (float) Math.cos(theta) * (value / dist);
            accel.y += (float) Math.sin(theta) * (value / dist);
//            accel.x += value * (Math.cos(theta) * dist);
//            accel.y += value * (Math.sin(theta) * dist);
//            vel.x += value * (m.getLoc().x - loc.x);
//            vel.y += value * (m.getLoc().y - loc.y);
        }
    }

    public boolean contains (int x, int y) {
        return ((loc.x - x) * (loc.x - x) + (loc.y - y) * (loc.y - y)) <= 115;
    }

}
