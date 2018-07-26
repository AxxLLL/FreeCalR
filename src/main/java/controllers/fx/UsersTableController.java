package controllers.fx;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import controllers.ControllerManager;
import init.StartFX;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import model.calendar.CalendarManager;
import model.community.users.User;
import model.export.ExportToWord;
import view.utils.FXMLPath;
import view.utils.FXMLUtils;
import view.utils.SimpleAlert;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UsersTableController {

    @FXML private TableView<User> dataTable;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> groupColumn;
    @FXML private TableColumn<User, CheckBox> saveColumn;
    private Initializer initializer = new Initializer();

    public UsersTableController() {
        ControllerManager.add(this);
    }

    TableView getTableElement() {
        return dataTable;
    }

    @FXML
    private void initialize() {
        initializer.initializeTable();
        initializer.initializeRowRightClickMenu();
    }

    @FXML
    private void saveButtonPressed() {
        List<User> usersToSave = StartFX.getUsersManager().getList();
        usersToSave.removeIf(user -> !user.isUserSaveToListEnabled());
        printList(usersToSave);
    }

    @FXML
    private void usersButtonPressed() {
        try {
            FXMLUtils.loadFXMLAsModal(StartFX.getScene().getWindow(), FXMLPath.USER_ADD_WINDOW, "Dodaj użytkownika");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void groupsButtonPressed() {
        try {
            FXMLUtils.loadFXMLAsModal(StartFX.getScene().getWindow(), FXMLPath.EDIT_GROUP_WINDOW, "Dodaj grupę");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        ObservableList<User> usersObservableList = FXCollections.observableList(StartFX.getUsersManager().getList());
        dataTable.setItems(usersObservableList);
        dataTable.refresh();
    }

    private File showSelectFileWindow(CalendarManager calendarManager, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word 2007", "*.docx"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.setTitle("Wybierz plik do zapisu listy");
        if(title == null) fileChooser.setInitialFileName(String.format("%s %d.docx", StartFX.monthName.getName(calendarManager.getMonth()), calendarManager.getYear()));
        else fileChooser.setInitialFileName(String.format("%s - %s %d.docx", title, StartFX.monthName.getName(calendarManager.getMonth()), calendarManager.getYear()));
        return fileChooser.showSaveDialog(StartFX.getScene().getWindow());
    }

    void printList(List<User> listOfUsers) {
        if(listOfUsers.isEmpty()) {
            SimpleAlert.showError("Brak użytkowników","Lista użytkowników, dla których chcesz wydrukować listę, jest pusta.");
        } else {
            CalendarManager calendarManager = StartFX.getCalendarManager();
            File fileToSave = showSelectFileWindow(calendarManager, listOfUsers.size() == 1 ? listOfUsers.get(0).getFullName() : null);
            if (fileToSave != null) {
                Alert alert = SimpleAlert.showInfo("Zapis do pliku", "Trwa zapisywanie danych do pliku" + System.lineSeparator() + "Może to potrwać parę chwil!", false);
                alert.show();
                try {
                    ExportToWord word = ExportToWord.of(listOfUsers, calendarManager);
                    word.exportDataToFile(fileToSave.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    alert.close();
                }
            }
        }
    }

    private class Initializer {
        private void initializeTable() {
            saveColumn.setStyle("-fx-alignment: center;");
            groupColumn.setStyle("-fx-alignment: center;");

            PropertyValueFactory<User, String> userNameProperty = new PropertyValueFactory<>("fullName");
            PropertyValueFactory<User, String> groupNameProperty = new PropertyValueFactory<>("groupName");
            PropertyValueFactory<User, CheckBox> saveUserListProperty = new PropertyValueFactory<>("userSaveToListCheckBoxElement");

            nameColumn.setCellValueFactory(userNameProperty);
            groupColumn.setCellValueFactory(groupNameProperty);
            saveColumn.setCellValueFactory(saveUserListProperty);

            saveColumn.setSortable(false);

        }

        private void initializeRowRightClickMenu() {
            dataTable.setRowFactory(tableView -> {
                final TableRow<User> row = new TableRow<>();
                final ContextMenu rowMenu = new UsersTableContextMenu((UsersTableController)ControllerManager.get(UsersTableController.class)).getContextMenu();

                row.contextMenuProperty().bind(
                        Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                .then(rowMenu)
                                .otherwise((ContextMenu) null));

                row.setOnMouseClicked(event -> {
                    if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
                        try {
                            FXMLUtils.loadFXMLAsModal(StartFX.getScene().getWindow(), FXMLPath.EDIT_USER_WINDOW, "Edytuj dane użytkownika");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                return row;
            });
        }
    }
}
