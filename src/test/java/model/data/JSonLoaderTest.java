package model.data;

import model.community.groups.Group;
import model.community.groups.GroupManager;
import model.community.users.User;
import model.community.users.UsersManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class JSonLoaderTest {

    @DisplayName("of(Path, UserManager, GroupManager): Should throw null pointer exception if one or more params is null")
    @Test
    void test_0() {
        UsersManager usersManager = UsersManager.of();
        assertThatNullPointerException().isThrownBy(() -> new JSonLoader(null, usersManager, GroupManager.of(usersManager)));
        assertThatNullPointerException().isThrownBy(() -> new JSonLoader(Paths.get("test.json"), null, GroupManager.of(usersManager)));
        assertThatNullPointerException().isThrownBy(() -> new JSonLoader(Paths.get("test.json"), usersManager, null));
    }

    @DisplayName("load(): Should put object from file to UsersManager and GroupManager")
    @Test
    void test_1() throws IOException {
        Path path = Paths.get("zFileTest", "validData.json");
        UsersManager usersManager = UsersManager.of();
        GroupManager groupManager = GroupManager.of(usersManager);
        JSonLoader jSonLoader = new JSonLoader(path, usersManager, groupManager);

        jSonLoader.load();

        List<User> listOfUsers = usersManager.getList();
        List<Group> listOfGroups = groupManager.getList();

        assertThat(listOfGroups.get(0).getName()).isEqualTo("Brak");
        assertThat(listOfGroups.get(1).getName()).isEqualTo("GroupA");
        assertThat(listOfGroups.get(2).getName()).isEqualTo("GroupB");
        assertThat(listOfGroups.get(3).getName()).isEqualTo("GroupC");

        assertThat(listOfUsers.get(0).getFullName()).isEqualTo("TestA TestAA");
        assertThat(listOfUsers.get(1).getFullName()).isEqualTo("TestB TestBB");
        assertThat(listOfUsers.get(2).getFullName()).isEqualTo("TestC TestCC");

        assertThat(listOfUsers.get(0).getGroup()).isEqualTo(listOfGroups.get(1));
        assertThat(listOfUsers.get(1).getGroup()).isEqualTo(listOfGroups.get(2));
        assertThat(listOfUsers.get(2).getGroup()).isEqualTo(GroupManager.DEFAULT_GROUP);
    }
}