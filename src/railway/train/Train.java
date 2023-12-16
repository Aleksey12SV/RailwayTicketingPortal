package railway.train;

public class Train {
    private String trainNumber;
    private String startingCity;
    private String destination;
    private String departureTime;
    private String departureDay;
    private int availableSeats;
    private double baseTicketPrice;

    public Train(String trainNumber, String startingCity, String destination, String departureTime, String departureDay, int availableSeats, double baseTicketPrice) {
        this.trainNumber = trainNumber;
        this.startingCity = startingCity;
        this.destination = destination;
        this.departureDay = departureDay;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
        this.baseTicketPrice = baseTicketPrice;
    }

    public String getTrainNumber() {
        return trainNumber;
    }
    public String getStartingCity() {
        return startingCity;
    }
    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }
    public String getDepartureDay() {
        return departureDay;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getBaseTicketPrice() {
        return baseTicketPrice;
    }

    public String getFormattedInfo() {
        return String.format("Train Number: %s%nStarting City: %s%nDestination City: %s%nDeparture Time: %s%nDestination Day: %s%nBase Ticket Price: %.2f",
                getTrainNumber(), getStartingCity(), getDestination(), getDepartureTime(), getDepartureDay(), getBaseTicketPrice());
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}