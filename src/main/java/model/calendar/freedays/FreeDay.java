package model.calendar.freedays;

import com.google.common.base.Preconditions;

import java.util.regex.Pattern;

public class FreeDay {
    private int day;
    private String name;
    private static final Pattern dayNamePattern = Pattern.compile("^[\\wóęąśłńćźż '-]{1,32}$");


    public static FreeDay of(int day) throws IllegalArgumentException {
        Preconditions.checkArgument(day > 0 && day <= 31, "Dzień musi być większy od 0 i mniejszy lub równy 31.");
        return FreeDay.of(day, "None");
    }

    public static FreeDay of(int day, String name) throws IllegalArgumentException {
        Preconditions.checkNotNull(name, "Parametr nazwy dnia nie może być null'em. Jeśli nie chcesz używać nazwy, zastosuj przeciążoną metodę 'of(int day)'.");
        Preconditions.checkArgument(!name.isEmpty(), "Nazwa dnia wolnego nie może być pusta. Jeśli nie chcesz używać nazwy, zastosuj przeciążoną metodę 'of(int day)'.");
        Preconditions.checkArgument(dayNamePattern.matcher((name = name.trim()).toLowerCase()).matches(), "Nazwa dnia wolnego może się składać się wyłącznie z liter, cyfr i znaku apostrofu.");
        return new FreeDay(day, name);
    }

    public int getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public FreeDay getCopy() {
        return FreeDay.of(day, name);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + day;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj.getClass() == getClass())) return false;
        if(obj == this) return true;

        FreeDay oDay = ((FreeDay)obj);
        return oDay.getDay() == getDay() && oDay.getName().equals(getName());
    }

    @Override
    public String toString() {
        return "Day: " + day + " | Name: " + name;
    }

    private FreeDay(int day, String name) {
        this.day = day;
        this.name = name;
    }

}
