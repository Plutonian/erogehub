package com.goexp.galgame.gui.view.search.frombrand.brand;

import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.common.website.GGBasesURL;
import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.view.common.control.URLHyperlink;
import com.goexp.galgame.gui.view.search.MainSearchController;
import com.goexp.galgame.gui.task.brand.BrandChangeTask;
import com.goexp.galgame.gui.task.brand.BrandListTask;
import com.goexp.galgame.gui.task.ChangeGameTask;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BrandTitleController {

    private final Logger logger = LoggerFactory.getLogger(BrandTitleController.class);
    public BooleanProperty stateChangeProperty = new SimpleBooleanProperty(false);
    @FXML
    private Text txtBrand;
    @FXML
    private MenuButton menuComp;
    @FXML
    private HBox boxWebsite;
    @FXML
    private ImageView imageFav;
    @FXML
    private FlowPane tagPanel;
    @FXML
    private ChoiceBox<BrandType> choiceBrandState;
    private Brand changeBrand;

    private Service<Boolean> changeBrandStateService = new Service<>() {
        @Override
        protected Task<Boolean> createTask() {
            return new BrandChangeTask(changeBrand);
        }
    };

    private Service<Void> changeGameStateService = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new ChangeGameTask.MultiLikeByBrand(changeBrand.id);
        }
    };


    private Service<ObservableList<Brand>> listBrandService = new Service<>() {
        @Override
        protected Task<ObservableList<Brand>> createTask() {
            return new BrandListTask.ByComp(changeBrand.comp);
        }
    };


    private ChangeListener<BrandType> listener;

    @FXML
    private void initialize() {
        var types = Stream.of(
                BrandType.values()
        )
                .filter(t -> t != BrandType.ALL)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        choiceBrandState.setItems(FXCollections.observableArrayList(types));
        choiceBrandState.setConverter(new StringConverter<>() {
            public String toString(BrandType object) {
                return object.getName();
            }

            public BrandType fromString(String string) {
                return BrandType.from(string);
            }
        });


        listener = (o, oldValue, newValue) -> {

            if (newValue != null) {
                logger.debug("<Action>Value:{},New:{}", choiceBrandState.getValue(), newValue);

                changeBrand.isLike = newValue;

                changeBrandStateService.restart();

                if (newValue == BrandType.PASS) {
                    changeGameStateService.restart();
                }
            }
        };

        changeBrandStateService.valueProperty().addListener((o, old, newV) -> {
            if (newV != null && newV) {
                stateChangeProperty.set(true);
            }

        });


        listBrandService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                var items = newValue.stream()
                        .map(brand -> {
                            var item = new MenuItem();
                            item.setText(brand.name);
                            item.setUserData(brand);
                            item.setOnAction(event -> {
                                MainSearchController.$this.viewBrand(brand);
                            });

                            return item;
                        })
                        .collect(Collectors.toList());

                menuComp.getItems().setAll(FXCollections.observableArrayList(items));
            }

        });


//        menuComp.addEventHandler(ActionEvent.ACTION, event -> {
//            if (event.getTarget() instanceof MenuItem) {
//                var brand = (Brand) ((MenuItem) event.getTarget()).getUserData();
//                MainSearchController.$this.viewBrand(brand);
//            }
//
//        });
    }

    public void init(Brand brand) {

        choiceBrandState.setVisible(true);

        stateChangeProperty.set(false);

        changeBrand = brand;
        txtBrand.setText(brand.name);

        if (brand.comp != null && !brand.comp.isEmpty()) {
            menuComp.setVisible(true);
            menuComp.setText(brand.comp);
            listBrandService.restart();
        } else
            menuComp.setVisible(false);

        boxWebsite.getChildren().clear();

        var getchuLink = new URLHyperlink("Getchu");
        getchuLink.setHref(GetchuURL.GameList.byBrand(brand.id));
        boxWebsite.getChildren().add(getchuLink);

        var ggBasesLink = new URLHyperlink("GGBases");
        ggBasesLink.setHref(GGBasesURL.fromTitle(brand.name));
        boxWebsite.getChildren().add(ggBasesLink);


        if (brand.website != null && brand.website.length() > 0) {
            var brandLink = new URLHyperlink("公式", brand.website);

            boxWebsite.getChildren().add(brandLink);

        }


        choiceBrandState.valueProperty().removeListener(listener);
        choiceBrandState.setValue(brand.isLike);
        choiceBrandState.valueProperty().addListener(listener);

    }

    public void initTag(ObservableList<Game> games) {
        Optional.ofNullable(games).ifPresent(list -> {

            var tagLbs = list.stream()
                    .flatMap(game -> game.tag.stream())
                    .filter(s -> s.trim().length() > 0)
                    .collect(Collectors.groupingBy(str -> str))
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(stringListEntry -> ((Map.Entry<String, List<String>>) stringListEntry).getValue().size()).reversed())
                    .limit(10)
                    .map(entry -> {
                        var lb = new Label();
                        lb.setText(entry.getKey());
                        lb.getStyleClass().add("tag");
                        return lb;
                    })
                    .collect(Collectors.toList());
            tagLbs.add(new Label("..."));

            tagPanel.getChildren().setAll(tagLbs);

        });
    }
}
