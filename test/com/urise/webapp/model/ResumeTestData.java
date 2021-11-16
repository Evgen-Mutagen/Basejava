package com.urise.webapp.model;

import java.time.Month;
import java.util.ArrayList;

public class ResumeTestData {
    public static final Resume RESUME_1;
    public static final Resume RESUME_2;
    public static final Resume RESUME_3;
    public static final Resume RESUME_4;
    public static final String UUID1 = "uuid1";
    public static final String UUID2 = "uuid2";
    public static final String UUID3 = "uuid3";
    public static final String UUID4 = "uuid4";

    static {
        RESUME_1 = new Resume(UUID1, "name1");
        RESUME_2 = new Resume(UUID2, "name2");
        RESUME_3 = new Resume(UUID3, "name3");
        RESUME_4 = new Resume(UUID4, "name4");
    }

    public static void main(String[] args) {
        getResume("uuid1", "Grigory Kislin");
        System.out.println(RESUME_1.getContacts());
        System.out.println(RESUME_1.getContacts().get(ContactType.MOBILE_PHONE));
        System.out.println(RESUME_1.getSections());
        System.out.println(RESUME_1.getSections().get(SectionType.EDUCATION));
    }

    public static Resume getResume(String uuid, String fullName) {
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

        RESUME_1.addSection(SectionType.EXPERIENCE, new OrganizationSection(new Organization(
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
                        "Аспирантура (программист С, С++)", " "))));

        return RESUME_1;
    }
}