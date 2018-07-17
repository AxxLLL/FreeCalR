package model.calendar.freedays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class FreeDayTest {

    /*
    * Valid names are only names contains letters, numbers, spaces, apostrophe symbol and name must be between
    * 1 to 32 chars. Spaces from start and end of name string are removed!
    * */

    @DisplayName("of(int): Minus, 0 and higher than 31 values should return IllegalArgumentException.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 32})
    void of_1(int value) {
        assertThatIllegalArgumentException().isThrownBy(() -> FreeDay.of(value));
    }

    @DisplayName("of(int): Correct values (from 1 to 31) should return FreeDay object")
    @ParameterizedTest
    @ValueSource(ints = {1, 15, 31})
    void of_2(int value) {
        assertThat(FreeDay.of(value)).isInstanceOf(FreeDay.class);
    }

    //Valid names info: Valid are only letters, numbers, space and apostrophe symbol, between 1 to 32 chars.
    @DisplayName("of(int, string): Invalid chars in name, should return IllegalArgumentException")
    @ParameterizedTest
    @ValueSource(strings = {"asd&", "asd*", "lol."})
    void of_3(String value) {
        assertThatIllegalArgumentException().isThrownBy(() -> FreeDay.of(1, value));
    }

    @DisplayName("of(int, string): empty or too high names, should return IllegalArgumentException")
    @ParameterizedTest
    @ValueSource(strings = {"", "    ", "ooooooooooooooooooooooooooooooooo"})
    void of_4(String value) {
        assertThatIllegalArgumentException().isThrownBy(() -> FreeDay.of(1, value));
    }

    @DisplayName("of(int, string): Null value, should return NullPointerException")
    @Test
    void of_5() {
        assertThatNullPointerException().isThrownBy(() -> FreeDay.of(1, null));
    }

    @DisplayName("of(int, string): Correct values should return FreeDay object")
    @ParameterizedTest
    @ValueSource(strings = {"a", "aa aa", "aa'aa", "1234567890", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "a9 -'", "óęąśłńćźżŻŹĆŃŁŚĄĘÓ"})
    void of_6(String value) {
        assertThat(FreeDay.of(1, value)).isInstanceOf(FreeDay.class);
    }

    @DisplayName("getDay(): getDay when construct params are valid, should return day that are send in param")
    @Test
    void getDay_1() {
        assertThat(FreeDay.of(4).getDay()).isEqualTo(4);
    }

    @DisplayName("getName(): getname when construct params are valid, should return day name that are send in param")
    @Test
    void getName_1() {
        assertThat(FreeDay.of(4, "Test Name").getName()).isEqualTo("Test Name");
    }


    @DisplayName("getCopy(): Copied element must be instance of FreeDay, but reference cannot be the same with original")
    @Test
    void getCopy_1() {
        FreeDay dayToCopy = FreeDay.of(1, "Test");
        FreeDay copiedDay = dayToCopy.getCopy();

        assertThat(copiedDay).hasSameClassAs(dayToCopy);
        assertThat(copiedDay).isNotSameAs(dayToCopy);
        assertThat(copiedDay).isEqualTo(dayToCopy);
    }
}