package com.goexp.galgame.common.model;

import java.time.LocalDate;

public enum DateType {
    PREVYEAR("先年", LocalDate.now().withMonth(1).withDayOfMonth(1).minusYears(1), LocalDate.now().withMonth(12).withDayOfMonth(31).minusYears(1)),
    YEAR("今年", LocalDate.now().withMonth(1).withDayOfMonth(1), LocalDate.now().withMonth(12).withDayOfMonth(31)),
    NEXTYEAR("来年", LocalDate.now().withMonth(1).withDayOfMonth(1).plusYears(1), LocalDate.now().withMonth(12).withDayOfMonth(31).plusYears(1)),
    PREVMONTH("先月", LocalDate.now().withDayOfMonth(1).minusMonths(1), LocalDate.now().withDayOfMonth(1).minusDays(1)),
    MONTH("今月", LocalDate.now().withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1)),
    NEXTMONTH("来月", LocalDate.now().withDayOfMonth(1).plusMonths(1), LocalDate.now().withDayOfMonth(1).plusMonths(2).minusDays(1));


    public final String name;
    public final LocalDate start;
    public final LocalDate end;

    DateType(String name, LocalDate start, LocalDate end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

}
