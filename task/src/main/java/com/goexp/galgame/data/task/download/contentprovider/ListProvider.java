package com.goexp.galgame.data.task.download.contentprovider;

import com.goexp.galgame.data.model.Game;

import java.util.List;

public interface ListProvider {

    List<Game> getList(int id);
}
