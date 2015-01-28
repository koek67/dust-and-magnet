package vis;

import com.sun.javafx.binding.StringFormatter;
import parser.Parser;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import quadtree.Quadtree;

import java.io.FileNotFoundException;
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
    private Quadtree q;
    private Map<String, LinkedList<Particle>> types;

    // selecting magnets
    private Magnet selected;
    private PVector offset;

    // UI stuff
    // button top-lefts
    private Button[] btnMag;
    private Button[] btnTypes;
    private Button trash;

    public Collection<Magnet> getMagnets() {
        return magnets.values();
    }

    /**
     * This method will populate the particles ArrayList with 500
     * randomly arranged particles.
     */
    public void fillParticlesTEST() {
        Random numGen = new Random();
        int numParticles = 200;
        for (int i = 0; i < numParticles; i++) {
            int randX = numGen.nextInt(WIDTH);
            int randY = numGen.nextInt(HEIGHT);
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
            q.add(a);
            addCategoryEntry(a);
        }
    }

    public void fillParticles() {
        try {
            parser.Parser2.fileContent();
        } catch (FileNotFoundException e) {}
        Random numGen = new Random();
        for (parser.Data d : parser.Parser2.data) {
            int randX = numGen.nextInt(WIDTH);
            int randY = numGen.nextInt(HEIGHT);
            Particle a = new Particle(randX, randY, d.norm, d.name, d.cat);
            particles.add(a);
            q.add(a);
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
        if (types.get(a.cat) != null) {
            types.get(a.cat).add(a);
            return false;
        } else {
            System.out.println(a.cat);
            LinkedList<Particle> cars = new LinkedList<Particle>();
            cars.add(a);
            types.put(a.cat, cars);
            return true;
        }
    }

    @Override
    public void setup() {
        size(WIDTH, HEIGHT);
        background(32);

        magnets = new HashMap<String, Magnet>();
        q = new Quadtree(new Rectangle(WIDTH, HEIGHT));
        particles = new ArrayList<Particle>();
        types = new HashMap<String, LinkedList<Particle>>();
        fillParticles();
//        System.out.println(q);
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
            q.add(p);
        }
        repel(q);
        for (Particle p : particles) {
            p.attract(this);
        }
//        drawQuadtree(q);
        for (Particle p : particles) {
            p.updateLocation();
            p.draw(this);
        }
        q.clear();

        boolean done = false;
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            if(selected == null && !done && p.contains(mouseX, mouseY)) {
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
//                text(p.name, p.loc.x + 5, p.loc.y - 5);
                // TODO this is diagnostic part
                text(p.name + " " + velToString(p.vel), p.loc.x + 5, p.loc.y - 5);
                PVector uVel = p.vel.get();
                uVel.mult(20);
                strokeWeight(5);
                stroke(256f);
                line(p.loc.x, p.loc.y, uVel.x + p.loc.x, uVel.y + p.loc.y);
                strokeWeight(1);
            }
        }
    }

    // This is a debug tool
    private String velToString(PVector v) {
        return String.format("[ %+.2f, %+.2f ] %.2f", v.x, v.y, v.mag());
    }

    private void repel(Quadtree q) {

        if (!q.isLeaf()) {
            for (Quadtree c : q.getChildren()) {
                repel(c);
            }
        }

        ArrayList<Particle> ps = q.getImmediate();
        ArrayList<Particle> os = q.getUp();
        for (int i = 0; i < ps.size(); i++) {
            Particle p = ps.get(i);
            // Checking with others in the same Quadtree
            for (int j = i + 1; j < ps.size(); j++) {
                repel(p, ps.get(j));
            }
            // Checking with other particles on parent Quadtrees
            for (int k = 0; k < os.size(); k++) {
                repel(p, os.get(k));
            }
        }
    }

    // TODO I don't know why this needs to start as false.
    boolean repel = false;

    private void repel(Particle p, Particle o) {
        if (!repel) { return; }
        float dist = (float) Math.sqrt(Math.pow((p.loc.x - o.loc.x), 2) + Math.pow((p.loc.y - o.loc.y), 2));
        if (dist < 21) {
            float mag = 1 / (float) Math.pow(dist / 15, 2);
            if (mag > 5) { mag = 5; }
            float theta = (float) Math.atan2(p.loc.y - o.loc.y, p.loc.x - o.loc.x);
            PVector d = new PVector((float) (mag * Math.cos(theta)), (float) (mag * Math.sin(theta)));
            p.vel.add(d);
            o.vel.sub(d);
        }
    }

    public void drawQuadtree(Quadtree curr) {

        if (!curr.isLeaf()) {
            Quadtree[] branches = curr.getChildren();
            for (Quadtree b : branches) {
                drawQuadtree(b);
            }
        }
        noFill();
        stroke(200f);
        rect(curr.bounds().x, curr.bounds().y, curr.bounds().width, curr.bounds().height);
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
        for (int i = 0; i < btnTypes.length; i++) {
            if (btnTypes[i].contains(mouseX, mouseY)) {
                Random numGen = new Random();
                // if the elements aren't highlighted, then highlight them
                // otherwise set highlight color to null
                // this is a "Toggle"
                if (types.get(btnTypes[i].name).get(0).hc == null) {
//                    int r = 256, g = 256, b = numGen.nextInt(50) + 200;
//                    int r = Integer.parseInt(("" + (btnTypes[i].name.hashCode())).substring(0, 3));
//                    int g = Integer.parseInt(("" + (btnTypes[i].name.hashCode())).substring(1, 4));
//                    int b = Integer.parseInt(("" + (btnTypes[i].name.hashCode())).substring(3, 6));
                    for (Particle p : types.get(btnTypes[i].name)) {
//                        p.hc = new PVector(r, g, b);
                        p.hc = new PVector(256, 256, 256);
                    }
                } else {
                    for (Particle p : types.get(btnTypes[i].name)) {
                        p.hc = null;
                    }
                }
            }
        }
        repel = !repel;
        System.out.println("Repel " + repel);
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
        // TODO font fall backs or more native choices
        textFont(new PFont(PFont.findFont("Andale Mono"), true));
        // current button that is being created
        // current size of the button
        PVector buttonSize;
        PVector button;

        buttonSize = new PVector(120, 30);
        button = new PVector(10, 10);
        Iterable<String> typeNames = types.keySet();
        System.out.println(types.keySet());
        btnTypes = new Button[types.size()];
        int i = 0;
        for (String t : typeNames) {
            btnTypes[i++] = new Button(t, (int) button.x, (int) button.y, (int) buttonSize.x, (int) buttonSize.y);
            button.y += buttonSize.y + 10;
            if (button.y >= HEIGHT - buttonSize.y) {
                button.y = 10;
                button.x += buttonSize.x + 10;
            }
        }
        button.x += buttonSize.x + 10;

        button.y = 10;
        buttonSize = new PVector(120, 60);
        // using <code>particles.get(0).data.size();</code>
        // to grab a "sample" data entry and find out number of magnets etc
//        System.out.println(particles.get(0).data);
        btnMag = new Button[particles.get(0).data.size()];
        Iterator<String> magNames = particles.get(0).data.keySet().iterator();
        for (int j = 0; j < btnMag.length; j++) {
            btnMag[j] = new Button(magNames.next(), (int) button.x, (int) button.y, (int) buttonSize.x, (int) buttonSize.y);
            button.x += buttonSize.x + 10;
        }

        trash = new Button("Trash", new Rectangle(WIDTH - (int)buttonSize.x - 10,
                (int)HEIGHT - (int)buttonSize.y - 10, (int)buttonSize.x, (int)buttonSize.y));
    }

    public void drawUI() {
        fill(200);
        for (int i = 0; i < btnMag.length; i++) {
            btnMag[i].draw(this, mouseX, mouseY);
        }
        for (int i = 0; i < btnTypes.length; i++) {
            btnTypes[i].draw(this, mouseX, mouseY);
        }
        trash.draw(this, mouseX, mouseY);
    }

    public String inButton(int x, int y) {
        for (int i = 0; i < btnMag.length; i++) {
            if (btnMag[i].contains(x, y)) return btnMag[i].name;
        }
        return null;
    }

}