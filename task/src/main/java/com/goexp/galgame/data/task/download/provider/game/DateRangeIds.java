package com.goexp.galgame.data.task.download.provider.game;

import com.goexp.common.util.DateUtil;
import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import com.goexp.galgame.data.task.download.provider.IdsProvider;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

public class DateRangeIds implements IdsProvider {

    @Override
    public List<Integer> getIds() {

        return GameQuery.fullTlp.query()
                .where(
                        and(
                                gte("publishDate", DateUtil.toDate(LocalDate.of(2019, 1, 1).toString() + " 00:00:00")),
                                lte("publishDate", DateUtil.toDate(LocalDate.of(2019, 12, 31).toString() + " 23:59:59"))
                        )
                )
                .list().stream()
                .map(game -> game.id)
                .collect(Collectors.toList());
    }
}
