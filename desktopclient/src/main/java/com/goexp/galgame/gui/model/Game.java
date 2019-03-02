package com.goexp.galgame.gui.model;

import com.goexp.galgame.common.model.GameState;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Game {

    public int id;
    public String name = "";
    public LocalDate publishDate;
    public String smallImg = "";
    public String website = "";
    public List<String> writer; //シナリオ
    public List<String> painter;//原画
    public List<String> type; //サブジャンル
    public List<String> tag;
    public String story = ""; // ストーリー(HTML)
    public String intro = "";
    public SimpleObjectProperty<GameState> state = new SimpleObjectProperty<GameState>();
    public int star;

    public Brand brand;


    public List<GameCharacter> gameCharacters;

    public List<GameImg> gameImgs;

    public int getStar() {
        return star;
    }

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

    public GameState getState() {
        return state.get();
    }

    public SimpleObjectProperty<GameState> stateProperty() {
        return state;
    }

    public void setState(GameState isLike) {
        this.state.set(isLike);
    }

    public Brand getBrand() {
        return brand;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return id == game.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publishDate=" + publishDate +
                ", smallImg='" + smallImg + '\'' +
                ", website='" + website + '\'' +
                ", writer=" + writer +
                ", painter=" + painter +
                ", type=" + type +
                ", tag=" + tag +
//                ", story='" + story + '\'' +
//                ", intro='" + intro + '\'' +
                ", state=" + state +
                ", star=" + star +
                ", brand=" + brand +
                ", gameCharacters=" + gameCharacters +
                ", gameImgs=" + gameImgs +
                '}';
    }

    public static class GameImg {

        //    public String id;
        public String src;
        public int index;

        public int gameId;

        @Override
        public String toString() {
            return "GameImg{" +
                    //                "id='" + id + '\'' +
                    ", src='" + src + '\'' +
                    ", index=" + index +
                    //                ", gameId=" + gameId +
                    '}';
        }
    }

    public static class GameCharacter {
        public String id;

        public String name;

        public String cv;

        public String intro;

        public int gameId;

        public String trueCV;

        public String img;
        public int index;

        @Override
        public String toString() {
            return "GameCharacter{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", cv='" + cv + '\'' +
                    //                ", story='" + story + '\'' +
                    ", gameId=" + gameId +
                    ", trueCV='" + trueCV + '\'' +
                    ", img='" + img + '\'' +
                    ", index='" + index + '\'' +
                    '}';
        }
    }
}
