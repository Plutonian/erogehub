package com.goexp.galgame.data.parser;

import com.goexp.galgame.data.model.Game;
import org.jsoup.Jsoup;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameGuideParser {

    public static class Sagaoz_Net {

        public List<Game.Guide> parse(String html) {
            Objects.requireNonNull(html);

            return Jsoup.parse(html)
                    .select("table[width=720]:not(:nth-of-type(1))")
                    .select("a")
                    .stream()
                    .filter(a -> !a.attr("href").isEmpty())
                    .map(a -> {

                        var guide = new Game.Guide();
                        guide.title = a.text();
                        guide.from = Game.Guide.DataFrom.sagaoz_net;
                        guide.href = a.attr("href");
                        guide.id = guide.href;

                        return guide;
                    })
                    .collect(Collectors.toUnmodifiableList());
        }
    }


    public static class Seiya_saiga {

        public List<Game.Guide> parse(String html) {
            Objects.requireNonNull(html);

            return Jsoup.parse(html)
                    .select("body > div > table > tbody > tr > th> table")
                    .stream()
                    .skip(4)
                    .flatMap(element -> {
                        return element.select("a")
                                .stream()
                                .map(a -> {

                                    var guide = new Game.Guide();
                                    guide.title = a.text();
                                    guide.from = Game.Guide.DataFrom.seiya_saiga_com;
                                    guide.href = "http://seiya-saiga.com/" + a.attr("href");
                                    guide.id = guide.href;

                                    return guide;
                                });
                    })
                    .collect(Collectors.toUnmodifiableList());
        }
    }
}
