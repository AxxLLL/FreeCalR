package controllers.fx;

import controllers.ControllerManager;
import init.StartFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.community.groups.Group;
import model.community.users.User;

import java.util.List;

public class EditUserWindowController {
    private @FXML TextField firstName;
    private @FXML TextField lastName;
    private @FXML ChoiceBox<Group> group;
    private @FXML Label messageLabel;
    private @FXML Button saveButton;
    private UsersTableController usersTableController = (UsersTableController)ControllerManager.get(UsersTableController.class);
    private User editedUser = (User) usersTableController.getTableElement().getSelectionModel().getSelectedItem();

    public EditUserWindowController() {
        ControllerManager.add(this);
    }

    @FXML
    private void initialize() {
        firstName.setText(editedUser.getFirstName());
        lastName.setText(editedUser.getLastName());

        ObservableList<Group> listOfGroups = FXCollections.observableList(StartFX.getGroupManager().getList());
        group.setItems(listOfGroups);
        group.getSelectionModel().select(editedUser.getGroup());
    }

    @FXML
    private void saveUserData() {
        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        Group group = this.group.getValue();

        try {
            User newUser = new User(firstName, lastName, group);
            if(!newUser.equals(editedUser) || newUser.getGroup() != editedUser.getGroup()) {

                List<User> copiedUsersList = StartFX.getUsersManager().getList();
                copiedUsersList.removeIf(e_user -> e_user.equals(editedUser));
                if(copiedUsersList.stream().noneMatch(e_user -> e_user.equals(newUser))) {
                    editedUser.copyUserData(newUser);
                    usersTableController.refreshTable();
                    showInfoMessage("Pomyślnie edytowano dane użytkownika!", "GREEN");
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                } else showInfoMessage("Użytkownik o takich danych jest już na liście!", "RED");
            } else showInfoMessage("Nie dokonano żadnych zmian!", "RED");
        } catch (IllegalArgumentException e) {
            showInfoMessage("Niepoprawna nazwa użytkownika!", "RED");
        }
    }

    private void showInfoMessage(String text, String color) {
        messageLabel.setText(text);
        messageLabel.setStyle("-fx-text-fill: " + color + ";");
    }
}
