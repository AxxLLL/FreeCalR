package model.community.groups;

import model.community.users.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class GroupTest {

    @DisplayName("Group(String): Should return null pointer exception when name is null")
    @Test
    void test_1() {
        assertThatNullPointerException().isThrownBy(() -> new Group(null));
    }

    @DisplayName("Group(String): Should return IllegalArgumentException when name is invalid")
    @ParameterizedTest()
    @ValueSource(strings = {"", "a", "   a", "asd*", "ufd!", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void test_2(String value) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Group(value));
    }

    @DisplayName("Group(String): Should return Group object when name is valid")
    @ParameterizedTest()
    @ValueSource(strings = {"aa", "łóęśążźćńŃĆŹŻĄŚŁÓĘ", "-_ ", "QWERTYUIOPASDFGHJKLZXCVBNM", "qwertyuiopasdfghjklzxcvbnm", "1234567890", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void test_3(String value) {
        assertThat(new Group(value)).isInstanceOf(Group.class);
    }

    @DisplayName("Group(String, boolean): Should return Group object when name is valid")
    @ParameterizedTest()
    @ValueSource(strings = {"aa", "łóęśążźćńŃĆŹŻĄŚŁÓĘ", "-_ ", "QWERTYUIOPASDFGHJKLZXCVBNM", "qwertyuiopasdfghjklzxcvbnm", "1234567890", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void test_4(String value) {
        assertThat(new Group(value, false)).isInstanceOf(Group.class);
    }

    @DisplayName("isEditable(): Should return value, given in constructor")
    @Test
    void test_5() {
        Group g0 = new Group("asd");
        Group g1 = new Group("asd", false);
        Group g2 = new Group("asd", true);

        assertThat(g0.isEditable()).isTrue();
        assertThat(g1.isEditable()).isFalse();
        assertThat(g2.isEditable()).isTrue();
    }

    @DisplayName("getName(): Should return name, given in constructor, after trim!")
    @ParameterizedTest
    @ValueSource(strings = {"asd", "   asd", "asd    "})
    void test_6(String value) {
        Group g = new Group(value);
        assertThat(g.getName()).isEqualTo(value.trim());
    }

    @DisplayName("copyGroupData(Group): Should override group data with data from other group! Group data can be changed only if isEditable param value is true")
    @Test
    void test_7() {
        Group gToCopy = new Group("test", false);
        Group g = new Group("ReplaceMe", true);
        g.copyGroupData(gToCopy);
        assertThat(g.getName()).isEqualTo(gToCopy.getName());
        assertThat(g.isEditable()).isEqualTo(gToCopy.isEditable());
        assertThat(g).isNotSameAs(gToCopy);
    }

    @DisplayName("copyGroupData(Group): Should return IllegalStateException if try replace group data, that is assigned as not editable.")
    @Test
    void test_8() {
        Group gToCopy = new Group("test", true);
        Group g = new Group("Not To Replace", false);
        assertThatIllegalStateException().isThrownBy(() -> g.copyGroupData(gToCopy));
        assertThat(g.getName()).isEqualTo("Not To Replace");
        assertThat(g.isEditable()).isFalse();
    }

    @DisplayName("copyGroupData(Group): Should return NullPointerException param is null.")
    @Test
    void test_17() {
        Group g = new Group("Not To Replace", false);
        assertThatNullPointerException().isThrownBy(() -> g.copyGroupData(null));
    }
}