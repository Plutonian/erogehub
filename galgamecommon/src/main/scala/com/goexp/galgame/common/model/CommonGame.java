package com.goexp.galgame.common.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public abstract class CommonGame {
    private static final Pattern NAME_SPLITER_REX = Pattern.compile("[〜「]");


    public static class Titles {
        public final String mainTitle;
        public final String subTitle;

        public Titles(String mainTitle, String subTitle) {
            this.mainTitle = mainTitle;
            this.subTitle = subTitle;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Titles.class.getSimpleName() + "[", "]")
                    .add("mainTitle='" + mainTitle + "'")
                    .add("subTitle='" + subTitle + "'")
                    .toString();
        }
    }

    public Titles getTitles() {
        String tName = name
                .replaceAll("(?:マウスパッド付|”Re-order”〜?|BLUE Edition|WHITE Edition|プレミアムエディション|EDITION|祝！TVアニメ化記念|“男の子用”付|“女の子用”付|期間限定感謝ぱっく|感謝ぱっく|Liar-soft Selection \\d{2})", "").trim()
                .replaceAll("(?:\\[[^]]+\\]$)|(?:＜[^＞]+＞)|(?:（[^）]+）$)", "")
                .replaceAll("[\\s　〜][^\\s〜　]+[版]", "")
                .replaceAll("(?:CD|DVD)(?:-ROM版)?", "").trim()
                .replaceAll("(?:\\[[^]]+\\]$)|(?:＜[^＞]+＞)|(?:（[^）]+）$)", "")
                .trim();

        final var matcher = NAME_SPLITER_REX.matcher(tName);
        final var find = matcher.find();
        String mainTitle;
        String subTitle;
        if (find) {
            mainTitle = tName.substring(0, matcher.start());

            subTitle = tName.substring(matcher.start());

        } else {
            mainTitle = tName;
            subTitle = "";
        }

        return new Titles(mainTitle.trim(), subTitle.trim());

    }


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

    public boolean isNew = false;


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


    public static class GameImg {

        public String src;
        public int index;

        @Override
        public String toString() {
            return new StringJoiner(", ", GameImg.class.getSimpleName() + "[", "]")
                    .add("" + index)
                    .add("'" + src + "'")
                    .toString();
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
            return new StringJoiner(", ", "\n" + GameCharacter.class.getSimpleName() + "[", "]")
                    .add("" + index)
                    .add("'" + name + "'")
                    .add("cv='" + cv + "'")
//                    .add("intro='" + intro + "'")
                    .add("trueCV='" + trueCV + "'")
//                    .add("img='" + img + "'")
                    .toString();
        }
    }

    public static class Guide {
        public String id;
        public String title;
        public String href;
        public String html;
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
            return new StringJoiner(", ", Guide.class.getSimpleName() + "[", "]")
                    .add("id='" + id + "'")
                    .add("title='" + title + "'")
                    .add("href='" + href + "'")
                    .add("from=" + from)
                    .toString();
        }

        public enum DataFrom {
            seiya_saiga_com(1, "誠也の部屋", "http://seiya-saiga.com/game/kouryaku.html"),
            sagaoz_net(2, "愚者の館", "http://sagaoz.net/foolmaker/game.html");

            public final int value;
            public final String name;
            public final String href;


            DataFrom(int value, String name, String href) {
                this.value = value;
                this.name = name;
                this.href = href;
            }

            public static DataFrom from(int value) {
                return Arrays.stream(values()).filter(from -> from.value == value).findFirst().orElseThrow(() -> new RuntimeException("Error create DataFrom from:" + value));
            }

            @Override
            public String toString() {
                return this.name;
            }
        }
    }
}
