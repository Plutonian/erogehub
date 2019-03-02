package com.goexp.galgame.gui.view.search.frombrand.brand.task;

import com.goexp.galgame.gui.db.IBrandQuery;
import com.goexp.galgame.gui.db.mongo.query.BrandQuery;
import com.goexp.galgame.gui.model.Brand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class BrandListTask {

    private static IBrandQuery brandQuery = new BrandQuery();

    public static class ByComp extends Task<ObservableList<Brand>> {


        private String name;

        public ByComp(String name) {
            this.name = name;
        }

        @Override
        protected ObservableList<Brand> call() {


            var list = brandQuery.listByComp(name);

            return FXCollections.observableArrayList(list);
        }

    }
}
