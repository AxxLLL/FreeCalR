package model.community;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public abstract class CommunityListAbs<T> {
    private List<T> dataList;

    public CommunityListAbs() {
        dataList = new ArrayList<>();
    }

    public List<T> getList() {
        return new ArrayList<>(dataList);
    }

    public boolean add(T data) {
        Preconditions.checkNotNull(data, "Parametr obiektu do dodania musi zostać określony.");
        return !dataList.contains(data) && dataList.add(data);
    }

    public boolean remove(T data) {
        Preconditions.checkNotNull(data, "Pozycja do usunięcia musi zostac określona.");
        return dataList.removeIf(obj -> obj.equals(data));
    }

    public T getByIndex(int index) {
        return isValidIndex(index) ? dataList.get(index) : null;
    }

    public int getIndex(T data) {
        return dataList.indexOf(data);
    }

    public int getSize() {
        return dataList.size();
    }

    public boolean isExists(T data) {
        return dataList.contains(data);
    }

    public void clear() {
        dataList.clear();
    }

    public void downInList(T item) {
        Preconditions.checkNotNull(item);
        int index = dataList.indexOf(item);
        if(index >= 0 && dataList.size() -1 > index) {
            dataList.set(index, dataList.get(index + 1));
            dataList.set(index + 1, item);
        }
    }

    public void upInList(T item) {
        Preconditions.checkNotNull(item);
        int index = dataList.indexOf(item);
        if(index >= 0 && index > 0) {
            dataList.set(index, dataList.get(index - 1));
            dataList.set(index - 1, item);
        }
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < dataList.size();
    }
}
