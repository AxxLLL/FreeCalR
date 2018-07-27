package controllers.fx;

import controllers.ControllerManager;
import init.ProgramInfo;
import javafx.fxml.FXML;
import view.utils.SimpleAlert;

public class MainWindowController {

    public MainWindowController() {
        ControllerManager.add(this);
    }

    @FXML
    private void onAboutMenuItemClick() {
        SimpleAlert.showInfo("O programie",
                "Nazwa: " + ProgramInfo.PROGRAM_NAME + System.lineSeparator() +
                        "Wersja: " + ProgramInfo.VERSION + " (r." + ProgramInfo.REL_VER + ")" + System.lineSeparator() +
                        "Ost. modyfikacja: " + ProgramInfo.LAST_CHANGE_DATE + System.lineSeparator() +
                        "Autor: " + ProgramInfo.AUTHOR
        );
    }
}
