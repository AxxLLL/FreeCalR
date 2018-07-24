package controllers;

import init.StartFX;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import model.calendar.CalendarManager;
import model.community.groups.Group;
import model.community.users.User;
import model.community.users.UsersManager;
import model.export.ExportToWord;
import view.utils.FXMLPath;
import view.utils.FXMLUtils;
import view.utils.SimpleAlert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsersTableController {
    private static UsersTableController controller;
    private List<String> monthNames = new ArrayList<>(Arrays.asList("Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"));

    @FXML private TableView<User> dataTable;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> groupColumn;
    @FXML private TableColumn<User, CheckBox> saveColumn;

    public UsersTableController() {
        UsersTableController.controller = this;
    }

    public TableView getTableElement() {
        return dataTable;
    }

    public static UsersTableController getController() {
        return controller;
    }

    @FXML
    private void initialize() {
        saveColumn.setStyle("-fx-alignment: center;");
        groupColumn.setStyle("-fx-alignment: center;");

        PropertyValueFactory<User, String> userNameProperty = new PropertyValueFactory<>("fullName");
        PropertyValueFactory<User, String> groupNameProperty = new PropertyValueFactory<>("groupName");
        PropertyValueFactory<User, CheckBox> saveUserListProperty = new PropertyValueFactory<>("userSaveToListCheckBoxElement");

        nameColumn.setCellValueFactory(userNameProperty);
        groupColumn.setCellValueFactory(groupNameProperty);
        saveColumn.setCellValueFactory(saveUserListProperty);

        dataTable.setRowFactory(tableView -> {
            final TableRow<User> row = new TableRow<>();
            final ContextMenu rowMenu = createTableContextMenu();

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
        if(title == null) fileChooser.setInitialFileName(String.format("%s %d.docx", monthNames.get(calendarManager.getMonth().getValue() - 1), calendarManager.getYear()));
        else fileChooser.setInitialFileName(String.format("%s - %s %d.docx", title, monthNames.get(calendarManager.getMonth().getValue() - 1), calendarManager.getYear()));
        return fileChooser.showSaveDialog(StartFX.getScene().getWindow());
    }

    private ContextMenu createTableContextMenu() {
        MenuItem editUserDataItem = new MenuItem("Edytuj dane użytkownika");
        MenuItem printListItem = new MenuItem("Drukuj listę");
        MenuItem selectGroupItem = new MenuItem("Zaznacz grupę na liście");
        MenuItem unselectGroupItem = new MenuItem("Odznacz grupę na liście");
        MenuItem selectAllGroupItem = new MenuItem("Zaznacz tylko tę grupę na liście");
        MenuItem unselectAllGroupItem = new MenuItem("Odznacz tylko tę grupę na liście");
        MenuItem deleteUserFromListItem = new MenuItem("Usuń użytkownika z listy");

        ContextMenu menu = new ContextMenu(printListItem, editUserDataItem, new SeparatorMenuItem(), selectGroupItem, unselectGroupItem, selectAllGroupItem, unselectAllGroupItem, new SeparatorMenuItem(), deleteUserFromListItem);

        editUserDataItem.setOnAction(event -> {
            try {
                FXMLUtils.loadFXMLAsModal(StartFX.getScene().getWindow(), FXMLPath.EDIT_USER_WINDOW, "Edytuj dane użytkownika");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        printListItem.setOnAction(event -> {
            printList(Arrays.asList(dataTable.getSelectionModel().getSelectedItem()));
        });

        deleteUserFromListItem.setOnAction(event -> {
            User user = dataTable.getSelectionModel().getSelectedItem();
            deleteUserFromList(user);
        });

        selectGroupItem.setOnAction(event -> {
            Group group = dataTable.getSelectionModel().getSelectedItem().getGroup();

            UsersManager users = StartFX.getUsersManager();
            User user;
            for(int i = 0; i < users.getSize(); i ++) {
                user = users.getByIndex(i);
                if(user.getGroup().equals(group)) {
                    user.getUserSaveToListCheckBoxElement().setSelected(true);
                }
            }
        });

        unselectGroupItem.setOnAction(event -> {
            Group group = dataTable.getSelectionModel().getSelectedItem().getGroup();

            UsersManager users = StartFX.getUsersManager();
            User user;
            for(int i = 0; i < users.getSize(); i ++) {
                user = users.getByIndex(i);
                if(user.getGroup().equals(group)) {
                    user.getUserSaveToListCheckBoxElement().setSelected(false);
                }
            }
        });

        selectAllGroupItem.setOnAction(event -> {
            Group group = dataTable.getSelectionModel().getSelectedItem().getGroup();

            UsersManager users = StartFX.getUsersManager();
            User user;
            for(int i = 0; i < users.getSize(); i ++) {
                user = users.getByIndex(i);
                user.getUserSaveToListCheckBoxElement().setSelected(user.getGroup().equals(group));
            }
        });

        unselectAllGroupItem.setOnAction(event -> {
            Group group = dataTable.getSelectionModel().getSelectedItem().getGroup();

            UsersManager users = StartFX.getUsersManager();
            User user;
            for(int i = 0; i < users.getSize(); i ++) {
                user = users.getByIndex(i);
                user.getUserSaveToListCheckBoxElement().setSelected(!user.getGroup().equals(group));
            }
        });
        return menu;
    }

    private void deleteUserFromList(User user) {
        String contentText = String.format("Czy na pewno chcesz usunąć użytkownika %s z listy?", user.getFullName());
        Alert deleteUserAlert = new Alert(Alert.AlertType.CONFIRMATION, contentText, ButtonType.YES, ButtonType.NO);
        deleteUserAlert.setTitle("Usuń użytkownika");
        deleteUserAlert.setHeaderText(null);
        deleteUserAlert.setGraphic(null);
        if(deleteUserAlert.showAndWait().get() == ButtonType.YES) {
            StartFX.getUsersManager().remove(user);
            refreshTable();
        }
    }

    private void printList(List<User> listOfUsers) {
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
}
