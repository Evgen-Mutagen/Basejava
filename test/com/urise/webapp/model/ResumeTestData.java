package com.urise.webapp.model;


import java.time.Month;
import java.time.YearMonth;

public class ResumeTestData {
    public static final Resume R1;
    public static final Resume R2;
    public static final Resume R3;
    public static final Resume R4;

    static {
        R1 = new Resume("uuid1", "Grigory Kislin");
        R2 = new Resume("uuid2", "name2");
        R3 = new Resume("uuid3", "name3");
        R4 = new Resume("uuid4", "name4");
    }

    public static void main(String[] args) {
        R1.getContact().put(ContactType.MOBILE_PHONE, " +7(921) 855-0482");
        R1.getContact().put(ContactType.MAIL, " gkislin@yandex.ru");
        R1.getContact().put(ContactType.SKYPE, "grigory.kislin");
        R1.getContact().put(ContactType.GITHUB, "github.com/gkislin");
        R1.getContact().put(ContactType.STACKOVERFLOW, "stackoverflow.com/users/548473/grigory-kislin");
        System.out.println(R1.getContact());
        System.out.println(R1.getContact().get(ContactType.MOBILE_PHONE));

        TextSection personal = new TextSection("Аналитический склад ума, сильная логика, креативность," +
                " инициативность. Пурист кода и архитектуры.");
        R1.getSection().put(SectionType.PERSONAL, personal);

        TextSection objective = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web" +
                " и Enterprise технологиям");
        R1.getSection().put(SectionType.OBJECTIVE, objective);

        ListSection qualification = new ListSection();
        qualification.save("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        R1.getSection().put(SectionType.QUALIFICATIONS, qualification);

        ListSection achievement = new ListSection();
        achievement.save("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP)." +
                " Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и " +
                "ведение проектов. Более 1000 выпускников.");
        R1.getSection().put(SectionType.ACHIEVEMENT, achievement);

        String name = "Wrike";
        String url = "https://www.wrike.com";
        String title = "Старший разработчик (backend)";
        YearMonth startOfWork = YearMonth.of(2014, Month.OCTOBER);
        YearMonth endOfWork = YearMonth.of(2016, Month.JANUARY);
        String description = ("Старший разработчик (backend)\n" +
                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring," +
                " MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, " +
                "авторизация по OAuth1, OAuth2, JWT SSO.");
        Organization company = new Organization(name, url, startOfWork, endOfWork, title, description);

        OrganizationSection companyName = new OrganizationSection();
        companyName.save(company);
        R1.getSection().put(SectionType.EXPERIENCE, companyName);

        name = "Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики";
        url = "https://itmo.ru/ru/";
        title = "Инженер (программист Fortran, C)";
        startOfWork = YearMonth.of(1993, Month.SEPTEMBER);
        endOfWork = YearMonth.of(1996, Month.JULY);
        description = " ";

        Organization univesety = new Organization(name, url, startOfWork, endOfWork, title, description);

        title = "Аспирантура (программист С, С++)";
        startOfWork = YearMonth.of(1987, Month.SEPTEMBER);
        endOfWork = YearMonth.of(1993, Month.JULY);

        univesety.addPeriod(startOfWork, endOfWork, title, description);
        R1.getSection().put(SectionType.EDUCATION, univesety);

        System.out.println(R1.getSection());
        System.out.println(R1.getSection().get(SectionType.EDUCATION));

    }
}