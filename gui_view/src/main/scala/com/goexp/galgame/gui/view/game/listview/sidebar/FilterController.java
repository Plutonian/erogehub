package com.goexp.galgame.gui.view.game.listview.sidebar;

import com.goexp.galgame.gui.view.DefaultController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;
import java.util.function.Predicate;

public abstract class FilterController<T> extends DefaultController {
    public final BooleanProperty onSetProperty = new SimpleBooleanProperty(false);
    public Predicate<T> predicate;

    public abstract void init(List<T> filteredGames);

}
