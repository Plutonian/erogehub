package com.goexp.galgame.gui.view.task;

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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GameSearchTask {
    private static IBrandQuery brandQuery = new BrandQuery();
    private static IGameQuery gameQuery = new GameQuery();

    public static class ByCV extends Task<ObservableList<Game>> {

        private String cv;
        private boolean real;

        public ByCV(String cv, boolean real) {
            this.cv = cv;
            this.real = real;
        }

        @Override
        protected ObservableList<Game> call() {

            List<Game> list = real ? gameQuery.queryByRealCV(cv) : gameQuery.queryByCV(cv);
            var templist = list.stream()
                    .distinct()
                    .peek(g -> {

                        Brand brand;
                        if (AppCache.brandCache.containsKey(g.brand.id)) {
                            brand = AppCache.brandCache.get(g.brand.id);
                        } else {
                            brand = brandQuery.getById(g.brand.id);
                            AppCache.brandCache.put(g.brand.id, brand);
                        }
                        g.brand = brand;
                    }).collect(Collectors.toList());

            return FXCollections.observableArrayList(templist);
        }
    }

    public static class ByPainter extends Task<ObservableList<Game>> {


        private String cv;

        public ByPainter(String cv) {
            this.cv = cv;

        }

        @Override
        protected ObservableList<Game> call() {

            var list = gameQuery.searchByPainter(cv).stream()
                    .distinct()
                    .peek(g -> {

                        Brand brand;
                        if (AppCache.brandCache.containsKey(g.brand.id)) {
                            brand = AppCache.brandCache.get(g.brand.id);
                        } else {
                            brand = brandQuery.getById(g.brand.id);
                            AppCache.brandCache.put(g.brand.id, brand);
                        }
                        g.brand = brand;
                    }).collect(Collectors.toList());

            return FXCollections.observableArrayList(list);
        }
    }

    public static class ByDateRange extends Task<ObservableList<Game>> {


        private LocalDate start;
        private LocalDate end;

        public ByDateRange(LocalDate start, LocalDate end) {
            this.start = start;
            this.end = end;

        }

        @Override
        protected ObservableList<Game> call() {
            var list = gameQuery.list(start, end).stream().peek(g -> {

                Brand brand;
                if (AppCache.brandCache.containsKey(g.brand.id)) {
                    brand = AppCache.brandCache.get(g.brand.id);
                } else {
                    brand = brandQuery.getById(g.brand.id);
                    AppCache.brandCache.put(g.brand.id, brand);
                }
                g.brand = brand;
            }).collect(Collectors.toList());

            return FXCollections.observableArrayList(list);
        }
    }

    public static class ByName extends Task<ObservableList<Game>> {


        private String name;

        public ByName(String name) {
            this.name = name;

        }

        @Override
        protected ObservableList<Game> call() {
            var list = gameQuery.searchByName(name).stream().peek(g -> {

                Brand brand;
                if (AppCache.brandCache.containsKey(g.brand.id)) {
                    brand = AppCache.brandCache.get(g.brand.id);
                } else {
                    brand = brandQuery.getById(g.brand.id);
                    AppCache.brandCache.put(g.brand.id, brand);
                }
                g.brand = brand;
            }).collect(Collectors.toList());

            return FXCollections.observableArrayList(list);
        }
    }

    public static class ByNameEx extends Task<ObservableList<Game>> {

        private String name;

        public ByNameEx(String name) {
            this.name = name;

        }

        @Override
        protected ObservableList<Game> call() {
            var list = gameQuery.searchByNameEx(name).stream().peek(g -> {

                Brand brand;
                if (AppCache.brandCache.containsKey(g.brand.id)) {
                    brand = AppCache.brandCache.get(g.brand.id);
                } else {
                    brand = brandQuery.getById(g.brand.id);
                    AppCache.brandCache.put(g.brand.id, brand);
                }
                g.brand = brand;
            }).collect(Collectors.toList());

            return FXCollections.observableArrayList(list);
        }
    }

    public static class ByTag extends Task<ObservableList<Game>> {


        final private String tag;

        public ByTag(String tag) {
            this.tag = tag;

        }

        @Override
        protected ObservableList<Game> call() {
            var list = gameQuery.searchByTag(tag).stream()
                    .peek(g -> {
                        Brand brand;
                        if (AppCache.brandCache.containsKey(g.brand.id)) {
                            brand = AppCache.brandCache.get(g.brand.id);
                        } else {
                            brand = brandQuery.getById(g.brand.id);
                            AppCache.brandCache.put(g.brand.id, brand);
                        }

                        g.brand = brand;
                    }).collect(Collectors.toList());

            return FXCollections.observableArrayList(list);
        }
    }

    public static class ByStarRange extends Task<ObservableList<Game>> {

        private int begin;

        private int end;

        public ByStarRange(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        protected ObservableList<Game> call() {

            var list = gameQuery.listByStarRange(begin, end).stream().peek(g -> {

                Brand brand;
                if (AppCache.brandCache.containsKey(g.brand.id)) {
                    brand = AppCache.brandCache.get(g.brand.id);
                } else {
                    brand = brandQuery.getById(g.brand.id);
                    AppCache.brandCache.put(g.brand.id, brand);
                }
                g.brand = brand;
            }).collect(Collectors.toList());

            return FXCollections.observableArrayList(list);
        }
    }

    public static class ByState extends Task<ObservableList<Game>> {

        private GameState gameState;

        public ByState(GameState gameState) {
            this.gameState = gameState;
        }

        @Override
        protected ObservableList<Game> call() {

            var list = gameQuery.list(gameState).stream().peek(g -> {

                Brand brand;
                if (AppCache.brandCache.containsKey(g.brand.id)) {
                    brand = AppCache.brandCache.get(g.brand.id);
                } else {
                    brand = brandQuery.getById(g.brand.id);
                    AppCache.brandCache.put(g.brand.id, brand);
                }
                g.brand = brand;
            }).collect(Collectors.toList());

            return FXCollections.observableArrayList(list);
        }
    }
}
