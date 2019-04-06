package com.goexp.galgame.gui.view.dataview.sidebar;

import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class BrandGroupController extends FilterController<Game> {

    @FXML
    private TreeView<DefaultItemNode> compTree;

    @FXML
    private void initialize() {
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
