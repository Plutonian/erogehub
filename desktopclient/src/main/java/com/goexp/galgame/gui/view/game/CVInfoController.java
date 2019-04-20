package com.goexp.galgame.gui.view.game;

import com.goexp.galgame.common.model.CV;
import com.goexp.galgame.gui.task.CVListTask;
import com.goexp.galgame.gui.util.TaskService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class CVInfoController {

    private final Logger logger = LoggerFactory.getLogger(CVInfoController.class);
    public BooleanProperty onLoadProperty = new SimpleBooleanProperty(false);
    /***
     * Biz
     */

    public String cv;
    /**
     * UI Com
     */

    @FXML
    private FlowPane cvFlow;
    private Service<ObservableList<CV>> loadCVService = new TaskService<>(CVListTask::new);



    /**
     * Event
     */

    @FXML
    private void initialize() {

        ChangeListener<Throwable> exceptionHandler = (observable, oldValue, newValue) -> {
            if (newValue != null)
                newValue.printStackTrace();
        };

        loadCVService.exceptionProperty().addListener(exceptionHandler);
        loadCVService.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                var nodes = newValue.stream()
                        .collect(Collectors.groupingBy(cv -> cv.star))
                        .entrySet().stream()
                        .sorted(Comparator.comparing(((Map.Entry entry) -> (Integer) entry.getKey())).reversed())
                        .map(entry -> {
                            var flow = new FlowPane();
                            var panel = new TitledPane(entry.getKey().toString(), flow);
                            panel.setCollapsible(false);
                            panel.setPrefWidth(300);

                            var links = entry.getValue().stream()
                                    .map(cv -> {

                                        var link = new Hyperlink(cv.name);
//                                        link.setStyle("-fx-font-size:18px");
                                        link.getStyleClass().add("tag");

                                        return link;
                                    })
                                    .collect(Collectors.toUnmodifiableList());

                            flow.getChildren().setAll(links);


                            return panel;
                        })
                        .collect(Collectors.toUnmodifiableList());

                cvFlow.getChildren().setAll(nodes);
            }

        });

        onLoadProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {
                HomeController.$this.loadCVTab(cv, true);
            }
        });

        cvFlow.addEventFilter(ActionEvent.ACTION, event -> {

            if (event.getTarget() instanceof Hyperlink) {
                var link = (Hyperlink) event.getTarget();
                cv = link.getText();
                onLoadProperty.set(true);
                onLoadProperty.set(false);
            }
        });


    }


    public void load() {

        loadCVService.restart();

    }


}
