// library

import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.TST;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static java.lang.Integer.parseInt;


/**
 *
 * @ author : Anastasiya Bogoslovskaya
 *
 */


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

        boolean exitProgram = false;
        while (!exitProgram) {
            System.out.println("\nEnter 1 to search for the shortest path between two bus stops of your choosing. \n" +
                    "Enter 2 to search for a certain bus stop by name/prefix. \n" +
                    "Enter 3 to search by arrival time. \n" +
                    "Or type 'exit' to terminate the program.\n\n" +
                    "Please enter 1, 2, 3 or type 'exit'");

            Scanner scanner = new Scanner(System.in);

            if (scanner.hasNext()) {
                String input = scanner.next();
                switch (input) {
                    case "1" -> findShortestPath();
                    case "2" -> searchBusStopName();
                    case "3" -> searchArrivalTime();
                    case "exit", "Exit", "EXIT" -> {
                        exitProgram = true;
                        System.out.println("The program has now terminated.");
                    }
                    default -> System.out.println("Please enter a valid input value of the following integers 1, 2, 3 or type 'exit'.\n");
                }
                //scanner.close();
            }
        }
        System.out.println("Thank you for using our Bus Management System.\n "
                + "See you next time!");
    }


    /**
     *
     */
    public static void findShortestPath() {
        System.out.println("Please enter Bus Stop ID you would to depart from :  ");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            int departingStop = scanner.nextInt();
            System.out.println("Please enter the Bus Stop ID you would like to arrive at :  ");
            if (scanner.hasNextInt()) {
                int arrivingStop = scanner.nextInt();
                if (departingStop != arrivingStop ) {
                    if(departingStop >= 0 && departingStop <= 12375 &&
                        arrivingStop >= 0 && arrivingStop <= 12375) {
                        int departIndex = Collections.binarySearch(stopID, departingStop);
                        int destIndex = Collections.binarySearch(stopID, arrivingStop);

                    System.out.println("Finding shortest path from bus stop " + departingStop + " to bus stop " + arrivingStop + "...");
                        dijkstraSP = new DijkstraSP(dijkstraGraph, departIndex);

                    if (dijkstraSP.hasPathTo(destIndex)) {
                        double lengthOfPath = dijkstraSP.distTo(destIndex);
                        System.out.println("Total Cost: " + lengthOfPath);

                            for (DirectedEdge stop : dijkstraSP.pathTo(destIndex)) {
                                System.out.println("Bus Stop ID: " + stopID.get(stop.to()) + "\t Cost: " + stop.weight() + "  (to get to from previous stop)");
                            }
                        } else
                            System.out.println("No path exists between these stops. ");
                    } else
                        System.out.println("Invalid bus stop ID.");
                } else
                    System.out.println("The Shortest Path is 0 as your arrival and departing stops are the same.");
            } else
                System.out.println("Invalid bus stop ID.");
        } else
            System.out.println("Invalid bus stop ID.");
    }

    /**
     *
     */
    public static void searchBusStopName() {
        System.out.println("Please enter a stops full name or the first few characters of the bus stop you are searching for: ");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            String input = scanner.next();
            int count = 0;
            for (String string : tst.keysWithPrefix(input.toUpperCase())) {
                count++;
                System.out.println(string);
            }
            if (count == 0)
                System.out.println("Stop ID not found.");
        }
    }

    /**
     *
     */
    public static void searchArrivalTime() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter arrival time in the format (hh:mm:ss) : ");
        if (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim(); // trim whitespace inputted by user
            String[] inputtedTimes = input.split(":"); // split user input by ":"

            //checking a valid time has been entered
            if (inputtedTimes.length == 3 && parseInt(inputtedTimes[0]) <= 23 &&
                    parseInt(inputtedTimes[1]) <= 59 && parseInt(inputtedTimes[2]) <= 59) {
                int count = 0;
                System.out.println("trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign," +
                        "pickup_type,drop_off_type,shape_dist_traveled");
                for (String s : stopTimes) {
                    String[] arrivalTimes = s.split(",");
                    if (arrivalTimes[1].trim().equals(input)) {
                        count++;
                        System.out.println(s);
                    }
                }
                if (count == 0) {
                    System.out.println("Invalid : There doesn't exist any stops with this arrival time.");
                }
            } else {
                System.out.println("Invalid arrival time format : must be entered as 'hh:mm:ss'");
            }
        }
    }

    /**
     * Reads in the stops from the stops.txt file
     *
     * @param filename the file to be read
     * @throws IllegalArgumentException if the input file is not found
     */
    public static void readStops(String filename) {
        try {
            if (filename == null)
                return;

            File myObj = new File(filename);
            Scanner scanner = new Scanner(myObj);
            scanner.nextLine();
            stopID = new ArrayList<>();
            tst = new TST<>();

            int count = 0;
            while (scanner.hasNextLine()) {
                String theLine = scanner.nextLine();

                String[] line = theLine.split(",");
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
     * @throws IllegalArgumentException if the input file is not found
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

                //Vertexes must be non-negative integers
                if(firstValue >= 0 && secondValue >=0) {
                    //add edge to the graph
                    edge = new DirectedEdge(firstValue, secondValue, weight);
                    dijkstraGraph.addEdge(edge);
                }
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
     * @throws IllegalArgumentException if the input file is not found
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
                        parseInt(arrivalTimeSplit[1]) <= 59 && parseInt(arrivalTimeSplit[2]) <= 59 &&
                        arrivalTimeSplit.length == 3)
                    stopTimes.add(secondLine);

                if (firstLine[0].equals(nextLine[0])) {


                    //get position of the bus stop
                    int firstValue = Collections.binarySearch(stopID, parseInt(firstLine[3]));
                    int secondValue = Collections.binarySearch(stopID, parseInt(nextLine[3]));

                    //Vertexes must be non-negative integers
                    if(firstValue >= 0 && secondValue >=0) {
                        //add the edge to the graph
                        edge = new DirectedEdge(firstValue, secondValue, 1);
                        dijkstraGraph.addEdge(edge);
                    }
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
