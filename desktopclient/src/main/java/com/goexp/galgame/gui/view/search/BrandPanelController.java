package com.goexp.galgame.gui.view.search;

import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.util.LocalRes;
import com.goexp.galgame.gui.util.TabSelect;
import com.goexp.galgame.gui.view.common.CommonBrandInfoTabController;
import com.goexp.galgame.gui.view.pagesource.WebViewController;
import com.goexp.galgame.gui.view.search.cell.BrandTreeCellController;
import com.goexp.galgame.gui.view.search.frombrand.brand.task.BrandSearchTask;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BrandPanelController {

    private final Logger logger = LoggerFactory.getLogger(BrandPanelController.class);

    /**
     * UI Com
     */

    public BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);

    public Brand targetBrand;

    @FXML
    private TextField textBrandKey;

    @FXML
    private TreeView<Brand> treeBrandPanel;


    @FXML
    private TreeTableView<Brand> tableBrand;

    @FXML
    private TreeTableColumn<Brand, String> colComp;

    @FXML
    private TreeTableColumn<Brand, String> colName;

    @FXML
    private TreeTableColumn<Brand, String> colWebsite;


    @FXML
    private TreeTableColumn<Brand, BrandType> colState;

    @FXML
    private TreeTableColumn<Brand, Void> colCommand;


    @FXML
    private ChoiceBox<BrandType> choiceBrandType;


    @FXML
    private Button btnSearch;

    @FXML
    private ToggleGroup typeGroup;


    private BrandType brandType = BrandType.LIKE;

    private String keyword;


    private Service<ObservableList<TreeItem<Brand>>> brandService = new Service<>() {
        @Override
        protected Task<ObservableList<TreeItem<Brand>>> createTask() {
            return new BrandSearchTask.ByType(brandType);
        }
    };

    private Service<ObservableList<TreeItem<Brand>>> brandByNameService = new Service<>() {
        @Override
        protected Task<ObservableList<TreeItem<Brand>>> createTask() {
            return new BrandSearchTask.ByName(keyword);
        }
    };

    private Service<ObservableList<TreeItem<Brand>>> brandByCompService = new Service<>() {
        @Override
        protected Task<ObservableList<TreeItem<Brand>>> createTask() {
            return new BrandSearchTask.ByComp(keyword);
        }
    };


    @FXML
    private void initialize() {

//        btnSearch.disableProperty().bind(textBrandKey.textProperty().length().isEqualTo(0));

        colComp.setCellValueFactory(new TreeItemPropertyValueFactory<>("comp"));
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        colWebsite.setCellValueFactory(new TreeItemPropertyValueFactory<>("website"));
        colState.setCellValueFactory(new TreeItemPropertyValueFactory<>("isLike"));

        colComp.setCellFactory(col -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                this.setText(null);
                this.setGraphic(null);

                Optional.ofNullable(this.getTreeTableRow())
                        .map(TreeTableRow::getTreeItem)
                        .ifPresent(treeitem -> {

                            if (!treeitem.isLeaf()) {
                                var brand = treeitem.getValue();

                                Optional.ofNullable(brand).ifPresent(b -> {
//
//                                    var link = new Label(brand.comp);
//                                    link.setStyle("-fx-font-size:18px");
//                                    this.setGraphic(link);

                                    this.setText(brand.comp);
                                });
                            }

                        });

            }
        });

        colWebsite.setCellFactory(col -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);

                if (!empty && item != null) {
                    Optional.ofNullable(this.getTreeTableRow())
                            .map(TreeTableRow::getTreeItem
                            ).ifPresent(treeitem -> {

                        if (treeitem.isLeaf()) {
                            if (item.length() > 0) {

                                final var brand = this.getTreeTableRow().getTreeItem().getValue();
                                var titleLabel = new Hyperlink();
                                titleLabel.setText(item);
                                titleLabel.setOnAction((event -> {
                                    var window = new Stage();
                                    Parent root = null;
                                    try {
                                        var loader = new FXMLLoader(getClass().getClassLoader().getResource("view/WebView.fxml"));
                                        root = loader.load();
                                        var controller = (WebViewController) loader.getController();
                                        controller.load(brand);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                    window.setTitle(brand.website);

                                    window.setWidth(1200);
                                    window.setMinWidth(1200);

                                    window.setHeight(800);
                                    window.setMinHeight(800);
                                    window.setScene(new Scene(root, Color.BLACK));

                                    window.show();


                                }));
                                this.setGraphic(titleLabel);
                            }
                        }
                    });
                }
            }
        });

        colCommand.setCellFactory(col -> new TreeTableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);

                if (!empty) {

                    Optional.ofNullable(this.getTreeTableRow())
                            .map(TreeTableRow::getTreeItem)
                            .ifPresent(treeitem -> {

                                if (treeitem.isLeaf()) {
                                    var brand = treeitem.getValue();

                                    Optional.ofNullable(brand).ifPresent(b -> {

                                        var link = new Hyperlink("関連ゲーム");
                                        link.setOnAction(event -> {
                                            targetBrand = brand;
                                            onLoadProperty.set(true);
                                            onLoadProperty.set(false);
                                        });
                                        this.setGraphic(link);
                                    });
                                }

                            });

                }
            }
        });


        ChangeListener<ObservableList<TreeItem<Brand>>> handler = (observable, oldValue, newValue) -> {

            if (newValue != null) {
                var root = new TreeItem<Brand>();
                root.getChildren().setAll(newValue);
                treeBrandPanel.setRoot(root);
                tableBrand.setRoot(root);

//                var subNode = newValue.stream()
//                        .map(brandTreeItem -> {
//
//                            var subBrand = new Brand();
//                            subBrand.children = brandTreeItem.getChildren().stream().map(subItem -> {
//                                return subItem.getValue();
//                            }).collect(Collectors.toList());
//                            var rootN = new TreeItem<>(brandTreeItem.getValue());
//                            rootN.getChildren().add(new TreeItem<>(subBrand));
//                            return rootN;
//                        }).collect(Collectors.toList());
//
//                var root2 = new TreeItem<Brand>();
//                root2.getChildren().setAll(subNode);
//                treeBrandPanel.setRoot(root2);


            }
        };

        //for data
        brandService.valueProperty().addListener(handler);

        brandByNameService.valueProperty().addListener(handler);
        brandByCompService.valueProperty().addListener(handler);


        var types = Stream.of(
                BrandType.values()
        ).collect(Collectors.toList());

        choiceBrandType.setItems(FXCollections.observableArrayList(types));
        choiceBrandType.setConverter(new StringConverter<>() {
            @Override
            public String toString(BrandType object) {
                return object.getName();
            }

            @Override
            public BrandType fromString(String string) {
                return BrandType.from(string);
            }
        });


        onLoadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {

                final var text = targetBrand.name;

                TabSelect.from(MainSearchController.$this.mainTabPanel)
                        .ifNotFind(b -> {
                            var conn = new CommonBrandInfoTabController();

                            var tab = new Tab(text, conn.node);
                            tab.setGraphic(new ImageView(LocalRes.BRAND_16_PNG.get()));
                            conn.load(targetBrand);
                            MainSearchController.$this.insertTab(tab);
                        })
                        .select(text);
            }
        });


//        flowBrand.addEventFilter(ActionEvent.ACTION, event -> {
//
//            if (event.getTarget() instanceof Hyperlink) {
//                var link = (Hyperlink) event.getTarget();
//                targetBrand = (Brand) link.getUserData();
//                onLoadProperty.set(true);
//            }
//
//        });


        treeBrandPanel.setCellFactory(new Callback<>() {
            @Override
            public TreeCell<Brand> call(TreeView<Brand> param) {
                return new TreeCell<>() {
                    @Override
                    protected void updateItem(Brand item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(null);
                        setGraphic(null);

                        if (!empty && item != null) {

                            var treeItem = this.getTreeItem();

                            if (treeItem.isLeaf()) {
//                                var flow = new FlowPane();
//                                flow.setHgap(5);
//                                if (item.children != null)
//                                    item.children.forEach(brand -> {
                                        var loader = new FXMLLoaderProxy("view/search/brandcell.fxml");
                                        var root = (Region) loader.load();
                                        var controller = (BrandTreeCellController) loader.getController();
                                        controller.load(item);

//                                        flow.getChildren().add(root);
//                                    });

                                setGraphic(root);
                            } else {
                                var node = new Label();
                                node.setText(item.comp);
                                node.setStyle("-fx-font-size:18px");

//                                var root = new BorderPane();
//
//                                var brandlist = new TilePane();
//                                brandlist.setPrefColumns(5);
//                                root.setCenter(brandlist);
//                                root.setTop(node);
//
//
//                                var brandNodes = treeItem.getChildren().stream()
//                                        .map(t -> {
//                                            var b = t.getValue();
//                                            var loader = new FXMLLoaderProxy("view/search/brandcell.fxml");
//                                            var rootNode = (Region) loader.load();
//                                            var controller = (BrandTreeCellController) loader.getController();
//                                            controller.load(b);
//
//                                            return rootNode;
//                                        }).collect(Collectors.toList());
//
//                                brandlist.getChildren().setAll(brandNodes);

                                setGraphic(node);
                            }

                        }
                    }
                };
            }
        });
    }


    public void load() {
        choiceBrandType.setValue(BrandType.LIKE);
    }


    @FXML
    private void choiceBrandType_OnAction(ActionEvent actionEvent) {
        brandType = choiceBrandType.getValue();


        logger.debug("Value: {}", choiceBrandType.getValue());

        brandService.restart();
    }

    @FXML
    private void search_OnAction(ActionEvent actionEvent) {
        keyword = textBrandKey.getText();
        logger.debug("Value: {}", keyword);

        var type = Integer.parseInt((String) typeGroup.getSelectedToggle().getUserData());

        if (type == 0)
            brandByNameService.restart();
        else {
            brandByCompService.restart();
        }
    }
}
