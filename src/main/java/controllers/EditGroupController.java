package controllers;

import init.StartFX;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.community.groups.Group;

import java.util.List;

public class EditGroupController {

    @FXML private TableView<Group> groupTable;
    @FXML private TableColumn<Group, String> nameColumn;
    @FXML private TableColumn <Group, Long> usersCountColumn;
    @FXML private TextField groupNameInput;
    @FXML private Label messageLabel;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;

    @FXML private void initialize() {
        initializeTable();
    }

    @FXML
    private void saveChangesBtn() {
        Group oldGroup = groupTable.getSelectionModel().getSelectedItem();
        if(oldGroup != null) {
            try {
                Group newGroup = new Group(groupNameInput.getText());
                List<Group> groupsList = StartFX.getGroupManager().getList();
                if (groupsList.stream().noneMatch(g -> g.equals(newGroup))) {
                    oldGroup.copyGroupData(newGroup);
                    refreshTable();
                    UsersTableController.getController().refreshTable();
                    showInfoMessage("Pomyślnie edytowano dane grupy!", "GREEN");
                } else {
                    showInfoMessage("Grupa o takiej nazwie już istnieje!", "RED");
                }
            } catch (IllegalArgumentException e) {
                showInfoMessage("Niepoprawna nazwa grupy!", "RED");
            } catch (NullPointerException e) {
                showInfoMessage("Podaj nazwę grupy!", "RED");
            }
        }
    }

    @FXML
    private void deleteGroupBtn() {
        Group selectedGroup = groupTable.getSelectionModel().getSelectedItem();
        if(selectedGroup != null) {
            Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz usunąć grupę " + selectedGroup.getName() + " z listy?", ButtonType.YES, ButtonType.NO);
            confirmDeleteAlert.setTitle("Usuń grupę");
            confirmDeleteAlert.setHeaderText(null);
            confirmDeleteAlert.setGraphic(null);
            if(confirmDeleteAlert.showAndWait().get() == ButtonType.YES) {
                if(StartFX.getGroupManager().remove(selectedGroup)) {
                    showInfoMessage("Grupa została usunięta z listy", "GREEN");
                    refreshTable();
                    UsersTableController.getController().refreshTable();
                    groupTable.getSelectionModel().selectFirst();
                } else {
                    showInfoMessage("Nie można usunąć grupy z listy", "RED");
                }

            }
        }
    }

    @FXML
    private void addNewGroupBtn() {
        try {
            if(StartFX.getGroupManager().add(new Group(groupNameInput.getText()))) {
                refreshTable();
                groupTable.getSelectionModel().selectLast();
                showInfoMessage("Dodano nową grupę!", "GREEN");
            } else showInfoMessage("Grupa o takiej nazwie już istnieje!", "RED");
        } catch(IllegalArgumentException e) {
            showInfoMessage("Nazwa grupy może składać się z wyłącznie liter"+System.lineSeparator()+"w przedziale od 2 do 32 znaków", "RED");
        }
    }

    private void initializeTable() {
        PropertyValueFactory<Group, String> groupNameProperty = new PropertyValueFactory<>("name");
        nameColumn.setCellValueFactory(groupNameProperty);

        usersCountColumn.setCellValueFactory(group -> new SimpleLongProperty(StartFX.getGroupManager().countUsersInGroup(group.getValue())).asObject());
        usersCountColumn.setStyle("-fx-alignment: center;");

        refreshTable();

        groupTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectionPosChanged());
        groupTable.getSelectionModel().selectFirst();
    }

    private void refreshTable() {
        ObservableList<Group> groupList = FXCollections.observableList(StartFX.getGroupManager().getList());
        groupTable.setItems(groupList);
        groupTable.refresh();
    }

    private void selectionPosChanged() {
        Group selectedGroup = groupTable.getSelectionModel().getSelectedItem();
        if(selectedGroup != null) {
            groupNameInput.setText(selectedGroup.getName());
            hideInfoMessage();
            if (!selectedGroup.isEditable()) {
                saveButton.setDisable(true);
                deleteButton.setDisable(true);
            } else {
                saveButton.setDisable(false);
                deleteButton.setDisable(false);
            }
        } else {
            groupNameInput.setText("");
            saveButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }

    private void showInfoMessage(String text, String color) {
        messageLabel.setText(text);
        messageLabel.setStyle("-fx-text-fill: " + color + ";");
    }

    private void hideInfoMessage() {
        messageLabel.setText("");
    }

}
