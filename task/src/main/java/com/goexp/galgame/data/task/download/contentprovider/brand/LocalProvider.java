package com.goexp.galgame.data.task.download.contentprovider.brand;

import com.goexp.galgame.data.Config;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.task.client.GetChu;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class LocalProvider {

    public static List<Game> getList(int id) {

        var path = Config.BRAND_CACHE_ROOT.resolve(String.format("%d.bytes", id));

        try {
            var bytes = Files.readAllBytes(path);
            return GetChu.BrandService.gamesFrom(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
