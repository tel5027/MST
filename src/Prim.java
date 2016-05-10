import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class Prim houses our Prim's algorithm for selecting a
 * minimum spanning tree from our randomly generated
 * graph.
 * @author Thomas LaSalle (tel5027)
 */
public class Prim {	

	private PriorityQueue pq;
	private int count;
	private ArrayList<Vertex> vert;
	
	/**
	 * Construct a new Prim's Algorithm object with the provided number
	 * of Vertices as well as the list of Vertices
	 * @param n The number of vertices
	 * @param vert The list of vertices
	 */
	public Prim(int n, ArrayList<Vertex> vert) {
		pq = new PriorityQueue(n);
		count = n;		
		this.vert = vert;
		Vertex vertices[] = vert.toArray(new Vertex[vert.size()]);
		
		for(int i = 1; i < n; i++) {
            pq.insert(vertices[i]);			
		}
	}
	
	/**
	 * Perform the Prim's MST Algorithm on the provided
	 * graph of edges, and return a list of vertices with the
	 * ideal edge (priority) and parent
	 * @param graph The random generated graph of edges
	 * @return The minimum spanning tree
	 */
	public Vertex[] PrimMST(Edge[] graph){		
		Vertex[] mst = new Vertex[count];
		
		Vertex u = vert.get(0);
		u.setPriority(0);
		mst[0] = u;
		int num = count-1;
		
		while(num > 0) {
			for(Vertex v : u.getAdjacents()) {
				for(int i = 0; i < graph.length; i++) {
					if(graph[i].getStartVertex() == u.getVertexID() || 
							graph[i].getStartVertex() == v.getVertexID()) {
						if(graph[i].getEndVertex() == v.getVertexID() || 
								graph[i].getEndVertex() == u.getVertexID()) {
							if(pq.contains(v) &&  graph[i].getWeight() < v.getPriority()) {
								v.setPriority(graph[i].getWeight());
								v.setParent(u.getVertexID());
							}							
						}
					}
						
				}
			}
			
			pq.reheapify();
			
			u = pq.delete();
			mst[u.getVertexID()] = u;
			num--;
		}

		return mst;
	}
	
	/**
	 * Class PriorityQueue provides an implementation of a Priority
	 * Queue as a binary min heap.
	 * @author Thomas LaSalle (tel5027)
	 */
	private class PriorityQueue{
		
		private Vertex heap[];
		private int count, size, qp[];
		
		/**
		 * Initialize an empty Priority Queue with the provided capacity
		 * @param capacity
		 */
		public PriorityQueue(int capacity){
			
			heap = new Vertex[capacity+1];
			qp = new int[capacity+1];
			count = capacity - 1;			
			size = 0;
			
			for(int i = 0; i < capacity; i++){
				qp[i] = -1;
			}
		}
		
		/**
		 * Exchange two items within the Priority Queue
		 * @param keyOne The index of the first key
		 * @param keyTwo The index of the second key
		 */
		private void exchange(int keyOne, int keyTwo) {
			
			Vertex temp;
			
			temp = heap[keyOne];
			heap[keyOne] = heap[keyTwo];
			heap[keyTwo] = temp;
			
			qp[heap[keyOne].getVertexID()] = keyOne;
			qp[heap[keyTwo].getVertexID()] = keyTwo;
		}
		
		/**
		 * Swim an item up the Priority Queue through repeated
		 * exchanges until the min heap is valid
		 * @param k The index of the item to swim up.
		 */
		private void swim(int k) {
			
			if(heap[(k/2)] != null && heap[k] != null){
				while(k > 1 && (heap[(k/2)].compareTo(heap[k]) > 0)) {
					exchange(k, (k/2));
					k = (k/2);
				}
			}			
		}
		
		/**
		 * Sink an item down the Priority Queue through repeated
		 * exchanges until the min heap is valid
		 * @param k The index of the item to sink down
		 */
		private void sink(int k) {
			
			while((2*k) <= count) {
				int j = (2*k);
				if(j < count && (heap[j].compareTo(heap[j+1]) > 0)) j++;
				if(!(heap[k].compareTo(heap[j]) > 0)) break;
				exchange(k, j);
				k = j;
			}
		}
		
		/**
		 * Insert a new item into the Priority Queue, and swim
		 * it up to its proper location.
		 * @param key The key to be inserted into the Priority Queue
		 */
		public void insert(Vertex key) {
			
			size++;
			heap[size] = key;
			qp[heap[size].getVertexID()] = size;
		   	swim(size);			
		}
		
		/**
		 * Delete a key from the Priority Queue and return it to
		 * the user
		 * @return The key which was deleted
		 */
		public Vertex delete() {
			
			exchange(1, count);
			Vertex min = heap[count--];
			sink(1);
			qp[heap[count+1].getVertexID()] = -1;
			heap[count+1] = null;
			size--;
			
			return min;
		}
	
		/**
		 * Check to see if this Priority Queue contains the
		 * provided key
		 * @param key The key to search for
		 * @return Whether the key exists in the Priority Queue or not
		 */
		public boolean contains(Vertex key) {
			return qp[key.getVertexID()] != -1;
		}
		
		/**
		 * Reheapify (or reset) the Priority Queue so that it is a valid
		 * min heap.
		 */
		public void reheapify() {
			Vertex heapCopy[] = heap;
			heap = new Vertex[count+1];
			size = 0;
			
			for(int j = 0; j < heapCopy.length; j++) {
				if(heapCopy[j] != null) insert(heapCopy[j]);
			}
		}		
	}
}