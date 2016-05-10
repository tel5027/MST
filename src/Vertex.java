import java.util.ArrayList;

/**
 * The Vertex class represents a Vertex (Node) in our
 * random generated graph
 * @author Thomas LaSalle (tel5027)
 */
public class Vertex implements Comparable<Vertex>{
	
	//Instance variables
	private int vertexID, priority, parent;
	private ArrayList<Vertex> adjacents;	
	
	private final int INFINITY = Integer.MAX_VALUE;
	
	/**
	 * Construct a new Vertex and it's adjacency list with the
	 * given ID number
	 * @param id The Vertex ID
	 */
	public Vertex(int id) {
		vertexID = id;
		priority = INFINITY;
		parent = -1;
		adjacents = new ArrayList<Vertex>();
	}
	
	public Vertex(int id, int priority) {
		vertexID = id;
		this.priority = priority;
		adjacents = new ArrayList<Vertex>();
	}
	
	/**
	 * Set this Vertex's ID number
	 * @param id The Vertex ID
	 */
	public void setVertexID(int id) {
		vertexID = id;
	}
	
	/**
	 * Get this Vertex's ID number
	 * @return The Vertex ID
	 */
	public int getVertexID() {
		return vertexID;
	}
	
	public void setPriority(int p) {
		priority = p;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setParent(int p) {
		parent = p;
	}
	
	public int getParent() {
		return parent;
	}
	
	/**
	 * Add a new Vertex which is adjacent to this one, that is one
	 * which is connected to this vertex by an edge
	 * @param v The adjacent vertex
	 */
	public void addAdjacentVertex(Vertex v) {
		if(v != null) {
			if(!adjacents.contains(v)) {
				adjacents.add(v);
			}			
		}		
	}
	
	/**
	 * Get this Vertex's adjacency list
	 * @return The adjacency list
	 */
	public ArrayList<Vertex> getAdjacents(){
		return adjacents;
	}

	@Override
	public int compareTo(Vertex arg0) {
		if(priority < arg0.getPriority()) return -1;
		else if(priority == arg0.getPriority()) return 0;
		else return 1;
	}
	
	@Override
	public String toString() {
		return "Vertex: (" + vertexID + "," + priority + "," + parent + ")";
	}

}
