package init.other.monthsnames;

import com.google.common.base.Preconditions;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MonthNames_PL implements MonthName {
    private static final List<String> monthNames = new ArrayList<>(Arrays.asList("Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"));

    @Override
    public String getName(Month month) {
        Preconditions.checkNotNull(month);
        return monthNames.get(month.getValue() - 1);
    }

    @Override
    public String getName(int month) {
        Preconditions.checkArgument(month >= 1 && month <= 12);
        return monthNames.get(month - 1);
    }

    @Override
    public int getMonthIndex(String name) {
        Preconditions.checkNotNull(name);
        return monthNames.indexOf(name) + 1;
    }

    @Override
    public List<String> getList() {
        return new ArrayList<>(monthNames);
    }
}
