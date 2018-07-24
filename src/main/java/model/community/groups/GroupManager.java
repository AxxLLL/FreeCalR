package model.community.groups;

import com.google.common.base.Preconditions;
import model.community.CommunityListAbs;
import model.community.users.UsersManager;

public class GroupManager extends CommunityListAbs<Group> {

    public static final Group DEFAULT_GROUP = new Group("Brak", false);
    private UsersManager usersManager;

    public static GroupManager of(UsersManager usersManager) {
        return new GroupManager(usersManager);
    }

    @Override
    public boolean remove(Group group) {
        Preconditions.checkNotNull(group, "Pozycja do usunięcia musi zostac określona.");
        Preconditions.checkState(group.isEditable(), "Nie można usunąć tej grupy z listy.");

        if(isExists(group)) {
            usersManager.getList().forEach(user -> {
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

    private GroupManager(UsersManager usersManager) {
        super();
        Preconditions.checkNotNull(usersManager, "Menedżer użytkowników dla grupy musi zostać określony.");
        this.usersManager = usersManager;
        add(DEFAULT_GROUP);
    }

    public long countUsersInGroup(Group group) {
        if(group == null) return 0;
        return usersManager.getList().stream().filter(u -> u.getGroup().equals(group)).count();
    }
}
