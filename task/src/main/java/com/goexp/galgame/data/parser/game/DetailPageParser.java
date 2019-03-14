package com.goexp.galgame.data.parser.game;

import com.goexp.galgame.data.model.Game;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DetailPageParser {

    public Game parse(int gameId, String html) {

        var root = Jsoup.parse(html);

        var game = new DetailPartParser().parse(gameId, root);
        game.gameCharacters = new GameCharPartParser().parse(gameId, root);
        game.gameImgs = new SimpleImgPartParser().parse(gameId, root);

        return game;
    }


    private static class DetailPartParser {

        private final static String selector = "#soft_table >tbody>tr:nth-of-type(2)";

        private final static Pattern BRAND_ID_REX = Pattern.compile("search_brand_id=(?<brandid>\\d+)$");

        private Game parse(final int gameId, final Document root) {

            var ele = root.select(selector);

            var g = new Game();
            g.id = gameId;

            g.painter = Arrays.asList(ele.select("td:contains(原画)").next().text()
                    .split("、"));

            g.writer = Arrays.asList(ele.select("td:contains(シナリオ)").next().text()
                    .split("、"));

            g.type = Arrays.asList(ele.select("td:contains(サブジャンル)").next().text()
                    .replace("[一覧]", "")
                    .split("、"));

            g.tag = Arrays.asList(ele.select("td:contains(カテゴリ)").next().text()
                    .replace("[一覧]", "")
                    .split("、"));

            g.story = root.select("#wrapper div.tabletitle:contains(ストーリー)").next()
                    .html()
                    .replaceAll("\\<[^\\>]*\\>", "")
                    .replace("<br>", "")
                    .trim();

            g.intro = root.select("#wrapper div.tabletitle:contains(商品紹介)").next()
                    .html()
                    .replaceAll("\\<[^\\>]*\\>", "")
                    .replace("<br>", "")
                    .trim();

            var brandUrl = ele.select("a:contains(このブランドの作品一覧)").attr("href");

            var m = BRAND_ID_REX.matcher(brandUrl);
            g.brandId = m.find() ? Integer.valueOf(m.group("brandid")) : 0;

            return g;
        }
    }

    private static class GameCharPartParser {
        private static final Pattern cvPattern = Pattern.compile("（?[Cc][vV]\\s*[：:\\.／/]?\\s*(?<cv>[^）]+)）?$");

        private int index = 1;

        private String parseName(String str) {
            return str.replaceAll("（?[Cc][vV][：:\\.／/]?）?", "").trim();
        }

        private String parseCV(String str) {
            var cvM = cvPattern.matcher(str);
            return cvM.find() ? cvM.group("cv").trim() : "";
        }

        private List<Game.GameCharacter> parse(int gameId, Document root) {
            return root.select("#wrapper div.tabletitle:contains(キャラクター)")
                    .next()
                    .select("tbody>tr:nth-of-type(2n+1)")
                    .stream()
                    .map(tr -> {

                        var gameCharacter = new Game.GameCharacter();
                        gameCharacter.index = index;
                        var title = tr.select("h2.chara-name").text();
                        gameCharacter.img = tr.select("td:nth-of-type(1)>img").attr("src");
                        gameCharacter.cv = parseCV(title);

//                    if (gameCharacter.cv.length() > 0) {
//                        var trueCV=cvset.stream().filter(cv -> cv.otherNames.contains(gameCharacter.cv.toLowerCase())).findAny().orElse(new CV("", ""));
                        gameCharacter.trueCV = "";
//                    }
                        gameCharacter.name = parseName(title.replace(gameCharacter.cv, ""));
                        gameCharacter.intro = tr.select("dl dd").html().replaceAll("\\<[^\\>]*\\>", "").trim();
                        index++;
                        return gameCharacter;
                    }).collect(Collectors.toList());
        }

    }

    private static class SimpleImgPartParser {

        private int imgIndex = 1;

        private List<Game.GameImg> parse(int gameId, Document root) {
            return root.select("#wrapper div.tabletitle:contains(サンプル画像)")
                    .next()
                    .select("a.highslide")
                    .stream()
                    .map(a -> {

                        var img = new Game.GameImg();
                        img.src = a.attr("href");
                        img.index = imgIndex;
                        imgIndex++;
                        return img;
                    }).collect(Collectors.toList());
        }
    }


}
