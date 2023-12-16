package railway.reservation;

import org.junit.Test;
import railway.train.Train;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class ReservationTest {

    @Test
    public void getReservationInfo_ShouldReturnFormattedString() {
        Train train = new Train("123", "CityA", "CityB", "10:00", "10/12/2023", 50, 100);
        Reservation reservation = new Reservation(train, 2, 200.0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDate = dateFormat.format(new Date());
        String expected = String.format(
                "Train Number: %s\nDestination: %s\nDeparture Time: %s\nNumber of Passengers: %d\nTotal Cost: $%.2f\nReservation Date: %s\n\n",
                train.getTrainNumber(), train.getDestination(), train.getDepartureTime(),
                2, 200.0, formattedDate);

        String actual = reservation.getReservationInfo();
        assertTrue(actual.contains(expected));
    }
}
