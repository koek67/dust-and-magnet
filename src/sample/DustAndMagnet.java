package sample;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.*;

/**
 * This class will handle all the Processing Graphics for the
 * Dust and Magnet simulation.
 */
public class DustAndMagnet extends PApplet {
    public static int WIDTH = 640;
    public static int HEIGHT = 640;

    public static Magnet adding = null;
    public static boolean magnetAdding = false;
    public static String addingString = "";
    public static double addingValue = -1;

    private ArrayList<Magnet> magnets = new ArrayList<Magnet>();
    private ArrayList<Particle> particles = new ArrayList<Particle>();

    private Magnet selected;
    private PVector offset;

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
            data.put("1", numGen.nextDouble());
            data.put("2", numGen.nextDouble());
            data.put("3", numGen.nextDouble());
            data.put("4", numGen.nextDouble());
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
    public static void addingMagnet(String name, double value){
        sample.DustAndMagnet.magnetAdding = true;
        sample.DustAndMagnet.addingString = name;
        sample.DustAndMagnet.addingValue = value;
        adding = new Magnet(name, value, 0, 0, null);
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
            adding.p = this;
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

    public void mousePressed() {
        // going in reverse to respect the order that they are drawn on screen
        // (top magnet will be selected in favor of ones on the bottom)
        for (int i = magnets.size() - 1; i >= 0; i--) {
            System.out.println(magnets.get(i));
            if (magnets.get(i).contains(mouseX, mouseY)) {
                selected = magnets.get(i);
                offset = new PVector(mouseX, mouseY);
                offset.sub(selected.getLoc());
                return;
            }
        }
    }

    public void mouseDragged() {
        if (selected == null) { return; }
        selected.setLoc(mouseX - offset.x, mouseY - offset.y);
    }

    public void mouseReleased() {
        if (selected == null) { return; }
        System.out.println("Released");
        selected = null;
    }





}
