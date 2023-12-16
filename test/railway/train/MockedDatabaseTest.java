package railway.train;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MockedDatabaseTest {

    @Test
    public void testGetAllTrains() {
        List<Train> trains = MockedDatabase.getAllTrains();

        assertNotNull(trains);
        assertFalse(trains.isEmpty());

        Train firstTrain = trains.get(0);
        assertEquals("City1", firstTrain.getStartingCity());
        assertEquals("City3", firstTrain.getDestination());
        assertEquals("Train1", firstTrain.getTrainNumber());
        assertEquals("09:30", firstTrain.getDepartureTime());
        assertEquals("18/12/2023", firstTrain.getDepartureDay());
        assertEquals(50, firstTrain.getAvailableSeats());
        assertEquals(150.0, firstTrain.getBaseTicketPrice());
    }
}
