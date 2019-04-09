package com.goexp.galgame.data.task.others;

import com.goexp.common.util.Strings;
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

    static class Processor {

        static final Pattern hostRegex = Pattern.compile("http[s]?://(?:ww[^\\.]+\\.)?(?<host>[^/]+)[/]?");

        private final static Set<String> rem = Set.of("x",
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

        private static List<String> clean(String host) {
            return Arrays.stream(host.split("\\.")).filter(s -> !rem.contains(s)).collect(Collectors.toUnmodifiableList());
        }

        private static String getComp(String host) {
            var temp = clean(host);
            return temp.size() > 0 ? temp.get(temp.size() - 1) : "";
        }

        private static String getHost(String url) {
            var hostMatcher = hostRegex.matcher(url);
            return hostMatcher.find() ? hostMatcher.group("host") : "";
        }


        static String comp(String url) {
            var host = getHost(url);

            return getComp(host);
        }
    }

    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(GroupBrandTask.class);


        var importor = new BrandDB();

        BrandQuery.tlp.query().list().stream()
                .filter(b -> !Strings.isEmpty(b.website))
                .collect(Collectors.groupingBy(b -> Processor.comp(b.website)))
                .forEach((k, v) -> {
                    if (!k.isEmpty() && v.size() > 1) {
                        logger.debug("{}", k);

                        v.forEach(b -> {
                            if (Strings.isEmpty(b.comp)) {
                                logger.info("Raw:{} New:{}", b.comp, k);
                                b.comp = k;
                                importor.updateComp(b);
                            }
                        });
                    }
                });

    }


    static class GetRemove {

        public static void main(String[] args) {

            BrandQuery.tlp.query().list().stream()
                    .filter(b -> !Strings.isEmpty(b.website))
                    .flatMap(b -> {
                        var host = Processor.getHost(b.website);

                        return Arrays.stream(host.split("\\.")).skip(1);
                    })
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.groupingBy(str -> str))
                    .keySet().stream()
                    .sorted(Comparator.comparing(String::length))
                    .forEach(k -> {
                        System.out.println("\"" + k + "\"" + ",");
                    });
        }
    }
}
