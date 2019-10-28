package com.goexp.galgame.common.model.game;

import java.time.LocalDate;
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


}
