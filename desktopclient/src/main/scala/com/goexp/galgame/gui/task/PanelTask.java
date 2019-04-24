package com.goexp.galgame.gui.task;

import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.Tags;
import com.goexp.galgame.gui.view.game.listview.sidebar.node.BrandItemNode;
import com.goexp.galgame.gui.view.game.listview.sidebar.node.CompItemNode;
import com.goexp.galgame.gui.view.game.listview.sidebar.node.DateItemNode;
import com.goexp.galgame.gui.view.game.listview.sidebar.node.DefaultItemNode;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class PanelTask {

    public static class GroupTag extends Task<List<HBox>> {
        private final List<Game> games;
        final Logger logger = LoggerFactory.getLogger(GroupTag.class);

        public GroupTag(List<Game> games) {
            this.games = games;
        }

        @Override
        protected List<HBox> call() {
            return createTagGroup(games);
        }

        private List<HBox> createTagGroup(List<Game> filteredGames) {

            return filteredGames.stream()
                    .filter(g -> g.tag.size() > 0)
                    .flatMap(g -> g.tag.stream().filter(t -> !t.isEmpty()))
                    .collect(Collectors.groupingBy(str -> str))
                    .entrySet().stream()
                    .sorted(Comparator.comparing((Map.Entry<String, List<String>> v) -> {
                        return v.getValue().size();
                    }).reversed())
                    .limit(20)
                    .map(k -> {

                        var key = k.getKey();
                        var value = k.getValue().size();


                        logger.debug("<createTagGroup> Name:{},Value:{}", key, value);
                        return new HBox(Tags.toNodes(List.of(key)).get(0), new Label("(" + value + ")"));
                    })
                    .collect(Collectors.toUnmodifiableList());

        }
    }

    public static class GroupDate extends Task<TreeItem<DateItemNode>> {

        private final List<Game> games;

        public GroupDate(List<Game> games) {
            this.games = games;
        }

        @Override
        protected TreeItem<DateItemNode> call() {
            return createDateGroup(games);
        }

        private TreeItem<DateItemNode> createDateGroup(List<Game> filteredGames) {
            var yearsStream = filteredGames.stream()
                    .filter(game -> game.publishDate != null)
                    .collect(groupingBy(game -> game.publishDate != null ? game.publishDate.getYear() : 0
                            , groupingBy(g -> g.publishDate != null ? g.publishDate.getMonthValue() : 0)))
                    .entrySet().stream()
                    .sorted(Comparator.comparing((Map.Entry n) -> (Integer) n.getKey()).reversed())
                    .map(d -> {

                        if (d.getKey() != 0) {
                            var count = d.getValue().entrySet().stream().mapToInt(m -> m.getValue().size()).sum();
                            var yearNode = new TreeItem<>(new DateItemNode(
                                    String.format("%d (%d)", d.getKey(), count)
                                    , LocalDate.of(d.getKey(), 1, 1).minusDays(1)
                                    , LocalDate.of(d.getKey(), 12, 31).plusDays(1)
                                    , count
                                    , DateItemNode.DateType.YEAR
                            ));

                            d.getValue().entrySet().stream().sorted(Comparator.comparing((Map.Entry n1) -> (Integer) n1.getKey()).reversed())
                                    .forEach(d1 -> {
                                        var monthNode = new TreeItem<>(new DateItemNode(
                                                String.format("%d (%d)", d1.getKey(), d1.getValue().size())
                                                , LocalDate.of(d.getKey(), d1.getKey(), 1).minusDays(1)
                                                , LocalDate.of(d.getKey(), d1.getKey(), 1).plusMonths(1)
                                                , d1.getValue().size()
                                                , DateItemNode.DateType.MONTH));

                                        yearNode.getChildren().add(monthNode);
                                    });

                            return yearNode;
                        }

                        return null;

                    })
                    .collect(Collectors.toList());


            var root = new TreeItem<DateItemNode>();
            root.getChildren().setAll(yearsStream);

            return root;

//            dateTree.setRoot(root);
        }

    }

    public static class GroupBrand extends Task<TreeItem<DefaultItemNode>> {


        private final List<Game> games;

        public GroupBrand(List<Game> games) {
            this.games = games;
        }

        @Override
        protected TreeItem<DefaultItemNode> call() {
            return createCompGroup(games);
        }

        private TreeItem<DefaultItemNode> createCompGroup(List<Game> filteredGames) {

            var comps = filteredGames.stream()
                    .collect(groupingBy(game -> Optional.ofNullable(game.brand.comp).orElse("")
                            , groupingBy(g -> g.brand)))
                    .entrySet().stream()
                    .map(d -> {

                        final var yearNode = new TreeItem<DefaultItemNode>(new CompItemNode(
                                d.getKey()
                                , d.getValue().entrySet().stream().mapToInt(m -> m.getValue().size()).sum()
                                , d.getKey()
                        ));

                        d.getValue().entrySet().stream().sorted(Comparator.comparing((Map.Entry<Brand, List<Game>> n1) -> n1.getValue().size()).reversed())
                                .forEach(d1 -> {
                                    final var monthNode = new TreeItem<DefaultItemNode>(new BrandItemNode(
                                            d1.getKey().name
                                            , d1.getValue().size()
                                            , d1.getKey()));

                                    yearNode.getChildren().add(monthNode);
                                });

                        return yearNode;

                    })
                    .sorted(Comparator.comparing((TreeItem<DefaultItemNode> item) -> item.getValue().count).reversed())
                    .collect(Collectors.toList());


            var root = new TreeItem<DefaultItemNode>();
            root.getChildren().setAll(comps);

            return root;
        }

    }
}
