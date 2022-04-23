import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.TST;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;


public class main {

    static EdgeWeightedDigraph dijkstraGraph;
    static DirectedEdge edge;
    static ArrayList<Integer> stopID;
    static TST<String> tst;
    static ArrayList<String> stopTimesInfo;
    static DijkstraSP dijkstraSP;

    public static final String[] STREET_PREFIXES = new String[] {"FLAGSTOP", "WB", "NB", "SB", "EB"};

    public static void main(String[] args) {

        readStops("stops.txt");
        readTransfers("transfers.txt");
        readStopTimes("stop_times.txt");

    }

    /**
     * Reads in the stops from the stops.txt file
     *
     * @param filename the file to be read
     */
    public static void readStops(String filename) {
        try {
            if (filename == null) {
                return;
            }
            File myObj = new File(filename);
            Scanner scanner = new Scanner(myObj);
            scanner.nextLine();
            stopID = new ArrayList<>();
            tst = new TST<>();

            int count = 0;
            while (scanner.hasNextLine()) {
                String theLine = scanner.nextLine();

                String[] line = scanner.nextLine().split(",");
                stopID.add(Integer.parseInt(line[0]));

                //cuts off the first 3 properties from the line
                int index = 0;
                for(int i = 0; i < 3; i++){
                    index = theLine.indexOf(',');
                }

                String[] splitBySpaces = line[2].split(" ");
                String streetPrefix = splitBySpaces[0];

                // places the street prefix at the back of the stop name line
                for (String prefix : STREET_PREFIXES){
                    if (streetPrefix.equals(prefix)){
                        line[2] = line[2].replace(streetPrefix, "").trim();
                        line[2] = line[2] + " " + streetPrefix;
                    }
                }

                // new reformatted line
                String newLine = line[2] + theLine.substring(index) + line[0] + ", " +  line[1];
                tst.put(newLine, Integer.toString(count));
                count++;
            }
            Collections.sort(stopID);
            dijkstraGraph = new EdgeWeightedDigraph(stopID.size());
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        }
    }

    /**
     * Reads in the transfers from the transfers.txt file
     *
     * @param filename the file to be read
     */
    public static void readTransfers(String filename) {
        try {
            if (filename == null) {
                return;
            }
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String[] line = myReader.nextLine().split(",");
                if (Objects.equals(line[2], "0")) {
                    edge = new DirectedEdge(Integer.parseInt(line[0]), Integer.parseInt(line[1]), 2);
                    dijkstraGraph.addEdge(edge);
                } else {
                    DirectedEdge edge = new DirectedEdge(Integer.parseInt(line[0]),
                            Integer.parseInt(line[1]),
                            (Integer.parseInt(line[3]) / 100));
                    dijkstraGraph.addEdge(edge);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        }
    }

    /**
     * Reads in the stop times from the stops_times.txt file
     *
     * @param filename the file to be read
     */
    public static void readStopTimes(String filename) {
        try {
            if (filename == null) {
                return;
            }
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            stopTimesInfo = new ArrayList<>();

            String line = myReader.nextLine();
            String[] firstLine = line.split(",");
            stopTimesInfo.add(line);

            while (myReader.hasNextLine()) {

                String line2 = myReader.nextLine();
                String[] nextLine = line2.split(",");
                stopTimesInfo.add(line2);

                if (Objects.equals(firstLine[0], nextLine[0])) {
                    DirectedEdge edge = new DirectedEdge(Integer.parseInt(firstLine[3]), Integer.parseInt(nextLine[3]), 1);
                    dijkstraGraph.addEdge(edge);
                }
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        }
    }



}
