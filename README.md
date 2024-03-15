# Graph-Time-Dependent-Shortest-Paths

## 1. Introduction

Algorithm to find the shortest path for an airplane to take to a destination, considering travel times between nodes and changing weather conditions with time. The goal is to minimize the "cost" of the path, influenced by the distance between nodes and the weather conditions when entering or exiting a node.

## 2. Definitions

### 2.1. Airport

Where planes depart, park, and land. Every airport has a unique AirportCode, an associated AirfieldName, a latitude, a longitude, and a parkingCost.

**AirportCode**: A unique identifier for the airport. Example: "SAW"  
**AirfieldName**: Name of the airfield to which the airport belongs.  
**Latitude**: First decimal number identifying the location of the airport. -90 ≤ latitude ≤ +90  
**Longitude**: Second decimal number identifying the location of the airport. -180 ≤ longitude ≤ +180  
**ParkingCost**: The cost of parking at the airport for 6 hours.

### 2.2. Airfield

A group of airports that share the same weather conditions. At a given AirfieldName and Time, there is a corresponding WeatherCode.

### 2.3. Weather Multiplier

Weather multipliers are used in the calculation of the cost to fly from one airport to another. They represent the effects of weather conditions on the cost of flight.

### 2.4. Distance

Distance between airports is given by The Haversine Formula.

### 2.5. Possible Flights

A set of possible flights from each airport to other airports.

### 2.6. Flight Cost

The cost of a flight is calculated based on the distance between the departure and landing airports, as well as the weather multipliers of both airports.

### 2.7. Parking Cost

The cost of parking at an airport for 6 hours.

### 2.8. Total Cost

The total cost of a sequence of successive flight and park operations is the sum of flight costs and parking costs.

### 2.9. Mission

Every mission consists of four elements: AirportOrigin, TimeOrigin, AirportDestination, and Deadline. The objective is to find the sequence of successive possible flight and park operations to reach the AirportDestination before the Deadline with the minimum total cost.

### 2.10. Time

Time progresses with every flight and park operation.

## 3. I/O Files & Execution

### 3.2. airports.csv

Contains essential attributes of the airports.

### 3.3. directions.csv

Contains the possible directions of flights between airports.

### 3.4. weather.csv

Contains weather conditions at specific times and airfields.

### 3.5. missions.in

Contains information about missions to be accomplished.

### 3.6. Main.java

The entry point of the program.

**Task 1**: Find successive possible flight operations from AirportOrigin to AirportDestination. Flights occur at TimeOrigin for each mission, the passage of time is not taken into account in flights, and there is no park operation.

**Task 2**: Find a sequence of successive flight and park operations to reach AirportDestination before the Deadline with the minimum total cost. Everything is taken into account here.

**task1-out**: Output file for Task 1. Contains the solution of each mission with airports in the sequence and the total cost.

**task2-out**: Output file for Task 2. Contains the solution of each mission. If a mission can be accomplished, it includes the sequence of flight and park operations and the total cost. If not, it indicates "No possible solution."

## Conclusion

This document outlines the file formats used in this project for finding time-dependent shortest paths in a graph, considering time-dependent edge costs. It also provides instructions for execution and file formats.

