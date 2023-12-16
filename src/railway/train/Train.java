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

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void displayTrainInfo() {
        System.out.println("Train Number: " + trainNumber);
        System.out.println("Starting City: " + startingCity);
        System.out.println("Destination: " + destination);
        System.out.println("Departure Time: " + departureTime);
        System.out.println("Available Seats: " + availableSeats);
        System.out.println("Base Ticket Price: $" + baseTicketPrice);
    }

    public void bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
            System.out.println("Seat booked successfully.");
        } else {
            System.out.println("No available seats for booking.");
        }
    }
}