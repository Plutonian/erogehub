package com.goexp.galgame.gui.view.task;

import com.goexp.common.util.DateUtil;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.db.mongo.query.BrandQuery;
import com.goexp.galgame.gui.db.mongo.query.GameQuery;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.AppCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

public class GameSearchTask {
    private static void fillGameWithBrand(Game g) {
        final var key = g.brand.id;

        g.brand = Optional.ofNullable(AppCache.brandCache.get(key))
                .orElseGet(() -> {
                    var brand = BrandQuery.tlp.query()
                            .where(eq(g.brand.id))
                            .one();
                    AppCache.brandCache.put(key, brand);

                    return brand;
                });
    }

    public static class ByCV extends Task<ObservableList<Game>> {

        private String cv;

        private boolean real;

        public ByCV(String cv, boolean real) {
            this.cv = cv;
            this.real = real;
        }

        @Override
        protected ObservableList<Game> call() {

            List<Game> list = real ? GameQuery.tlp.query()
                    .where(eq("gamechar.truecv", cv))
                    .list() : GameQuery.tlp.query()
                    .where(eq("gamechar.cv", cv))
                    .list();
            var templist = list.stream()
                    .distinct()
                    .peek(GameSearchTask::fillGameWithBrand)
                    .collect(Collectors.toUnmodifiableList());

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

            var list = GameQuery.tlp.query()
                    .where(eq("painter", cv))
                    .list().stream()
                    .distinct()
                    .peek(GameSearchTask::fillGameWithBrand)
                    .collect(Collectors.toUnmodifiableList());

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
            var list = GameQuery.tlp.query()
                    .where(
                            and(
                                    gte("publishDate", DateUtil.toDate(start.toString() + " 00:00:00")),
                                    lte("publishDate", DateUtil.toDate(end.toString() + " 23:59:59"))
                            )
                    )
                    .list().stream()
                    .peek(GameSearchTask::fillGameWithBrand)
                    .collect(Collectors.toUnmodifiableList());

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

            var list = GameQuery.tlp.query()
                    .where(regex("name", "^" + name))
                    .list().stream()
                    .peek(GameSearchTask::fillGameWithBrand)
                    .collect(Collectors.toUnmodifiableList());

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

            var list = GameQuery.tlp.query()
                    .where(regex("name", name))
                    .list().stream()
                    .peek(GameSearchTask::fillGameWithBrand)
                    .collect(Collectors.toUnmodifiableList());

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

            var list = GameQuery.tlp.query()
                    .where(eq("tag", tag))
                    .list().stream()
                    .peek(GameSearchTask::fillGameWithBrand)
                    .collect(Collectors.toUnmodifiableList());

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


            var list = GameQuery.tlp.query()
                    .where(
                            and(
                                    gte("star", begin),
                                    lte("star", end)
                            )
                    )
                    .list().stream()
                    .peek(GameSearchTask::fillGameWithBrand)
                    .collect(Collectors.toUnmodifiableList());

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

            var list = GameQuery.tlp.query()
                    .where(eq("state", gameState.getValue()))
                    .list().stream()
                    .peek(GameSearchTask::fillGameWithBrand)
                    .collect(Collectors.toUnmodifiableList());

            return FXCollections.observableArrayList(list);
        }
    }
}
