package model.community.users;

import com.google.common.base.Preconditions;
import init.Main;
import javafx.scene.control.CheckBox;
import model.community.groups.Group;
import model.community.groups.GroupManager;

import java.util.regex.Pattern;

public class User {

    private String firstName;
    private String lastName;
    private Group group;
    private CheckBox saveToList; //JavaFX checkbox

    public User(String firstName, String lastName, Group group) {
        Preconditions.checkNotNull(firstName, "Imię użytkownika musi zostać podane!");
        Preconditions.checkNotNull(lastName, "Nazwisko użytkownika musi zostać podane!");
        Preconditions.checkNotNull(group, "Grupa użytkownika musi zostać podana!");
        Preconditions.checkArgument(isValidName(firstName), "Imię użytkownika jest niepoprawne!");
        Preconditions.checkArgument(isValidName(lastName), "Nazwisko użytkownika jest niepoprawne!");

        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        //this.saveToList = new CheckBox();
        //this.saveToList.setSelected(true);
    }

    public User(String firstName, String lastName) {
        this(firstName, lastName, GroupManager.DEFAULT_GROUP);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        Preconditions.checkNotNull(group, "Grupa musi zostać określona!");
        this.group = group;
    }

    public String getGroupName() {
        return group.getName();
    }

    public boolean isUserSaveToListEnabled() {
        return saveToList.isSelected();
    }

    public CheckBox getUserSaveToListCheckBoxElement() {
        return saveToList;
    }

    public void copyUserData(User user) {
        Preconditions.checkNotNull(user, "Użytkownik, którego dane mają być skopiowane, musi zostać określony!");
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.group = user.group;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != getClass()) return false;
        if(obj == this) return true;

        User user = (User) obj;
        return user.getFirstName().equals(getFirstName()) && user.getLastName().equals(getLastName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("First name: " + getFirstName() + " | ");
        sb.append("Last name: " + getLastName() + " | ");
        sb.append("Group name: " + getGroupName());
        return sb.toString();
    }

    public static boolean isValidName(String name) {
        Pattern namePattern = Pattern.compile("^[a-zęółśążźćń'\\-]{2,16}$");
        return name != null && namePattern.matcher(name.toLowerCase()).matches();
    }

}
