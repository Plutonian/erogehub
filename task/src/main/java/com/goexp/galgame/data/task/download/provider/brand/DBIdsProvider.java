package com.goexp.galgame.data.task.download.provider.brand;

import com.goexp.galgame.data.db.query.mongdb.BrandQuery;
import com.goexp.galgame.data.task.download.provider.IdsProvider;

import java.util.List;
import java.util.stream.Collectors;

public class DBIdsProvider implements IdsProvider {
    final private BrandQuery brandService = new BrandQuery();

    @Override
    public List<Integer> getIds() {

        return brandService.list().stream().map(brand -> brand.id).collect(Collectors.toList());

    }
}
