
import java.util.Collections;
import java.util.Scanner;
import static java.lang.Integer.parseInt;


public class main {
    public static void main(String[] args) {

        boolean exitProgram = false;
        while (!exitProgram) {
            System.out.println("Enter 1 to search for the shortest path between two bus stops of your choosing. \n" +
                    "Enter 2 to search for a certain bus stop by name/prefix. \n" +
                    "Enter 3 to search by arrival time. \n" +
                    "Or type 'exit' to terminate the program.\n");
            System.out.println("Please enter 1, 2, 3 or type 'exit'");

            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                switch (input) {
                    case 1:
                        findShortestPath();
                        break;
                    case 2:
                        searchBusStopName();
                        break;
                    case 3:
                        //searchArrivalTime();
                        break;
                    default:
                        System.out.println("Please enter a valid input value of the following integers 1, 2, 3 or type 'exit'.\n");
                }
            } else {
                String input = scanner.next();
                if (input.equalsIgnoreCase("exit")) {
                    exitProgram = true;
                    System.out.println("The program has now terminated.");
                } else {
                    System.out.println("Please enter a valid input value of the following integers 1, 2, 3 or type 'exit'.\n");
                }
            }
        }

    }

    public static void findShortestPath() {
        System.out.println("Please enter Bus Stop ID you would to depart from :  ");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            int departingStop = scanner.nextInt();
            System.out.println("Please enter the Bus Stop ID you would like to arrive at :  ");
            if (scanner.hasNextInt()) {
                int arrivingStop = scanner.nextInt();
                if (departingStop != arrivingStop) {
                    int departIndex = Collections.binarySearch(stopID, departingStop);
                    int destIndex = Collections.binarySearch(stopID, arrivingStop);

                    System.out.println("Finding shortest path from bus stop " + departingStop + " to bus stop " + arrivingStop);

                    dijkstraSP = new DijkstraSP(dijkstraGraph, departIndex);

                    if (dijkstraSP.hasPathTo(destIndex)) {
                        double lengthOfPath = dijkstraSP.distTo(destIndex);
                        System.out.println("Cost: " + lengthOfPath);

                        for (DirectedEdge stop : dijkstraSP.pathTo(destIndex)) {
                            System.out.println("Bus Stop ID: " + stopID.get(stop.to()) + "\t Cost : " + stop.weight() + "  (to get to from previous stop)");
                        }
                    } else {
                        System.out.println("No path exists between these stops. ");
                    }
                } else {
                    System.out.println("The Shortest Path is 0 as your arrival and departing stops are the same.");
                }
            } else {
                System.out.println("Invalid bus stop ID.");
            }
        } else {
            System.out.println("Invalid bus stop ID.");
        }
    }

    public static void searchBusStopName() {
        System.out.println("Please enter a stops full name or the first few characters of the bus stop you are searching for: ");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            String input = scanner.next();
            int count = 0;
            for (String string : TST.keysWithPrefix(input.toUpperCase())) {
                count++;
                System.out.println(string);
            }
            if (count == 0) {
                System.out.println("Stop name not found, please try again.");
            }

        }
    }

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
}
