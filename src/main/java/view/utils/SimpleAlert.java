package view.utils;

import javafx.scene.control.Alert;

public class SimpleAlert {
    public static Alert showError(String title, String message) {
        return show(Alert.AlertType.ERROR, title, message, true);
    }

    public static Alert showInfo(String title, String message) {
        return show(Alert.AlertType.INFORMATION, title, message, true);
    }

    public static Alert showInfo(String title, String message, boolean show) {
        return show(Alert.AlertType.INFORMATION, title, message, show);
    }

    private static Alert show(Alert.AlertType type, String title, String message, boolean show) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setGraphic(null);
        alert.setHeaderText(null);
        if(show) alert.show();
        return alert;
    }

}
