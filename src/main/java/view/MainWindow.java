package view;

import com.google.common.base.Preconditions;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.utils.FXMLPath;
import view.utils.FXMLUtils;

import java.io.IOException;

public class MainWindow {
    private Scene mainWindowScene;

    public static MainWindow start(Stage stage) {
        return new MainWindow(stage);
    }

    private MainWindow(Stage primaryStage) {
        Preconditions.checkNotNull(primaryStage, "Parametry startowe okna programu muszą być określone");
        try {
            mainWindowScene = new Scene(FXMLUtils.loadFXML(FXMLPath.MAIN_WINDOW));
            primaryStage.setScene(mainWindowScene);
            primaryStage.initStyle(StageStyle.UNIFIED);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Free Calendar");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Błąd: Wystąpił błąd przy próbie uruchomienia programu!");
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        return this.mainWindowScene;
    }

}
