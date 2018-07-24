package init;

import controllers.CalendarController;
import controllers.UsersTableController;
import init.other.FreeDaysInPoland;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.calendar.CalendarManager;
import model.calendar.freedays.FreeDaysManager;
import model.community.groups.GroupManager;
import model.community.users.UsersManager;
import model.data.JSonLoader;
import model.data.JSonSaver;
import view.MainWindow;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StartFX extends Application {

    private static MainWindow mainWindow;
    private static final FreeDaysManager freeDaysManager = new FreeDaysManager();
    private static CalendarManager calendarManager;
    private static UsersManager usersManager = UsersManager.of();
    private static GroupManager groupManager = GroupManager.of(usersManager);
    private static Path pathToDataFile = Paths.get("zfileTest", "saver.json");

    public static void main(String[] args) {
        pathToDataFile = getPathBySystem();

        FreeDaysInPoland.addStaticFreeDaysInPolandToManager(freeDaysManager);
        FreeDaysInPoland.addMovableFreeDaysInPolandToManager(freeDaysManager);
        calendarManager = CalendarManager.of(freeDaysManager);
        try {
            //new JSonLoader(Paths.get("zfileTest", "validData.json"), usersManager, groupManager).load();
            new JSonLoader(pathToDataFile, usersManager, groupManager).load();
        } catch (NoSuchFileException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mainWindow = MainWindow.start(primaryStage);
        CalendarController.getController().initializeCalendar();
        UsersTableController.getController().refreshTable();
    }

    @Override
    public void stop() {
        try{
            new JSonSaver(pathToDataFile, usersManager, groupManager).save();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static Scene getScene() {
        return mainWindow.getScene();
    }

    public static FreeDaysManager getFreeDaysManager() {
        return freeDaysManager;
    }

    public static CalendarManager getCalendarManager() {
        return calendarManager;
    }

    public static GroupManager getGroupManager() {
        return groupManager;
    }

    public static UsersManager getUsersManager() { return usersManager; }

    private static Path getPathBySystem() {
        String path;
        if(System.getProperty("os.name").startsWith("Windows")) {
            if(System.getProperty("os.name").contains("xp")) {
                path = (System.getProperty("user.home") + "/Application Data/FreeCallR").replace("\\", "/");
            } else {
                path = (System.getProperty("user.home") + "/AppData/Local/FreeCallR").replace("\\", "/");
            }


        } else {
            path = System.getProperty("user.home") + System.lineSeparator() + "FreeCallR";
        }
        return Paths.get(path, "data.json");
    }
}