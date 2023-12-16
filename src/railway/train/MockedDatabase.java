package railway.train;

import java.util.ArrayList;
import java.util.List;

public class MockedDatabase {

    public static List<Train> getAllTrains() {
        List<Train> trains = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            String startingCity = "City" + (i % 5 + 1); // Cycling through 5 cities
            String destinationCity = "City" + ((i + 2) % 5 + 1); // Cycling through 5 cities with an offset

            Train train = new Train("Train" + i, startingCity, destinationCity,
                    "09:30",
                    "18/12/2023",50, 100 + 50*(i%5));
            trains.add(train);
        }

        return trains;
    }
}
