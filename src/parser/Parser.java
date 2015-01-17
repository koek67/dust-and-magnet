
package parser;
import java.util.*;
import java.io.*;

public class Parser {
  public static ArrayList<Data> data;
  public static void fileContent() throws Exception{
    final int ROWS = 1202;
    final int COLUMNS = 5;
    data = new ArrayList<Data>();
    ArrayList<String> models = new ArrayList<String>();
    ArrayList<String> makes = new ArrayList<String>();
    Scanner scanner = new Scanner(new File("assets/carREAL.txt"));
    boolean done = false;
    boolean first = false;
    int count = 0;
    while (scanner.hasNext()) {
      try {
        String line = scanner.nextLine();
        String[] elem = line.split("\t");
        int year = Integer.parseInt(elem[0]);
        String make = elem[2];
        String model = elem[3];
        double liters = Double.parseDouble(elem[6]);
        double cyl = Double.parseDouble(elem[7]);
        //System.out.println(elem[9]);
        double cF = Double.parseDouble(elem[9]);
        double hwy = Double.parseDouble(elem[10]);
        String cat = elem[69];
        if (!models.contains(model)) {
          data.add(new Data(year, make, model, liters, cyl, cF, hwy, cat));
          models.add(model);
        }
        if (!makes.contains(make)) {
          makes.add(make);
        }
      } catch (Exception e) {
        System.out.println(count + 1);
      }
      count++;
    }

    // normalze liters
    double max = data.get(0).liters;
    double min = max;
    for (Data d : data) {
      if (d.liters > max)
        max = d.liters;
      else if (d.liters < min)
        min = d.liters;
    }
    double range = max - min;
    for (Data d : data) {
      d.liters = (d.liters - min) / range;
    }

    // normalize cyl
    max = data.get(0).cyl;
    min = data.get(0).cyl;
    for (Data d : data) {
      if (d.cyl > max)
      max = d.cyl;
      else if (d.cyl < min)
      min = d.cyl;
    }
    range = max - min;
    for (Data d : data) {
      d.cyl = (d.cyl - min) / range;
    }

    // normalize cF
    max = data.get(0).cF;
    min = data.get(0).cF;
    for (Data d : data) {
      if (d.cF > max)
      max = d.cF;
      else if (d.cF < min)
      min = d.cF;
    }
    range = max - min;
    for (Data d : data) {
      d.cF = (d.cF - min) / range;
    }

    // normlaize hwy
    max = data.get(0).hwy;
    min = data.get(0).hwy;
    for (Data d : data) {
      if (d.hwy > max)
      max = d.hwy;
      else if (d.hwy < min)
      min = d.hwy;
    }
    range = max - min;
    for (Data d : data) {
      d.hwy = (d.hwy - min) / range;
    }

    System.out.println(data);
    System.out.println(data.size());

  }

}
