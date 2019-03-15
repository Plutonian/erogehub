package com.goexp.galgame.data.task.others;

import com.goexp.galgame.data.db.importor.mongdb.BrandDB;
import com.goexp.galgame.data.db.query.mongdb.BrandQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class GroupBrandTask {

    static Set<String> rem = Set.of("x",
            "ad",
            "bz",
            "cc",
            "co",
            "ea",
            "gr",
            "id",
            "in",
            "jp",
            "kt",
            "la",
            "me",
            "ne",
            "nu",
            "nz",
            "or",
            "oz",
            "ph",
            "pw",
            "sc",
            "tk",
            "to",
            "tv",
            "vc",
            "com",
            "net",
            "app",
            "ass",
            "fc2",
            "web",
            "jpn",
            "biz",
            "dti",
//            "ssw",
//            "q-x",
            "ics",
            "kir",
//            "mmv",
            "org",
            "xii",
//            "m3e",
//            "zoo",
//            "suki",
            "info",
            "from",
            "site",
            "soft",
            "sexy",
            "game",
            "software"
    );

    static List<String> clean(String host) {
        return Arrays.stream(host.split("\\.")).filter(s -> !rem.contains(s)).collect(Collectors.toUnmodifiableList());
    }

    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(GroupBrandTask.class);


        var importor = new BrandDB();

        var hostRegex = Pattern.compile("http[s]?://(?:ww[^\\.]+\\.)?(?<host>[^/]+)[/]?");

        BrandQuery.tlp.query()
                .list()
                .stream()
                .filter(b -> {
                    return b.website != null && b.website.trim().length() > 0;
                })
                .collect(Collectors.groupingBy(b -> {
                    var hostMatcher = hostRegex.matcher(b.website);
                    var host = hostMatcher.find() ? hostMatcher.group("host") : "";

                    logger.debug("{}", b.website);
                    logger.debug("{}", host);


                    var cleaned = clean(host);

                    return cleaned.size() > 0 ? cleaned.get(cleaned.size() - 1) : "";

                }))
                .entrySet().stream()

                .filter(entry -> !entry.getKey().isEmpty() && entry.getValue().size() > 1)
                .forEach(entry -> {

                    logger.debug("{}", entry.getKey());

                    entry.getValue().forEach(b -> {
                        b.comp = entry.getKey();
//                        logger.debug("{}", b);
                        importor.updateComp(b);
                    });


                });


    }


    static class GetRemove {

        public static void main(String[] args) {

            var hostRegex = Pattern.compile("http[s]?://(?:ww[^\\.]+\\.)?(?<host>[^/]+)[/]?");

            BrandQuery.tlp.query()
                    .list()
                    .stream()
                    .filter(b -> {
                        return b.website != null && b.website.trim().length() > 0;
                    })
                    .flatMap(b -> {
                        var hostMatcher = hostRegex.matcher(b.website);
                        var host = hostMatcher.find() ? hostMatcher.group("host") : "";


                        return Arrays.stream(host.split("\\.")).skip(1);
                    })
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.groupingBy(str -> str))
                    .keySet().stream()
                    .sorted(Comparator.comparing(str -> str.length()))
                    .forEach(k -> {

                        System.out.println("\"" + k + "\"" + ",");
                    });
        }
    }
}
