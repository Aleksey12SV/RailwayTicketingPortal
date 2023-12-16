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
        this.reservationDate = new Date();
    }

    public String getReservationInfo() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDate = dateFormat.format(reservationDate);

        return String.format(
                "Train Number: %s\nDestination: %s\nDeparture Time: %s\nNumber of Passengers: %d\nTotal Cost: $%.2f\nReservation Date: %s\n\n",
                train.getTrainNumber(), train.getDestination(), train.getDepartureTime(),
                numberOfPassengers, totalCost, formattedDate);
    }

}
