/*
* TO DO List
* - Zaimplementować listę ruchomych świąt w klasie init.other.FreeDaysInPoland w metodzie addMovableFreeDaysInPolandToManager.
* */

package init;

import init.other.FreeDaysInPoland;
import model.calendar.CalendarManager;
import model.calendar.freedays.FreeDaysManager;
import model.community.groups.GroupManager;
import model.community.users.UsersManager;

public class Main {
    private static final FreeDaysManager freeDaysManager = new FreeDaysManager();
    private static CalendarManager calendarManager;
    private static GroupManager groupManager = GroupManager.of();
    private static UsersManager usersManager = UsersManager.of();


    public static void main(String[] args) {
        FreeDaysInPoland.addStaticFreeDaysInPolandToManager(freeDaysManager);
        FreeDaysInPoland.addMovableFreeDaysInPolandToManager(freeDaysManager);
        calendarManager = CalendarManager.of(freeDaysManager);

    }

    public static FreeDaysManager getFreeDaysManager() {
        return freeDaysManager;
    }

    public static CalendarManager getCalendarManager() {
        return calendarManager;
    }

    public static GroupManager getGroupManager() {
        return groupManager;
    }

    public static UsersManager getUsersManager() { return usersManager; }

}
