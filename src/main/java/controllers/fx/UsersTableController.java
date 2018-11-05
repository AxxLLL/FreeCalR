package controllers.fx;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import controllers.ControllerManager;
import init.StartFX;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import model.calendar.CalendarManager;
import model.community.users.User;
import model.community.users.UsersManager;
import model.export.ExportToWord;
import view.utils.FXMLPath;
import view.utils.FXMLUtils;
import view.utils.SimpleAlert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UsersTableController {

    @FXML private TableView<User> dataTable;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> groupColumn;
    @FXML private TableColumn<User, CheckBox> saveColumn;
    @FXML private TableColumn moveItemColumn;
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
        initializer.initializeMoveItemFactory();
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

    @FXML
    private void selectAllGroupsButton() {
        int selected = 0;
        List<User> usersList = StartFX.getUsersManager().getList();
        for(User user : usersList) {
            selected = (user.isUserSaveToListEnabled()) ? selected + 1 : selected - 1;
        }
        for(User user : usersList) {
            user.getUserSaveToListCheckBoxElement().setSelected(!(selected > 0));
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
        fileChooser.setInitialDirectory(findUserSaveFileDir());
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
                Alert alert = SimpleAlert.showInfo("Zapis do pliku", "Trwa zapisywanie danych do pliku" + System.lineSeparator() + "Może to potrwać parę chwil!");
                try {
                    ExportToWord word = ExportToWord.of(listOfUsers, calendarManager);
                    word.exportDataToFile(fileToSave.toPath());
                    alert.close();
                    SimpleAlert.showInfo("Zapis do pliku", "Zapisano listę!");
                } catch (IOException e) {
                    alert.close();
                    e.printStackTrace();
                }
            }
        }
    }

    private File findUserSaveFileDir() {
        String home = System.getProperty("user.home");
        if(Files.exists(Paths.get(home, "/Pulpit"))) return Paths.get(home, "/Pulpit").toFile();
        else if(Files.exists(Paths.get(home, "/Desktop"))) return Paths.get(home, "/Desktop").toFile();
        else return new File(home);
    }

    private class Initializer {
        private void initializeTable() {
            nameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
            nameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
            saveColumn.setStyle("-fx-alignment: CENTER;");
            moveItemColumn.setStyle("-fx-alignment: CENTER;");

            PropertyValueFactory<User, String> userNameProperty = new PropertyValueFactory<>("fullName");
            PropertyValueFactory<User, String> groupNameProperty = new PropertyValueFactory<>("groupName");
            PropertyValueFactory<User, CheckBox> saveUserListProperty = new PropertyValueFactory<>("userSaveToListCheckBoxElement");

            nameColumn.setCellValueFactory(userNameProperty);
            groupColumn.setCellValueFactory(groupNameProperty);
            saveColumn.setCellValueFactory(saveUserListProperty);
            moveItemColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
            moveItemColumn.setCellFactory(initializeMoveItemFactory());

            saveColumn.setSortable(false);
            moveItemColumn.setSortable(false);
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

        private Callback<TableColumn<User, String>, TableCell<User, String>> initializeMoveItemFactory() {
            return new Callback<TableColumn<User, String>, TableCell<User, String>>() {
                @Override
                public TableCell call(final TableColumn<User, String> param) {
                    final TableCell<User, String> cell = new TableCell<User, String>() {

                        final Button btnUp = new Button("▲");
                        final Button btnDown = new Button("▼");

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            btnUp.setStyle("-fx-background-color: null; -fx-border-size: 1px; -fx-border-color: EEEEEE;");
                            btnDown.setStyle("-fx-background-color: null; -fx-border-size: 1px; -fx-border-color: EEEEEE;");
                            btnUp.setCursor(Cursor.HAND);
                            btnDown.setCursor(Cursor.HAND);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                HBox hbox = new HBox(btnUp, btnDown);
                                UsersManager usersManager = StartFX.getUsersManager();
                                btnUp.setOnAction(event -> {
                                    User user = getTableView().getItems().get(getIndex());
                                    usersManager.upInList(user);
                                    refreshTable();
                                });
                                btnDown.setOnAction(event -> {
                                    User user = getTableView().getItems().get(getIndex());
                                    usersManager.downInList(user);
                                    refreshTable();
                                });
                                setGraphic(hbox);
                                setText(null);
                            }
                        }
                    };
                    return cell;
                }
            };
        }
    }
}