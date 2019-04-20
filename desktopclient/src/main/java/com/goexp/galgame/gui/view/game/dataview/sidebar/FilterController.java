package com.goexp.galgame.gui.view.game.dataview.sidebar;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;
import java.util.function.Predicate;

public abstract class FilterController<T> {
    public BooleanProperty onSetProperty = new SimpleBooleanProperty(false);
    public Predicate<T> predicate;

    public abstract void init(List<T> filteredGames);

    public static abstract class DefaultItemNode {
        public String title;
        public int count;

        public DefaultItemNode(String title, int count) {
            this.title = title;
            this.count = count;
        }
    }
}
