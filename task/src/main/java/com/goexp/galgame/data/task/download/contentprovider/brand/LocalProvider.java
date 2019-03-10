package com.goexp.galgame.data.task.download.contentprovider.brand;

import com.goexp.galgame.data.Config;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.task.client.GetChu;
import com.goexp.galgame.data.task.download.contentprovider.ListProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class LocalProvider implements ListProvider {

    @Override
    public List<Game> getList(int id) {

        return Optional.ofNullable(getContent(id)).map(bytes -> {
            return GetChu.BrandService.gamesFrom(bytes);
        }).orElse(null);
    }

    private byte[] getContent(int id) {
        try {
            var path = Config.BRAND_CACHE_ROOT.resolve(String.format("%d.bytes", id));

            if (Files.exists(path)) {
                return Files.readAllBytes(path);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
