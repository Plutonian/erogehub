package com.goexp.galgame.data.parser.game;

import com.goexp.galgame.data.model.Game;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ListPageParser {

    public List<Game> parse(String html) {


        return Jsoup.parse(html)
                .select("ul.display>li>div.content_block")

                .stream()
                .map(GameParser::parse)
                .collect(Collectors.toUnmodifiableList());
    }


    private static class GameParser {
        private static Game parse(Element item) {

            var g = new Game();

            var img = item.select("img.lazy").attr("data-original");

            var gEle = item.select("div.content_block a.blueb");
            var name = gEle.text();
            var url = gEle.attr("href");

            g.id = parseId(url);
            g.name = name;
            g.smallImg = img;

            var raw = item.select("div.content_block p").text();
            g.publishDate = parseDate(raw);

            return g;
        }

        private static int parseId(String url) {
            var m = Pattern.compile("id=(?<id>\\d+)").matcher(url);
            return m.find() ? Integer.parseInt(m.group("id")) : 0;
        }

        private static LocalDate parseDate(String str) {

            var m = Pattern.compile("発売日：(?<date>\\d{4}/[0-1]\\d/[0-3]\\d)").matcher(str);
            return m.find() ? LocalDate.parse(m.group("date"), DateTimeFormatter.ofPattern("yyyy/MM/dd")) : null;
        }
    }
}
