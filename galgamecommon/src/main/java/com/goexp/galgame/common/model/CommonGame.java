package com.goexp.galgame.common.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommonGame {
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

    public List<GameCharacter> gameCharacters;
    public List<GameImg> gameImgs;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommonGame)) return false;
        CommonGame game = (CommonGame) o;
        return id == game.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "CommonGame{" +
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
                ", gameCharacters=" + gameCharacters +
                ", gameImgs=" + gameImgs +
                '}';
    }

    public String simpleView() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publishDate=" + publishDate +
                ", imgUrl='" + smallImg + '\'' +
                '}';
    }

    public static class GameImg {

        public String src;
        public int index;

        @Override
        public String toString() {
            return "GameImg{" +
                    ", src='" + src + '\'' +
                    ", index=" + index +
                    '}';
        }
    }

    public static class GameCharacter {
        public String name;

        public String cv;

        public String intro;

        public String trueCV;

        public String img;
        public int index;

        @Override
        public String toString() {
            return "GameCharacter{" +
                    ", name='" + name + '\'' +
                    ", cv='" + cv + '\'' +
                    //                ", story='" + story + '\'' +
                    ", trueCV='" + trueCV + '\'' +
                    ", img='" + img + '\'' +
                    ", index='" + index + '\'' +
                    '}';
        }
    }

    public static class Guide {
        public String id;
        public String title;
        public String href;
        public DataFrom from;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Guide)) return false;
            Guide guide = (Guide) o;
            return id.equals(guide.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "Guide{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", href='" + href + '\'' +
                    ", from=" + from +
                    '}';
        }

        public enum DataFrom {
            seiya_saiga_com(1),
            sagaoz_net(2);

            private int value;

            DataFrom(int value) {
                this.value = value;
            }

            public static DataFrom from(int value) {
                return Arrays.stream(values()).filter(from -> from.value == value).findFirst().orElseThrow(() -> new RuntimeException("Error DataFrom value:" + value));
            }

            public int getValue() {
                return value;
            }
        }
    }
}
