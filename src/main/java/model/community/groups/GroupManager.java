package model.community.groups;

import com.google.common.base.Preconditions;
import init.Main;
import model.community.CommunityListAbs;

public class GroupManager extends CommunityListAbs<Group> {

    public static final Group DEFAULT_GROUP = new Group("Brak", false);

    public static GroupManager of() {
        return new GroupManager();
    }

    @Override
    public boolean remove(Group group) {
        Preconditions.checkNotNull(group, "Pozycja do usunięcia musi zostac określona.");
        Preconditions.checkState(group.isEditable(), "Nie można usunąć tej grupy z listy.");

        if(isExists(group)) {
            Main.getUsersManager().getList().forEach(user -> {
               if(user.getGroup() == group) {
                   user.setGroup(DEFAULT_GROUP);
               }
            });
            return super.remove(group);
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("* - List of model.users_and_groups.groups" + System.lineSeparator());
        for(int i = 0; i < getSize(); i ++) {
            sb.append(String.format("* ID: %d | Name: %s%n", i, getByIndex(i).getName()));
        }
        sb.append("* - End of list");
        return sb.toString();
    }

    private GroupManager() {
        super();
        add(DEFAULT_GROUP);
    }
}
