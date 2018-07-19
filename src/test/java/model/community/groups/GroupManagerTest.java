package model.community.groups;

import init.Main;
import model.community.users.User;
import model.community.users.UsersManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GroupManagerTest {

    GroupManager groupManager;

    @BeforeEach
    void beforeEach() {
        groupManager = GroupManager.of();
    }

    @DisplayName("remove(Group): Should return NullPointerException when param is null")
    @Test
    void test_1() {
        assertThatNullPointerException().isThrownBy(() -> groupManager.remove(null));
    }

    @DisplayName("remove(Group): Should return IllegalStateException when try to delete group signed as not editable")
    @Test
    void test_2() {
        assertThatIllegalStateException().isThrownBy(() -> groupManager.remove(GroupManager.DEFAULT_GROUP));
    }

    @DisplayName("remove(Group): Should return False when try to delete valid group, but not added to list")
    @Test
    void test_3() {
        assertThat(groupManager.remove(new Group("Test"))).isFalse();
    }

    @DisplayName("remove(Group): Should return True when try to delete valid group")
    @Test
    void test_4() {
        Group group = new Group("Test");
        groupManager.add(group);
        assertThat(groupManager.remove(group)).isTrue();
    }

    @DisplayName("remove(Group): Should remove group, and users whom have this same group, group should be set to default")
    @Test
    void test_5() {
        Group groupA = new Group("GroupA");
        Group groupB = new Group("GroupB");
        Group groupC = new Group("GroupC");

        Main.getGroupManager().clear();
        Main.getGroupManager().add(groupA);
        Main.getGroupManager().add(groupB);
        Main.getGroupManager().add(groupC);

        User[] users = new User[10];

        UsersManager usersManager = UsersManager.of();
        usersManager.add(users[0] = new User("UserA", "User", groupA));
        usersManager.add(users[1] = new User("UserB", "User", groupA));
        usersManager.add(users[2] = new User("UserC", "User", groupA));
        usersManager.add(users[3] = new User("UserD", "User", groupB));
        usersManager.add(users[4] = new User("UserE", "User", groupB));
        usersManager.add(users[5] = new User("UserF", "User", groupB));
        usersManager.add(users[6] = new User("UserG", "User", groupC));
        usersManager.add(users[7] = new User("UserH", "User", groupC));
        usersManager.add(users[8] = new User("UserI", "User", groupC));
        usersManager.add(users[9] = new User("UserJ", "User", GroupManager.DEFAULT_GROUP));

        assertThat(users[0].getGroup()).isEqualTo(groupA);
        assertThat(users[1].getGroup()).isEqualTo(groupA);
        assertThat(users[2].getGroup()).isEqualTo(groupA);
        assertThat(users[3].getGroup()).isEqualTo(groupB);
        assertThat(users[4].getGroup()).isEqualTo(groupB);
        assertThat(users[5].getGroup()).isEqualTo(groupB);
        assertThat(users[6].getGroup()).isEqualTo(groupC);
        assertThat(users[7].getGroup()).isEqualTo(groupC);
        assertThat(users[8].getGroup()).isEqualTo(groupC);
        assertThat(users[9].getGroup()).isEqualTo(GroupManager.DEFAULT_GROUP);
        Main.getGroupManager().remove(groupA);
        assertThat(users[0].getGroup()).isEqualTo(GroupManager.DEFAULT_GROUP);
        assertThat(users[1].getGroup()).isEqualTo(GroupManager.DEFAULT_GROUP);
        assertThat(users[2].getGroup()).isEqualTo(GroupManager.DEFAULT_GROUP);
        assertThat(users[3].getGroup()).isEqualTo(groupB);
        assertThat(users[4].getGroup()).isEqualTo(groupB);
        assertThat(users[5].getGroup()).isEqualTo(groupB);
        assertThat(users[6].getGroup()).isEqualTo(groupC);
        assertThat(users[7].getGroup()).isEqualTo(groupC);
        assertThat(users[8].getGroup()).isEqualTo(groupC);
        assertThat(users[9].getGroup()).isEqualTo(GroupManager.DEFAULT_GROUP);

        Main.getGroupManager().clear();

    }
}