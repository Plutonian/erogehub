package com.goexp.galgame.data.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Game {

    public int id;
    public String name;
    public LocalDate publishDate;
    public String smallImg;
    public String website;
    public List<String> writer; //シナリオ
    public List<String> painter;//原画
    public List<String> type; //サブジャンル
    public List<String> tag;
    public String story = ""; // ストーリー(HTML)
    public String intro = "";


    public int brandId;

    public List<GameCharacter> gameCharacterList;

    public List<Img> imgList;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return id == game.id &&
//                Objects.equals(name, game.name) &&
//                Objects.equals(publishDate, game.publishDate) &&
//                Objects.equals(imgUrl, game.imgUrl) &&
//                Objects.equals(website, game.website) &&
                Objects.equals(brandId, game.brandId) &&
                Objects.equals(writer, game.writer) &&
                Objects.equals(painter, game.painter) &&
                Objects.equals(type, game.type) &&
                Objects.equals(tag, game.tag) &&
                Objects.equals(story, game.story);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
//                name,
//                publishDate,
//                imgUrl,
                brandId,
                writer,
                painter,
                type,
                tag,
                story);
    }

//    @Override
//    public String toString() {
//        return "Game{" +
//                "id=" + id +
//                ", writer='" + writer + '\'' +
//                ", painter='" + painter + '\'' +
//                ", type='" + type + '\'' +
//                ", tag='" + tag + '\'' +
//                ", story='" + (story!=null?story.length():0) + '\'' +
//                '}';
//    }


    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
//                ", name='" + name + '\'' +
//                ", publishDate=" + publishDate +
//                ", imgUrl='" + imgUrl + '\'' +
//                ", website='" + website + '\'' +
                ", writer='" + writer + '\'' +
                ", painter='" + painter + '\'' +
                ", type='" + type + '\'' +
                ", tag='" + tag + '\'' +
                ", story='" + (story != null ? story.length() : 0) + '\'' +
//                ", hash='" + hash + '\'' +
//                ", brandId=" + brandId +
                '}';
    }

    public String simpleView() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publishDate=" + publishDate +
                ", imgUrl='" + smallImg + '\'' +
                ", brandId=" + brandId +
                '}';
    }

    public static class Img {

        public String id;
        public String src;
        public int index;
        public int gameId;

        @Override
        public String toString() {
            return "Img{" +
                    "id='" + id + '\'' +
                    ", src='" + src + '\'' +
                    ", index=" + index +
                    ", gameId=" + gameId +
                    '}';
        }
    }

    public static class GameCharacter {
        public String id;

        public String name;
        public String cv;
        public String trueCV = "";
        public String img;
        public String intro;
        public int index;

        public int gameId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GameCharacter)) return false;
            GameCharacter that = (GameCharacter) o;
            return index == that.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index);
        }

        @Override
        public String toString() {
            return "GameCharacter{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", cv='" + cv + '\'' +
                    //                ", trueCV='" + trueCV + '\'' +
                    ", img='" + img + '\'' +
                    //                ", intro='" + intro + '\'' +
                    ", index='" + index + '\'' +
                    ", gameId=" + gameId +
                    '}';
        }
    }

    public static class Guide {
        public enum DataFrom {
            seiya_saiga_com(1),
            sagaoz_net(2);

            private int value;

            DataFrom(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        public String id;

        public String title;
        public String content;
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
//                    ", content='" + content + '\'' +
                    ", from=" + from +
                    '}';
        }
    }
}
