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
    public static int WIDTH = 1280;
    public static int HEIGHT = 800;

    private Map<String, Magnet> magnets;
    private ArrayList<Particle> particles;
    private Map<String, LinkedList<Particle>> makes;

    // selecting magnets
    private Magnet selected;
    private PVector offset;

    // UI stuff
    // button top-lefts
    private Button[] btnMag;
    private Button[] btnMake;
    private PVector buttonSize;
    private Button trash;

    public Collection<Magnet> getMagnets() {
        return magnets.values();
    }

    /**
     * This method will populate the particles ArrayList with 500
     * randomly arranged particles.
     */
    public void fillParticles() {
        Random numGen = new Random();
        int numParticles = 700;
        for (int i = 0; i < numParticles; i++) {
            int randX = numGen.nextInt(WIDTH);
            int randY = numGen.nextInt(HEIGHT);
            double val = numGen.nextDouble();
            HashMap<String, Double> data = new HashMap<String, Double>();
            data.put("1", numGen.nextDouble());
            data.put("2", numGen.nextDouble());
            data.put("3", numGen.nextDouble());
            data.put("4", numGen.nextDouble());
            String make = "";
            if (i % 2 == 0)
                make = "Honda";
            else
                make = "Toyota";
            Particle a = new Particle(randX, randY, data, "" + i, make);
            particles.add(a);
            addCategoryEntry(a);
        }
    }

    /**
     *
     * @param a
     * @return new category?
     */
    public boolean addCategoryEntry(Particle a) {
//        System.out.println(a.make);
        if (makes.get(a.make) != null) {
            makes.get(a.make).add(a);
            return false;
        } else {
            LinkedList<Particle> cars = new LinkedList<Particle>();
            cars.add(a);
            makes.put(a.make, cars);
            return true;
        }
    }

    @Override
    public void setup() {
        size(WIDTH, HEIGHT);

        magnets = new HashMap<String, Magnet>();
        particles = new ArrayList<Particle>();

        makes = new HashMap<String, LinkedList<Particle>>();

        background(32);
        fillParticles();
        initUI();
    }

    @Override
    public void draw() {
//        stroke(64, 64, 64, 100);
//        fill(64, 64, 64, 100);
//        rect(0, 0, WIDTH, HEIGHT);
        background(32);
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
        boolean done = false;
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            if(!mousePressed && !done && p.contains(mouseX, mouseY)) {
                if (p.name != null) {
                    textSize(16);
                    fill(256f);
                    text(p.name, p.loc.x + 5, p.loc.y - 5);
                    done = true;
                }
            }
            if(p.drawName) {
                textSize(16);
                fill(256f);
                text(p.name, p.loc.x + 5, p.loc.y - 5);
            }
        }
    }

    public void mouseClicked() {
        boolean done = false;
        for (int i = particles.size() - 1; i >= 0 && !done; i--) {
            Particle p = particles.get(i);
            if(p.contains(mouseX, mouseY)) {
                p.drawName = !p.drawName;
                done = true;
            }
        }
        for (int i = 0; i < btnMake.length; i++) {
            if (btnMake[i].contains(mouseX, mouseY)) {
                // if the elements aren't highlighted, then highlight them
                // otherwise set highlight color to null
                // this is a "Toggle"
                if (makes.get(btnMake[i].name).get(0).hc == null) {
                    int r = Integer.parseInt(("" + (btnMake[i].name.hashCode())).substring(0, 3));
                    int g = Integer.parseInt(("" + (btnMake[i].name.hashCode())).substring(1, 4));
                    int b = Integer.parseInt(("" + (btnMake[i].name.hashCode())).substring(3, 6));
                    for (Particle p : makes.get(btnMake[i].name)) {
                        p.hc = new PVector(r, g, b);
                    }
                } else {
                    for (Particle p : makes.get(btnMake[i].name)) {
                        p.hc = null;
                    }
                }
            }
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
        btnMag = new Button[4];
        buttonSize = new PVector(60, 50);
        PVector button = new PVector(10, HEIGHT - buttonSize.y - 10);
        for (int i = 0; i < 4; i++) {
            btnMag[i] = new Button((int) button.x, (int) button.y, (int) buttonSize.x, (int) buttonSize.y);
            button.x += buttonSize.x + 10;
        }

        trash = new Button("Trash", new Rectangle(WIDTH - (int)buttonSize.x - 10,
                (int)HEIGHT - (int)buttonSize.y - 10, (int)buttonSize.x, (int)buttonSize.y));


        System.out.println(makes);
        Iterable<String> makeNames = makes.keySet();
        btnMake = new Button[makes.size()];
        buttonSize.x /= 2;
        int i = 0;
        for (String m : makeNames) {
            btnMake[i++] = new Button(m, (int) button.x, (int) button.y, (int) buttonSize.x, (int) buttonSize.y);
            button.x += buttonSize.x + 10;
            System.out.println(m);
        }
    }

    public void drawUI() {
        fill(200);
        for (int i = 0; i < btnMag.length; i++) {
            btnMag[i].draw(this);
        }
        for (int i = 0; i < btnMake.length; i++) {
            btnMake[i].draw(this);
        }
        trash.draw(this);
    }

    public String inButton(int x, int y) {
        for (int i = 0; i < btnMag.length; i++) {
            if (btnMag[i].contains(x, y)) return btnMag[i].name;
        }
        return null;
    }

}