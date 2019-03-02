package com.goexp.galgame.gui.view.search.frombrand.brand.task;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.db.IBrandQuery;
import com.goexp.galgame.gui.db.IGameQuery;
import com.goexp.galgame.gui.db.mongo.query.BrandQuery;
import com.goexp.galgame.gui.db.mongo.query.GameQuery;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.AppCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.stream.Collectors;

public class BrandGetGameTask {
    private static IBrandQuery brandQuery = new BrandQuery();
    private static IGameQuery gameQuery = new GameQuery();


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
                brand = brandQuery.getById(brandId);
                AppCache.brandCache.put(brandId, brand);
            }

            var list = gameQuery.listByBrand(brandId).stream().peek(g -> {
                g.brand = brand;
            }).collect(Collectors.toList());

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
                brand = brandQuery.getById(brandId);
                AppCache.brandCache.put(brandId, brand);
            }

            var list = gameQuery.list(brandId, gameState).stream().peek(g -> {
                g.brand = brand;
            }).collect(Collectors.toList());

            return FXCollections.observableArrayList(list);
        }
    }
}
