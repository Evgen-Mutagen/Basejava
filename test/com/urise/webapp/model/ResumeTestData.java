package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.UUID;

public class ResumeTestData {
    public static Resume RESUME_1;
    public static final Resume RESUME_2;
    public static final Resume RESUME_3;
    public static final Resume RESUME_4;
    public static final String UUID1 = UUID.randomUUID().toString();
    public static final String UUID2 = UUID.randomUUID().toString();
    public static final String UUID3 = UUID.randomUUID().toString();
    public static final String UUID4 = UUID.randomUUID().toString();

    static {
        RESUME_1 = new Resume(UUID1, "name1");
        RESUME_2 = new Resume(UUID2, "name2");
        RESUME_3 = new Resume(UUID3, "name3");
        RESUME_4 = new Resume(UUID4, "name4");
    }

    public static void main(String[] args) {
        getResume1("uuid1", "Grigory Kislin");
        getResume2("uuid1", "Grigory Kislin");
        System.out.println(RESUME_2.getSections());
    }

    public static Resume getResume1(String uuid, String fullName) {
        RESUME_1.addContact(ContactType.MOBILE_PHONE, " +7(921) 855-0482");
        RESUME_1.addContact(ContactType.MAIL, " gkislin@yandex.ru");
        RESUME_1.addContact(ContactType.SKYPE, "grigory.kislin");
        RESUME_1.addContact(ContactType.GITHUB, "github.com/gkislin");
        RESUME_1.addContact(ContactType.STACKOVERFLOW, "stackoverflow.com/users/548473/grigory-kislin");
        TextSection personal = new TextSection("Аналитический склад ума, сильная логика, креативность," +
                " инициативность. Пурист кода и архитектуры.");
        RESUME_1.addSection(SectionType.PERSONAL, personal);

        TextSection objective = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web" +
                " и Enterprise технологиям");
        RESUME_1.addSection(SectionType.OBJECTIVE, objective);

        ListSection qualification = new ListSection(new ArrayList<>());
        qualification.save("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        RESUME_1.addSection(SectionType.QUALIFICATIONS, qualification);

        ListSection achievement = new ListSection(new ArrayList<>());
        achievement.save("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP)." +
                " Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и " +
                "ведение проектов. Более 1000 выпускников.");
        RESUME_1.addSection(SectionType.ACHIEVEMENT, achievement);

       /* RESUME_1.addSection(SectionType.EXPERIENCE, new OrganizationSection(new Organization(
                "Wrike", "https://www.wrike.com",
                new Organization.Position(2014, Month.OCTOBER, 2016, Month.JANUARY,
                        "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы" +
                        " управления проектами Wrike (Java 8 API, Maven, Spring," +
                        " MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, " +
                        "авторизация по OAuth1, OAuth2, JWT SSO."))));

        RESUME_1.addSection(SectionType.EDUCATION, new OrganizationSection(new Organization("Санкт-Петербургский " +
                "национальный исследовательский университет информационных технологий, механики и оптики",
                "https://itmo.ru/ru/",
                new Organization.Position(1987, Month.SEPTEMBER,
                        1993, Month.JULY, "Инженер (программист Fortran, C)", " "),
                new Organization.Position(1993, Month.SEPTEMBER, 1996, Month.JULY,
                        "Аспирантура (программист С, С++)", " ")))); */
        return RESUME_1;
    }

    public static Resume getResume2(String uuid, String fullName) {
        RESUME_2.addContact(ContactType.MOBILE_PHONE, " +7900000000");
        RESUME_2.addContact(ContactType.MAIL, " pikachu@yandex.ru");

        TextSection objective2 = new TextSection("Главный разработчик разработок");
        RESUME_2.addSection(SectionType.OBJECTIVE, objective2);

        ListSection qualification2 = new ListSection(new ArrayList<>());
        qualification2.save("много знаний");
        qualification2.save("много знаний2");
        qualification2.save("много знаний3");
        RESUME_2.addSection(SectionType.QUALIFICATIONS, qualification2);
        return RESUME_2;
    }

    public static Resume getResume3(String uuid, String fullName) {
        RESUME_3.addContact(ContactType.MOBILE_PHONE, " +76666666666");
        RESUME_3.addContact(ContactType.SKYPE, "Alexander Nevsky");
        RESUME_3.addContact(ContactType.MAIL, " Mr.Universe@yahoo.com");

        TextSection objective3 = new TextSection("Мистер вселенная");
        RESUME_3.addSection(SectionType.OBJECTIVE, objective3);

        ListSection achievement3 = new ListSection(new ArrayList<>());
        achievement3.save("актёр, режиссёр, спортсмен");
        RESUME_3.addSection(SectionType.ACHIEVEMENT, achievement3);
        return  RESUME_3;
    }
}