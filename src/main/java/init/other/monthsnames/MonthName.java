package init.other.monthsnames;

import java.time.Month;
import java.util.List;

public interface MonthName {
    String getName(Month month);
    String getName(int month);
    int getMonthIndex(String name);
    List<String> getList();
}
