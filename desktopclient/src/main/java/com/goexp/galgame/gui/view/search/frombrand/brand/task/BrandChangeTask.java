package com.goexp.galgame.gui.view.search.frombrand.brand.task;

import com.goexp.galgame.gui.db.mongo.BrandDB;
import com.goexp.galgame.gui.model.Brand;
import javafx.concurrent.Task;

public class BrandChangeTask extends Task<Boolean> {
    private BrandDB brandService = new BrandDB();

    private Brand brand;

    public BrandChangeTask(Brand brand) {
        this.brand = brand;
    }

    @Override
    protected Boolean call() {

        brandService.updateIsLike(brand);

        return true;
    }
}