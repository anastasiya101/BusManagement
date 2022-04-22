import java.util.Scanner;

public class main {
    public static void main(String[] args) {
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
