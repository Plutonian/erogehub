package com.goexp.galgame.data.parser;

import com.goexp.galgame.common.model.TagType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

public class GetchuTagParser {

    public List<TagType> parse(String html) {
        return Jsoup.parse(html)
                .select("#wrapper div.pc_headword:contains(カテゴリ一覧)").first()
                .parent()
                .select("div.category_pc_t")
                .stream()
                .map(this::parse)
                .collect(Collectors.toUnmodifiableList());
    }

    private TagType parse(Element item) {


        var tagType = new TagType();
        tagType.type = item.text();
        tagType.order = 0;


        tagType.tags = item.nextElementSibling().select("a")
                .stream()
                .map(ele -> ele.text().trim())
                .collect(Collectors.toUnmodifiableList());

        return tagType;

    }
}
