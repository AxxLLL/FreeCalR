package model.data;

import model.community.groups.Group;
import model.community.groups.GroupManager;
import model.community.users.User;
import model.community.users.UsersManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;

class JSonSaverTest {

    @DisplayName("of(Path, UserManager, GroupManager): Should throw null pointer exception if one or more params is null")
    @Test
    void test_0() {
        UsersManager usersManager = UsersManager.of();
        assertThatNullPointerException().isThrownBy(() -> new JSonSaver(null, usersManager, GroupManager.of(usersManager)));
        assertThatNullPointerException().isThrownBy(() -> new JSonSaver(Paths.get("test.json"), null, GroupManager.of(usersManager)));
        assertThatNullPointerException().isThrownBy(() -> new JSonSaver(Paths.get("test.json"), usersManager, null));
    }
/*
    @DisplayName("of(Path, UserManager, GroupManager): Should throw IllegalStateException exception if path to file can't be builded")
    @Test
    void test_1() {
        Path path = Paths.get("test", "test.json");
        UsersManager usersManager = UsersManager.of();
        assertThatIllegalStateException().isThrownBy(() -> new JSonSaver(path, usersManager, GroupManager.of(usersManager)));
    }
*/
    @DisplayName("save(): Should create new json file and put in some data")
    @Test
    void test_2() throws IOException {
        UsersManager usersManager = createDefaultUsersManager();
        GroupManager groupManager = createDefaultGroupManager(usersManager);

        usersManager.getByIndex(0).setGroup(groupManager.getByIndex(1));
        usersManager.getByIndex(1).setGroup(groupManager.getByIndex(2));

        Path temp = Files.createTempFile(Paths.get("zFileTest"), "test_", ".json");
        JSonSaver jSonSaver = new JSonSaver(temp, usersManager, groupManager);
        jSonSaver.save();

        assertThat(Files.isRegularFile(temp)).isTrue();
        assertThat(Files.size(temp)).isPositive();

        Files.delete(temp);
    }

    private UsersManager createDefaultUsersManager() {
        UsersManager manager = UsersManager.of();
        manager.add(new User("TestA", "TestA"));
        manager.add(new User("TestB", "TestB"));
        manager.add(new User("TestC", "TestC"));
        return manager;
    }

    private GroupManager createDefaultGroupManager(UsersManager usersManager) {
        GroupManager manager = GroupManager.of(usersManager);
        manager.add(new Group("GroupA"));
        manager.add(new Group("GroupB"));
        manager.add(new Group("GroupC"));
        return manager;
    }

}