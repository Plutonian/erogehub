package com.goexp.galgame.gui.view.search.frombrand.brand.task;

import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.gui.db.mongo.query.BrandQuery;
import com.goexp.galgame.gui.model.Brand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;

public class BrandListTask {

    public static class ByComp extends Task<ObservableList<Brand>> {


        private String name;

        public ByComp(String name) {
            this.name = name;
        }

        @Override
        protected ObservableList<Brand> call() {


            var list = BrandQuery.tlp.query()
                    .where(
                            and(
                                    eq("comp", name),
                                    ne("type", BrandType.PASS.getValue())
                            )
                    )
                    .sort(
                            and(
                                    descending("type"),
                                    descending("name")
                            )
                    )
                    .list();

            return FXCollections.observableArrayList(list);
        }

    }
}
