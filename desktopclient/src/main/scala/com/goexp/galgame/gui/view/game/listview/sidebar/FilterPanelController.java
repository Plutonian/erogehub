package com.goexp.galgame.gui.view.game.listview.sidebar;

import com.goexp.galgame.common.model.DateRange;
import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class FilterPanelController extends FilterController<Game> {

    @FXML
    private FlowPane starbox;

    @FXML
    private FlowPane statebox;

    protected void initialize() {

        reset();
    }

    public void reset() {
        var nodes = Stream.of(GameState.values())
                .sorted(Comparator.comparing(GameState::getValue).reversed())
                .map(gameType -> {
                    var btn = new CheckBox(gameType.getName());
                    btn.setUserData(gameType);

                    if (gameType.getValue() > GameState.BLOCK.getValue())
                        btn.setSelected(true);

                    return btn;
                }).collect(Collectors.toList());

        statebox.getChildren().setAll(nodes);


        var starnodes = IntStream.rangeClosed(0, 5)
                .boxed()
                .sorted(Comparator.comparing(Integer::intValue).reversed())
                .map(star -> {
                    var btn = new CheckBox(star.toString());

                    btn.setSelected(true);

                    btn.setUserData(star);

                    return btn;
                }).collect(Collectors.toList());

        starbox.getChildren().setAll(starnodes);

        setP();
    }

    @FXML
    private void SetFilter_OnAction(ActionEvent event) {
        setP();

        onSetProperty.set(true);
        onSetProperty.set(false);
    }

    private void setP() {
        var stars = starbox.getChildren().stream()
                .filter(node -> ((CheckBox) node).isSelected())
                .map(node -> Integer.parseInt(((CheckBox) node).getText()))
                .collect(Collectors.toSet());

        var states = statebox.getChildren().stream()
                .filter(node -> ((CheckBox) node).isSelected())
                .map(node -> (GameState) (node.getUserData()))
                .collect(Collectors.toSet());

        if (stars.isEmpty())
            stars.add(0);

        predicate = game -> stars.contains(game.star) && states.contains(game.state.get());
    }

    @Override
    public void init(List<Game> filteredGames) {

    }

    public static class BrandGroupController extends FilterController<Game> {

        @FXML
        private TreeView<DefaultItemNode> compTree;

        protected void initialize() {
            compTree.setCellFactory(itemNodeTreeView -> {
                return new TreeCell<>() {
                    @Override
                    protected void updateItem(DefaultItemNode item, boolean empty) {
                        super.updateItem(item, empty);

                        setGraphic(null);
                        setText(null);

                        if (!empty && item != null) {

                            setText(String.format("%s (%d)", item.title, item.count));
                        }
                    }
                };
            });
            compTree.getSelectionModel().selectedItemProperty().addListener((o, old, item) -> {
                if (item != null) {

                    if (item.getValue() instanceof CompItemNode) {
                        predicate = game -> {
                            // target comp can be null,so...change null to empty
                            var comp = game.brand.comp != null ? game.brand.comp : "";

                            return comp.equals(((CompItemNode) item.getValue()).comp);
                        };

                    } else {
                        predicate = game -> (game.brand.equals(((BrandItemNode) item.getValue()).brand));
                    }

                    onSetProperty.set(true);
                    onSetProperty.set(false);
                }
            });

        }

        @Override
        public void init(List<Game> filteredGames) {
            createCompGroup(filteredGames);
        }

        private void createCompGroup(List<Game> filteredGames) {

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


            compTree.setRoot(root);
        }

        static class BrandItemNode extends DefaultItemNode {

            Brand brand;

            public BrandItemNode(String title, int count, Brand brand) {
                super(title, count);
                this.brand = brand;
            }

        }

        static class CompItemNode extends DefaultItemNode {
            String comp;

            public CompItemNode(String title, int count, String comp) {
                super(title, count);
                this.comp = comp;
            }
        }
    }

    public static class DateGroupController extends FilterController<Game> {

        @FXML
        private TreeView<DateItemNode> dateTree;

        protected void initialize() {

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
                                    , DateGroupController.DateItemNode.DateType.YEAR
                            ));

                            d.getValue().entrySet().stream().sorted(Comparator.comparing((Map.Entry n1) -> (Integer) n1.getKey()).reversed())
                                    .forEach(d1 -> {
                                        var monthNode = new TreeItem<>(new DateItemNode(
                                                String.format("%d (%d)", d1.getKey(), d1.getValue().size())
                                                , LocalDate.of(d.getKey(), d1.getKey(), 1).minusDays(1)
                                                , LocalDate.of(d.getKey(), d1.getKey(), 1).plusMonths(1)
                                                , d1.getValue().size()
                                                , DateGroupController.DateItemNode.DateType.MONTH));

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
}
