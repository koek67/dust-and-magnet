package sample;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.*;
import java.awt.Rectangle;

/**
 * This class will handle all the Processing Graphics for the
 * Dust and Magnet simulation.
 */
public class DustAndMagnet extends PApplet {
    public static int WIDTH = 800;
    public static int HEIGHT = 640;

    private Map<String, Magnet> magnets;
    private ArrayList<Particle> particles;

    // selecting magnets
    private Magnet selected;
    private PVector offset;

    // UI stuff
    // button top-lefts
    private Button[] buttons;
    private PVector buttonSize;
    private Button trash;

    public Collection<Magnet> getMagnets() {
        return magnets.values();
    }

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
            particles.add(new Particle(randX, randY, data));
        }
    }

    @Override
    public void setup() {
        size(WIDTH, HEIGHT);
        stroke(155, 0, 0);

        magnets = new HashMap<String, Magnet>();
        particles = new ArrayList<Particle>();

        background(255);
        fillParticles();
        initUI();
    }

    @Override
    public void draw() {
        background(255);
        // if a new magnet is being added, draw it
        // at the mouse cursor location
        drawDnM();
        drawUI();
    }

    public void drawDnM() {
        fill(256);
        // iterate through all the current magnets
        // and draw them
        for (Magnet m : magnets.values()) {
            m.draw(this);
        }

        // iterate through all the current particles
        // and draw them
        for (Particle p : particles) {
            p.attract(this);
            p.updateLocation();
            p.draw(this);
        }
    }

    public void mousePressed() {
        // going in reverse to respect the order that they are drawn on screen
        // (top magnet will be selected in favor of ones on the bottom)
        Iterable<Magnet> ms = magnets.values();
        for (Magnet m : ms) {
            if (m.contains(mouseX, mouseY)) {
                selectMagnet(m, mouseX, mouseY);
                return;
            }
        }

        String btnName = inButton(mouseX, mouseY);
        if (btnName != null) {
            Magnet newMagnet = new Magnet(btnName, 1, mouseX, mouseY);
            if (magnets.get(newMagnet.getName()) != null) {
                magnets.remove(newMagnet.getName());
            }
            magnets.put(newMagnet.getName(), newMagnet);
            selectMagnet(newMagnet, mouseX, mouseY);
        }
    }

    public void selectMagnet(Magnet m, int x, int y) {
        selected = m;
        offset = new PVector(x, y);
        offset.sub(selected.getLoc());
    }

    public void mouseDragged() {
        if (selected == null) {
            return;
        }
        selected.setLoc(mouseX - offset.x, mouseY - offset.y);
    }

    public void mouseMoved() {

    }

    public void mouseReleased() {
        if (selected == null) {
            return;
        }
        if (trash.contains(mouseX, mouseY) && selected != null) {
            magnets.remove(selected.getName());
        }
        selected = null;
    }

    public void initUI() {
        buttons = new Button[4];
        buttonSize = new PVector(60, 50);
        PVector button = new PVector(10, HEIGHT - buttonSize.y - 10);
        for (int i = 0; i < 4; i++) {
            buttons[i] = new Button((int)button.x, (int)button.y, (int)buttonSize.x, (int)buttonSize.y);
            button.x += buttonSize.x + 10;
        }

        trash = new Button("Trash", new Rectangle(WIDTH - (int)buttonSize.x - 10,
                (int)HEIGHT - (int)buttonSize.y - 10, (int)buttonSize.x, (int)buttonSize.y));
    }

    public void drawUI() {
        fill(200);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].draw(this);
        }
        trash.draw(this);
    }

    public String inButton(int x, int y) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].contains(x, y)) return buttons[i].name;
        }
        return null;
    }

}