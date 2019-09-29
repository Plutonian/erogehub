package com.goexp.galgame.gui.view.brand;

import com.goexp.common.util.string.Strings;
import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.brand.search.ByComp;
import com.goexp.galgame.gui.task.brand.search.ByName;
import com.goexp.galgame.gui.task.brand.search.ByType;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainPanelController extends DefaultController {

    /**
     * UI Com
     */

    public final BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);

    public Brand targetBrand;
    public TableColumn<Brand, LocalDate> colStart;
    public TableColumn<Brand, LocalDate> colEnd;
    public TableColumn<Brand, Integer> colSize;

    @FXML
    private TextField textBrandKey;


    @FXML
    private TableView<Brand> tableBrand;

    @FXML
    private TableColumn<Brand, String> colComp;

    @FXML
    private TableColumn<Brand, String> colName;

    @FXML
    private TableColumn<Brand, String> colWebsite;


    @FXML
    private TableColumn<Brand, BrandType> colState;

    @FXML
    private TableColumn<Brand, Void> colCommand;


    @FXML
    private ChoiceBox<BrandType> choiceBrandType;


    @FXML
    private Button btnSearch;

    @FXML
    private ToggleGroup typeGroup;


    private BrandType brandType = BrandType.LIKE;

    private String keyword;


    private final Service<List<Brand>> brandService = new TaskService<>(() -> new ByType(brandType));

    private final Service<List<Brand>> brandByNameService = new TaskService<>(() -> new ByName(keyword));

    private final Service<List<Brand>> brandByCompService = new TaskService<>(() -> new ByComp(keyword));


    @Override
    protected void initialize() {

//        btnSearch.disableProperty().bind(textBrandKey.textProperty().length().isEqualTo(0));

        colComp.setCellValueFactory(new PropertyValueFactory<>("comp"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colWebsite.setCellValueFactory(new PropertyValueFactory<>("website"));
        colState.setCellValueFactory(new PropertyValueFactory<>("isLike"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));

        colStart.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                this.setText(null);
                this.setGraphic(null);

                if (item != null && !empty)
                    this.setText(String.valueOf(item.getYear()));
            }
        });

        colEnd.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                this.setText(null);
                this.setGraphic(null);

                if (item != null && !empty)
                    this.setText(String.valueOf(item.getYear()));
            }
        });

        colWebsite.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);

                if (!empty && !Strings.isEmpty(item)) {
                    var titleLabel = new Hyperlink();
                    titleLabel.setText(item);
                    titleLabel.setOnAction(event -> Websites.open(item));
                    this.setGraphic(titleLabel);
                }
            }
        });

        colCommand.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(null);

                if (!empty) {
                    var link = new Hyperlink("関連ゲーム");
                    link.setOnAction(event -> {
                        targetBrand = this.getTableRow().getItem();
                        onLoadProperty.set(true);
                        onLoadProperty.set(false);
                    });
                    this.setGraphic(link);

                }
            }
        });


        ChangeListener<List<Brand>> handler = (observable, oldValue, newValue) -> {

            if (newValue != null) {
                tableBrand.setItems(FXCollections.observableArrayList(newValue));
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
                    tab.setGraphic(new ImageView(LocalRes.BRAND_16_PNG().get()));
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
