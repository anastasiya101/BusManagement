import java.util.Scanner;

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
                switch(input) {
                    case 1:
                        //findShortestPath();
                        break;
                    case 2:
                        //searchBusStopName();
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

    }

    public static void searchBusStopName() {
        System.out.println("Please enter a stops full name or the first few characters of the bus stop you are searching for: ");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()){
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

    }

}
