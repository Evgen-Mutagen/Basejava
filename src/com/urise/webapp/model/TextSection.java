package com.urise.webapp.model;

import java.util.Objects;

public class TextSection extends AbstractSection {
    private static final long serialVersionUID = 1L;
    private String title;
    public static final TextSection NEW = new TextSection("");

    public TextSection(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public TextSection() {
    }

    public static String getTextCont(SectionType type, Resume resume) {
        AbstractSection section = resume.getSection(type);
        return section == null ? "" : ((TextSection) resume.getSection(type)).getTitle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSection that = (TextSection) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "TextSection{" +
                "title='" + title + '\'' +
                '}';
    }
}
