package init.other;

import model.calendar.freedays.FreeDay;
import model.calendar.freedays.FreeDaysManager;

import java.time.LocalDate;
import java.time.Month;

public class FreeDaysInPoland {

    public static void addMovableFreeDaysInPolandToManager(FreeDaysManager manager, int year) {
        for(Month month : Month.values()) {
            manager.getFreeDayInMonthList(month).forEach(day -> {
                if(day.isMovable()) {
                    manager.remove(month, day);
                }
            });
        }

        LocalDate monEasterDate = MovableFeast.getEasterDate(year).plusDays(1);
        LocalDate corpustCristDate = MovableFeast.getCorpusChristiDate(year);
        manager.add(monEasterDate.getMonth(), FreeDay.of(monEasterDate.getDayOfMonth(), "Poniedziałek wielkanocny", true));
        manager.add(corpustCristDate.getMonth(), FreeDay.of(corpustCristDate.getDayOfMonth(), "Boże ciało", true));
    }

    public static void addStaticFreeDaysInPolandToManager(FreeDaysManager manager) {
        manager.add(Month.JANUARY, FreeDay.of(1, "Nowy rok"));
        manager.add(Month.JANUARY, FreeDay.of(6, "Trzech króli"));

        manager.add(Month.MAY, FreeDay.of(1, "Święto Pracy"));
        manager.add(Month.MAY, FreeDay.of(3, "Święto Konstytucji 3'ego Maja"));

        manager.add(Month.AUGUST, FreeDay.of(15, "Wniebowzięcie NMP"));

        manager.add(Month.NOVEMBER, FreeDay.of(1, "Wszystkich Świętych"));
        manager.add(Month.NOVEMBER, FreeDay.of(11, "Święto Niepodległości"));

        manager.add(Month.DECEMBER, FreeDay.of(25, "Boże Narodzenie - pierwszy dzień"));
        manager.add(Month.DECEMBER, FreeDay.of(26, "Boże Narodzenie - drugi dzień"));
    }
}
