package model.calendar;

import com.google.common.base.Preconditions;
import model.calendar.day.Day;
import model.calendar.day.DayType;
import model.calendar.freedays.FreeDaysManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class CalendarManager extends Calendar {

    public static CalendarManager of(FreeDaysManager manager, Month month, int year) {
        return new CalendarManager(manager, month, year);
    }

    public static CalendarManager of(FreeDaysManager manager) {
        LocalDate now = LocalDate.now();
        return new CalendarManager(manager, now.getMonth(), now.getYear());
    }

    public CalendarManager switchToNextYear() {
        return new CalendarManager(getFreeDaysManager(), getMonth(), getYear() + 1);
    }

    public CalendarManager switchToPreviousYear() {
        return new CalendarManager(getFreeDaysManager(), getMonth(), getYear() - 1);
    }

    public CalendarManager switchToNextMonth() throws IllegalStateException {
        Preconditions.checkState(getMonth().getValue() + 1 <= 12, "Wartość miesiąca przekroczyła dopuszczalną wartość");
        return new CalendarManager(getFreeDaysManager(), getMonth().plus(1), getYear());
    }

    public CalendarManager switchToPreviousMonth() {
        Preconditions.checkState(getMonth().getValue() - 1 >= 1, "Wartość miesiąca przekroczyła dopuszczalną wartość");
        return new CalendarManager(getFreeDaysManager(), getMonth().minus(1), getYear());
    }

    public DayOfWeek getStartingDayOfWeek() {
        return getDate().getDayOfWeek();
    }

    public Day getDay(int day) {
        return (isValidDayIndex(day) ? getDaysInMonthList().get(day - 1) : null);
    }

    public Month getMonth() {
        return getDate().getMonth();
    }

    public int getYear() {
        return getDate().getYear();
    }

    public long countWorkDaysInMonth() {
        return getDaysInMonthList().stream().filter(day -> day.getDayType() == DayType.NORMAL).count();
    }

    public int countDaysInMonth() {
        return getDate().lengthOfMonth();
    }

    private boolean isValidDayIndex(int index) {
        return (index > 0 && index <= getDate().lengthOfMonth());
    }

    private CalendarManager(FreeDaysManager manager, Month month, int year) {
        super(manager, month, year);
    }
}

