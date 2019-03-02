package com.goexp.galgame.data.util;


import com.goexp.galgame.common.model.DateRange;

import java.time.LocalDate;

public class DateRangeUtil {

    public static DateRange yearToNow() {
        return new DateRange(LocalDate.now().withDayOfYear(1), LocalDate.now());
    }

    public static DateRange monthToNow() {
        return new DateRange(LocalDate.now().withDayOfMonth(1), LocalDate.now());
    }
}
