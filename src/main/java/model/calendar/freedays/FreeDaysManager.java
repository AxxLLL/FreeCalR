package model.calendar.freedays;

import com.google.common.base.Preconditions;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeDaysManager {

    private Map<Month, List<FreeDay>> mapOfFreeDays;
    private static final int[] maxDaysInMonth = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public FreeDaysManager() {
        mapOfFreeDays = new HashMap<>();
        for(Month month : Month.values()) {
            mapOfFreeDays.put(month, new ArrayList<>());
        }
    }

    public boolean add(Month month, FreeDay day) {
        Preconditions.checkNotNull(day, "Parametr dnia nie może być null'em.");
        Preconditions.checkNotNull(month, "Parametr miesiąca nie może być null'em.");
        Preconditions.checkArgument(day.getDay() <= maxDaysInMonth[month.ordinal()], "Wartość dnia nie może przekraczać maksymalnej liczby dni w wybranym miesiącu (" + day.getDay() + " <= " + maxDaysInMonth[month.ordinal()] + ")");

        List<FreeDay> listOfFreeDaysInMonth = mapOfFreeDays.get(month);
        if(!listOfFreeDaysInMonth.contains(day)) {
            return listOfFreeDaysInMonth.add(day);
        }
        return false;
    }

    public boolean remove(Month month, FreeDay day) {
        Preconditions.checkNotNull(day, "Parametr dnia nie może być null'em.");
        Preconditions.checkNotNull(month, "Parametr miesiąca nie może być null'em.");

        List<FreeDay> listOfFreeDaysInMonth = mapOfFreeDays.get(month);
        if(listOfFreeDaysInMonth.contains(day)) {
            return listOfFreeDaysInMonth.remove(day);
        }
        return false;
    }

    public int countFreeDaysInMonth(Month month) {
        Preconditions.checkNotNull(month, "Parametr miesiąca nie może być null'em.");
        return mapOfFreeDays.get(month).size();
    }

    public List<FreeDay> getFreeDayInMonthList(Month month) {
        Preconditions.checkNotNull(month, "Parametr miesiąca nie może być null'em.");
        return new ArrayList<>(mapOfFreeDays.get(month));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        mapOfFreeDays.forEach((key, value) -> {
            if(value.size() > 0) {
                builder.append(String.format("* Month: %s [%d free day(s)]%n", key, value.size()));
                value.forEach(day -> builder.append(String.format("- %s%n", day)));
                builder.append("*" + System.lineSeparator());
            }
        });

        builder.delete(builder.length() - 3, builder.length());
        return builder.toString();
    }

}
