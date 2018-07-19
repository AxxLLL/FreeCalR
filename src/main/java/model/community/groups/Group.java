package model.community.groups;

import com.google.common.base.Preconditions;

import java.util.regex.Pattern;

public class Group {

    private boolean editable;
    private String name;

    public Group(String name) {
        this(name, true);
    }

    public Group(String name, boolean editable) {
        Preconditions.checkNotNull(name, "Parametr nazwy musi przyjmować wartość.");
        Preconditions.checkArgument(isValidName(name = name.trim()), "Nazwa może składać się z liter, cyfr oraz symboli myślnika lub podkreślenia, oraz musi zawierać się w przedziale od 2 do 32 znaków.");

        this.name = name;
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }

    public String getName() {
        return name;
    }

    public void copyGroupData(Group group) {
        Preconditions.checkNotNull(group, "Parametr grupy musi zostać określony!");
        Preconditions.checkState(isEditable(), "Tej grupy nie można edytować!");
        this.name = group.getName();
        this.editable = group.isEditable();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != getClass()) return false;
        if(obj == this) return true;
        Group gObj = (Group)obj;
        return gObj.name.equals(name);
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean isValidName(String name) {
        Pattern namePattern = Pattern.compile("^[\\wóęąśłńćźż \\-_]{2,32}$");
        return name != null && namePattern.matcher(name.toLowerCase()).matches();
    }
}
