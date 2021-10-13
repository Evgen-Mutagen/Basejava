package com.urise.webapp.model;

import java.time.YearMonth;

public class Period {
    private final YearMonth startOfWork;
    private final YearMonth endOfWork;
    private final String title;
    private final String description;

    public Period(YearMonth startOfWork, YearMonth endOfWork, String title, String description) {
        this.startOfWork = startOfWork;
        this.endOfWork = endOfWork;
        this.title = title;
        this.description = description;
    }

    public YearMonth getStartOfWork() {
        return startOfWork;
    }

    public YearMonth getEndOfWork() {
        return endOfWork;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!startOfWork.equals(period.startOfWork)) return false;
        if (!endOfWork.equals(period.endOfWork)) return false;
        if (!title.equals(period.title)) return false;
        return description.equals(period.description);
    }

    @Override
    public int hashCode() {
        int result = startOfWork.hashCode();
        result = 31 * result + endOfWork.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Period{" +
                "startOfWork=" + startOfWork +
                ", endOfWork=" + endOfWork +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
