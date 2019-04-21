package com.goexp.galgame.gui.view.game.detailview.part;

import com.goexp.common.util.DateUtil;
import com.goexp.galgame.gui.util.DefaultController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateShowController extends DefaultController {


    @FXML
    private Label lbYear;

    @FXML
    private Label lbMonth;

    @FXML
    private Label lbDay;

    @FXML
    private Label lbDays;


    @FXML
    private Region calPanel;
    @FXML
    private Region strPanel;


    public void load(LocalDate date) {
        if (date == null) {
            strPanel.setVisible(true);
            calPanel.setVisible(false);
            lbDays.setText("");
            return;
        }

        if (DateUtil.needFormat(date)) {
            strPanel.setVisible(true);
            calPanel.setVisible(false);
            lbDays.setText(DateUtil.formatDate(date));
        } else {

            strPanel.setVisible(false);
            calPanel.setVisible(true);
            lbYear.setText(date.format(DateTimeFormatter.ofPattern("yyyy")));
            lbMonth.setText(date.format(DateTimeFormatter.ofPattern("MMM")));
            lbDay.setText(date.format(DateTimeFormatter.ofPattern("dd")));
            calPanel.toFront();
        }
    }

    @Override
    protected void initialize() {

    }
}
