package com.urise.webapp.util;

import com.urise.webapp.model.*;

public class HtmlUtil {

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

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

}
