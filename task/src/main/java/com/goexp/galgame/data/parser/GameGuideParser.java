package com.goexp.galgame.data.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.function.Consumer;

public class GameGuideParser {

    public static class Sagaoz_Net {

        public void parse(String html, Consumer<Element> back) {
            Objects.requireNonNull(html);
            Objects.requireNonNull(back);

            Jsoup.parse(html)
                    .select("table[width=720]:not(:nth-of-type(1))")
                    .select("a")
                    .stream()
                    .filter(a -> !a.attr("href").isEmpty())
                    .forEach(a -> {
                        back.accept(a);
                    });
        }


    }
}
