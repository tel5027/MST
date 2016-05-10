
/**
 * The Edge class represents an Edge in our random generated graph
 * @author Thomas LaSalle (tel5027)
 */
public class Edge implements Comparable<Edge>{
	
	//Instance Variables
	private int weight, startVertex, endVertex;
	
	/**
	 * Construct a new Edge with the given weight, start and end vertices
	 * @param w The edge weight
	 * @param s The start Vertex
	 * @param e The end Vertex
	 */
	public Edge(int w, int s, int e) {
		weight = w;
		startVertex = s;
		endVertex = e;
	}
	
	/**
	 * Set the Edge weight
	 * @param w The edge weight
	 */
	public void setWeight(int w) {
		weight = w;
	}
	
	/**
	 * Get the Edge weight
	 * @return The edge weight
	 */
	public int getWeight() {
		return weight;
	}
	
	/**
	 * Set this Edge's starting vertex
	 * @param s The start vertex
	 */
	public void setStartVertex(int s) {
		startVertex = s;
	}
	
	/**
	 * Get this Edge's starting vertex
	 * @return The start vertex
	 */
	public int getStartVertex() {
		return startVertex;
	}
	
	/**
	 * Set this Edge's ending vertex
	 * @param e The end vertex
	 */
	public void setEndVertex(int e) {
		endVertex = e;
	}
	
	/**
	 * Get this Edge's ending vertex
	 * @return The end vertex
	 */
	public int getEndVertex() {
		return endVertex;
	}

	@Override
	public int compareTo(Edge arg0) {
		
		if(weight == arg0.getWeight()) {
			if(startVertex == arg0.getStartVertex()) {
				return endVertex - arg0.getEndVertex();
			}
			else {
				return startVertex - arg0.getStartVertex();
			}
		}
		
		return weight - arg0.getWeight();
	}
	
	public String toString() {
		return "" + startVertex + " " + endVertex + " weight = " + weight;
	}
}
