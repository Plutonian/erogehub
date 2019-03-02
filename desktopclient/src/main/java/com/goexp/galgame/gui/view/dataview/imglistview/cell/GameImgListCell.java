package com.goexp.galgame.gui.view.dataview.imglistview.cell;

import com.goexp.galgame.gui.util.FXMLLoaderProxy;
import com.goexp.galgame.gui.view.dataview.imglistview.GameImgListCellController;
import com.goexp.galgame.gui.model.Game;
import javafx.scene.layout.Region;

public class GameImgListCell {
    private Game item;

    public GameImgListCell(Game item) {
        this.item = item;
    }

    public Region invoke() {
        var loader = new FXMLLoaderProxy("view/game_explorer/listview/img/img_list_cell.fxml");
        var node = (Region) loader.load();
        var controller = (GameImgListCellController) loader.getController();
        controller.game = item;
        controller.load();
        return node;
    }
}