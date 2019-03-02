package com.goexp.galgame.data.task.download.provider;

import java.util.List;

public class TestIdsProvider implements IdsProvider {
    @Override
    public List<Integer> getIds() {
        return List.of(1);
    }
}
