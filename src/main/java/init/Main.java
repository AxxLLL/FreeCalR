/*
* TO DO List
* - Zaimplementować listę ruchomych świąt w klasie init.other.FreeDaysInPoland w metodzie addMovableFreeDaysInPolandToManager.
* */

package init;

import init.other.FreeDaysInPoland;
import model.calendar.freedays.FreeDaysManager;

public class Main {
    private static final FreeDaysManager freeDaysManager = new FreeDaysManager();

    public static void main(String[] args) {
        FreeDaysInPoland.addStaticFreeDaysInPolandToManager(freeDaysManager);
        FreeDaysInPoland.addMovableFreeDaysInPolandToManager(freeDaysManager);
    }

}