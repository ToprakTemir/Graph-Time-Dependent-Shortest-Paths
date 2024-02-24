import java.io.*;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    public static final int sixHours = 21600;
    public static HashMap<String, Airport> airports = new HashMap<>(); // maps airports' names with the airports
    public static HashMap<String, Airfield> airfields = new HashMap<>(); // maps airfields' names with the airfields
    public static int maxParkingCount;
    public static HashMap<String, AirportInTime> airportsInTime = new HashMap<>(); // maps "airportName+time" with the AirportInTime object
    public static void main(String[] args) throws IOException {

        String airportFileName = args[0];
        String directionsFileName = args[1];
        String weatherFileName = args[2];
        String missionsFileName = args[3];
        String task1outFileName = args[4];
        String task2outFileName = args[5];

        File airportFile = new File(airportFileName);
        File directionsFile = new File(directionsFileName);
        File weatherFile = new File(weatherFileName);
        File missionsFile = new File(missionsFileName);
        File task1outFile = new File(task1outFileName);
        File task2outFile = new File(task2outFileName);

        FileWriter task1outWriter = new FileWriter(task1outFile, false);
        FileWriter task2outWriter = new FileWriter(task2outFile, false);
        PrintWriter task1out = new PrintWriter(task1outWriter, true);
        PrintWriter task2out = new PrintWriter(task2outWriter, true);

        Scanner airportsInput = new Scanner(airportFile);
        Scanner directionsInput = new Scanner(directionsFile);
        Scanner weatherInput = new Scanner(weatherFile);
        Scanner missionsInput = new Scanner(missionsFile);

        long startTime = System.currentTimeMillis();

        //reading weather input
        weatherInput.nextLine();
        while (weatherInput.hasNext()) {
            String[] curLine = weatherInput.nextLine().split(",");
            String name = curLine[0];
            int time = Integer.parseInt(curLine[1]);
            int weatherCode = Integer.parseInt(curLine[2]);

            Airfield currentAirfield;
            if (!airfields.containsKey(name)) {
                currentAirfield = new Airfield(name);
                airfields.put(name, currentAirfield);
            }
            else
                currentAirfield = airfields.get(name);

            currentAirfield.weatherStatus.put(time, weatherCode);
        }

        // reading airports input
        airportsInput.nextLine();
        while (airportsInput.hasNext()) {
            String[] curLine = airportsInput.nextLine().split(",");
            String name = curLine[0];
            Airport airport = new Airport(name);
            airport.airfield = airfields.get(curLine[1]);
            airport.latitude = Double.parseDouble(curLine[2]);
            airport.longitude = Double.parseDouble(curLine[3]);
            airport.parkingCost = Double.parseDouble(curLine[4]);
            airports.put(name, airport);
        }

        // reading directions input
        directionsInput.nextLine();
        while (directionsInput.hasNext()) {
            String[] curLine = directionsInput.nextLine().split(",");
            Airport from = airports.get(curLine[0]);
            Airport to = airports.get(curLine[1]);
            from.adjacencyList.add(to);
        }

        // handling the missions

        int adjacencyListWasAdjusted = 0;
        int adjacencyListWasNotAdjusted = 0;

        String plane = missionsInput.nextLine();
        while (missionsInput.hasNext()) {
            String[] curLine = missionsInput.nextLine().split(" ");

            Airport origin = airports.get(curLine[0]);
            Airport destination = airports.get(curLine[1]);
            int startingTime = Integer.parseInt(curLine[2]);
            int deadline = Integer.parseInt(curLine[3]);

            // task1
            // for task1, we assume all departures and landings happen at startingTime, there is no parking, and arriving before deadline is always possible

            // dijkstra's algorithm

            for (Airport airport : airports.values()) {
                airport.known = false;
                airport.distance = Double.MAX_VALUE;
//                airport.distanceToDestination = heuristic(airport, destination);
            }

            origin.distance = 0;
            PriorityQueue<Airport> boundaryNodes = new PriorityQueue<>();
            boundaryNodes.add(origin);

            while (!boundaryNodes.isEmpty()) {

                Airport current = boundaryNodes.poll();
                if (current == destination) break;
                if (current.known) continue;
                current.known = true;

                    for (Airport neighbor : current.adjacencyList) {
                        // ignore known neighbors
                        if (!neighbor.known) {
                            double cost = cost(current, neighbor, startingTime, startingTime);
                            if (current.distance + cost < neighbor.distance) {
                                neighbor.distance = cost + current.distance;
                                neighbor.path = current;
                                boundaryNodes.add(neighbor.copy());
                            }
                        }
                    }

            }

            printPath(destination, origin, task1out, destination.distance, true);

            // task2

            int minFlightTime = getMinFlightTimeFromTask1(destination, origin, plane);
            maxParkingCount = ((deadline - startingTime - minFlightTime) / sixHours) + 2;

            airportsInTime = new HashMap<>(); // I found out that deleting all saved airports instead of resetting them one by one is marginally faster, so I'm doing that here
//            for (AirportInTime airportInTime : airportsInTime.values()) {
//                airportInTime.distance = Double.MAX_VALUE;
//                airportInTime.known = false;
//                airportInTime.path = null;
//                airportInTime.parkingCount = 0;
//            }

            AirportInTime originInTime = getAirportInTime(origin, startingTime, airportsInTime);
            originInTime.distance = 0;

            PriorityQueue<AirportInTime> BoundaryNodes = new PriorityQueue<>();
            airportsInTime.put(originInTime.airport.name + startingTime, originInTime);
            BoundaryNodes.add(originInTime);

            Path bestPath = new Path();
            bestPath.cost = Double.MAX_VALUE;

            while (!BoundaryNodes.isEmpty()) {

                // pre check
                AirportInTime current = BoundaryNodes.poll();
                if (current.distance > bestPath.cost) continue;
                if (current.time > deadline) continue;
                if (current.parkingCount > maxParkingCount) continue;

                if (current.airport.name.equals(destination.name))
                    if (current.distance < bestPath.cost)
                        bestPath = new Path(current, originInTime);

                if (current.known) continue;
                current.known = true;


                // adjusting the adjacency list

                // add parked future of the current airport
                if (current.time + sixHours <= deadline) {
                    AirportInTime parkedFuture = getAirportInTime(current.airport, current.time + sixHours, airportsInTime);
                    current.adjacencyList.add(parkedFuture);
                }

                // add all the neighbors' correct time version of the current airport
                for (Airport neighbor : current.airport.adjacencyList) {
                    int timeWeCanReachNeighbor = current.time + flightTime(current.airport.distanceTo(neighbor), plane);
                    if (timeWeCanReachNeighbor > deadline) continue;
                    AirportInTime neighborInTime = getAirportInTime(neighbor, timeWeCanReachNeighbor, airportsInTime);
                    current.adjacencyList.add(neighborInTime);
                }

                // main loop of neighbors

                for (AirportInTime neighbor : current.adjacencyList) {
                    // ignore known neighbors
                    if (neighbor == null) continue;
                    if (!neighbor.known) {
                        double cost = cost(current, neighbor);
                        if (current.distance + cost < neighbor.distance) {
                            neighbor.distance = cost + current.distance;
                            neighbor.path = current;

                            if (current.airport.name.equals(neighbor.airport.name))
                                neighbor.parkingCount = current.parkingCount + 1;
                            else
                                neighbor.parkingCount = current.parkingCount;

                            BoundaryNodes.add(neighbor.copy());
                        }
                    }
                }
            }

            if (bestPath.cost != Double.MAX_VALUE)
                printPath(bestPath, task2out);
            else
                task2out.println("No possible solution.");

        }
        airportsInput.close();
        directionsInput.close();
        weatherInput.close();
        missionsInput.close();
        task1out.close();
        task2out.close();

//        System.out.println("Total execution time: " + (System.currentTimeMillis() - startTime)/1000.00 + "s");
    }

    public static AirportInTime getAirportInTime(Airport airport, int time, HashMap<String, AirportInTime> airportsInTime) {
//        AirportInTime airportInTime = airportsInTime.get(airport.name + time);
//        if (airportInTime == null) {
//            airportInTime = new AirportInTime(airport, time);
//            airportsInTime.put(airport.name + time, airportInTime);
//        }
//        return airportInTime;
        return airportsInTime.computeIfAbsent(airport.name + time, k -> new AirportInTime(airport, time));
    }
    public static double heuristic(Airport node, Airport destination) {
        // A*
        return node.distanceTo(destination);
    }

    public static double cost(Airport origin, Airport destination, int departureTime, int arrivalTime) {
        return (300 * destination.weatherMultiplier(arrivalTime) * origin.weatherMultiplier(departureTime)) + origin.distanceTo(destination);
    }
    public static double cost(AirportInTime origin, AirportInTime destination) {
        if (!origin.airport.name.equals(destination.airport.name))
            return (300 * destination.weatherMultiplier() * origin.weatherMultiplier()) + origin.distanceTo(destination);
        else
            return origin.airport.parkingCost;
    }

    public static int flightTime(double distance, String plane) {
        switch (plane) {
            case "Carreidas 160" -> {
                if (distance <= 175) return sixHours;
                else if (distance <= 350) return 2*sixHours;
                else return 3 * sixHours;
            }
            case "Orion III" -> {
                if (distance <= 1500) return sixHours;
                else if (distance <= 3000) return 2 * sixHours;
                else return 3 * sixHours;
            }
            case "Skyfleet S570" -> {
                if (distance <= 500) return sixHours;
                else if (distance <= 1000) return 2 * sixHours;
                else return 3 * sixHours;
            }
            case "T-16 Skyhopper" -> {
                if (distance <= 2500) return sixHours;
                else if (distance <= 5000) return 2 * sixHours;
                else return 3 * sixHours;
            }
        }
        return -1;
    }

    public static void printPath(Path path, PrintWriter out) {
        for (int i=path.path.size()-1; i >= 0; i--) {

            if (i < path.path.size() - 1 && path.path.get(i).airport.name.equals(path.path.get(i+1).airport.name))
                out.print("PARK ");
            else
                out.print(path.path.get(i).airport.name + " ");

        }
        out.printf("%.5f\n", path.cost);
    }
    public static void printPath(Airport destination, Airport origin, PrintWriter out, double totalCost, boolean isCalledFromMain) {
        if (destination != origin) {
            printPath(destination.path, origin, out, totalCost, false);
        }
        out.print(destination.name + " ");

        if (isCalledFromMain)
            out.printf("%.5f\n", totalCost);
    }

    public static int getMinFlightTimeFromTask1(Airport destination, Airport origin, String plane) {
        int totalTime = 0;
        while (destination != origin) {
            totalTime += flightTime(destination.distanceTo(destination.path), plane);
            destination = destination.path;
        }
        return totalTime;
    }
}
