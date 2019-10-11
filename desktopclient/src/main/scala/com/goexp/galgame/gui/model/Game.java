package com.goexp.galgame.gui.model;

import com.goexp.common.util.string.Strings;
import com.goexp.galgame.common.model.CommonGame;
import com.goexp.galgame.common.model.GameState;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import static com.goexp.common.util.string.ConsoleColors.RED;

public class Game extends CommonGame {
    public SimpleObjectProperty<GameState> state = new SimpleObjectProperty<>();
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
        return String.join(",", writer);
    }

    public String getPainter() {
        return String.join(",", painter);
    }

    public String getType() {
        return String.join(",", type);
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
        return infoView();
    }


    public String debugView() {
        return new StringJoiner(", ", Game.class.getSimpleName() + "[", "]")
                .add("id=" + RED.s(String.valueOf(id)))
                .add("name='" + RED.s(name) + "'")
                .add("publishDate=" + publishDate)
                .add("smallImg='" + smallImg + "'")
                .add("website='" + website + "'")
                .add("writer=" + writer)
                .add("painter=" + painter)
                .add("type=" + type)
                .add("tag=" + tag)
//                .add("story='" + story + "'")
//                .add("intro='" + intro + "'")
                .add("state=" + state)
                .add("star=" + star)
                .add("brand=" + brand)
                .add("gameImgs=" + gameImgs)
                .add("gameCharacters=" + gameCharacters)
                .toString();
    }

    public String infoView() {
        return new StringJoiner(", ", Game.class.getSimpleName() + "[", "]")
                .add("id=" + RED.s(String.valueOf(id)))
                .add("name='" + RED.s(name) + "'")
                .add("publishDate=" + publishDate)
                .add("smallImg='" + smallImg + "'")
//                .add("website='" + website + "'")
                .add("writer=" + writer)
                .add("painter=" + painter)
                .add("type=" + type)
                .add("tag=" + tag)
//                .add("story='" + story + "'")
//                .add("intro='" + intro + "'")
                .add("state=" + state.get())
                .add("star=" + star)
                .add("\nbrand=" + brand)
                .add("\ngameImgs=" + Optional.ofNullable(gameImgs).map(List::size).orElse(0))
                .add("\ngameCharacters=" + gameCharacters)
                .toString();
    }

    public boolean isOkState() {
        return Optional.ofNullable(this.state)
                .map(ObjectPropertyBase::get)
                .map(gs -> gs.value >= GameState.UNCHECKED.value || gs == GameState.PACKAGE)
                .orElse(false);
    }

    public boolean isOkImg() {
        return Strings.isNotEmpty(smallImg) && smallImg.startsWith("http");
    }

}
