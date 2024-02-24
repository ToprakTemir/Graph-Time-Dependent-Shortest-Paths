import java.util.HashMap;

public class Airfield {
    public String name;
//    public Airport[] airports;
    public HashMap<Integer, Integer> weatherStatus; // maps time values with the weather code at that time

    public Airfield(String name) {
        this.name = name;
        weatherStatus = new HashMap<>();
    }

    public int weatherAt(int time) {
        return weatherStatus.get(time);
    }


}
