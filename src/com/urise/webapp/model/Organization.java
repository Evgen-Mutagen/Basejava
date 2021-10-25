package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@XmlAccessorType(XmlAccessType.FIELD)
public class Organization extends AbstractSection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Link homePage;
    private List<Period> periods = new ArrayList<>();

    public Organization(String name, String url, YearMonth startOfWork, YearMonth endOfWork, String title, String description) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(startOfWork, "startOfWork must not be null");
        Objects.requireNonNull(endOfWork, "endOfWork must not be null");
        Objects.requireNonNull(title, "title must not be null");
        this.homePage = new Link(name, url);
        this.periods.add(new Period(startOfWork, endOfWork, title, description));
    }


    public Organization(Link homePage, List<Period> periods) {
        this.homePage = homePage;
        this.periods = periods;
    }


    public void addPeriod(YearMonth startOfWork, YearMonth endOfWork, String title, String description) {
        this.periods.add(new Period(startOfWork, endOfWork, title, description));
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!homePage.equals(that.homePage)) return false;
        return periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + periods.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homePage=" + homePage +
                ", periods=" + periods +
                '}';
    }
}

