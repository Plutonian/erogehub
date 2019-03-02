package com.goexp.galgame.gui.view.task;

import com.goexp.galgame.gui.db.mongo.query.TagQuery;
import com.goexp.galgame.gui.model.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class TagListTask extends Task<ObservableList<Tag.TagType>> {

    @Override
    protected ObservableList<Tag.TagType> call() throws Exception {
        return FXCollections.observableArrayList(new TagQuery().types());
    }
}
