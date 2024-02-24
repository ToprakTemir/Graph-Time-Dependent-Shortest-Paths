import java.util.ArrayList;

public class Path implements Comparable<Path> {
    public ArrayList<AirportInTime> path;
    public double cost;
    public double arrivalTime;

    public Path() {
        path = new ArrayList<>();
    }
    public Path(AirportInTime arrivedDestination, AirportInTime origin) {
        this.cost = arrivedDestination.distance;
        path = new ArrayList<>();

        AirportInTime currentAirport = arrivedDestination;
        while (currentAirport != origin) {
            path.add(currentAirport);
            currentAirport = currentAirport.path;
        }
        path.add(origin);

        arrivalTime = arrivedDestination.time;
    }
    public int compareTo(Path other) {
        double difference = this.cost - other.cost;

        if (difference > 0) return 1;
        else if (difference < 0) return -1;
        else return 0;
    }
}
