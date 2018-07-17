package model.calendar.day;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DayTest {

    @DisplayName("of(DayType): Null values in param should throw NullPointerException")
    @Test
    void test_1() {
        assertThatNullPointerException().isThrownBy(() -> Day.of(null));
    }

    @DisplayName("of(DayType): Valid values should return object of Day class")
    @Test
    void test_2() {
        assertThat(Day.of(DayType.NORMAL)).isInstanceOf(Day.class);
        assertThat(Day.of(DayType.HOLIDAY)).isInstanceOf(Day.class);
        assertThat(Day.of(DayType.SATURDAY)).isInstanceOf(Day.class);
    }

    @DisplayName("Check types: Base type and type should have this same values after class init")
    @Test
    void test_3() {
        Day day = Day.of(DayType.NORMAL);
        assertThat(day.getBaseDayType()).isEqualTo(day.getDayType());
    }

    @DisplayName("setDayType(DayType): Null values in param should throw NullPointerException")
    @Test
    void test_4() {
        assertThatNullPointerException().isThrownBy(() -> Day.of(DayType.NORMAL).setDayType(null));
    }

    @DisplayName("setDayType(DayType): If new type is same as old, method should return false")
    @Test
    void test_5() {
        assertThat(Day.of(DayType.NORMAL).setDayType(DayType.NORMAL)).isFalse();
    }

    @DisplayName("setDayType(DayType): If new type is different as old, method should change type and return true")
    @Test
    void test_6() {
        Day day = Day.of(DayType.NORMAL);
        assertThat(day.setDayType(DayType.SATURDAY)).isTrue();
        assertThat(day.getBaseDayType()).isEqualTo(DayType.NORMAL);
        assertThat(day.getDayType()).isEqualTo(DayType.SATURDAY);
    }

    /*
    Switches:
        Normal day -> Holiday -> Normal day
        Saturday -> Normal day -> Holiday -> Saturday
        Holiday -> Normal day -> Holiday
    */
    @DisplayName("switchDayType(): Switches days (check description in test).")
    @Test
    void test_7() {
        Day normal = Day.of(DayType.NORMAL);
        Day saturday = Day.of(DayType.SATURDAY);
        Day holiday = Day.of(DayType.HOLIDAY);

        assertThat(normal.switchDayType()).isEqualTo(DayType.HOLIDAY);
        assertThat(normal.switchDayType()).isEqualTo(DayType.NORMAL);

        assertThat(holiday.switchDayType()).isEqualTo(DayType.NORMAL);
        assertThat(holiday.switchDayType()).isEqualTo(DayType.HOLIDAY);

        assertThat(saturday.switchDayType()).isEqualTo(DayType.NORMAL);
        assertThat(saturday.switchDayType()).isEqualTo(DayType.HOLIDAY);
        assertThat(saturday.switchDayType()).isEqualTo(DayType.SATURDAY);
    }

}