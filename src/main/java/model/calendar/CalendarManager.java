package model.calendar;

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
