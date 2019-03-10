package com.goexp.galgame.data.task.download.provider.brand;

import com.goexp.galgame.data.Config;
import com.goexp.galgame.data.task.download.provider.IdsProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorIDSProvider implements IdsProvider {

    @Override
    public List<Integer> getIds() {

        if (!Files.exists(Config.BRAND_ERROR_FILE)) {
            return new ArrayList<>();
        }


        try {
            var list = Files.lines(Config.BRAND_ERROR_FILE)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            Files.delete(Config.BRAND_ERROR_FILE);

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
