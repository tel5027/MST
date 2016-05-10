import java.util.Random;
import java.util.ArrayList;

/**
 * The Graph class randomly generates an
 * undirected, connected graph, based on provided data,
 * prints the graph, as well as perform a Depth-First search to ensure
 * connectedness of the graph.
 * @author Thomas LaSalle (tel5027)
 */
public class Graph {
	
	//Instance Variables
	private Random edgeRand, weightRand;
	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges, primEdges;
	private Edge[][] edgeMatrix;
	
	private int count = 0, prev = -1,  n, dfs[], preds[];
	private double r, p;
	private boolean marked[];
	
	private SortAlgorithm sort = new SortAlgorithm();
	
	private long seed, sTime, fTime, tTime;
	
	private Kruskal kruskalAlg;
	private Prim primAlgMatrix, primAlgList;
	
	//Constants
	private final int MIN_WEIGHT = 1;
	private final int INFINITY = Integer.MAX_VALUE;
	
	/**
	 * Generate a new random graph with the given data
	 * @param n The number of vertices in the graph
	 * @param seed The random number generator seed
	 * @param p The probability that two vertices are connected via an edge
	 */
	public Graph(int n, long seed, double p) {
		this.n = n;
		this.seed = seed;
		this.p = p;
		
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		primEdges = new ArrayList<Edge>();
		
		marked = new boolean[n];
		dfs = new int[n];
		preds = new int[n];
		
		edgeMatrix = new Edge[n][n];
		
		kruskalAlg = new Kruskal(n);		
		
		generateGraph();
	}
	
	/**
	 * Add a new Vertex to the Graph
	 * @param v The Vertex ID
	 */
	public void addVertex(int v) {		
		vertices.add(new Vertex(v));		
	}
	
	/**
	 * addEdge adds an edge to the graph, ensuring that it does not
	 * overwrite edges which currently exist in the graph
	 * @param weight The edge weight
	 * @param start The starting vertex
	 * @param end The ending vertex
	 */
	public void addEdge(int weight, int start, int end) {
		Edge first = new Edge(weight, start, end);
		Edge second = new Edge(weight, end, start);
		
		edges.add(first);
		edges.add(second);
		
		edgeMatrix[start][end] = first;
		edgeMatrix[end][start] = second;
		
		vertices.get(start).addAdjacentVertex(vertices.get(end));
		vertices.get(end).addAdjacentVertex(vertices.get(start));			
	}
	
	/**
	 * generateGraph generates the random graph, provides the amount
	 * of time it takes to create the graph, and then prints the data.
	 */
	public void generateGraph() {
		sTime = System.currentTimeMillis();
		
		edgeRand = new Random(seed);
		weightRand = new Random ((2*seed));

		int weight;
		int range;
		
		while(count != n) {
			
			count = 0;
			range = n - MIN_WEIGHT + 1;
			
			if(vertices != null && edges != null) {
				vertices.clear();
				edges.clear();
			}
			
			for(int x = 0; x < n; x++){
				for(int y = 0; y < n; y++){
					edgeMatrix[x][y] = null;
				}
			}
			
			for(int i = 0; i < n; i++) {
				addVertex(i);
				marked[i] = false;
			}
			
			for(int x = 0; x < n; x++) {
				
				for(int y = (x + 1); y < n; y++) {					
				
					r = edgeRand.nextDouble();
					if(r <= p) {
						weight = MIN_WEIGHT + weightRand.nextInt(range);
						addEdge(weight, x, y);							
					}					
				}
			}
			
			primAlgMatrix = new Prim(n, vertices);
			primAlgList = new Prim(n, vertices);
			dfs(vertices.get(0));
		}
		
		fTime = System.currentTimeMillis();
		tTime = fTime - sTime;
		
		//Print the data and perform the MST algorithms
		//printing their data as well
		printData();
		performKruskalAlg();
		performPrimsAlg();
	}
	
	/**
	 * getEdgeByCoords returns an edge based on it's start and end
	 * vertices
	 * @param x The starting vertex
	 * @param y The ending vertex
	 * @return The edge at those coordinates, or null if no edge exists
	 */
	private Edge getEdgeByCoords(int x, int y) {
		
		for(Edge e : edges) {
			if((e.getStartVertex() == x) && (e.getEndVertex() == y)) {
				return e;
			}			
		}
		
		return null;
	}
	
	/**
	 * Print the Adjacency Matrix representation of the random graph
	 * with proper formatting
	 */
	private void printAdjacencyMatrix() {
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				Edge e = edgeMatrix[x][y];
				
				if(e != null) {
					int wgt = e.getWeight();
					System.out.print(wgt + "   ");
				}
				else {
					System.out.print("0   ");
				}
				
				if((y + 1) == n) {
					System.out.println("");
					System.out.println("");
				}
			}
		}
	}
	
	/**
	 * Print the Adjacency List representation of the random graph
	 * with proper formatting
	 */
	private void printAdjacencyList() {
		for(int x = 0; x < n; x++) {
			System.out.print(x + "-> ");
			
			for(Vertex v : vertices.get(x).getAdjacents()) {
				Edge e = getEdgeByCoords(x, v.getVertexID());
				System.out.print(v.getVertexID() + "(" + e.getWeight() + ") ");
			}
			
			System.out.println("");
		}
	}
	
	/**
	 * Print the results of the dfs performed on the random
	 * generated graph
	 */
	private void printDFSData() {
		System.out.println("\nDepth-First Search:");
		for(int i = 0; i < n; i++) {
			System.out.print(dfs[i] + " ");
		}
		System.out.println("");
		System.out.println("Predecessors:");
		for(int j = 0; j < n; j++) {
			System.out.print(preds[j] + " ");
		}
		System.out.println("");
			
	}
	
	/**
	 * Combine all of the previous print helpers to print all
	 * relevant data to the user
	 */
	public void printData() {
		System.out.println("TEST: n=" + n + ", seed=" + seed + ", p=" + p);
		System.out.println("Time to generate the graph: " + tTime + " milliseconds\n");
		
		//If the number of vertices is less than 10, we print the Adjacency Matrix/List
		//to the user.
		if(n < 10) {
			System.out.println("The graph as an adjacency matrix:\n");
			printAdjacencyMatrix();
			System.out.println("The graph as an adjacency list:\n");
			printAdjacencyList();
			printDFSData();
		}
	}
	
	/**
	 * dfs recursively performs a Depth-First search on the graph,
	 * keeping track of the number of vertices it visits (to determine
	 * connectedness) while also keeping track of each vertex's
	 * predecessors.
	 * @param root The root vertex to begin the search from
	 */
	public void dfs(Vertex root) {				
		
		if(root == null) return;
		
		if(count == 0){
			prev = -1;
		}
		
		int node = root.getVertexID();
		
		marked[node] = true;
		count++;
		dfs[node] = node;
		preds[node] = prev;		
		
		for(Vertex v : root.getAdjacents()) {
			if(!marked[v.getVertexID()]) {
				prev = root.getVertexID();
				dfs(v);
			}
		}		
	}
	
	/**
	 * trimListForSort trims the Edge Adjacency list of "duplicate" edges
	 * (Edges in the list whose start and finish vertices are swapped)
	 * to prepare the list for sorting.
	 */
	public void trimListForSort(){		
		//Trim the Adjacency List to remove duplicates
		for(int k = 1; k < edges.size(); k++) {
			edges.remove(k);			
		}
	}
	
	/**
	 * Converts the 2D Edge Matrix array into a 1D array for sorting.
	 * Also trims out any null Edges
	 * @param edge2D The Edge Matrix
	 * @return A 1D array representation of the Edge Matrix with all null Edges
	 *                  removed.
	 */
	public Edge[] convert2DArray(Edge[][] edge2D){
		Edge[] edgeArray;

		int count = 0;
		
		//Trim the Adjacency Matrix to remove duplicates
		for(Edge e : edges) {
			if(e != null) count++;
		}
		
		edgeArray = new Edge[(count/2)];
		int j = 0;
		
		for(int x = 0; x < n; x++) {
			for(int y = (x + 1); y < n; y++) {
				if(edge2D[x][y] != null) {
					edgeArray[j] = edge2D[x][y];
					j++;
				}
			}
		}
		
		return edgeArray;
	}
	
	/**
	 * resetVertices resets the priorities and parents of each Vertex for
	 * easier reuse with Prim's Algorithm
	 */
	public void resetVertices(){
		
		for(Vertex v : vertices){
			v.setPriority(INFINITY);
			v.setParent(-1);
		}
	}
	
	/**
	 * Sort both the Adjacency Matrix and Adjacency List of edges using
	 * three different sorting algorithms (Insertion Sort, Count Sort, 
	 * and Quick Sort). Also, obtain a minimum spanning tree from the
	 * graphs, sum the weights of all of the edges in the MST and
	 * provide a runtime for each.
	 * Print the edges contained in the MST, summation and runtime with proper 
	 * formatting.
	 */
	public void performKruskalAlg(){
		
		long sortStartTime, sortFinishTime;
		int weightSum = 0;
				
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH MATRIX USING INSERTION SORT");
		
		sortStartTime = System.currentTimeMillis();
		Edge[] matrixInsert = sort.insertionSort(convert2DArray(edgeMatrix));
		ArrayList<Edge> matrixInsMST = kruskalAlg.KruskalMST(matrixInsert);
		
		for(Edge e : matrixInsMST) {
			weightSum += e.getWeight();
			if(n < 10) System.out.println(e);			
		}
		
		kruskalAlg.reset();
		
		sortFinishTime = System.currentTimeMillis();
		System.out.print("\n");
		System.out.println("Total Weight of MST using Kruskal: " + weightSum);
		System.out.println("Runtime: " + (sortFinishTime - sortStartTime) 
				+ " milliseconds");
		
		weightSum = 0; //Reinitialize weightSum to 0 after each sort/printout
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH MATRIX USING COUNT SORT");
		
		sortStartTime = System.currentTimeMillis();
		Edge[] matrixCount = sort.countSort(convert2DArray(edgeMatrix), n);
		ArrayList<Edge> matrixCntMST = kruskalAlg.KruskalMST(matrixCount);
		
		for(Edge e : matrixCntMST) {
			weightSum += e.getWeight();
			if(n < 10) System.out.println(e);			
		}
		
		kruskalAlg.reset();
		
		sortFinishTime = System.currentTimeMillis();
		System.out.print("\n");
		System.out.println("Total Weight of MST using Kruskal: " + weightSum);
		System.out.println("Runtime: " + (sortFinishTime - sortStartTime) 
				+ " milliseconds");		
		
		weightSum = 0;
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH MATRIX USING QUICKSORT");
		
		sortStartTime = System.currentTimeMillis();
		Edge[] matrixQuick = sort.quickSort(convert2DArray(edgeMatrix), 0, 
				(convert2DArray(edgeMatrix).length-1));
		ArrayList<Edge> matrixQckMST = kruskalAlg.KruskalMST(matrixQuick);
		
		for(Edge e : matrixQckMST) {
			weightSum += e.getWeight();
			if(n < 10) System.out.println(e);			
		}
		
		kruskalAlg.reset();
		
		sortFinishTime = System.currentTimeMillis();
		System.out.print("\n");
		System.out.println("Total Weight of MST using Kruskal: " + weightSum);
		System.out.println("Runtime: " + (sortFinishTime - sortStartTime) 
				+ " milliseconds");
		
		weightSum = 0;
		
		for(Edge e : edges){
			primEdges.add(e);
		}
		trimListForSort();
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH LIST USING INSERTION SORT");
		
		sortStartTime = System.currentTimeMillis();
		Edge[] listInsert = sort.insertionSort(edges.toArray(new Edge[edges.size()]));
		ArrayList<Edge> listInsMST = kruskalAlg.KruskalMST(listInsert);
		
		for(Edge e : listInsMST) {
			weightSum += e.getWeight();
			if(n < 10) System.out.println(e);			
		}
		
		kruskalAlg.reset();
		
		sortFinishTime = System.currentTimeMillis();
		System.out.print("\n");
		System.out.println("Total Weight of MST using Kruskal: " + weightSum);
		System.out.println("Runtime: " + (sortFinishTime - sortStartTime) 
				+ " milliseconds");
		
		weightSum = 0;
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH LIST USING COUNT SORT");
		
		sortStartTime = System.currentTimeMillis();
		Edge[] listCount = sort.countSort(edges.toArray(new Edge[edges.size()]), n);
		ArrayList<Edge> listCntMST = kruskalAlg.KruskalMST(listCount);
		
		for(Edge e : listCntMST) {
			weightSum += e.getWeight();
			if(n < 10) System.out.println(e);			
		}
		
		kruskalAlg.reset();
		
		sortFinishTime = System.currentTimeMillis();
		System.out.print("\n");
		System.out.println("Total Weight of MST using Kruskal: " + weightSum);
		System.out.println("Runtime: " + (sortFinishTime - sortStartTime) 
				+ " milliseconds");
		
		weightSum = 0;
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH LIST USING QUICKSORT");
		
		sortStartTime = System.currentTimeMillis();
		Edge[] listQuick = sort.quickSort(edges.toArray(new Edge[edges.size()]), 0, 
				edges.toArray(new Edge[edges.size()]).length-1);
		ArrayList<Edge> listQckMST = kruskalAlg.KruskalMST(listQuick);
		
		for(Edge e : listQckMST) {
			weightSum += e.getWeight();
			if(n < 10) System.out.println(e);			
		}
		
		kruskalAlg.reset();
		
		sortFinishTime = System.currentTimeMillis();
		System.out.print("\n");
		System.out.println("Total Weight of MST using Kruskal: " + weightSum);
		System.out.println("Runtime: " + (sortFinishTime - sortStartTime) 
				+ " milliseconds");
		
		weightSum = 0;
	}
	
	/**
	 * Perform Prim's Algorithm on the randomly generated graph to
	 * obtain a minimum spanning tree from the graph.
	 * Sum the weights of the edges in the MST, provide a runtime, and
	 * print the results to the console.
	 */
	public void performPrimsAlg(){
		long primStartTime, primFinishTime;
		int weightSum = 0;
		edges = primEdges;
						
		System.out.println("===================================");
		System.out.println("PRIM WITH ADJACENCY MATRIX");
		
		primStartTime = System.currentTimeMillis();
		
		Vertex[] mstMatrix = primAlgMatrix.PrimMST(convert2DArray(edgeMatrix));
		
		for(int j = 1; j < mstMatrix.length; j++){
			Vertex v = mstMatrix[j];
			if(n < 10){
				System.out.println("" + v.getParent() + " " + v.getVertexID() + " weight = " + v.getPriority());
			}
			weightSum = weightSum + v.getPriority();
		}
		
		primFinishTime = System.currentTimeMillis();
		System.out.print("\n");
		System.out.println("Total Weight of MST using Prim: " + weightSum);
		System.out.println("Runtime: " + (primFinishTime - primStartTime) 
				+ " milliseconds");
		
		weightSum = 0; //Reinitialize weightSum to 0 after each sort/printout
		
		//Re-trim the edge list and reset the parents/priorities of the vertices
		trimListForSort();
		resetVertices();
		
		System.out.println("===================================");
		System.out.println("PRIM WITH ADJACENCY LIST");
		
		Vertex[] mstList = primAlgList.PrimMST
				(edges.toArray(new Edge[edges.size()]));
		
		for(int j = 1; j < mstList.length; j++){
			Vertex v = mstList[j];
			if(n < 10){
				System.out.println("" + v.getParent() + " " + v.getVertexID() + " weight = " + v.getPriority());
			}						
			weightSum = weightSum + v.getPriority();			
		}
		
		primFinishTime = System.currentTimeMillis();
		System.out.print("\n");
		System.out.println("Total Weight of MST using Prim: " + weightSum);
		System.out.println("Runtime: " + (primFinishTime - primStartTime) 
				+ " milliseconds");
		
		weightSum = 0;
	}
}