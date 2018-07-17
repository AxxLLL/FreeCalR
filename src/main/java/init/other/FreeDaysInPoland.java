package init.other;

import model.calendar.Month;
import model.calendar.freedays.FreeDay;
import model.calendar.freedays.FreeDaysManager;

public class FreeDaysInPoland {

    public static void addMovableFreeDaysInPolandToManager(FreeDaysManager manager) {
        /*
        * TO DO
        * */
        manager.add(Month.APRIL, FreeDay.of(1, "Wielkanoc"));
        manager.add(Month.APRIL, FreeDay.of(2, "Poniedziałek Wielkanocny"));
    }

    public static void addStaticFreeDaysInPolandToManager(FreeDaysManager manager) {
        manager.add(Month.JANUARY, FreeDay.of(1, "Nowy rok"));
        manager.add(Month.JANUARY, FreeDay.of(6, "Trzech króli"));

        manager.add(Month.MAY, FreeDay.of(1, "Święto Pracy"));
        manager.add(Month.MAY, FreeDay.of(3, "Święto Konstytucji 3'ego Maja"));
        manager.add(Month.MAY, FreeDay.of(20, "Zielone Świątki"));
        manager.add(Month.MAY, FreeDay.of(31, "Boże Ciało"));

        manager.add(Month.AUGUST, FreeDay.of(15, "Wniebowzięcie NMP"));

        manager.add(Month.NOVEMBER, FreeDay.of(1, "Wszystkich Świętych"));
        manager.add(Month.NOVEMBER, FreeDay.of(11, "Święto Niepodległości"));

        manager.add(Month.DECEMBER, FreeDay.of(25, "Boże Narodzenie - pierwszy dzień"));
        manager.add(Month.DECEMBER, FreeDay.of(26, "Boże Narodzenie - drugi dzień"));

    }
}
