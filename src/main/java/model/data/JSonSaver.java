package model.data;

import com.google.common.base.Preconditions;
import model.community.groups.Group;
import model.community.groups.GroupManager;
import model.community.users.UsersManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSonSaver {

    private final Path pathToFile;
    private final UsersManager usersManager;
    private final GroupManager groupManager;

    public JSonSaver(Path path, UsersManager usersManager, GroupManager groupManager) throws IOException {
        Preconditions.checkNotNull(path);
        Preconditions.checkNotNull(usersManager);
        Preconditions.checkNotNull(groupManager);
        Preconditions.checkState(tryToBuildPath(path), "Nie można uwtorzyć ścieżki do zapisu pliku.");

        this.pathToFile = path;
        this.usersManager = usersManager;
        this.groupManager = groupManager;
    }

    public void save() {
        StringBuilder outputString = new StringBuilder("{" + System.lineSeparator());
        outputString.append(getUsersData());
        outputString.append("," + System.lineSeparator());
        outputString.append(getGroupsData());
        outputString.append("}");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile.toString()))) {
            writer.write(outputString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private StringBuilder getUsersData() {
        StringBuilder usersString = new StringBuilder("\"users\": [" + System.lineSeparator());

        usersManager.getList().forEach(user -> {
            usersString.append(String.format("{\"firstName\": \"%s\", \"lastName\": \"%s\", \"groupID\": %d},%n", user.getFirstName(), user.getLastName(), groupManager.getIndex(user.getGroup())));
        });
        if(usersString.lastIndexOf(",") != -1) usersString.deleteCharAt(usersString.lastIndexOf(","));

        usersString.append("]" + System.lineSeparator());
        return usersString;
    }

    private StringBuilder getGroupsData() {
        StringBuilder groupsString = new StringBuilder("\"groups\": [" + System.lineSeparator());

        int index = 0;
        for(Group group: groupManager.getList()) {
            groupsString.append(String.format("{\"id\": %d, \"name\": \"%s\"},%n", index ++, group.getName()));
        }
        if(groupsString.lastIndexOf(",") != -1) groupsString.deleteCharAt(groupsString.lastIndexOf(","));

        groupsString.append("]" + System.lineSeparator());
        return groupsString;
    }

    private boolean tryToBuildPath(Path path) throws IOException {
        Path parent = path.getParent();
        if(parent != null) {
            if(Files.exists(parent)) return true;
            else {
                Files.createDirectories(parent);
                return true;
            }
        }
        return false;
    }
}
