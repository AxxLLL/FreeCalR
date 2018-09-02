package view.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;

public class FXMLUtils {

    public static Pane loadFXML(FXMLPath window) throws IOException {
        FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource("/" + window.getPath()));
        return loader.load();
    }

    public static Stage loadFXMLAsModal(Window owner, FXMLPath window, String title) throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(loadFXML(window)));
        stage.setResizable(false);
        stage.setTitle(title);
        stage.show();
        return stage;
    }

}