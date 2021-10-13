package com.urise.webapp.model;

public enum ContactType {
    MOBILE_PHONE("Мобильный телефон"),
    MAIL("Электронная почта"),
    SKYPE("Skype"),
    GITHUB("Github"),
    LINKEDIN("Linkedin"),
    STACKOVERFLOW("Stackoverflow");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "ContactType{" +
                "title='" + title + '\'' +
                '}';
    }
}
