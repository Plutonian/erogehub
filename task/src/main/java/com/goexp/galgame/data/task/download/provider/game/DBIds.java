package com.goexp.galgame.data.task.download.provider.game;

import com.goexp.galgame.data.db.query.GameService;
import com.goexp.galgame.data.task.download.provider.IdsProvider;

import java.util.List;

public class DBIds implements IdsProvider {

    final private GameService gameService = new GameService();

    @Override
    public List<Integer> getIds() {
        return gameService
                .idsAll();
    }
}
