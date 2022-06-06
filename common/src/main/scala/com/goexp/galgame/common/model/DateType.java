package com.goexp.galgame.common.model;

import java.time.LocalDate;
public enum DateType {
    YEAR("今年", LocalDate.now().withMonth(1).withDayOfMonth(1), LocalDate.now().withMonth(12).withDayOfMonth(31)),
    MONTH("今月", LocalDate.now().withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1));


    public final String name;
    public final LocalDate start;
    public final LocalDate end;

    DateType(String name, LocalDate start, LocalDate end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

}
