package com.goexp.galgame.gui.task.game.panel.group.node;

import com.goexp.galgame.common.model.DateRange;

import java.time.LocalDate;

public class DateItem extends DefaultItem {

    public final DateRange range;
    public final DateType dateType;

    public DateItem(String title, LocalDate start, LocalDate end, int count, DateType dateType) {
        this(title, new DateRange(start, end), count, dateType);
    }


    public DateItem(String title, DateRange range, int count, DateType dateType) {
        super(title, count);
        this.range = range;
        this.dateType = dateType;
    }

    public enum DateType {
        YEAR,
        MONTH
    }
}