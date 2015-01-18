package parser;

import com.sun.javafx.binding.StringFormatter;

import java.util.*;
public class Data {
    // normalized data
    public HashMap<String, Double> norm;
    private int year;
    public double liters, cyl, cF, hwy;
    public String make, model, cat;

    public Data(int year,String make,String model,double liters,double cyl,double cF,double hwy,String cat) {
        this.year = year;
        this.make = make;
        this.model = model;
        this.liters = liters;
        this.cyl = cyl;
        this.cF = cF;
        this.hwy = hwy;
        this.cat = cat;
    }

    public void makeStuff() {
        norm = new HashMap<String, Double>();
        norm.put("Liters", liters);
        norm.put("Cylinders", cyl);
        norm.put("City MPG", cF);
        norm.put("Hwy MPG", hwy);
        System.out.println(this);
    }

    @Override
    public String toString() {
        return StringFormatter.format("%10s %s %10f %10f %7f %7f", make, model, liters, cyl, cF, hwy).getValue();
    }

}
