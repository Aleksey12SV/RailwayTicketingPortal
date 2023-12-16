package railway.train;

import java.util.ArrayList;
import java.util.List;

public class MockedDatabase {

    public static List<Train> getAllTrains() {
        List<Train> trains = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            String startingCity = "City" + (i % 5 + 1);
            String destinationCity = "City" + ((i + 2) % 5 + 1);

            Train morningTrain = createTrain("Train" + i, startingCity, destinationCity, "09:30", "18/12/2023", 50, 100 + 50 * (i % 5));
            trains.add(morningTrain);

            Train afternoonTrain = createTrain("Train" + i, startingCity, destinationCity, "15:30", "19/12/2023", 10, 100 + 50 * (i % 5));
            trains.add(afternoonTrain);
        }

        return trains;
    }
    private static Train createTrain(String trainNumber, String startingCity, String destinationCity, String departureTime, String departureDay, int availableSeats, double baseTicketPrice) {
        return new Train(trainNumber, startingCity, destinationCity, departureTime, departureDay, availableSeats, baseTicketPrice);
    }
}
