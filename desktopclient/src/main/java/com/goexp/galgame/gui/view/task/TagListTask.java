package com.goexp.galgame.gui.view.task;

import com.goexp.galgame.common.model.TagType;
import com.goexp.galgame.gui.db.mongo.query.TagQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import static com.mongodb.client.model.Sorts.ascending;

public class TagListTask extends Task<ObservableList<TagType>> {

    @Override
    protected ObservableList<TagType> call() {
        return FXCollections.observableArrayList(
                TagQuery.tlp.query()
                        .sort(ascending("order"))
                        .list()
        );
    }
}
