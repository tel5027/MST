import java.io.FileNotFoundException;
import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * MST contains the main program for the random graph generation
 * @author Thomas LaSalle (tel5027)
 */
public class MST {

	/**
	 * The Main program
	 * @param args Command Line arguments
	 * @throws FileNotFoundException 
	 *     thrown if the input file is not found
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		//Instance Variables
		//Initialized to default values for compilation
		int n = 0;
		long seed = 0;
		double p = 0.0;
		Scanner reader = null;
		
		//If there is not 1 argument, the input file is missing
		if(args.length != 1) {
	    	usage("Input file not found");
		}
		
		//Pull the argument for the filename
		//Create a Scanner object to read data from the file
		File file = new File(args[0]);
		
		try{
			 reader = new Scanner(file);
		}
		catch(FileNotFoundException e){
			usage("Input file not found");
		}
		
		
		//Ensure that the first two lines are n and seed (Integers)
		//If they are not, terminate the program
		try {
			n = reader.nextInt();
			seed = reader.nextInt();
		}
		catch(NumberFormatException e) {
			reader.close();
			usage("n and seed must be integers");
		}
		catch(InputMismatchException e) {
			reader.close();
			usage("n and seed must be integers");
		}
				
		//Ensure the final line is p (Double)
		//If not, terminate the program
		try {
			p = reader.nextDouble();
		}
		catch(InputMismatchException e) {
			reader.close();
			usage("p must be a real number");
		}
				
		//If n < 2 or p is not between 0 and 1, terminate the program
		if(n < 2){
			reader.close();
			usage("n must be greater than 1");
		}
		if(p < 0.0 || p > 1.0){
			reader.close();
			usage("p must be between 0 and 1");
		}
		
		//Construct the graph, and print it out.
		Graph g = new Graph(n,seed,p);
		
		//Close the scanner object
		reader.close();

	}
	
	/**
	 * Usage prints a message to the terminal and terminates
	 * with status code 1
	 * @param msg The message to print
	 */
	public static void usage(String msg) {
		System.out.println(msg);
		System.exit(1);
	}

}
