package com.goexp.galgame.gui.task.game.panel.node;

import com.goexp.galgame.common.model.DateRange;

import java.time.LocalDate;

public class DateItemNode extends DefaultItemNode {

    public DateRange range;
    public DateType dateType;

    public DateItemNode(String title, LocalDate start, LocalDate end, int count, DateType dateType) {
        this(title, new DateRange(start, end), count, dateType);
    }


    public DateItemNode(String title, DateRange range, int count, DateType dateType) {
        super(title, count);
        this.range = range;
        this.dateType = dateType;
    }

    public enum DateType {
        YEAR,
        MONTH
    }
}