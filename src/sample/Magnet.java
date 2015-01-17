/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

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
    DustAndMagnet p;
    private float rad2;
    private float dia;

    public Magnet(String name, double value, int x, int y, DustAndMagnet p) {
        this.name = name;
        this.value = value;
        loc = new PVector(x, y);
        setRadius(25);
        this.p = p;
    }

    public Magnet(double value, int x, int y, DustAndMagnet p) {
        this("name", value, x, y, p);
    }

    public Magnet() {
        this("", -1, -1, -1, null);
    }

    public String getName() {return name;}
    public double getValue() {return value;}
    public PVector getLoc() {return loc;}
    public void setLoc(float x, float y) { loc.x = x; loc.y = y; }
    public void setRadius(float rad) {
        rad2 = rad * rad;
        dia = rad * 2;
    }
    public void draw() {
        p.ellipse(loc.x, loc.y, dia, dia);
    }

    public boolean contains (int x, int y) {
        return ((loc.x - x) * (loc.x - x) + (loc.y - y) * (loc.y - y)) <= rad2;
    }
}
