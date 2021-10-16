package com.urise.webapp.model;

import java.io.Serial;
import java.util.*;
import java.io.Serializable;

/**
 * Initial resume class
 */
public class Resume implements Serializable  {
    @Serial
    private static final long serialVersionUID = 1L;

    private Map<SectionType, AbstractSection> section = new EnumMap<>(SectionType.class);
    private Map<ContactType, String> contact = new EnumMap<>(ContactType.class);

    private final String uuid;
    private final String fullName;

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must non be  null ");
        Objects.requireNonNull(fullName, "fullName must non be  null ");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public Map<SectionType, AbstractSection> getSection() {
        return section;
    }

    public Map<ContactType, String> getContact() {
        return contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!section.equals(resume.section)) return false;
        if (!contact.equals(resume.contact)) return false;
        if (!uuid.equals(resume.uuid)) return false;
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = section.hashCode();
        result = 31 * result + contact.hashCode();
        result = 31 * result + uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Resume{" +
                "section=" + section +
                ", contact=" + contact +
                ", uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}