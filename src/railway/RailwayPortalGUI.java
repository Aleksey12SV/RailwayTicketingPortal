package railway;

import railway.booking.BookingModule;
import railway.reservation.Reservation;
import railway.train.Train;
import railway.train.gui.TrainCellRenderer;
import railway.utils.HelperGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.util.List;

public class RailwayPortalGUI {
    private static final int DOUBLE_CLICK_COUNT = 2;
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
    private JList<Train> trainList;
    private DefaultListModel<Train> trainListModel;
    private Train selectedTrain;
    private JButton viewBookingsButton;

    void configureListeners(BookingModule bookingSystem) {
        passengersField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateInput();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateInput();
            }
        });
        trainList.addListSelectionListener(e -> {
            // Update the selectedTrain when the selection changes
            if (!e.getValueIsAdjusting()) {
                selectedTrain = trainList.getSelectedValue();
                updateBookButtonState();
            }
        });
        trainList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == DOUBLE_CLICK_COUNT) { // Check for double-click
                    int numberOfPassengers = Integer.parseInt(passengersField.getText());
                    boolean hasChild = withChildCheckBox.isSelected();
                    boolean hasSeniorDiscount = seniorDiscountCheckBox.isSelected();
                    boolean hasFamilyCard = familyCardCheckBox.isSelected();
                    double price = bookingSystem.calculateTicketPrice(selectedTrain, numberOfPassengers, hasSeniorDiscount, hasChild, hasFamilyCard);
                    JOptionPane.showMessageDialog(frame, selectedTrain.getFormattedInfo() + String.format("%nDynamic Price: %.2f", price));
                }
            }
        });

        addCityButton.addActionListener(e -> {
            int newIndex = multiCityPanel.getComponentCount();
            JPanel newCityRow = createCityRow(newIndex);
            multiCityPanel.add(newCityRow);
            multiCityPanel.revalidate();
            multiCityPanel.repaint();
        });

        viewBookingsButton.addActionListener(new ActionListener() {
            private void viewBookings(BookingModule bookingSystem) {
                List<Reservation> userReservations = bookingSystem.getReservations();

                if (userReservations.isEmpty()) {
                    String noBookingsMessage = "You have no bookings.";
                    JOptionPane.showMessageDialog(frame, noBookingsMessage, "No Bookings", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    StringBuilder bookingsMessage = new StringBuilder("Your Bookings:\n");
                    for (Reservation reservation : userReservations) {
                        bookingsMessage.append(reservation.getReservationInfo());
                    }

                    JOptionPane.showMessageDialog(frame, bookingsMessage.toString(), "Your Bookings", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                viewBookings(bookingSystem);
            }
        });

        multiCityCheckBox.addItemListener(e -> {
            boolean isEnabled = e.getStateChange() == ItemEvent.SELECTED;
            addCityButton.setEnabled(isEnabled);
        });

        searchButton.addActionListener(e -> {
            String destination = "";
            String startCity = "";
            String time = "";
            String day="";

            if (!multiCityCheckBox.isSelected()) {
                JPanel currentCityRow = (JPanel) multiCityPanel.getComponent(0);
                JTextField fromField = (JTextField) currentCityRow.getComponent(1);
                JTextField toField = (JTextField) currentCityRow.getComponent(3);
                startCity = fromField.getText();
                destination = toField.getText();
            } else {
                // Handle multi-city scenario if needed
            }

            try {
                MaskFormatter dateFormatter = (MaskFormatter) dateField.getFormatter();
                day = dateFormatter.valueToString(dateField.getValue());
                MaskFormatter timeFormatter = (MaskFormatter) timeField.getFormatter();
                time = timeFormatter.valueToString(timeField.getValue());

            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            List<Train> matchingTrains = bookingSystem.searchTrains(startCity, destination,day, time);

            trainListModel.clear();
            if (matchingTrains.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No matching trains found.");
            } else {
                for (Train train : matchingTrains) {
                    trainListModel.addElement(train);
                }
            }
        });

        bookButton.addActionListener(e -> {
            int numberOfPassengers = Integer.parseInt(passengersField.getText());
            boolean isRoundTrip = roundTripRadioButton.isSelected();
            boolean hasChild = withChildCheckBox.isSelected();
            boolean hasSeniorDiscount = seniorDiscountCheckBox.isSelected();
            boolean hasFamilyCard = familyCardCheckBox.isSelected();
            if(numberOfPassengers <= 0) {JOptionPane.showMessageDialog(frame, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE); return;}

            boolean bookingResult = bookingSystem.bookTicket(selectedTrain, numberOfPassengers, isRoundTrip, hasSeniorDiscount, hasChild, hasFamilyCard);
            if (bookingResult) {
                int selectedIndex = trainList.getSelectedIndex();
                trainListModel.setElementAt(selectedTrain, selectedIndex);

                String message = "Available seats updated.";
                JOptionPane.showMessageDialog(frame, message);
            } else {
                String errorMessage = "Booking failed. Please try again or choose another train.";
                JOptionPane.showMessageDialog(frame, errorMessage, "Booking Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private void addComponentsToFrame() {
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

        frame.add(new JScrollPane(trainList));
        frame.add(bookButton);
        frame.add(viewBookingsButton);
    }
    private JPanel createCityRow(int index) {
        JPanel cityRow = new JPanel();
        cityRow.setName("CityRow" + index);
        Component[] x = frame.getContentPane().getComponents();
        JPanel previousCityRow = null;
        int previousIndex = index-1;
        for(Component component : x){
            if (component instanceof JPanel panel) {
                previousCityRow = (JPanel) panel.getComponent(previousIndex);
                break;
            }
        }
        JTextField previousToField = null;
        if (previousCityRow != null) {
            Component[] components = previousCityRow.getComponents();
            for (Component component : components) {
                if (component instanceof JTextField && component.getName() != null && component.getName().startsWith("toField")) {
                    previousToField = (JTextField) component;
                    break;
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
    private void createMultiCityPanel(){
        JPanel cityRow = createCityRow(0);
        multiCityPanel.setLayout(new BoxLayout(multiCityPanel, BoxLayout.Y_AXIS));
        multiCityPanel.add(cityRow);
    }
    private void createComponents() {
        multiCityCheckBox = new JCheckBox("Multi-City");
        multiCityPanel = new JPanel();
        addCityButton = new JButton("Add City");
        dateField = new JFormattedTextField(HelperGUI.createDateFormatter());
        oneWayRadioButton = new JRadioButton("One Way");
        oneWayRadioButton.setSelected(true);
        roundTripRadioButton = new JRadioButton("Round Trip");
        roundTripRadioButton.setSelected(false);
        familyCardCheckBox = new JCheckBox("Family Card");
        withChildCheckBox = new JCheckBox("With Child");
        seniorDiscountCheckBox = new JCheckBox("Senior Discount");
        searchButton = new JButton("Search Trains");
        bookButton = new JButton("Book Ticket");
        trainListModel = new DefaultListModel<>();
        trainList = new JList<>(trainListModel);
        trainList.setCellRenderer(new TrainCellRenderer());
        timeField = new JFormattedTextField(HelperGUI.createTimeFormatter());
        passengersField = new JTextField(5);
        viewBookingsButton = new JButton("View Bookings");
        ButtonGroup ticketTypeGroup = new ButtonGroup();

        ticketTypeGroup.add(oneWayRadioButton);
        ticketTypeGroup.add(roundTripRadioButton);

        addCityButton.setEnabled(false);
        bookButton.setEnabled(false);
        createMultiCityPanel();
    }
    private void initializeGUI() {
        frame = new JFrame("Railway Ticketing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new FlowLayout());

        createComponents();
        addComponentsToFrame();

        frame.setVisible(true);
    }

    private void validateInput() {
        try {
            int numberOfPassengers = Integer.parseInt(passengersField.getText());
            if (numberOfPassengers <= 0) {
                JOptionPane.showMessageDialog(frame, "Number of passengers must be a non-negative integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                passengersField.setText("1");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            passengersField.setText("1");
        }
    }

    private void updateBookButtonState() {
        bookButton.setEnabled(trainList.getSelectedValue() != null);
    }

    public RailwayPortalGUI() {
        BookingModule bookingSystem = new BookingModule();
        initializeGUI();
        configureListeners(bookingSystem);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RailwayPortalGUI::new);
    }
}
