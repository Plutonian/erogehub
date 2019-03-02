package com.goexp.galgame.data.parser;

import com.goexp.galgame.data.model.Brand;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GetchuBrandParser {

    public List<Brand> parse(String html) {

        var list = new ArrayList<Brand>();

        Jsoup.parse(html)
                .select("#wrapper > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td table[cellpadding=1]")
                .stream()
                .skip(1)
                .forEach(item -> {
                    list.addAll(parse(item));
                });

        return list;
    }

    private List<Brand> parse(Element item) {

//        var index = item.select("b").text().replace("行", "");


        return item.nextElementSibling().select("tr:nth-of-type(2) tr")
                .stream()
                .filter(ele -> {
                    return ele.html().length() > 0;
                })
                .map(ele -> {

                    var brand = new Brand();

                    var titleEle = ele.select("td:nth-of-type(1)>a");
                    var idUrl = titleEle.attr("href");
                    brand.name = titleEle.text();

                    var m = Pattern.compile("search_brand_id=(?<id>\\d+)").matcher(idUrl);
                    brand.id = m.find() ? Integer.parseInt(m.group("id")) : 0;

                    var websiteEle = ele.select("td:nth-of-type(2)>a");
                    brand.website = websiteEle.attr("href");
//                    brand.index = index;

                    return brand;
                }).collect(Collectors.toList());

    }
}
