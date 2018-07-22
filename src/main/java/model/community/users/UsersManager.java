package model.community.users;

import model.community.CommunityListAbs;

public class UsersManager extends CommunityListAbs<User> {

    public static UsersManager of() {
        return new UsersManager();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("* - List of model.users_and_groups.users" + System.lineSeparator());
        getList().forEach(user -> {
            sb.append(String.format("* First name: %s | Last name: %s | Group: %s%n", user.getFirstName(), user.getLastName(), user.getGroupName()));
        });
        return sb.toString();
    }

    private UsersManager() {
        super();
    }
}
