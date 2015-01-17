package sample;

import processing.core.PApplet;
import java.util.*;

/**
 * This class will handle all the Processing Graphics for the
 * Dust and Magnet simulation.
 */
public class DustAndMagnet extends PApplet {
    public static int WIDTH = 640;
    public static int HEIGHT = 640;

    public static Magnet adding = null;
    private static boolean magnetAdding = false;
    private static String addingString = "";
    private static int addingValue = -1;

    private ArrayList<Magnet> magnets = new ArrayList<Magnet>();
    private ArrayList<Particle> particles = new ArrayList<Particle>();

    public ArrayList<Magnet> getMagnets() {return magnets;}

    /**
     * This method will populate the particles ArrayList with 100
     * randomly arranged particles.
     */
    public void fillParticles() {
        Random numGen = new Random();
        int numParticles = 100;
        for (int i = 0; i < numParticles; i++) {
            int randX = numGen.nextInt(WIDTH);
            int randY = numGen.nextInt(HEIGHT);
            double val = numGen.nextDouble();
            HashMap<String, Double> data = new HashMap<String, Double>();
            data.put("name", numGen.nextDouble());
            System.out.println(data.get("name"));
            particles.add(new Particle(randX, randY, this, data));
        }
    }

    /**
     * When a button on the GUI is pressed, this method will
     * be called to let this class know that another magnet
     * is being added. In response, a new magnet will be drawn
     * at the location of the mouse cursor until the mouse is
     * clicked at which point the magnet will lock into its
     * location.
     */
    public static void addingMagnet(String name, int value){
        sample.DustAndMagnet.magnetAdding = true;
        sample.DustAndMagnet.addingString = name;
        sample.DustAndMagnet.addingValue = value;
        adding = new Magnet();
    }
    @Override
    public void setup() {
        size(HEIGHT, WIDTH);
        stroke(155, 0, 0);
        background(255);
        fillParticles();
    }

    @Override
    public void draw() {
        background(255);
        // if a new magnet is being added, draw it
        // at the mouse cursor location
        if (magnetAdding && adding != null) {
            adding = new Magnet(addingString, addingValue, mouseX, mouseY, this);
            adding.draw();
        }

        // iterate through all the current magnets
        // and draw them
        for (Magnet m : magnets) {
            m.draw();
        }

        // iterate through all the current particles
        // and draw them
        for (Particle p : particles) {
            p.attract();
            p.updateLocation();
            p.draw();
        }
    }

    public void mouseClicked() {
        if (magnetAdding && adding != null) {
            magnets.add(adding);
            magnetAdding = false;
            adding = null;
        }
    }



}
