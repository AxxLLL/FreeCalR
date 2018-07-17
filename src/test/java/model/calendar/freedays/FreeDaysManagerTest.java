package model.calendar.freedays;

import model.calendar.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class FreeDaysManagerTest {
    private FreeDaysManager manager;

    @BeforeEach
    void before() {
        manager = new FreeDaysManager();
    }

    /*
    * This test checking private method: 'addSurlyValidDaysToManager' too. Const. number of '5' is from count elems in 'getSampleList' method.
    * */
    @DisplayName("add(Month, FreeDay): Test valid elements. They should be added to list.")
    @Test
    void test_1() {
        Month month = Month.JANUARY;
        assertThat(addSurlyValidDaysToManager(month)).isTrue();
        assertThat(manager.countFreeDaysInMonth(month)).isEqualTo(5);
    }

    @DisplayName("add(Month, FreeDay): Null values of month or day should throw IllegalArgumentException.")
    @Test
    void test_2() {
        assertThatNullPointerException().isThrownBy(() -> manager.add(null, FreeDay.of(1)));
        assertThatNullPointerException().isThrownBy(() -> manager.add(Month.APRIL, null));
    }

    @DisplayName("add(Month, FreeDay): Try to add existing days, should return false.")
    @Test
    void test_3() {
        Month month = Month.APRIL;
        addSurlyValidDaysToManager(month);

        getSampleList().forEach(day -> {
            assertThat(manager.add(month, day)).isFalse();
        });

        assertThat(manager.countFreeDaysInMonth(month)).isEqualTo(getSampleList().size());

    }

    @DisplayName("remove(Month, FreeDay): Null values of month or day should throw IllegalArgumentException.")
    @Test
    void test_4() {
        assertThatNullPointerException().isThrownBy(() -> manager.remove(null, FreeDay.of(1)));
        assertThatNullPointerException().isThrownBy(() -> manager.remove(Month.MARCH, null));
    }

    @DisplayName("remove(Month, FreeDay): Remove valid elements should return true.")
    @Test
    void test_5() {
        Month month = Month.JANUARY;
        addSurlyValidDaysToManager(month);
        manager.getFreeDayInMonthList(month).forEach(day -> {
            assertThat(manager.remove(month, day)).isTrue();
        });
        assertThat(manager.countFreeDaysInMonth(month)).isZero();
    }

    @DisplayName("remove(Month, FreeDay): Remove invalid elements should return false.")
    @ParameterizedTest
    @ValueSource(ints = {20, 21, 22, 23})
    void test_6(int value) {
        Month month = Month.JANUARY;
        addSurlyValidDaysToManager(month);
        assertThat(manager.remove(month, FreeDay.of(value))).isFalse();
        assertThat(manager.countFreeDaysInMonth(month)).isEqualTo(getSampleList().size());
    }

    @DisplayName("getFreeDaysInMonth(Month): Should return list with this same elems as getSamleList.")
    @Test
    void test_7() {
        Month month = Month.JUNE;
        addSurlyValidDaysToManager(month);
        assertThat(manager.getFreeDayInMonthList(month)).containsExactlyElementsOf(getSampleList());
    }

    @DisplayName("countFreeDaysInMonth(Month): Should return size of list same as getSampleList.")
    @Test
    void test_8() {
        Month month = Month.JUNE;
        addSurlyValidDaysToManager(month);
        assertThat(manager.countFreeDaysInMonth(month)).isEqualTo(getSampleList().size());
    }

    private List<FreeDay> getSampleList() {
        List<FreeDay> validFreeDaysList = new ArrayList<>();
        validFreeDaysList.add(FreeDay.of(1));
        validFreeDaysList.add(FreeDay.of(2));
        validFreeDaysList.add(FreeDay.of(3));
        validFreeDaysList.add(FreeDay.of(4));
        validFreeDaysList.add(FreeDay.of(5));
        return validFreeDaysList;
    }

    private boolean addSurlyValidDaysToManager(Month month) {
        List<FreeDay> list = getSampleList();
        list.forEach(day -> {
            manager.add(month, day);
        });
        return list.size() == manager.countFreeDaysInMonth(month);
    }
}