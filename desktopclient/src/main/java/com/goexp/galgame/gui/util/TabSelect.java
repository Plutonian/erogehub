package com.goexp.galgame.gui.util;

import com.goexp.galgame.gui.view.game.HomeController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.function.Supplier;

public class TabSelect {

    private final TabPane root;
    private Supplier<Tab> notFind;

    private TabSelect(TabPane root) {
        this.root = root;
    }

    public static TabSelect from(TabPane root) {
        return new TabSelect(root);
    }

    public static TabSelect from() {
        return new TabSelect(HomeController.$this.mainTabPanel);
    }

    public TabSelect ifNotFind(Supplier<Tab> notFind) {
        this.notFind = notFind;

        return this;
    }

    public void select(String title) {
        root.getTabs().stream()
                .filter(targetTab -> targetTab.getText().equals(title))
                .findAny()
                .ifPresentOrElse(tab -> root.getSelectionModel().select(tab),
                        () -> HomeController.$this.insertTab(notFind.get()));
    }
}
