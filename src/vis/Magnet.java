/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vis;

import processing.core.PApplet;
import processing.core.PVector;

/**
 *
 * @author koushikkrishnan
 */
public class Magnet {
    private String name;
    private PVector loc;
    private double value;
    private float rad2;
    private float dia;

    public Magnet(String name, double value, int x, int y) {
        this.name = name;
        this.value = value;
        loc = new PVector(x, y);
        setRadius(25);
    }

    public Magnet(double value, int x, int y) {
        this("name", value, x, y);
    }

    public Magnet() {
        this("", -1, -1, -1);
    }

    public String getName() {return name;}
    public double getValue() {return value;}
    public PVector getLoc() {return loc;}
    public void setLoc(float x, float y) { loc.x = x; loc.y = y; }
    public void setRadius(float rad) {
        rad2 = rad * rad;
        dia = rad * 2;
    }
    public void draw(DustAndMagnet d) {
        d.stroke(0);
        d.fill(256f);
        d.ellipse(loc.x, loc.y, dia, dia);
        d.fill(0);
        d.textSize(20);
        d.text(name, loc.x - 6, loc.y + 6);
    }

    public boolean contains (int x, int y) {
        return ((loc.x - x) * (loc.x - x) + (loc.y - y) * (loc.y - y)) <= rad2;
    }
}
