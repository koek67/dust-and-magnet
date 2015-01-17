package parser;

import java.util.*;
public class Data {
  private HashMap<String,Double> one;
  private HashMap<String, String> two;
  private ArrayList<String> cats;
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
  public void setLiters(int i) {liters = i;}

  public Data(HashMap<String, Double> one, HashMap<String, String> two, ArrayList<String> cats) {
    // this.name = name;
    // this.pop = pop;
    // this.cats = cats;
  }

  public String toString() {
    System.out.format("%d %30s 30%s %30f%10f%7f%7f%7s", year, make, model, liters, cyl, cF, hwy, cat);
    System.out.print("\n");
    return "";
  }

}
