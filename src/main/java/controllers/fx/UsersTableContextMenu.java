package controllers.fx;

import init.StartFX;
import javafx.scene.control.*;
import model.community.groups.Group;
import model.community.users.User;
import model.community.users.UsersManager;
import view.utils.FXMLPath;
import view.utils.FXMLUtils;

import java.io.IOException;
import java.util.Arrays;

public class UsersTableContextMenu {
    private ContextMenu contextMenu;
    private MenuItem editUserDataItem = new MenuItem("Edytuj dane użytkownika");
    private MenuItem printListItem = new MenuItem("Zapisz listę użytkownika");
    private MenuItem selectGroupItem = new MenuItem("Zaznacz grupę na liście");
    private MenuItem unselectGroupItem = new MenuItem("Odznacz grupę na liście");
    private MenuItem selectAllGroupItem = new MenuItem("Zaznacz tylko tę grupę na liście");
    private MenuItem unselectAllGroupItem = new MenuItem("Odznacz tylko tę grupę na liście");
    private MenuItem deleteUserFromListItem = new MenuItem("Usuń użytkownika z listy");
    private UsersTableController controller;
    private TableView<User> dataTable;

    UsersTableContextMenu(UsersTableController controller) {
        this.controller = controller;
        this.dataTable = controller.getTableElement();

        contextMenu = new ContextMenu(
                printListItem,
                editUserDataItem,
                new SeparatorMenuItem(),
                selectGroupItem,
                unselectGroupItem,
                selectAllGroupItem,
                unselectAllGroupItem,
                new SeparatorMenuItem(),
                deleteUserFromListItem
        );

        addEditUserDataAction();
        addPrintListAction();
        addUserDeleteAction();
        addSelectGroupAction();
        addUnselectGroupAction();
        addSelectSaveAllGroupAction();
        addUnselectSaveAllGroupAction();
    }

    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    private void addEditUserDataAction() {
        editUserDataItem.setOnAction(event -> {
            try {
                FXMLUtils.loadFXMLAsModal(StartFX.getScene().getWindow(), FXMLPath.EDIT_USER_WINDOW, "Edytuj dane użytkownika");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addPrintListAction() {
        printListItem.setOnAction(event -> {
            controller.printList(Arrays.asList(dataTable.getSelectionModel().getSelectedItem()));
        });
    }

    private void addUserDeleteAction() {
        deleteUserFromListItem.setOnAction(event -> {
            User user = dataTable.getSelectionModel().getSelectedItem();
            deleteUserFromList(user);
        });
    }

    private void addSelectGroupAction() {
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
    }

    private void addUnselectGroupAction() {
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
    }

    private void addSelectSaveAllGroupAction() {
        selectAllGroupItem.setOnAction(event -> {
            Group group = dataTable.getSelectionModel().getSelectedItem().getGroup();

            UsersManager users = StartFX.getUsersManager();
            User user;
            for(int i = 0; i < users.getSize(); i ++) {
                user = users.getByIndex(i);
                user.getUserSaveToListCheckBoxElement().setSelected(user.getGroup().equals(group));
            }
        });
    }

    private void addUnselectSaveAllGroupAction() {
        unselectAllGroupItem.setOnAction(event -> {
            Group group = dataTable.getSelectionModel().getSelectedItem().getGroup();

            UsersManager users = StartFX.getUsersManager();
            User user;
            for(int i = 0; i < users.getSize(); i ++) {
                user = users.getByIndex(i);
                user.getUserSaveToListCheckBoxElement().setSelected(!user.getGroup().equals(group));
            }
        });
    }

    private void deleteUserFromList(User user) {
        String contentText = String.format("Czy na pewno chcesz usunąć użytkownika %s z listy?", user.getFullName());
        Alert deleteUserAlert = new Alert(Alert.AlertType.CONFIRMATION, contentText, ButtonType.YES, ButtonType.NO);
        deleteUserAlert.setTitle("Usuń użytkownika");
        deleteUserAlert.setHeaderText(null);
        deleteUserAlert.setGraphic(null);
        if(deleteUserAlert.showAndWait().get() == ButtonType.YES) {
            StartFX.getUsersManager().remove(user);
            controller.refreshTable();
        }
    }
}