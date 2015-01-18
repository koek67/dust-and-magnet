package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * Created by koushikkrishnan on 1/17/15.
 */
public class Parser2 {

    public static ArrayList<Data> data;
    public static void fileContent() throws FileNotFoundException{
        data = new ArrayList<Data>();
        ArrayList<String> models = new ArrayList<String>();
        ArrayList<String> makes = new ArrayList<String>();
        Scanner scanner = new Scanner(new File("assets/carNEW.txt"));
        boolean first = true;
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Sedan");
        categories.add("Sports Car");
        categories.add("SUV");
        categories.add("Wagon");
        categories.add("Minivan");
        categories.add("Pickup");
        scanner.nextLine();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] elem = line.split("\t");
            Data a = new Data();
            a.name = elem[0];
            int j = 1;
            for (; j < 7 && Integer.parseInt(elem[j]) != 1; j++) {}
            if (j == 1) a.cat = "Sedan";
            else if (j == 2) a.cat = "Sports Car";
            else if (j == 3) a.cat = "SUV";
            else if (j == 4) a.cat = "Wagon";
            else if (j == 5) a.cat = "Minivan";
            else if (j == 6) a.cat = "Pickup";
            a.price = Double.parseDouble(elem[9]);
            a.liters = Double.parseDouble(elem[11]);
            a.cyl = Double.parseDouble(elem[12]);
            a.hp = Double.parseDouble(elem[13]);
            a.cmpg = Double.parseDouble(elem[14]);
            a.hmpg = Double.parseDouble(elem[15]);
            data.add(a);
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

        // normalize cmpg
        max = data.get(0).cmpg;
        min = data.get(0).cmpg;
        for (Data d : data) {
            if (d.cmpg > max)
                max = d.cmpg;
            else if (d.cmpg < min)
                min = d.cmpg;
        }
        range = max - min;
        for (Data d : data) {
            d.cmpg = (d.cmpg - min) / range;
        }

        // normlaize hmpg
        max = data.get(0).hmpg;
        min = data.get(0).hmpg;
        for (Data d : data) {
            if (d.hmpg > max)
                max = d.hmpg;
            else if (d.hmpg < min)
                min = d.hmpg;
        }
        range = max - min;
        for (Data d : data) {
            d.hmpg = (d.hmpg - min) / range;
        }

        // normlaize hp
        max = data.get(0).hp;
        min = data.get(0).hp;
        for (Data d : data) {
            if (d.hp > max)
                max = d.hp;
            else if (d.hp < min)
                min = d.hp;
        }
        range = max - min;
        for (Data d : data) {
            d.hp = (d.hp - min) / range;
        }

        max = data.get(0).price;
        min = data.get(0).price;
        for (Data d : data) {
            if (d.price > max)
                max = d.price;
            else if (d.price < min)
                min = d.price;
        }
        range = max - min;
        for (Data d : data) {
            d.price = (d.price - min) / range;
        }

//        System.out.println(data);
        System.out.println(data.size());

        for (Data d : data) { d.makeStuff(); }

    }

    public static void main(String[] args) throws Exception{
        fileContent();
    }

}
