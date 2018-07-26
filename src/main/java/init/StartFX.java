package init;

import controllers.fx.CalendarController;
import controllers.ControllerManager;
import controllers.fx.UsersTableController;
import init.other.FreeDaysInPoland;
import init.other.SavePathGenerator;
import init.other.monthsnames.MonthName;
import init.other.monthsnames.MonthNames_PL;
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
    private static Path pathToDataFile;
    public static MonthName monthName = new MonthNames_PL();

    public static void main(String[] args) {
        pathToDataFile = Paths.get(SavePathGenerator.getPath().toString(), "data.json");

        FreeDaysInPoland.addStaticFreeDaysInPolandToManager(freeDaysManager);
        FreeDaysInPoland.addMovableFreeDaysInPolandToManager(freeDaysManager);
        calendarManager = CalendarManager.of(freeDaysManager);
        try {
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
        ((CalendarController) ControllerManager.get(CalendarController.class)).initializeCalendar();
        ((UsersTableController) ControllerManager.get(UsersTableController.class)).refreshTable();
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

}