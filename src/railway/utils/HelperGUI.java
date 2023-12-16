package railway.utils;

import javax.swing.text.MaskFormatter;

public class HelperGUI {
    public static MaskFormatter createDateFormatter() {
        MaskFormatter dateFormatter = null;
        try {
            dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return dateFormatter;
    }

    public static MaskFormatter createTimeFormatter() {
        MaskFormatter timeFormatter = null;
        try {
            timeFormatter = new MaskFormatter("##:##");
            timeFormatter.setPlaceholderCharacter('_');
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return timeFormatter;
    }
}
