package model.calendar;

import model.calendar.freedays.FreeDaysManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.*;

class CalendarSwitcherTest {
    private static FreeDaysManager freeDaysManager;
    private CalendarManager defaultCalendarManager;

    @BeforeAll
    static void beforeAll() {
        freeDaysManager = new FreeDaysManager();
    }

    @BeforeEach
    void beforeEach() {
        defaultCalendarManager = CalendarManager.of(freeDaysManager);
    }

    @DisplayName("switchToNextYear(): Should return new CalendarManager object with next year date")
    @Test
    void test_1() {
        LocalDate now = LocalDate.now();
        int currYear = now.getYear();

        for(int i = 0; i < 12; i ++) {
            assertThat(defaultCalendarManager.getYear()).isEqualTo(currYear ++);
            defaultCalendarManager = CalendarSwitcher.switchToNextYear(defaultCalendarManager);
            assertThat(defaultCalendarManager).isExactlyInstanceOf(CalendarManager.class);
        }
    }

    @DisplayName("switchToPreviousYear(): Should return new CalendarManager object with previous year date")
    @Test
    void test_2() {
        LocalDate now = LocalDate.now();
        int currYear = now.getYear();

        for(int i = 0; i < 12; i ++) {
            assertThat(defaultCalendarManager.getYear()).isEqualTo(currYear --);
            defaultCalendarManager = CalendarSwitcher.switchToPreviousYear(defaultCalendarManager);
            assertThat(defaultCalendarManager).isExactlyInstanceOf(CalendarManager.class);
        }
    }

    @DisplayName("switchToPreviousYear(): Should throw IllegalArgumentException when try to get back below 2000 year")
    @Test
    void test_3() {
        CalendarManager localManager = CalendarManager.of(freeDaysManager, Month.JANUARY, 2001);
        assertThat(localManager = CalendarSwitcher.switchToPreviousYear(localManager)).isExactlyInstanceOf(CalendarManager.class);
        CalendarManager finalLocalManager = localManager;
        assertThatIllegalArgumentException().isThrownBy(() -> CalendarSwitcher.switchToPreviousYear(finalLocalManager));
    }

    @DisplayName("switchToNextMonth(): Should return new CalendarManager object with next month date")
    @Test
    void test_4() {
        LocalDate now = LocalDate.of(2018, 1, 1);
        Month currMonth = now.getMonth();
        CalendarManager localManager = CalendarManager.of(freeDaysManager, Month.JANUARY, 2018);

        for(int i = 0; i < 11; i ++) {
            assertThat(localManager.getMonth()).isEqualTo(currMonth);
            currMonth = currMonth.plus(1);
            assertThat((localManager = CalendarSwitcher.switchToNextMonth(localManager))).isExactlyInstanceOf(CalendarManager.class);
        }
    }

    @DisplayName("switchToPreviousMonth(): Should return new CalendarManager object with previous month date")
    @Test
    void test_5() {
        LocalDate now = LocalDate.of(2018, 12, 1);
        Month currMonth = now.getMonth();
        CalendarManager localManager = CalendarManager.of(freeDaysManager, Month.DECEMBER, 2018);

        for(int i = 0; i < 11; i ++) {
            assertThat(localManager.getMonth()).isEqualTo(currMonth);
            currMonth = currMonth.minus(1);
            assertThat((localManager = CalendarSwitcher.switchToPreviousMonth(localManager))).isExactlyInstanceOf(CalendarManager.class);
        }
    }

    @DisplayName("switchToNextMonth(): When current month is december, method should return IllegalStateException")
    @Test
    void test_6() {
        CalendarManager localManager = CalendarManager.of(freeDaysManager, Month.DECEMBER, 2018);
        assertThatIllegalStateException().isThrownBy(() -> CalendarSwitcher.switchToNextMonth(localManager));
    }

    @DisplayName("switchToNextMonth(): When current month is january, method should return IllegalStateException")
    @Test
    void test_7() {
        CalendarManager localManager = CalendarManager.of(freeDaysManager, Month.JANUARY, 2018);
        assertThatIllegalStateException().isThrownBy(() -> CalendarSwitcher.switchToPreviousMonth(localManager));
    }
}