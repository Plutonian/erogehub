package com.goexp.galgame.gui.view;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DefaultController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    protected abstract void initialize();
}
