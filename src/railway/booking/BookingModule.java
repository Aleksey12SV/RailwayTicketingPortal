package railway.booking;

import railway.reservation.Reservation;
import railway.train.MockedDatabase;
import railway.train.Train;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class BookingModule {
    private static final double CHILD_DISCOUNT_PERCENTAGE = 0.5;
    private static final double SENIOR_DISCOUNT_PERCENTAGE = 0.66;
    private static final double FAMILY_CARD_DISCOUNT_PERCENTAGE = 0.9;
    private static final double NON_RUSH_HOUR_DISCOUNT_PERCENTAGE = 0.95;
    private List<Train> availableTrains;
    private List<Reservation> reservations;

    public BookingModule() {
        availableTrains = MockedDatabase.getAllTrains();
        reservations = new ArrayList<>();
    }

    public List<Train> searchTrains(String startCity, String destination, String day, String time) {
        List<Train> matchingTrains = new ArrayList<>();
        for (Train train : availableTrains) {
            if (matchesSearchCriteria(train, startCity, destination, day, time)) {
                matchingTrains.add(train);
            }
        }
        return matchingTrains;
    }

    private boolean matchesSearchCriteria(Train train, String startCity, String destination, String day, String time) {
        return train.getDestination().equalsIgnoreCase(destination) &&
                train.getStartingCity().equalsIgnoreCase(startCity) &&
                train.getDepartureTime().equalsIgnoreCase(time) &&
                train.getDepartureDay().equalsIgnoreCase(day);
    }

    public double calculateTicketPrice(Train train, int numberOfPassengers, boolean hasSeniorDiscount, boolean hasChild, boolean hasFamilyCard) {
        double basePrice = train.getBaseTicketPrice() * numberOfPassengers;
        boolean isRushHour = isRushHour(train);

        if (hasFamilyCard && hasChild) {
            return basePrice * CHILD_DISCOUNT_PERCENTAGE;
        } else if (hasSeniorDiscount) {
            return basePrice * SENIOR_DISCOUNT_PERCENTAGE;
        } else if (hasFamilyCard) {
            return basePrice * FAMILY_CARD_DISCOUNT_PERCENTAGE;
        } else if (!isRushHour) {
            return basePrice * NON_RUSH_HOUR_DISCOUNT_PERCENTAGE;
        } else {
            return basePrice;
        }
    }

    boolean isRushHour(Train train) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            Date departureTime = timeFormat.parse(train.getDepartureTime());
            int hour = departureTime.getHours();
            int minutes = departureTime.getMinutes();
            return ((hour <= 9 && minutes <= 30) || ((hour >= 16 && minutes >= 0) && (hour <= 19 && minutes <= 30)));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean bookTicket(Train train, int numberOfPassengers, boolean isRoundTrip, boolean hasSeniorDiscount, boolean hasChild, boolean hasFamilyCard) {
        if (train.getAvailableSeats() >= numberOfPassengers) {
            double totalCost = calculateTicketPrice(train, numberOfPassengers, hasSeniorDiscount, hasChild, hasFamilyCard);
            if (isRoundTrip) totalCost *= 2;

            Reservation reservation = new Reservation(train, numberOfPassengers, totalCost);
            reservations.add(reservation);

            train.setAvailableSeats(train.getAvailableSeats() - numberOfPassengers);

            String successMessage = String.format("Booking successful! Total cost: $%.2f", totalCost);
            showMessageDialog("Success", successMessage, JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            showMessageDialog("Error", "Not enough available seats for booking.", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void showMessageDialog(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}
