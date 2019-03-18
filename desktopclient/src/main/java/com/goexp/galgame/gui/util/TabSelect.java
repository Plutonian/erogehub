package com.goexp.galgame.gui.util;

import javafx.scene.control.TabPane;

import java.util.function.Consumer;

public class TabSelect {

    private Consumer notFind;

    private TabPane root;

    private TabSelect(TabPane root) {
        this.root = root;
    }

    public static TabSelect from(TabPane root) {
        return new TabSelect(root);
    }

    public TabSelect ifNotFind(Consumer notFind) {
        this.notFind = notFind;

        return this;
    }

    public void select(String text) {
        var target = root
                .getTabs().stream()
                .filter(targetTab -> targetTab.getText().equals(text))
                .findAny();

        if (target.isPresent()) {
            root.getSelectionModel().select(target.get());
        } else {
            if (notFind != null)
                notFind.accept(text);
        }
    }
}
