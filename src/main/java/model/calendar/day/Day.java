package model.calendar.day;

import com.google.common.base.Preconditions;

public class Day {

    private DayType dayType;
    private final DayType baseDayType;

    public static Day of(DayType type) throws IllegalArgumentException  {
        Preconditions.checkNotNull(type, "Parametr typu dnia nie może być null'em.");
        return new Day(type);
    }

    public DayType switchDayType() {
        switch(baseDayType) {
            case NORMAL: dayType = dayType == baseDayType ? DayType.HOLIDAY : baseDayType; break;
            case HOLIDAY: dayType = dayType == baseDayType ? DayType.NORMAL : baseDayType; break;
            case SATURDAY:
                dayType = dayType == baseDayType ? DayType.NORMAL : dayType == DayType.NORMAL ? DayType.HOLIDAY : baseDayType;
                break;
        }
        return dayType;
    }

    public boolean setDayType(DayType type) throws IllegalArgumentException {
        Preconditions.checkNotNull(type, "Parametr typu dnia nie może być null'em.");
        if(type != dayType) {
            dayType = type;
            return true;
        }
        return false;
    }

    public DayType getDayType() {
        return dayType;
    }

    public DayType getBaseDayType() {
        return baseDayType;
    }

    private Day(DayType type) {
        this.dayType = type;
        this.baseDayType = type;
    }
}
