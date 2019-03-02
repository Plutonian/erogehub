package com.goexp.galgame.data.task.download.contentprovider.brand;

import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.task.client.GetChu;
import com.goexp.galgame.data.task.download.contentprovider.ListProvider;

import java.util.List;

public class RemoteProvider implements ListProvider {

    private final GetChu getchu = new GetChu();

    @Override
    public List<Game> getList(int id) {
        return GetChu.BrandService.gamesFrom(id);
    }
}
