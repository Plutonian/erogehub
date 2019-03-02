package com.goexp.galgame.data.task.download.provider.game;

import com.goexp.galgame.data.db.query.mysql.GameService;
import com.goexp.galgame.data.task.download.provider.IdsProvider;

import java.time.LocalDate;
import java.util.List;

public class DateRangeIds implements IdsProvider {

    final private GameService gameService = new GameService();

    @Override
    public List<Integer> getIds() {

        return gameService
                .idsFrom(LocalDate.of(2019, 1, 1),
                        LocalDate.of(2019, 12, 31)
                );
    }
}
