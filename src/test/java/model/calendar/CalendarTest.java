package model.calendar;

import model.calendar.day.Day;
import model.calendar.day.DayType;
import model.calendar.freedays.FreeDaysManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CalendarTest {
    private static FreeDaysManager freeDaysManager;
    private Calendar defaultCalendarManager;

    @BeforeAll
    static void beforeAll() {
        freeDaysManager = new FreeDaysManager();
    }

    @BeforeEach
    void beforeEach() {
        LocalDate now = LocalDate.now();
        defaultCalendarManager = new TestClassManager(freeDaysManager, now.getMonth(), now.getYear());
    }

    @DisplayName("getDate(): Should return current date when use default manager")
    @Test
    void test_1() {
        LocalDate now = LocalDate.now();
        now = now.minusDays(now.getDayOfMonth() - 1);
        assertThat(defaultCalendarManager.getDate()).isEqualTo(now);
    }

    @DisplayName("getDaysInMonthList(): Size of list should be equals to num of days in month from LocalDate")
    @Test
    void test_2() {
        assertThat(defaultCalendarManager.getDaysInMonthList().size()).isEqualTo(LocalDate.now().lengthOfMonth());
    }

    @DisplayName("getDaysInMonthList(): Days in current month should be represented in list (Including saturdays and sundays)")
    @Test
    void test_3() {
        DayType dayTypeFromLocalDate;
        List<Day> listOfDays = defaultCalendarManager.getDaysInMonthList();

        LocalDate now = LocalDate.now();
        Month startingMonth = now.getMonth();
        now = now.minusDays(now.getDayOfMonth() - 1);

        while(now.getMonth() == startingMonth) {
            dayTypeFromLocalDate = now.getDayOfWeek() == DayOfWeek.SATURDAY ? DayType.SATURDAY : now.getDayOfWeek() == DayOfWeek.SUNDAY ? DayType.HOLIDAY : DayType.NORMAL;
            assertThat(listOfDays.get(now.getDayOfMonth() - 1).getDayType()).isEqualTo(dayTypeFromLocalDate);
            now = now.plusDays(1);
        }
    }

    @DisplayName("getFreeDaysManager(): FreeDaysManager in CalendarManager should have this same reference as default")
    @Test
    void test_4() {
        assertThat(defaultCalendarManager.getFreeDaysManager()).isSameAs(freeDaysManager);
    }

    @DisplayName("copy(Calendar): Should throw NullPointerException when param is null")
    @Test
    void test_5() {
        assertThatNullPointerException().isThrownBy(() -> defaultCalendarManager.copy(null));
    }

    @DisplayName("copy(Calendar): Should replaced old data with new data from new Calendar object")
    @Test
    void test_6() {
        final int YEAR = 2005;
        final Month MONTH = Month.APRIL;
        FreeDaysManager newFreeDaysManager = new FreeDaysManager();
        Calendar newCalendar = new TestClassManager(newFreeDaysManager, MONTH, YEAR);

        defaultCalendarManager.copy(newCalendar);

        assertThat(defaultCalendarManager.getFreeDaysManager()).isEqualTo(newCalendar.getFreeDaysManager());
        assertThat(defaultCalendarManager.getDaysInMonthList()).isEqualTo(newCalendar.getDaysInMonthList());
        assertThat(defaultCalendarManager.getDate()).isEqualTo(newCalendar.getDate());
    }

    private class TestClassManager extends Calendar {
        TestClassManager(FreeDaysManager manager, Month month, int year) {
            super(manager, month, year);
        }
    }
}