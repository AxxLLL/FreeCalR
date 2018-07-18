package model.calendar;

import model.calendar.day.Day;
import model.calendar.day.DayType;
import model.calendar.freedays.FreeDaysManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CalendarManagerTest {
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

    //--------------------------------------------------------------------
    // Calendar abstract class tests
    //--------------------------------------------------------------------

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

    //--------------------------------------------------------------------
    // CalendarManager class tests
    //--------------------------------------------------------------------

    @DisplayName("of(*): Null values in constructors should throw NullPointerException")
    @Test
    void test_5() {
        assertThatNullPointerException().isThrownBy(() -> CalendarManager.of(null));
        assertThatNullPointerException().isThrownBy(() -> CalendarManager.of(freeDaysManager, null, 2010));
        assertThatNullPointerException().isThrownBy(() -> CalendarManager.of(null, Month.JANUARY, 2010));
    }

    @DisplayName("of(*): Valid values in constructors should return CalendarManager object")
    @Test
    void test_6() {
        assertThat(CalendarManager.of(freeDaysManager)).isInstanceOf(CalendarManager.class);
        assertThat(CalendarManager.of(freeDaysManager, Month.MAY, 2010)).isInstanceOf(CalendarManager.class);
    }

    @DisplayName("of(FreeDaysManager, Month, int): Invalid year value (below 2000) should throw IllegalArgumentException")
    @Test
    void test_7() {
        assertThatIllegalArgumentException().isThrownBy(() -> CalendarManager.of(freeDaysManager, Month.JANUARY, 1999));
    }

    @DisplayName("getStartingDayOfWeek(): Should return this same value as LocalDate when we get starting of the week day type")
    @Test
    void test_8() {
        LocalDate now = LocalDate.now();
        now = now.minusDays(now.getDayOfMonth() - 1);
        assertThat(defaultCalendarManager.getStartingDayOfWeek()).isSameAs(now.getDayOfWeek());
    }

    @DisplayName("getDay(int): Try to get day with invalid value (ex. -1, 0, 32) should return null value")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 32})
    void test_8(int value) {
        CalendarManager localManager = CalendarManager.of(freeDaysManager, Month.DECEMBER, 2018); //Month should have 31 days to test
        assertThat(localManager.getDay(value)).isNull();
    }

    @DisplayName("getDay(int): Try to get valid day should return Day object")
    @ParameterizedTest
    @ValueSource(ints = {1, 15, 31})
    void test_9(int value) {
        CalendarManager localManager = CalendarManager.of(freeDaysManager, Month.DECEMBER, 2018); //Month should have 31 days to test
        assertThat(localManager.getDay(value)).isExactlyInstanceOf(Day.class);
    }

    @DisplayName("getMonth(): Should return this same month as LocalDate")
    @Test
    void test_10() {
        LocalDate now = LocalDate.now();
        assertThat(defaultCalendarManager.getMonth()).isEqualTo(now.getMonth());
    }

    @DisplayName("getYear(): Should return this same year as LocalDate")
    @Test
    void test_11() {
        LocalDate now = LocalDate.now();
        assertThat(defaultCalendarManager.getYear()).isEqualTo(now.getYear());
    }

    @DisplayName("countDaysInMonth(): Should return this same value as LocalDate lenght of month")
    @Test
    void test_12() {
        LocalDate now = LocalDate.now();
        assertThat(defaultCalendarManager.countDaysInMonth()).isEqualTo(now.lengthOfMonth());
    }

    @DisplayName("countWorkDaysInMonth(int): Try to get day with invalid value (ex. -1, 0, 32) should return null value")
    @Test
    void test_13() {
        CalendarManager localManager_December = CalendarManager.of(freeDaysManager, Month.DECEMBER, 2018); //Count without any holidays (exclude saturdays and sundays)
        assertThat(localManager_December.countWorkDaysInMonth()).isEqualTo(21);
        CalendarManager localManager_February = CalendarManager.of(freeDaysManager, Month.FEBRUARY, 2018); //Count without any holidays (exclude saturdays and sundays)
        assertThat(localManager_February.countWorkDaysInMonth()).isEqualTo(20);
    }

}