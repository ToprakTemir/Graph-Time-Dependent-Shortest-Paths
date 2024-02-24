import java.util.ArrayList;

public class AirportInTime implements Comparable<AirportInTime> {
    public Airport airport;
    public int time;
    public ArrayList<AirportInTime> adjacencyList;
    public boolean known;
    public double distance;
    public AirportInTime path;
    public int parkingCount;
    public AirportInTime(Airport airport, int time) {
        this.airport = airport;
        this.time = time;
        this.parkingCount = 0;
        distance = Double.MAX_VALUE;
        this.adjacencyList = new ArrayList<>();
    }
    public AirportInTime(Airport airport, int time, int parkingCount) {
        this.airport = airport;
        this.time = time;
        this.parkingCount = parkingCount;
        this.adjacencyList = new ArrayList<>();
    }

    public double distanceTo(AirportInTime other) {
        return airport.distanceTo(other.airport);
    }
    public double weatherMultiplier() {
        return airport.weatherMultiplier(time);
    }

    public int compareTo(AirportInTime other) {
        double difference = this.distance - other.distance;

        if (difference > 0) return 1;
        else if (difference < 0) return -1;
        else return 0;
    }

    // does not work
//    public AirportInTime getFutureAirportAfterParking(int deadline){
//
//        int timeSixHoursLater = time + Main.sixHours;
//
//        if (Main.airportsInTime.containsKey(airport.name + timeSixHoursLater))
//            if (Main.airportsInTime.get(airport.name + timeSixHoursLater).path.airport.name.equals(airport.name))
//                return Main.airportsInTime.get(airport.name + timeSixHoursLater);
//
//        if (parkingCount == Main.maxParkingCount || time + Main.sixHours > deadline)
//            return null;
//
//        return new AirportInTime(airport, time + Main.sixHours, parkingCount + 1);
//
//    }

    public AirportInTime copy(){
        AirportInTime copy = new AirportInTime(airport, time, parkingCount);
        copy.adjacencyList = new ArrayList<>(this.adjacencyList);
        copy.known = known;
        copy.distance = distance;
        copy.path = path;
        return copy;
    }
}
