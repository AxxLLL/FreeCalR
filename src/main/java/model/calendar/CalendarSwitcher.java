package model.calendar;

import com.google.common.base.Preconditions;

public class CalendarSwitcher {

    public static CalendarManager switchToNextYear(CalendarManager current) {
        return CalendarManager.of(current.getFreeDaysManager(), current.getMonth(), current.getYear() + 1);
    }

    public static CalendarManager switchToPreviousYear(CalendarManager current) {
        return CalendarManager.of(current.getFreeDaysManager(), current.getMonth(), current.getYear() - 1);
    }

    public static CalendarManager switchToNextMonth(CalendarManager current) throws IllegalStateException {
        Preconditions.checkState(current.getMonth().getValue() + 1 <= 12, "Wartość miesiąca przekroczyła dopuszczalną wartość");
        return CalendarManager.of(current.getFreeDaysManager(), current.getMonth().plus(1), current.getYear());
    }

    public static CalendarManager switchToPreviousMonth(CalendarManager current) {
        Preconditions.checkState(current.getMonth().getValue() - 1 >= 1, "Wartość miesiąca przekroczyła dopuszczalną wartość");
        return CalendarManager.of(current.getFreeDaysManager(), current.getMonth().minus(1), current.getYear());
    }
}
