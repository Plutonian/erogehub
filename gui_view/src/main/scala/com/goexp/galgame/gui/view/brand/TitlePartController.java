package com.goexp.galgame.gui.view.brand;

import com.goexp.common.util.string.Strings;
import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.gui.model.Brand;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.task.TaskService;
import com.goexp.galgame.gui.task.brand.ChangeIsLikeTask;
import com.goexp.galgame.gui.task.brand.list.ByComp;
import com.goexp.galgame.gui.task.game.change.MultiLikeByBrand;
import com.goexp.galgame.gui.view.DefaultController;
import com.goexp.galgame.gui.view.game.HomeController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TitlePartController extends DefaultController {

    public BooleanProperty stateChangeProperty = new SimpleBooleanProperty(false);
    @FXML
    private Text txtComp;
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

    private Service changeBrandStateService = new TaskService(() -> new ChangeIsLikeTask(changeBrand));
    private Service<Void> changeGameStateService = new TaskService<>(() -> new MultiLikeByBrand(changeBrand.id()));
    private Service<List<Brand>> listBrandService = new TaskService<>(() -> new ByComp(changeBrand.comp()));


    private ChangeListener<BrandType> listener;

    protected void initialize() {
        var types = Stream.of(
                BrandType.values()
        )
                .filter(t -> t != BrandType.ALL)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        choiceBrandState.setItems(FXCollections.observableArrayList(types));
        choiceBrandState.setConverter(new StringConverter<>() {
            public String toString(BrandType brandType) {
                return brandType.name;
            }

            public BrandType fromString(String string) {
                return BrandType.from(string);
            }
        });


        listener = (o, oldValue, newValue) -> {

            if (newValue != null) {
                logger.debug("<Action>Value:{},New:{}", choiceBrandState.getValue(), newValue);

                changeBrand.setIsLike(newValue);

                changeBrandStateService.restart();

                if (newValue == BrandType.BLOCK) {
                    changeGameStateService.restart();
                }
            }
        };

        changeBrandStateService.valueProperty().addListener((o, old, newV) -> {
            if (newV != null) {
                stateChangeProperty.set(true);
            }

        });


        listBrandService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                var items = newValue.stream()
                        .map(brand -> {
                            var item = new MenuItem();
                            item.setText(brand.name());
                            item.setUserData(brand);
                            item.setOnAction(event -> {
                                HomeController.$this.viewBrand(brand);
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
//                HomeController.$this.viewBrand(brand);
//            }
//
//        });
    }

    public void init(Brand brand) {

        choiceBrandState.setVisible(true);

        stateChangeProperty.set(false);

        changeBrand = brand;
        menuComp.setText(brand.name());

        if (Strings.isNotEmpty(brand.comp())) {
            txtComp.setText(brand.comp());
            listBrandService.restart();
        } else {
//            menuComp.setVisible(false);
        }

        boxWebsite.getChildren().clear();


        choiceBrandState.valueProperty().removeListener(listener);
        choiceBrandState.setValue(brand.isLike());
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
