package railway.train.gui;

import railway.train.Train;

import javax.swing.*;
import java.awt.*;

public class TrainCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Train train = (Train) value;
        String trainDetails = String.format("%s - Seats: %d - Base Price: $%.2f",
                train.getTrainNumber(), train.getAvailableSeats(), train.getBaseTicketPrice());
        return super.getListCellRendererComponent(list, trainDetails, index, isSelected, cellHasFocus);
    }
}