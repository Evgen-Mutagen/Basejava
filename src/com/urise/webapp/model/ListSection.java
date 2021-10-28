package com.urise.webapp.model;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<String> list;

    public ListSection(List<String> list) {
        this.list = list;
    }

    public void save(String text) {
        list.add(text);
    }

    public List<String> getList() {
        return list;
    }

    public ListSection() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return "ListSection{" +
                "list=" + list +
                '}';
    }
}
