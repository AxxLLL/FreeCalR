package view.utils;

public enum FXMLPath {

    MAIN_WINDOW("MainWindow.fxml"),
    USER_ADD_WINDOW("AddUserWindow.fxml"),
    EDIT_USER_WINDOW("EditUserData.fxml"),
    EDIT_GROUP_WINDOW("EditGroupWindow.fxml");

    //-------------------------

    private String fullPath;
    FXMLPath(String path) {
        fullPath = String.format("FXML/%s", path);
    }

    public String getPath() {
        return fullPath;
    }
}
