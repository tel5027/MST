import java.util.ArrayList;

/**
 * Class Kruskal houses Kruskal's Algorithm for selecting a
 * Minimum Spanning Tree from our randomly generated graph
 * @author Thomas LaSalle (tel5027)
 */
public class Kruskal {
	
	//Instance variables
	private int parent[];
	private int rank[];
	private int count;
	
	/**
	 * Construct a new Kruskal object based on the provided
	 * number of vertices.
	 * @param n The number of vertices
	 */
	public Kruskal(int n) {
		count = n;
		parent = new int[n];
		rank = new int[n];
		
		for(int i = 0; i < n; i++) {
			parent[i] = i;
			rank[i] = 0;
		}
		
	}
	
	/**
	 * Reset the current Kruskal Object to its default state for
	 * easier reuse
	 */
	public void reset() {
		for(int i = 0; i < count; i++) {
			parent[i] = i;
			rank[i] = 0;
		}
	}
	
	/**
	 * Find uses path compression to determine the index of the
	 * set containing the given vertex.
	 * @param index The index of the vertex
	 * @return The index of the set containing the vertex
	 */
	private int find(int index) {
		int i = index;
		
		//Perform Path Compression
		if(i != parent[i]) {
			parent[i] = find(parent[i]);
		}
		
		return parent[i];
	}
	
	/**
	 * Union combines two different sets into one, using a
	 * union-by-rank heuristic to determine the new root of the set
	 * @param start The start vertex
	 * @param end The end vertex
	 */
	private void union(int start, int end) {
		int first = find(start);
		int last = find(end);
		
		if(first == last) return;
		
		if(rank[first] < rank[last]) {
			parent[first] = last;
		}
		else if(rank[first] > rank[last]) {
			parent[last] = first;
		}
		else {
			parent[last] = first;
			rank[first]++;
		}

	}
	
	/**
	 * KruskalMST performs Kruskal's algorithm on the provided array of
	 * edges, utilizing union-by-rank and path compression, to form a
	 * minimum spanning tree
	 * @param graph The list of sorted Edges to pull the tree
	 * @return The minimum spanning tree
	 */
	public ArrayList<Edge> KruskalMST(Edge[] graph) {
		ArrayList<Edge> mst = new ArrayList<Edge>();
		
		int index = 0;
		
		while((index != graph.length) && (mst.size() != (count - 1))) {
			
			int start = graph[index].getStartVertex();
			int end = graph[index].getEndVertex();
			
			if(find(start) != find(end)) {
				mst.add(graph[index]);
				union(start, end);
			}
			
			index++;
		}
		
		return mst;
	}

}
