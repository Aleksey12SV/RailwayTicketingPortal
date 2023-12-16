package railway.booking;

import org.junit.Before;
import org.junit.Test;
import railway.train.Train;

import java.util.List;

import static org.junit.Assert.*;

public class BookingModuleTest {
    private BookingModule bookingModule;
    private static final double CHILD_DISCOUNT_PERCENTAGE = 0.5;
    private static final double SENIOR_DISCOUNT_PERCENTAGE = 0.66;
    private static final double FAMILY_CARD_DISCOUNT_PERCENTAGE = 0.9;
    private static final double NON_RUSH_HOUR_DISCOUNT_PERCENTAGE = 0.95;

    @Before
    public void setUp() {
        bookingModule = new BookingModule();
    }

    @Test
    public void searchTrains_WithValidInput_ShouldReturnMatchingTrains() {
        List<Train> matchingTrains = bookingModule.searchTrains("City1", "City3", "18/12/2023", "09:30");
        assertEquals(6, matchingTrains.size());
    }
    @Test
    public void calculateTicketPrice_WithSeniorDiscount_ShouldApplyDiscount() {
        Train train = new Train("123", "CityA", "CityB", "10:00", "10/12/2023", 50, 100);
        int numberOfPassengers = 2;
        double price = bookingModule.calculateTicketPrice(train, numberOfPassengers, true, false, false);
        assertEquals(train.getBaseTicketPrice()*numberOfPassengers*SENIOR_DISCOUNT_PERCENTAGE, price, 0.01);
    }

    @Test
    public void calculateTicketPrice_WithFamilyCardAndChild_ShouldApplyChildDiscount() {
        Train train = new Train("123", "CityA", "CityB", "15:00", "13/05/2010", 50, 100);
        int numberOfPassengers = 2;
        double price = bookingModule.calculateTicketPrice(train, numberOfPassengers, false, true, true);
        assertEquals(train.getBaseTicketPrice()*numberOfPassengers*CHILD_DISCOUNT_PERCENTAGE, price, 0.01);
    }
    @Test
    public void calculateTicketPrice_WithFamilyCardAndSeniorDiscount_ShouldApplySeniorDiscount() {
        Train train = new Train("123", "CityA", "CityB", "15:00", "13/05/2010", 50, 100);
        double price = bookingModule.calculateTicketPrice(train, 1, true, false, true);
        assertEquals(train.getBaseTicketPrice()*SENIOR_DISCOUNT_PERCENTAGE, price, 0.01);
    }

    @Test
    public void calculateTicketPrice_WithFamilyCardAndRushHour_ShouldApplyFamilyCardDiscount() {
        Train train = new Train("123", "CityA", "CityB", "18:30", "13/05/2010", 50, 100);
        double price = bookingModule.calculateTicketPrice(train, 1, false, false, true);
        assertEquals(train.getBaseTicketPrice()*FAMILY_CARD_DISCOUNT_PERCENTAGE, price, 0.01);
    }

    @Test
    public void calculateTicketPrice_WithNoDiscount_ShouldApplyNonRushHourDiscount() {
        Train train = new Train("123", "CityA", "CityB", "15:30", "13/05/2010", 50, 100);
        double price = bookingModule.calculateTicketPrice(train, 1, false, false, false);
        assertEquals(train.getBaseTicketPrice()*NON_RUSH_HOUR_DISCOUNT_PERCENTAGE, price, 0.01);
    }

    @Test
    public void calculateTicketPrice_WithRushHour_ShouldNotApplyAnyDiscount() {
        Train train = new Train("123", "CityA", "CityB", "18:30", "13/05/2010", 50, 100);
        double price = bookingModule.calculateTicketPrice(train, 1, false, false, false);
        assertEquals(train.getBaseTicketPrice(), price, 0.01);
    }

    @Test
    public void isRushHour_WithRushHourTime_ShouldReturnTrue() {
        Train train = new Train("123", "CityA", "CityB", "09:00", "13/05/2010", 50, 100);
        boolean isRushHour = bookingModule.isRushHour(train);
        assertTrue(isRushHour);
    }

    @Test
    public void isRushHour_WithNonRushHourTime_ShouldReturnFalse() {
        Train train = new Train("123", "CityA", "CityB", "15:00", "13/05/2010", 50, 100);
        boolean isRushHour = bookingModule.isRushHour(train);
        assertFalse(isRushHour);
    }


}
