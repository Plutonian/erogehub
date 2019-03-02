package com.goexp.galgame.gui.db;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Game;

import java.time.LocalDate;
import java.util.List;

public interface IGameQuery {
    List<Game> list(GameState gameState);

    List<Game> listByStarRange(int begin, int end);

    List<Game> listByBrand(int brandId);

    List<Game> list(int brandId, GameState gameState);

    List<Game> list(LocalDate start, LocalDate end);

    List<Game> searchByTag(String tag);

    List<Game> searchByName(String keyword);

    List<Game> searchByNameEx(String keyword);

    List<Game> searchByPainter(String keyword);

    List<Game> queryByCV(String keyword);

    List<Game> queryByRealCV(String keyword);
}
