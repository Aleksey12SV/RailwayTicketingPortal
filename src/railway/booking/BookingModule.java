package railway.booking;

import railway.reservation.Reservation;
import railway.train.MockedDatabase;
import railway.train.Train;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class BookingModule {
    private List<Train> availableTrains;
    private List<Reservation> reservations;

    private static boolean isRushHour(Date departureTime) {
        int hour = departureTime.getHours();
        int minutes = departureTime.getMinutes();
        return ((hour <= 9 && minutes <= 30) || ((hour >= 16 && minutes >= 0) && (hour <= 19 && minutes <= 30)));
    }

    public BookingModule() {
        availableTrains = MockedDatabase.getAllTrains();
        reservations = new ArrayList<>();
    }

    public List<Train> searchTrains(String startCity, String destination, String day, String time) {
        List<Train> matchingTrains = new ArrayList<>();
        for (Train train : availableTrains) {
            if (train.getDestination().equalsIgnoreCase(destination) && train.getStartingCity().equalsIgnoreCase(startCity) && train.getDepartureTime().equalsIgnoreCase(time) && train.getDepartureDay().equalsIgnoreCase(day)) {
                matchingTrains.add(train);
            }
        }
        return matchingTrains;
    }

    public double calculateTicketPrice(Train train, int numberOfPassengers, boolean hasSeniorDiscount, boolean hasChild, boolean hasFamilyCard) {
        double basePrice = train.getBaseTicketPrice() * numberOfPassengers;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        boolean isRushHour = false;
        try {
            isRushHour = isRushHour(timeFormat.parse(train.getDepartureTime()));
        }catch (ParseException e) {
            e.printStackTrace();
        }


        if(hasFamilyCard && hasChild) {
            return basePrice * 0.5;
        } else if (hasSeniorDiscount) {
            return basePrice * 0.66; // 34% discount for seniors
        } else if (hasFamilyCard) {
            return basePrice * 0.9;
        } else if(!isRushHour){
            return basePrice * 0.95; // 5% discount for non-rush hour travel
        } else{
            return basePrice;
        }

    }

    public boolean bookTicket(Train train, int numberOfPassengers, boolean isRoundTrip, boolean hasSeniorDiscount, boolean hasChild, boolean hasFamilyCard) {
        if (train.getAvailableSeats() >= numberOfPassengers) {
            double totalCost = calculateTicketPrice(train, numberOfPassengers, hasSeniorDiscount, hasChild, hasFamilyCard);
            if(isRoundTrip) totalCost = totalCost * 2;

            // Create a reservation and add it to the list
            Reservation reservation = new Reservation(train, numberOfPassengers, totalCost);
            reservations.add(reservation);

            // Update available seats on the train
            train.setAvailableSeats(train.getAvailableSeats() - numberOfPassengers);

            System.out.println("Booking successful! Total cost: $" + totalCost);
            return true;
        } else {
            System.out.println("Not enough available seats for booking.");
            return false;
        }
    }
    public List<Reservation> getReservations(){
        return reservations;
    }
    public void displayReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations yet.");
        } else {
            System.out.println("Your Reservations:");
            for (Reservation reservation : reservations) {
                reservation.displayReservationInfo();
            }
        }
    }
}
