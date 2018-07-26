package controllers.fx;

import controllers.ControllerManager;
import javafx.fxml.FXML;
import view.utils.SimpleAlert;

public class MainWindowController {

    public MainWindowController() {
        ControllerManager.add(this);
    }

    @FXML
    private void onAboutMenuItemClick() {
        SimpleAlert.showInfo("O programie",
                "Nazwa: FreeCalR"+System.lineSeparator()+
                        "Wersja: 2.0.1 (26.07.2018)"+System.lineSeparator()+
                        "Autor: Rafał (AxL) Żółtański");
    }

}
