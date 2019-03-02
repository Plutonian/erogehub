package com.goexp.galgame.gui.view.task;

import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.gui.db.ICVQuery;
import com.goexp.galgame.gui.db.mongo.query.CVQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CVListTask extends Task<ObservableList<CV>> {


    private ICVQuery cvQuery = new CVQuery();

    @Override
    protected ObservableList<CV> call() {

        var list = cvQuery.cvList();

        return FXCollections.observableArrayList(list);
    }
}
