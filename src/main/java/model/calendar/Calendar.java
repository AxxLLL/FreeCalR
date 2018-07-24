package model.calendar;

import com.google.common.base.Preconditions;
import model.calendar.day.Day;
import model.calendar.day.DayType;
import model.calendar.freedays.FreeDay;
import model.calendar.freedays.FreeDaysManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

abstract public class Calendar {
    private List<Day> daysInMonthList;
    private LocalDate currentDate;
    private FreeDaysManager freeDaysManager;

    LocalDate getDate() {
        return currentDate;
    }

    List<Day> getDaysInMonthList() {
        return daysInMonthList;
    }

    FreeDaysManager getFreeDaysManager() {
        return freeDaysManager;
    }

    Calendar(FreeDaysManager manager, Month month, int year) throws IllegalArgumentException, NullPointerException {
        Preconditions.checkNotNull(manager, "Parametr menadżera musi być zainicjalizowany.");
        Preconditions.checkNotNull(month, "Parametr miesiąca musi być zainicjalizowany.");
        Preconditions.checkArgument(year >= 2000, "Parametr roku musi być większy lub równy 2000.");

        this.freeDaysManager = manager;
        this.currentDate = LocalDate.of(year, month.ordinal() + 1, 1);
        this.daysInMonthList = new ArrayList<>();

        initializeDaysInMonth();
    }

    void copy(Calendar calendar) {
        Preconditions.checkNotNull(calendar, "Obiekt do skopiowania musi zostać zainicjalizowany!");
        this.freeDaysManager = calendar.freeDaysManager;
        this.currentDate = calendar.currentDate;
        this.daysInMonthList = calendar.daysInMonthList;
    }

    private void initializeDaysInMonth() {
        DayType type;
        DayOfWeek dayOfWeek;
        Month month = currentDate.getMonth();
        LocalDate tempDate = LocalDate.of(currentDate.getYear(), month, 1);

        do {
            dayOfWeek = tempDate.getDayOfWeek();
            type = (dayOfWeek == DayOfWeek.SATURDAY ? DayType.SATURDAY : (dayOfWeek == DayOfWeek.SUNDAY ? DayType.HOLIDAY : DayType.NORMAL));
            daysInMonthList.add(Day.of(type));

            if(isFreeDayInMonth(month, tempDate.getDayOfMonth())) {
                daysInMonthList.get(tempDate.getDayOfMonth() - 1).setDayType(DayType.HOLIDAY);
            }

            tempDate = tempDate.plusDays(1);
        } while(tempDate.getMonth() == month);
    }

    private boolean isFreeDayInMonth(Month month, int day) {
        for(FreeDay freeDay : freeDaysManager.getFreeDayInMonthList(month)) {
            if(freeDay.getDay() == day) return true;
        }
        return false;
    }

}
