package com.goexp.galgame.gui.view.dataview.sidebar;

import com.goexp.galgame.common.model.DateRange;
import com.goexp.galgame.gui.model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class DateGroupController extends FilterController<Game> {

    @FXML
    private TreeView<DateItemNode> dateTree;

    @FXML
    private void initialize() {

        dateTree.setCellFactory(dateItemNodeTreeView -> {
            return new TreeCell<>() {

                @Override
                protected void updateItem(DateItemNode item, boolean empty) {
                    super.updateItem(item, empty);

                    setGraphic(null);
                    setText(null);

                    if (!empty && item != null) {
                        setText(item.title);
                    }
                }
            };
        });
        dateTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                predicate = game -> game.publishDate != null && game.publishDate.isBefore(newValue.getValue().range.end) && game.publishDate.isAfter(newValue.getValue().range.start);

                onSetProperty.set(true);
                onSetProperty.set(false);
            }
        });

    }

    public void init(List<Game> filteredGames) {

        createDateGroup(filteredGames);
    }

    private void createDateGroup(List<Game> filteredGames) {
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


        dateTree.setRoot(root);
    }

    static class DateItemNode extends DefaultItemNode {

        DateRange range;
        DateType dateType;

        public DateItemNode(String title, LocalDate start, LocalDate end, int count, DateType dateType) {
            this(title, new DateRange(start, end), count, dateType);
        }


        public DateItemNode(String title, DateRange range, int count, DateType dateType) {
            super(title, count);
            this.range = range;
            this.dateType = dateType;
        }

        enum DateType {
            YEAR,
            MONTH
        }
    }
}
