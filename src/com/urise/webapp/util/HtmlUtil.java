package com.urise.webapp.util;

import com.urise.webapp.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlUtil {
    public static String getFullReference(ContactType ct, String value) {
        if (ct.getTitle() == null) return "" + value;
        return ((ct.getRef() == null) || (value == null)) ? ""
                : String.format("<a href=\"%s%s\">%2$s</a>", ct.getRef(), value);
    }

    public static <T> List<T> sectionToHtmlList(AbstractSection section) {
        if (section instanceof TextSection) {
            return (List<T>) Arrays.asList(((TextSection) section).getTitle());
        } else if (section instanceof ListSection) {
            return (List<T>) ((ListSection) section).getList();
        }  else if (section instanceof OrganizationSection) {
        return (List<T>) ((OrganizationSection) section).getOrganizations();
        }
        return new ArrayList<>();
    }

    public static String getTextCont(SectionType type, Resume resume) {
        AbstractSection section = resume.getSection(type);
        return section == null ? "" : ((TextSection) resume.getSection(type)).getTitle();
    }

    public static String getListCont(SectionType type, Resume resume) {
        AbstractSection section = resume.getSection(type);
        return
                section == null ? "" :
                        String.join("\n", ((ListSection) resume.getSection(type)).getList());
    }
}
