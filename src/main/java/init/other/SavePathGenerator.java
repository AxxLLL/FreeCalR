package init.other;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SavePathGenerator {

    enum SYSTEM_TYPE {WINDOWS}

    private static final Map<String, List<Object>> systemPredefinedValues = new HashMap<>();
    static {
        systemPredefinedValues.put("Windows XP", new ArrayList<>(Arrays.asList(SYSTEM_TYPE.WINDOWS, "XP")));
        systemPredefinedValues.put("Windows 7", new ArrayList<>(Arrays.asList(SYSTEM_TYPE.WINDOWS, "7")));
        systemPredefinedValues.put("Windows 8", new ArrayList<>(Arrays.asList(SYSTEM_TYPE.WINDOWS, "8")));
        systemPredefinedValues.put("Windows 8.1", new ArrayList<>(Arrays.asList(SYSTEM_TYPE.WINDOWS, "8.1")));
        systemPredefinedValues.put("Windows 10", new ArrayList<>(Arrays.asList(SYSTEM_TYPE.WINDOWS, "10")));
    }

    public static Path getPath() {
        String systemName = System.getProperty("os.name");
        switch((SYSTEM_TYPE)systemPredefinedValues.get(systemName).get(0)) {
            case WINDOWS:
                return getPathForWindows(systemName);
            default:
                return getDefaultPath();
        }
    }

    private static Path getPathForWindows(String systemName) {
        final String home = System.getProperty("user.home").replace("\\", "/");
        final String oldWindowsPath = home + "/Application Data";
        final String newWindowsPath = home + "/AppData";

        if (new File(oldWindowsPath).exists()) {
            return Paths.get(oldWindowsPath, "FreeCalR");
        } else if (new File(newWindowsPath).exists()) {
            return Paths.get(newWindowsPath, "Local", "FreeCalR");
        } else {
            return getDefaultPath();
        }
    }

    private static Path getDefaultPath() {
        final String home = System.getProperty("user.home").replace("\\", "/");
        return Paths.get(home, "FreeCalR");
    }
}
