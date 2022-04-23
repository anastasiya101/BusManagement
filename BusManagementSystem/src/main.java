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

import static java.lang.Integer.parseInt;


public class main {

    static EdgeWeightedDigraph dijkstraGraph;
    static DirectedEdge edge;
    static ArrayList<Integer> stopID;
    static TST<String> tst;
    static ArrayList<String> stopTimes;
    static DijkstraSP dijkstraSP;

    public static final String[] STREET_PREFIXES = new String[]{"FLAGSTOP", "WB", "NB", "SB", "EB"};

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
                for (int i = 0; i < 3; i++) {
                    index = theLine.indexOf(',');
                }

                String[] splitBySpaces = line[2].split(" ");
                String streetPrefix = splitBySpaces[0];

                // places the street prefix at the back of the stop name line
                for (String prefix : STREET_PREFIXES) {
                    if (streetPrefix.equals(prefix)) {
                        line[2] = line[2].replace(streetPrefix, "").trim();
                        line[2] = line[2] + " " + streetPrefix;
                    }
                }

                // new reformatted line
                String newLine = line[2] + theLine.substring(index) + line[0] + ", " + line[1];
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
            Scanner scanner = new Scanner(myObj);
            scanner.nextLine();

            int weight;

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");

                // if the transfer type is 0 then weight is 2
                if (line[2].equals("0")) {
                    weight = 2;
                } else {
                    weight = (parseInt(line[3])) / 100;
                }
                // get the values of the two stops
                int firstValue = Collections.binarySearch(stopID, parseInt(line[0]));
                int secondValue = Collections.binarySearch(stopID, parseInt(line[1]));

                //add edge to the graph
                DirectedEdge edge = new DirectedEdge(firstValue, secondValue, weight);
                dijkstraGraph.addEdge(edge);
            }
            scanner.close();

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
            Scanner scanner = new Scanner(myObj);
            stopTimes = new ArrayList<>();
            scanner.nextLine();
            String line = scanner.nextLine();
            String[] firstLine = line.split(",");

            while (scanner.hasNextLine()) {

                String secondLine = scanner.nextLine();
                String[] nextLine = secondLine.split(",");
                String arrivalTime = nextLine[1].trim();
                String[] arrivalTimeSplit = arrivalTime.split(":");

                if (parseInt(arrivalTimeSplit[0]) <= 23 && parseInt(arrivalTimeSplit[0]) >= 0 &&
                        parseInt(arrivalTimeSplit[1]) <= 59 &&
                        parseInt(arrivalTimeSplit[2]) <= 59)
                    stopTimes.add(secondLine);

                if (firstLine[0].equals(nextLine[0])) {

                    //get position of the bus stop
                    int firstValue = Collections.binarySearch(stopID, parseInt(firstLine[3]));
                    int secondValue = Collections.binarySearch(stopID, parseInt(nextLine[3]));

                    //add the edge to the graph
                    DirectedEdge edge = new DirectedEdge(firstValue, secondValue, 1);
                    dijkstraGraph.addEdge(edge);
                }

                firstLine = nextLine;
            }
            Collections.sort(stopTimes);
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        }
    }


}
