import java.util.ArrayList;
import java.util.HashMap;

public class Airport implements Comparable<Airport> {
    public String name;
    public Airfield airfield;
    public double latitude; // -90 <= latitude <= 90
    public double longitude; // -180 <= longitude <= 180
    public double parkingCost;
    public ArrayList<Airport> adjacencyList;
//    public ArrayList<Airport> airportsPointingToThis;

    public boolean known;
    public double distance;
    public Airport path;
//    public double distanceToDestination;

    public Airport(String name) {
        this.name = name;
        adjacencyList = new ArrayList<>();
    }

    public double distanceTo(Airport other) {
//        double lat1 = Math.toRadians(this.latitude);
//        double lat2 = Math.toRadians(other.latitude);
//        double lon1 = Math.toRadians(this.longitude);
//        double lon2 = Math.toRadians(other.longitude);
//        return 2 * 6371 * Math.asin(Math.sqrt(Math.pow(Math.sin((lat1 - lat2) / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lon1 - lon2) / 2), 2)));

        // do all the things below in a single line

        return 2 * 6371 * Math.asin(Math.sqrt(Math.pow(Math.sin((Math.toRadians(this.latitude) - Math.toRadians(other.latitude)) / 2), 2) + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude)) * Math.pow(Math.sin((Math.toRadians(this.longitude) - Math.toRadians(other.longitude)) / 2), 2)));
    }

    public double weatherMultiplier(int time) {
        int weatherCode = airfield.weatherAt(time);

        // converting weatherCode to binary
        int[] binaryExpression = new int[5];
        for (int i = 0; i < 5; i++) {
            binaryExpression[i] = weatherCode % 2;
            weatherCode /= 2;
        }

//        int B_w = binaryExpression[4]; // wind
//        int B_r = binaryExpression[3]; // rain
//        int B_s = binaryExpression[2]; // snow
//        int B_h = binaryExpression[1]; // hail
//        int B_b = binaryExpression[0]; // bolt
//
//        return (B_w * 1.05 + (1-B_w)) * (B_r * 1.05 + (1-B_r)) * (B_s * 1.10 + (1-B_s)) * (B_h * 1.15 + (1-B_h)) * (B_b * 1.20 + (1-B_b));


return (binaryExpression[4] * 1.05 + (1-binaryExpression[4])) * (binaryExpression[3] * 1.05 + (1-binaryExpression[3])) * (binaryExpression[2] * 1.10 + (1-binaryExpression[2])) * (binaryExpression[1] * 1.15 + (1-binaryExpression[1])) * (binaryExpression[0] * 1.20 + (1-binaryExpression[0]));
    }

    public int compareTo(Airport other) {
        double difference = this.distance - other.distance; //+ (this.distanceToDestination - other.distanceToDestination) * 0.1;

        if (difference > 0) return 1;
        else if (difference < 0) return -1;
        else return 0;
    }

    public Airport copy() {
        Airport copy = new Airport(this.name);
        copy.airfield = this.airfield;
        copy.latitude = this.latitude;
        copy.longitude = this.longitude;
        copy.parkingCost = this.parkingCost;
        copy.adjacencyList = new ArrayList<>(this.adjacencyList);
//        copy.distanceToDestination = this.distanceToDestination;
        copy.known = this.known;
        copy.distance = this.distance;
        copy.path = this.path;
        return copy;
    }
}
