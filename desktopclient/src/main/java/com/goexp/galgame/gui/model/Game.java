package com.goexp.galgame.gui.model;

import com.goexp.galgame.common.model.CommonGame;
import com.goexp.galgame.common.model.GameState;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Game extends CommonGame {
    public SimpleObjectProperty<GameState> state = new SimpleObjectProperty<GameState>();
    public Brand brand;
    public int star;


    public LocalDate getPublishDate() {
        return publishDate;
    }

    public String getName() {
        return name;
    }

    public String getSmallImg() {
        return smallImg;
    }

    public String getWebsite() {
        return website;
    }

    public int getId() {
        return id;
    }

    public String getStory() {
        return story;
    }

    public String getWriter() {
        return writer.stream().collect(Collectors.joining(","));
    }

    public String getPainter() {
        return painter.stream().collect(Collectors.joining(","));
    }

    public String getType() {
        return type.stream().collect(Collectors.joining(","));
    }

    public List<String> getTag() {
        return tag;
    }

    public String getIntro() {
        return intro;
    }

    public GameState getState() {
        return state.get();
    }

    public void setState(GameState isLike) {
        this.state.set(isLike);
    }

    public SimpleObjectProperty<GameState> stateProperty() {
        return state;
    }

    public int getStar() {
        return star;
    }

    public Brand getBrand() {
        return brand;
    }


    @Override
    public String toString() {
        return "Game{" +
                "state=" + state +
                ", brand=" + brand +
                "} " + super.toString();
    }
}
