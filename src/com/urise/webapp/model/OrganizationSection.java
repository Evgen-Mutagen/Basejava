package com.urise.webapp.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<Organization> list = new ArrayList<>();

    public void save(Organization name) {
        list.add(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return "OrganizationSection{" +
                "list=" + list +
                '}';
    }
}

