package com.goexp.galgame.gui.view.game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.TilePane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateController {

    private final Logger logger = LoggerFactory.getLogger(DateController.class);


    public BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);

    public BooleanProperty onYearLoadProperty = new SimpleBooleanProperty(false);

    public String title;

    public LocalDate from;

    public LocalDate to;

    @FXML
    private TilePane dateCon;

    @FXML
    private TilePane flowYear;

    private ToggleGroup yearSelect = new ToggleGroup();

    private ToggleGroup monthSelect = new ToggleGroup();

    @FXML
    private void initialize() {

        var monthNodes = IntStream.rangeClosed(1, 12).boxed()
                .map(month -> {
                    var tog = new ToggleButton();
                    tog.setUserData(month);

                    if (month.equals(LocalDate.now().getMonthValue()))
                        tog.setText("今月");
                    else
                        tog.setText(month + "月");

                    tog.setToggleGroup(monthSelect);

                    return tog;
                })
                .collect(Collectors.toList());

        dateCon.getChildren().setAll(monthNodes);


        monthSelect.selectedToggleProperty().addListener((o, old, newV) -> {
            if (newV != null) {

                var month = (Integer) newV.getUserData();

                from = LocalDate.now().withMonth(month).withDayOfMonth(1);
                to = from.plusMonths(1).minusDays(1);
                title = month + "月";

                onLoadProperty.set(true);

                logger.debug("Range:{}  {}", from, to);
                onLoadProperty.set(false);
            }

        });


        var yearNodes = IntStream.rangeClosed(2000, LocalDate.now().getYear()).boxed()
                .map(year -> {
                    var tog = new ToggleButton();
                    tog.setUserData(year);


                    if (year.equals(LocalDate.now().getYear()))
                        tog.setText("今年");
                    else
                        tog.setText(year + "年");

                    tog.setToggleGroup(yearSelect);

                    return tog;
                })
                .collect(Collectors.toList());

        flowYear.getChildren().setAll(yearNodes);

        yearSelect.selectedToggleProperty().addListener((o, old, newV) -> {
            if (newV != null) {

                var year = (Integer) newV.getUserData();

                from = LocalDate.of(year, 1, 1);
                to = LocalDate.of(year, 12, 31);
                title = year + "年";


                onYearLoadProperty.set(true);
                logger.debug("Range:{}  {}", from, to);
                onYearLoadProperty.set(false);
            }

        });
    }

    public void load() {
        onLoadProperty.set(false);
    }
}
