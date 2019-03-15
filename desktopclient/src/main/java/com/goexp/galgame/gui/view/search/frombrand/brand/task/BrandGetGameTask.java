package com.goexp.galgame.gui.view.search.frombrand.brand.task;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.db.mongo.query.BrandQuery;
import com.goexp.galgame.gui.db.mongo.query.GameQuery;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.AppCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class BrandGetGameTask {


    public static class ByBrand extends Task<ObservableList<Game>> {

        private int brandId;


        public ByBrand(int brandId) {
            this.brandId = brandId;

        }

        @Override
        protected ObservableList<Game> call() {

            Brand brand;
            if (AppCache.brandCache.containsKey(brandId)) {
                brand = AppCache.brandCache.get(brandId);
            } else {

                brand = BrandQuery.tlp.query()
                        .where(eq("_id", brandId))
                        .one();
                AppCache.brandCache.put(brandId, brand);
            }

            var list = GameQuery.tlp.query()
                    .where(eq("brandId", brandId))
                    .list().stream()
                    .peek(g -> {
                        g.brand = brand;
                    })
                    .collect(Collectors.toList());

            return FXCollections.observableArrayList(list);
        }
    }

    public static class ByState extends Task<ObservableList<Game>> {

        private int brandId;
        private GameState gameState;

        public ByState(int brandId, GameState gameState) {
            this.brandId = brandId;
            this.gameState = gameState;
        }

        @Override
        protected ObservableList<Game> call() {

            Brand brand;
            if (AppCache.brandCache.containsKey(brandId)) {
                brand = AppCache.brandCache.get(brandId);
            } else {

                brand = BrandQuery.tlp.query()
                        .where(eq("_id", brandId))
                        .one();
                AppCache.brandCache.put(brandId, brand);
            }

            var list = GameQuery.tlp.query()
                    .where(
                            and(
                                    eq("brandId", brandId),
                                    eq("state", gameState.getValue())
                            )
                    )
                    .list().stream()
                    .peek(g -> {
                        g.brand = brand;
                    })
                    .collect(Collectors.toList());

            return FXCollections.observableArrayList(list);
        }
    }
}
