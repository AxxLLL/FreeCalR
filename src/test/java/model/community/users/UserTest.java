package model.community.users;

import model.community.groups.Group;
import model.community.groups.GroupManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    private static final Group DEFAULT_GROUP = GroupManager.DEFAULT_GROUP;
    private static final Group OTHER_GROUP = new Group("Other");

    @DisplayName("isValidName(String): Should return false when param is null")
    @Test
    void test_1() {
        assertThat(User.isValidName(null)).isFalse();
    }

    /*
    * Valid are letters (with polish chars), symbols - and '. Name must be between 2 to 16 chars.
    * */
    @DisplayName("isValidName(String): Should return false when param is invalid")
    @ParameterizedTest
    @ValueSource(strings = {"asd1", "asd asd", "asd&", "asd!", "asd_asd", "a", "aaaaaaaaaaaaaaaaa"})
    void test_2() {
        assertThat(User.isValidName(null)).isFalse();
    }

    @DisplayName("isValidName(String): Should return true when param is valid")
    @ParameterizedTest
    @ValueSource(strings = {"aa", "qwertyuiop", "asdfghjklzxcvbnm", "QWERTYUIOP", "ASDFGHJKLZXCVBNM", "żźćńłśąóŻŹĆŃŁŚĄÓ", "-'a"})
    void test_3() {
        assertThat(User.isValidName(null)).isFalse();
    }

    @DisplayName("User(String,String): Should return NullPointerException when params are null")
    @Test
    void test_4() {
        assertThatNullPointerException().isThrownBy(() -> new User(null, "asd"));
        assertThatNullPointerException().isThrownBy(() -> new User("asd", null));
        assertThatNullPointerException().isThrownBy(() -> new User(null, null));
    }

    @DisplayName("User(String,String): Should return User object when params are valid")
    @Test
    void test_5() {
        assertThat(new User("asd", "asd")).isExactlyInstanceOf(User.class);
    }

    @DisplayName("User(String,String,Group): Should return NullPointerException when group param is null")
    @Test
    void test_6() {
        assertThatNullPointerException().isThrownBy(() -> new User("asd", "asd", null));
    }

    @DisplayName("User(String,String,Group): Should return Group object when object is valid.")
    @Test
    void test_8() {
        assertThat(new User("asd", "asd", DEFAULT_GROUP)).isExactlyInstanceOf(User.class);
    }

    @DisplayName("getFirstName(): Should return first name given in constructor")
    @Test
    void test_9() {
        User user = new User("test", "nothing");
        assertThat(user.getFirstName()).isEqualTo("test");
    }

    @DisplayName("getLastName(): Should return last name given in constructor")
    @Test
    void test_10() {
        User user = new User("nothing", "test");
        assertThat(user.getLastName()).isEqualTo("test");
    }

    @DisplayName("getFullName(): Should return first and last spliced by space")
    @Test
    void test_11() {
        User user = new User("name", "lastname");
        assertThat(user.getFullName()).isEqualTo("name lastname");
    }

    @DisplayName("getGroup(): Should return group reference given in constructor")
    @Test
    void test_12() {
        User user = new User("test", "test", DEFAULT_GROUP);
        assertThat(user.getGroup()).isEqualTo(DEFAULT_GROUP);
    }

    @DisplayName("setGroup(Group): Should return new group reference given in parameter")
    @Test
    void test_13() {
        User user = new User("test", "test", DEFAULT_GROUP);
        assertThat(user.getGroup()).isEqualTo(DEFAULT_GROUP);
        user.setGroup(OTHER_GROUP);
        assertThat(user.getGroup()).isEqualTo(OTHER_GROUP);
    }

    @DisplayName("setGroup(Group): Should return NullPointerException when group is null")
    @Test
    void test_14() {
        User user = new User("test", "test");
        assertThatNullPointerException().isThrownBy(() -> user.setGroup(null));
    }

    @DisplayName("copyUserData(User): Should override group data with data from other group! Group data can be changed only if isEditable param value is true")
    @Test
    void test_16() {
        User uToCopy = new User("testA", "testAA", OTHER_GROUP);
        User u = new User("testB", "testBB", DEFAULT_GROUP);

        u.copyUserData(uToCopy);
        assertThat(u.getFullName()).isEqualTo(uToCopy.getFullName());
        assertThat(u.getGroup()).isEqualTo(uToCopy.getGroup());
        assertThat(u).isNotSameAs(uToCopy);
    }

    @DisplayName("copyUserData(User): Should return NullPointerException param is null.")
    @Test
    void test_17() {
        User u = new User("testB", "testBB", DEFAULT_GROUP);
        assertThatNullPointerException().isThrownBy(() -> u.copyUserData(null));
    }

}