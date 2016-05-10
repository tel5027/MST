/**
 * The SortAlgorithm class provides the sorting algorithms used
 * to sort the edges in our Randomly Generated Graph
 * @author Thomas LaSalle (tel5027)
 */
public class SortAlgorithm {
	
	/**
	 * Instantiate a new Sorter. No parameters are necessary
	 */
	public SortAlgorithm(){
		//Initialize new Sorter
	}
	
	/**
	 * Perform a Quicksort on the provided Edge array
	 * @param edgeList The Edge List array to sort
	 * @param left The left index to start sorting 
	 * @param right The right index to start sorting
	 * @return The sorted array of edges
	 */
	public Edge[] quickSort(Edge[] edgeList, int left, int right){
		if(right <= left) return edgeList;		
		
		int pivot = partition(edgeList, left, right);
		quickSort(edgeList, left, (pivot-1));
		quickSort(edgeList, (pivot + 1), right);
		
		return edgeList;
	}
	
	/**
	 * Perform a Count Sort on the provided Edge array
	 * @param edgeList The Edge List array
	 * @param radix The maximum weight size, used as the count array radix
	 * @return The sorted array of edges
	 */
	public Edge[] countSort(Edge[] edgeList, int radix){
		int N = edgeList.length;
		int[] count = new int[radix+1];
		Edge[] aux = new Edge[N];
		
		for(int i = 0; i < N; i++) {
			count[edgeList[i].getWeight()]++;
		}
		
		for(int r = 0; r < radix; r++) {
			count[r+1] += count[r];
		}
		
		for(int i = 0; i < N; i++) {
			aux[(count[edgeList[i].getWeight()-1]++)] = edgeList[i];
		}
		
		for(int i = 0; i < N; i++) {
			edgeList[i] = aux[i];
		}
		
		return edgeList;
	}
	
	/**
	 * Perform an Insertion Sort on the provided array
	 * of Edges.
	 * @param edgeList The Edge List array
	 * @return The sorted array of edges
	 */
	public Edge[] insertionSort(Edge[] edgeList){
		int N = edgeList.length;
		
		for(int i = 0; i < N; i++) {
			for(int j = i; j > 0; j--) {
				if(edgeList[j].compareTo(edgeList[j-1]) < 0) {
					swap(edgeList, j, (j-1));
				}
			}
		}
		return edgeList;
	}
	
	/**
	 * The swap method swaps the elements of the array where
	 * necessary
	 * @param edgeList The Edge list array
	 * @param left The left element
	 * @param right The right element
	 */
	private void swap(Edge[] edgeList, int left, int right) {
		Edge temp;
		
		temp = edgeList[left];
		edgeList[left] = edgeList[right];
		edgeList[right] = temp;
	}
	
	/**
	 * The partition method divides the Edge array for use by
	 * the Quicksort algorithm.
	 * @param edgeList The Edge List array to sort
	 * @param low The low (left) side from the pivot
	 * @param hi The high (right) side from the pivot
	 * @return The index of the pivot
	 */
	private int partition(Edge[] edgeList, int low, int hi) {
		int i = low;
		int j = hi + 1;
		Edge e = edgeList[low];
		
		while(true) {
			while (edgeList[++i].compareTo(e) < 0) {
				if (i == hi) break;
			}
			
			while(edgeList[--j].compareTo(e) > 0) {
				if (j == low) break;
			}
			
			if(i >= j) break;
			
			swap(edgeList, i, j);
		}
		swap(edgeList, low, j);
		return j;
	}

}
