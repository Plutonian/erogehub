package com.goexp.galgame.data.task.download.provider.brand;

import com.goexp.galgame.data.db.query.BrandService;
import com.goexp.galgame.data.task.download.provider.IdsProvider;

import java.util.List;

public class CodeIdsProvider implements IdsProvider {

    final private BrandService brandService = new BrandService();


    @Override
    public List<Integer> getIds() {

        return List.of(10945);
    }
}
