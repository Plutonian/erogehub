package com.goexp.galgame.data.task.download.provider.brand;

import com.goexp.galgame.data.db.query.mongdb.BrandQuery;
import com.goexp.galgame.data.task.download.provider.IdsProvider;

import java.util.List;
import java.util.stream.Collectors;

public class DBIdsProvider implements IdsProvider {


    @Override
    public List<Integer> getIds() {

        return BrandQuery.tlp.query()
                .list().stream()
                .map(brand -> brand.id)
                .collect(Collectors.toList());

    }
}
