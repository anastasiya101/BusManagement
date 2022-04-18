import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.TST;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class main {

    static EdgeWeightedDigraph dijkstraGraph;
    static DirectedEdge edge;
    static ArrayList<Integer> busStops;
    static TST<String> tst;


    public static void main(String[] args) {

        readStops("stops.txt");

    }

    /**
     * Method to read in the stops.txt file and initialise all data that needs to be used.
     *
     * @param filename name of the file to be read
     */
    public static void readStops(String filename) {
        try {
            if (filename == null) {
                return;
            }
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            busStops = new ArrayList<>();
            tst = new TST<>();
            while (myReader.hasNextLine()) {
                String[] line = myReader.nextLine().split(",");
                busStops.add(Integer.parseInt(line[0]));
            }
            Collections.sort(busStops);
            dijkstraGraph = new EdgeWeightedDigraph(busStops.size());
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
