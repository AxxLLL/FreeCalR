package model.data;

import com.google.common.base.Preconditions;

import model.community.groups.Group;
import model.community.groups.GroupManager;
import model.community.users.User;
import model.community.users.UsersManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JSonLoader {
    private final Path pathToFile;
    private final UsersManager usersManager;
    private final GroupManager groupManager;

    public JSonLoader(Path path, UsersManager usersManager, GroupManager groupManager) {
        Preconditions.checkNotNull(path);
        Preconditions.checkNotNull(usersManager);
        Preconditions.checkNotNull(groupManager);

        this.pathToFile = path;
        this.usersManager = usersManager;
        this.groupManager = groupManager;
    }

    public void load() throws IOException {
        List<String> rLines = Files.readAllLines(pathToFile, StandardCharsets.UTF_8);
        StringBuilder readFileBuilder = new StringBuilder();
        rLines.forEach(readFileBuilder::append);

        JSONObject jsonObject = new JSONObject(readFileBuilder.toString());
        loadGroups(jsonObject);
        loadUsers(jsonObject);
    }

    private void loadGroups(JSONObject object) {
        JSONArray groupsJSonArray = object.getJSONArray("groups");
        for(int i = 0; i < groupsJSonArray.length(); i ++) {
            String name = groupsJSonArray.getJSONObject(i).getString("name");
            groupManager.add(new Group(name));
        }
    }

    private void loadUsers(JSONObject object) {
        JSONArray usersJSonArray = object.getJSONArray("users");
        for(int i = 0; i < usersJSonArray.length(); i ++) {
            String firstName = usersJSonArray.getJSONObject(i).getString("firstName");
            String lastName = usersJSonArray.getJSONObject(i).getString("lastName");
            int groupID = usersJSonArray.getJSONObject(i).getInt("groupID");
            usersManager.add(new User(firstName, lastName, groupManager.getByIndex(groupID)));
        }
    }
}
