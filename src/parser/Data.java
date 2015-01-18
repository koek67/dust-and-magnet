package parser;

import com.sun.javafx.binding.StringFormatter;

import java.util.*;
public class Data {
    // normalized data
    public HashMap<String, Double> norm;
    public double hp;
    public double liters, cyl, cmpg, hmpg, price;
    public String name, cat;

    public Data(){}


    protected void makeStuff() {
        norm = new HashMap<String, Double>();
        norm.put("Liters", liters);
        norm.put("Cylinders", cyl);
        norm.put("City MPG", cmpg);
        norm.put("Hwy MPG", hmpg);
        norm.put("HP", hp);
        norm.put("$", price);
    }

    public String toString() {
        String s = "";
        s += "------------------";
        s += "Name: " + name + "\n";
        s += "Cat: " + cat + "\n";
        s += "Price: " + price + "\n";
        s += "Size: " + liters + "\n";
        s += "Cyl: " + cyl + "\n";
        s += "Hp: " + hp + "\n";
        s += "cmpg: " + cmpg + "\n";
        s += "hmpg: " + hmpg + "\n";
        s += "------------------" + "\n";
        System.out.println(s);
        return s;
    }

}
