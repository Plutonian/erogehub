package com.goexp.galgame.gui.view.search.cell;


import com.goexp.galgame.gui.view.common.CommonBrandInfoTabController;
import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.util.LocalRes;
import com.goexp.galgame.gui.util.TabSelect;
import com.goexp.galgame.gui.view.pagesource.WebViewController;
import com.goexp.galgame.gui.view.search.MainSearchController;
import com.goexp.galgame.gui.model.Brand;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BrandTreeCellController {

    @FXML
    private Label textName;

    @FXML
    private HBox boxLink;


    private Brand brand;


    public void load(Brand brand) {
        this.brand = brand;

        textName.setText(brand.name);

        var titleLabel = new Hyperlink();
        titleLabel.setText("Website");
        titleLabel.setOnAction((event -> {
            var window = new Stage();


            var loader = new FXMLLoaderProxy("view/WebView.fxml");
            var root = (Parent) loader.load();
            var controller = (WebViewController) loader.getController();
            controller.load(brand);
            window.setTitle(brand.website);

            window.setWidth(1200);
            window.setMinWidth(1200);

            window.setHeight(800);
            window.setMinHeight(800);
            window.setScene(new Scene(root, Color.BLACK));

            window.show();


        }));

        var link = new Hyperlink("関連ゲーム");
        link.setOnAction(event -> {
            final var text = brand.name;


            TabSelect.from(MainSearchController.$this.mainTabPanel)
                    .ifNotFind(b -> {
                        var conn = new CommonBrandInfoTabController();

                        var tab = new Tab(text, conn.node);
                        tab.setGraphic(new ImageView(LocalRes.BRAND_16_PNG.get()));
                        conn.load(brand);
                        MainSearchController.$this.insertTab(tab);
                    })
                    .select(text);
        });

        boxLink.getChildren().setAll(titleLabel, link);
    }
}
