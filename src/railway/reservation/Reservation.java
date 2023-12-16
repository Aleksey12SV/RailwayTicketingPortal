package railway.reservation;

import railway.train.Train;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
    private Train train;
    private int numberOfPassengers;
    private double totalCost;
    private Date reservationDate;

    public Reservation(Train train, int numberOfPassengers, double totalCost) {
        this.train = train;
        this.numberOfPassengers = numberOfPassengers;
        this.totalCost = totalCost;
        this.reservationDate = new Date(); // Set the reservation date to the current date and time
    }

    // Getters for attributes

    public Train getTrain() {
        return train;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Date getReservationDate() {
        return reservationDate;
    }


    public void displayReservationInfo() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Reservation Date: " + dateFormat.format(reservationDate));
        System.out.println("Train Number: " + train.getTrainNumber());
        System.out.println("Destination: " + train.getDestination());
        System.out.println("Departure Time: " + train.getDepartureTime());
        System.out.println("Number of Passengers: " + numberOfPassengers);
        System.out.println("Total Cost: $" + totalCost);
    }
}
