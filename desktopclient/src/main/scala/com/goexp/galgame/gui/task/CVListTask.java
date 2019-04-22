package com.goexp.galgame.gui.task;

import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.gui.db.mongo.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CVListTask extends Task<ObservableList<CV>> {

    @Override
    protected ObservableList<CV> call() {

        var list = Query.CVQuery.tlp.query().list();

        return FXCollections.observableArrayList(list);
    }
}
