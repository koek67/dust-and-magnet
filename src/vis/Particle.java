package vis;

import processing.core.PApplet;
import processing.core.PVector;
import quadtree.Quadtree;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Particle {
    PVector loc, vel, accel, repulseVel;
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
    Rectangle bounds;
    int dia;
    int dia2;
    int bound;

    boolean drawName;
    Quadtree quadtree;

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
        dia = 20;
        dia2 = dia * 2;
        bound = (int) (Math.pow((dia / 2), 2) * 1.15);
        bounds = new Rectangle((int) loc.x, (int) loc.y, dia, dia);
    }

    public void draw(DustAndMagnet p) {
        p.fill(66, 255, 35, 100);
        p.stroke(66, 255, 35, 100);
        if (hc != null) {
            p.fill(hc.x, hc.y, hc.z, 100);
            p.stroke(hc.x, hc.y, hc.z, 100);
        }
        p.ellipse(loc.x, loc.y, dia, dia);
    }

    public void updateLocation() {
        loc.add(vel);
//        loc.add(repulseVel);
        vel.mult(.4f);
        bounds.x = (int) loc.x;
        bounds.y = (int) loc.y;
    }

    public void attract(DustAndMagnet p) {
        Iterable<Magnet> ms = (Iterable<Magnet>) p.getMagnets();
//        vel = new PVector();
        for (Magnet m : ms) {
            String attrName = m.getName();
            double value = data.get(attrName);
            vel.x += m.getValue() * value * (m.getLoc().x - loc.x) / 50;
            vel.y += m.getValue() * value * (m.getLoc().y - loc.y) / 50;
        }
    }

    public boolean contains (int x, int y) {
        return ((loc.x - x) * (loc.x - x) + (loc.y - y) * (loc.y - y)) <= bound;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setQuadtree(Quadtree quadtree) {
//        this.quadtree = quadtree;
    }

    public String toString() {
        String s = name;
        return s;
    }
}
