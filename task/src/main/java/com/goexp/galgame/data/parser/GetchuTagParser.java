package com.goexp.galgame.data.parser;

import com.goexp.galgame.data.model.Tag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetchuTagParser {

    public List<Tag.TagType> parse(String html) {

        var list = new ArrayList<Tag.TagType>();

        Jsoup.parse(html)
                .select("#wrapper div.pc_headword:contains(カテゴリ一覧)").first()
                .parent()
                .select("div.category_pc_t")
                .forEach(item -> {
                    list.add(parse(item));
                });

        return list;
    }

    private Tag.TagType parse(Element item) {


        var tagType = new Tag.TagType();
        tagType.type = item.text();
        tagType.order = 0;


        tagType.tags = item.nextElementSibling().select("a")
                .stream()
                .map(ele -> {
                    return ele.text();
                }).collect(Collectors.toList());

        return tagType;

    }
}
