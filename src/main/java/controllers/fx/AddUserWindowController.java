package controllers.fx;
import controllers.ControllerManager;
import init.StartFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.community.groups.Group;
import model.community.users.User;

public class AddUserWindowController {
    @FXML private TextField firstNameInput;
    @FXML private TextField lastNameInput;
    @FXML private ChoiceBox<Group> groupList;
    @FXML private Label messageLabel;

    public AddUserWindowController() {
        ControllerManager.add(this);
    }

    @FXML
    void initialize() {
        ObservableList<Group> groupItems = FXCollections.observableList(StartFX.getGroupManager().getList());
        groupList.setItems(groupItems);
        groupList.getSelectionModel().selectFirst();
    }

    @FXML
    private void addUserToList() {
        try {
            if(!StartFX.getUsersManager().add(new User(firstNameInput.getText(), lastNameInput.getText(), groupList.getValue()))) {
                showInfoMessage("Użytkownik już jest na liście!", "RED");
            } else {
                showInfoMessage("Użytkownik został dodany do listy!", "GREEN");
                ((UsersTableController)ControllerManager.get(UsersTableController.class)).refreshTable();
            }
        } catch (IllegalArgumentException e) {
            showInfoMessage("Niepoprawna nazwa użytkownika!", "RED");
        }
    }

    private void showInfoMessage(String text, String color) {
        messageLabel.setText(text);
        messageLabel.setStyle("-fx-text-fill: " + color + ";");
    }

}
