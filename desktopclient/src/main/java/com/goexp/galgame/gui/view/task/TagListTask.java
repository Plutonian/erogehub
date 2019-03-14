package com.goexp.galgame.gui.view.task;

import com.goexp.galgame.common.model.TagType;
import com.goexp.galgame.gui.db.mongo.query.TagQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class TagListTask extends Task<ObservableList<TagType>> {

    @Override
    protected ObservableList<TagType> call() throws Exception {
        return FXCollections.observableArrayList(new TagQuery().types());
    }
}
