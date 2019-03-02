package com.goexp.galgame.gui.view.pagesource;

import com.goexp.galgame.gui.db.mongo.BrandDB;
import com.goexp.galgame.gui.model.Brand;
import javafx.concurrent.Task;

public class UpdateBrandWebsiteTask extends Task<Void> {

    private final BrandDB brandDB = new BrandDB();

    private final Brand brand;

    public UpdateBrandWebsiteTask(Brand brand) {
        this.brand = brand;
    }

    @Override
    protected Void call() {
        brandDB.updateWebsite(brand);

        return null;
    }
}
