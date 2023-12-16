package railway;

import railway.booking.BookingModule;
import railway.reservation.Reservation;
import railway.train.Train;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;
import java.util.List;

public class RailwayPortalGUI {
    private JFrame frame;
    private JCheckBox multiCityCheckBox;
    private JPanel multiCityPanel;
    private JButton addCityButton;
    private JFormattedTextField dateField;
    private JFormattedTextField timeField;
    private JRadioButton oneWayRadioButton;
    private JRadioButton roundTripRadioButton;
    private JCheckBox familyCardCheckBox;
    private JCheckBox withChildCheckBox;
    private JCheckBox seniorDiscountCheckBox;
    private JButton searchButton;
    private JButton bookButton;
    private JTextField passengersField;
    private JTextField previousToField;
    private JList<Train> trainList;
    private DefaultListModel<Train> trainListModel;
    private JButton selectTrainButton;
    private Train selectedTrain;
    private JButton viewBookingsButton;

    private MaskFormatter createTimeFormatter() {
        MaskFormatter timeFormatter = null;
        try {
            timeFormatter = new MaskFormatter("##:##");
            timeFormatter.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeFormatter;
    }
    private void viewBookings(BookingModule bookingSystem) {
        // Retrieve and display the user's reservations
        List<Reservation> userReservations = bookingSystem.getReservations(); // Obtain the user's reservations from the booking system or other components

        // Check if the user has any reservations
        if (userReservations.isEmpty()) {
            String noBookingsMessage = "You have no bookings.";
            JOptionPane.showMessageDialog(frame, noBookingsMessage, "No Bookings", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Display the user's reservations in a dialog
            StringBuilder bookingsMessage = new StringBuilder("Your Bookings:\n");
            for (Reservation reservation : userReservations) {
                String reservationInfo = String.format(
                        "Train Number: %s\nDestination: %s\nDeparture Time: %s\nNumber of Passengers: %d\nTotal Cost: $%.2f\n\n",
                        reservation.getTrain().getTrainNumber(), reservation.getTrain().getDestination(),
                        reservation.getTrain().getDepartureTime(), reservation.getNumberOfPassengers(),
                        reservation.getTotalCost());
                bookingsMessage.append(reservationInfo);
            }

            JOptionPane.showMessageDialog(frame, bookingsMessage.toString(), "Your Bookings", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public RailwayPortalGUI() {
        trainListModel = new DefaultListModel<>();
        trainList = new JList<>(trainListModel);
        selectTrainButton = new JButton("Select Train");
        selectedTrain = null;
        passengersField = new JTextField(5);
        BookingModule bookingSystem = new BookingModule();

        frame = new JFrame("Railway Ticketing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new FlowLayout());

        // Create GUI components
        multiCityCheckBox = new JCheckBox("Multi-City");
        multiCityPanel = new JPanel();
        addCityButton = new JButton("Add City");
        dateField = new JFormattedTextField(createDateFormatter());
        oneWayRadioButton = new JRadioButton("One Way");
        oneWayRadioButton.setSelected(true);
        roundTripRadioButton = new JRadioButton("Round Trip");
        roundTripRadioButton.setSelected(false);
        familyCardCheckBox = new JCheckBox("Family Card");
        withChildCheckBox = new JCheckBox("With Child");
        seniorDiscountCheckBox = new JCheckBox("Senior Discount");
        searchButton = new JButton("Search Trains");
        bookButton = new JButton("Book Ticket");
        // Create a DefaultListModel for the JList
        trainListModel = new DefaultListModel<>();
        trainList = new JList<>(trainListModel);
        trainList.setCellRenderer(new TrainCellRenderer());
        timeField = new JFormattedTextField(createTimeFormatter());

        ButtonGroup ticketTypeGroup = new ButtonGroup();
        ticketTypeGroup.add(oneWayRadioButton);
        ticketTypeGroup.add(roundTripRadioButton);

        multiCityPanel.setLayout(new BoxLayout(multiCityPanel, BoxLayout.Y_AXIS));

        // Create the initial city row
        JPanel cityRow = createCityRow(0);

        // Add components to the frame
        frame.add(multiCityCheckBox);
        frame.add(multiCityPanel);
        frame.add(addCityButton);
        frame.add(new JLabel("Date: "));
        frame.add(dateField);
        frame.add(new JLabel("Time (HH:mm): "));
        frame.add(timeField);
        frame.add(new JLabel("Number of Passengers: "));
        frame.add(passengersField);
        frame.add(oneWayRadioButton);
        frame.add(roundTripRadioButton);
        frame.add(familyCardCheckBox);
        frame.add(withChildCheckBox);
        frame.add(seniorDiscountCheckBox);
        frame.add(searchButton);

        frame.add(new JScrollPane(trainList));  // Add a JScrollPane for the JList
        frame.add(selectTrainButton);
        frame.add(bookButton);
        viewBookingsButton = new JButton("View Bookings");
        frame.add(viewBookingsButton);
        addCityButton.setEnabled(false);
        bookButton.setEnabled(false);

        // Add the initial city row to the multi-city panel
        multiCityPanel.add(cityRow);

        // Initialize the previous "To" field
        previousToField = (JTextField) cityRow.getComponent(3);

        trainList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Update the selectedTrain when the selection changes
                if (!e.getValueIsAdjusting()) {
                    selectedTrain = trainList.getSelectedValue();
                }
            }
        });

        selectTrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedTrain != null) {
                    int numberOfPassengers = Integer.parseInt(passengersField.getText());
                    boolean isRoundTrip = roundTripRadioButton.isSelected(); // Check if round trip is selected
                    boolean hasChild = withChildCheckBox.isSelected();
                    boolean hasSeniorDiscount = seniorDiscountCheckBox.isSelected();
                    boolean hasFamilyCard = familyCardCheckBox.isSelected(); // Check if family card is selected
                    double price = bookingSystem.calculateTicketPrice(selectedTrain, numberOfPassengers, hasSeniorDiscount, hasChild, hasFamilyCard);
                    JOptionPane.showMessageDialog(frame, "Selected Train: " + selectedTrain.getTrainNumber() +
                            "\nStarting City: " + selectedTrain.getStartingCity() +
                            "\nDestination City: " + selectedTrain.getDestination()
                            +
                            "\nDeparture Time: " + selectedTrain.getDepartureTime()+
                            "\nDestination Day: " + selectedTrain.getDepartureDay()+
                            "\nPrice: " + price);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a train.");
                }
            }
        });

        // Action listener for the "Add City" button
        addCityButton.addActionListener(e -> {
            int newIndex = multiCityPanel.getComponentCount(); // Calculate the new index
            JPanel newCityRow = createCityRow(newIndex);
            multiCityPanel.add(newCityRow);
            multiCityPanel.revalidate();
            multiCityPanel.repaint();
        });

        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewBookings(bookingSystem);  // Call the method to view bookings
            }
        });

        // Add an ItemListener to the "Multi-City" checkbox
        multiCityCheckBox.addItemListener(e -> {
            boolean isEnabled = e.getStateChange() == ItemEvent.SELECTED;
            addCityButton.setEnabled(isEnabled); // Enable or disable the "Add City" button
        });

        searchButton.addActionListener(e -> {
            // Get user inputs from the GUI components
            String destination = ""; // Replace with actual destination input
            String startCity = "";
            String time = ""; // Replace with actual time input
            String day="";

            // Example: Extracting destination from JTextField
            if (!multiCityCheckBox.isSelected()) {
                JPanel currentCityRow = (JPanel) multiCityPanel.getComponent(0);
                JTextField fromField = (JTextField) cityRow.getComponent(1);
                JTextField toField = (JTextField) cityRow.getComponent(3);
                startCity = fromField.getText();
                destination = toField.getText();
            } else {
                // Handle multi-city scenario if needed
            }

            // Extracting time from JFormattedTextField
            try {
                MaskFormatter dateFormatter = (MaskFormatter) dateField.getFormatter();
                day = dateFormatter.valueToString(dateField.getValue());
                MaskFormatter timeFormatter = (MaskFormatter) timeField.getFormatter();
                time = timeFormatter.valueToString(timeField.getValue());

            } catch (ParseException ex) {
                ex.printStackTrace(); // Handle parsing exception
            }



            // Call the BookingSystem method to search trains
            List<Train> matchingTrains = bookingSystem.searchTrains(startCity, destination,day, time);


            trainListModel.clear(); // Clear the previous trains
            if (matchingTrains.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No matching trains found.");
            } else {
                bookButton.setEnabled(true);
                for (Train train : matchingTrains) {
                    trainListModel.addElement(train);
                }
            }
        });

        bookButton.addActionListener(e -> {
            // Get user inputs from the GUI components
            int numberOfPassengers = Integer.parseInt(passengersField.getText());
            boolean isRoundTrip = roundTripRadioButton.isSelected(); // Check if round trip is selected
            boolean hasChild = withChildCheckBox.isSelected();
            boolean hasSeniorDiscount = seniorDiscountCheckBox.isSelected();
            boolean hasFamilyCard = familyCardCheckBox.isSelected(); // Check if family card is selected


            // Call the BookingSystem method to book a ticket
            boolean bookingResult = bookingSystem.bookTicket(selectedTrain, numberOfPassengers, isRoundTrip, hasSeniorDiscount, hasChild, hasFamilyCard);
            if (bookingResult) {
                // Update the available seats in the displayed train list
                int selectedIndex = trainList.getSelectedIndex();
                trainListModel.setElementAt(selectedTrain, selectedIndex);

                String message = "Booking successful! Available seats updated.";
                JOptionPane.showMessageDialog(frame, message);
            } else {
                String errorMessage = "Booking failed. Please try again or choose another train.";
                JOptionPane.showMessageDialog(frame, errorMessage, "Booking Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
    private class TrainCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Assuming value is a Train object
            Train train = (Train) value;
            String trainDetails = String.format("%s - Seats: %d - Base Price: $%.2f",
                    train.getTrainNumber(), train.getAvailableSeats(), train.getBaseTicketPrice());
            return super.getListCellRendererComponent(list, trainDetails, index, isSelected, cellHasFocus);
        }
    }
    private MaskFormatter createDateFormatter() {
        MaskFormatter dateFormatter = null;
        try {
            dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatter;
    }

    private JPanel createCityRow(int index) {
        JPanel cityRow = new JPanel();
        cityRow.setName("CityRow" + index); // Assign a custom name to the panel
        Component[] x = frame.getContentPane().getComponents();
        JPanel previousCityRow = null;
        int previousIndex = index-1;
        for(Component component : x){
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                previousCityRow = (JPanel) panel.getComponent(index-1);
                break; // Stop searching once you've found the specific cityRow
            }
        }
        JTextField previousToField = null;
        if (previousCityRow != null) {
            // Iterate through the components in the previous cityRow
            Component[] components = previousCityRow.getComponents();
            for (Component component : components) {
                if (component instanceof JTextField && component.getName() != null && component.getName().startsWith("toField")) {
                    previousToField = (JTextField) component;
                    break; // Stop searching once you've found the previous "To" field
                }
            }
        }
        String previousText = previousToField != null ? previousToField.getText() : "";
        JTextField fromField = new JTextField(previousText, 10);
        fromField.setName("fromField" + index);
        JTextField toField = new JTextField(10);
        toField.setName("toField" + index);
        cityRow.add(new JLabel("From: "));
        cityRow.add(fromField);
        cityRow.add(new JLabel("To: "));
        cityRow.add(toField);

        return cityRow;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RailwayPortalGUI());
    }
}
