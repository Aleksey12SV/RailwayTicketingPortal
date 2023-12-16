package railway.train;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrainTest {

    @Test
    public void testGetFormattedInfo() {
        Train train = new Train("123", "CityA", "CityB", "10:00", "10/10/2001", 50, 100.0);

        String expected = String.format("Train Number: 123%nStarting City: CityA%nDestination City: CityB%nDeparture Time: 10:00%nDestination Day: 10/10/2001%nBase Ticket Price: 100.00");
        String actual = train.getFormattedInfo();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetAvailableSeats() {
        Train train = new Train("123", "CityA", "CityB", "10:00", "10/10/2001", 50, 100.0);

        assertEquals(50, train.getAvailableSeats());

        train.setAvailableSeats(40);

        assertEquals(40, train.getAvailableSeats());
    }

}
