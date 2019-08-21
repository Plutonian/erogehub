package com.goexp.galgame.gui.view.brand;

import com.goexp.common.util.Strings;
import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.brand.BrandSearchTask;
import com.goexp.galgame.gui.util.TabSelect;
import com.goexp.galgame.gui.util.Websites;
import com.goexp.galgame.gui.util.res.LocalRes;
import com.goexp.galgame.gui.view.DefaultController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainPanelController extends DefaultController {

    /**
     * UI Com
     */

    public BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);

    public Brand targetBrand;

    @FXML
    private TextField textBrandKey;


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


    private Service<List<TreeItem<Brand>>> brandService = new TaskService<>(() -> new BrandSearchTask.ByType(brandType));

    private Service<List<TreeItem<Brand>>> brandByNameService = new TaskService<>(() -> new BrandSearchTask.ByName(keyword));

    private Service<List<TreeItem<Brand>>> brandByCompService = new TaskService<>(() -> new BrandSearchTask.ByComp(keyword));


    @Override
    protected void initialize() {

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
                        .filter(TreeItem::isLeaf)
                        .map(TreeItem::getValue)
                        .ifPresent(brand -> {
                            this.setText(brand.comp());
                        });

            }
        });

        colWebsite.setCellFactory(col -> {

            return new TreeTableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setGraphic(null);

                    if (!empty && !Strings.isEmpty(item)) {
                        Optional.ofNullable(this.getTreeTableRow())
                                .map(TreeTableRow::getTreeItem)
                                .filter(TreeItem::isLeaf)
                                .map(TreeItem::getValue)
                                .ifPresent(brand -> {
                                    var titleLabel = new Hyperlink();
                                    titleLabel.setText(item);
                                    titleLabel.setOnAction(event -> {
                                        Websites.open(brand.website());
                                    });
                                    this.setGraphic(titleLabel);
                                });
                    }
                }
            };
        });

        colCommand.setCellFactory(col -> new TreeTableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);

                if (!empty) {
                    Optional.ofNullable(this.getTreeTableRow())
                            .map(TreeTableRow::getTreeItem)
                            .filter(TreeItem::isLeaf)
                            .map(TreeItem::getValue)
                            .ifPresent(brand -> {
                                var link = new Hyperlink("関連ゲーム");
                                link.setOnAction(event -> {
                                    targetBrand = brand;
                                    onLoadProperty.set(true);
                                    onLoadProperty.set(false);
                                });
                                this.setGraphic(link);
                            });

                }
            }
        });


        ChangeListener<List<TreeItem<Brand>>> handler = (observable, oldValue, newValue) -> {

            if (newValue != null) {
                var root = new TreeItem<Brand>();
                root.getChildren().setAll(FXCollections.observableArrayList(newValue));
                tableBrand.setRoot(root);
            }
        };

        //for data
        brandService.valueProperty().addListener(handler);

        brandByNameService.valueProperty().addListener(handler);
        brandByCompService.valueProperty().addListener(handler);


        var types = Stream.of(BrandType.values()).collect(Collectors.toUnmodifiableList());

        choiceBrandType.setItems(FXCollections.observableArrayList(types));
        choiceBrandType.setConverter(new StringConverter<>() {
            @Override
            public String toString(BrandType brandType) {
                return brandType.name;
            }

            @Override
            public BrandType fromString(String string) {
                return BrandType.from(string);
            }
        });


        onLoadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {

                final var text = targetBrand.name();

                TabSelect.from().ifNotFind(() -> {
                    var conn = new CommonInfoTabController();

                    var tab = new Tab(text, conn.node);
                    tab.setGraphic(new ImageView(LocalRes.BRAND_16_PNG.get()));
                    conn.load(targetBrand);

                    return tab;
                }).select(text);
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

    }


    public void load() {
        choiceBrandType.setValue(BrandType.CHECKING);
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
