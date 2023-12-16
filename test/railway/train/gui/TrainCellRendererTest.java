package railway.train.gui;

import org.junit.jupiter.api.Test;
import railway.train.Train;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainCellRendererTest {

    @Test
    public void testGetListCellRendererComponent() {
        Train train = new Train("TestTrain", "CityA", "CityB", "12:00", "18/12/2023", 20, 50.0);

        TrainCellRenderer renderer = new TrainCellRenderer();
        JList<?> list = new JList<>();
        TrainCellRenderer component = (TrainCellRenderer) renderer.getListCellRendererComponent(list, train, 0, false, false);

        assertNotNull(component);

        assertEquals("TestTrain - Seats: 20 - Base Price: $50.00", component.getText());
    }
}
