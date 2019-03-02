package com.goexp.galgame.data.task.download.provider.game;

import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import com.goexp.galgame.data.task.download.provider.IdsProvider;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DateRangeIds implements IdsProvider {

    final private GameQuery gameService = new GameQuery();

    @Override
    public List<Integer> getIds() {

        return gameService
                .list(LocalDate.of(2019, 1, 1),
                        LocalDate.of(2019, 12, 31)
                ).stream()
                .map(game -> game.id)
                .collect(Collectors.toList());
    }
}
