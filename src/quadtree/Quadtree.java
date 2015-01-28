package quadtree;

import vis.Particle;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

/**
 * This class defines the Quadtree node and all its functions.
 * 
 * @author Andrew Dai (bunsenmcdubbs)
 */
public class Quadtree {

    public static final int MAX_OBJECTS = 500;

    private Quadtree[] nodes;
	private Quadtree parent;
    private int level;
    private ArrayList<Particle> objects;
    private Rectangle bounds;
    private boolean leaf;
    
    /**
     * Initializes a new Quadtree node with bounds
     * @param pBounds - bounds of the new node (one quarter of the parent node)
     */
    public Quadtree(Rectangle pBounds) {
        this(0, null, pBounds);
    }
    
    /**
     * Initializes a new Quadtree node with bounds and a level
     * @param pLevel - level of the new node (top level = 0)
     * @param pBounds - bounds of the new node (one quarter of the parent node)
     */
    private Quadtree(int pLevel, Quadtree pParent, Rectangle pBounds) {
        level = pLevel;
        bounds = pBounds;
		this.parent = pParent;
        clear();
    }

	public void clear() {
//		nodes = new Quadtree[4];
		objects = new ArrayList<Particle>();
		leaf = true;
	}

    /**
     * Add a shape into the Quadtree.
     *  - It can be added to this node (if density hasn't been reached).
     *  - Added to this node if children don't completely contains the shape.
     *  - Added to child node.
     *  - Rejected because it doesn't fit in this or child nodes (return -1)
     * @param s - shape to be added to the Quadtree
     * @return the level that the shape was added to.
     */
    public int add(Particle s) {
    	if(!this.contains(s)) {
    		return -1; // TODO error code! redo with exception
    	}
    	if(leaf && objects.size() >= MAX_OBJECTS) {
    		split();
    		return add(s);
    	} 
    	if(!leaf) { 
    		for (Quadtree q : nodes) {
    			// successfully added s into child node
    			if(q.contains(s)) {
    				return q.add(s);
    			}
    		}
    	}
    	// if failed to fit into child, add here
		objects.add(s);
		s.setQuadtree(this);
		return level;
    }
    
    /**
     * Get all objects at this node
     * @return an ArrayList of all the shapes contained within this
	 * Quadtree and all its children
	 *
	 * TODO fix this, infinite loops or something
	 * TODO fix heap space errors (out of memory)
     */
    @SuppressWarnings("unchecked")
	public ArrayList<Particle> getAll() {
		ArrayList<Particle> obj = objects;
		// Traverse up
		Quadtree curr = parent;
		while(curr != null) {
			obj.addAll(parent.objects);
			curr = parent.parent;
		}
		// Traverse down
    	for(int i = 0; nodes != null && i < nodes.length; i++) {
    		obj.addAll(nodes[i].getAll());
    	}
    	obj.addAll(objects);
    	return obj;
    }

	public ArrayList<Particle> getUp() {
		ArrayList<Particle> obj = new ArrayList<Particle>();
		// Traverse up
		Quadtree curr = parent;
		while (curr != null) {
			obj.addAll(parent.objects);
			curr = curr.parent;
		}
		return obj;
	}

	private void addAll(ArrayList<Particle> parent, ArrayList<Particle> child) {
		for (Particle c : child) {
			parent.add(c);
		}
	}

	/**
	 *
	 * @return only the immediate shapes contained in this Quadtree
	 * (none of the children's shapes)
	 */
	public ArrayList<Particle> getImmediate() {
		return objects;
	}
    
    /**
     * @return all the child nodes of this quadtree.Quadtree
     */
    public Quadtree[] getChildren() {
    	return nodes;
    }

	/**
	 * @return if this Quadtree is a leaf (no children)
	 */
	public boolean isLeaf() { return leaf; }

	/**
	 * @return the level of this Quadtree (root is 0)
	 */
	public int level() { return level; }

	/**
	 * @return the bounding box of this Quadtree
	 */
	public Rectangle bounds() { return bounds; }
    
    /**
     * Similar to contains() except it checks if the entire shape (bounding box)
     * is contained inside the bounds of this node.
     * @param s - shape in question
     * @return true if this node complete contains the shape
     */
	private boolean contains(Particle s) {
		return bounds.contains(s.getBounds());
	}

	/**
	 * Splits the quadtree.Quadtree into nodes and adds the shapes to the proper children
	 */
	private void split() {
		nodes = new Quadtree[4];
		leaf = false;
		int nWidth = bounds.width / 2;
		int nHeight = bounds.height / 2;
		nodes[0] = new Quadtree(level + 1, this, new Rectangle(bounds.x, bounds.y, nWidth, nHeight));
		nodes[1] = new Quadtree(level + 1, this, new Rectangle(bounds.x + nWidth, bounds.y, nWidth, nHeight));
		nodes[2] = new Quadtree(level + 1, this, new Rectangle(bounds.x, bounds.y + nHeight, nWidth, nHeight));
		nodes[3] = new Quadtree(level + 1, this, new Rectangle(bounds.x + nWidth, bounds.y + nHeight, nWidth, nHeight));
		
		@SuppressWarnings("unchecked")
		ArrayList<Particle> objs = (ArrayList<Particle>) objects.clone();
		objects = new ArrayList<Particle>();
		for(Particle s : objs) {
			add(s);
		}
	}

	public String toString() {
		String s = "";
		s += "Level " + level + "\n";
		for (int j = 0; j < level; j++) {
			s += "\t";
		}
		s += "Shapes: " + objects.toString() + "\n";
		if (!leaf) {
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < nodes[i].level; j++) {
					s += "\t";
				}
				s += "Node " + i + ": " + nodes[i].toString();
			}
		}
		return s;
	}

}
