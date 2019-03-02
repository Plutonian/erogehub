package com.goexp.galgame.common.model;

import java.time.LocalDate;

public class DateRange {

    public final LocalDate start;

    public final LocalDate end;

    public DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "DateRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
