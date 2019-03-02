package com.goexp.galgame.common.website;

import com.goexp.common.util.UrlBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class GetchuURL {

    private final static String searchUrl = "http://www.getchu.com/php/search.phtml";

    public static String getURLfromTitle(String title) {

        return UrlBuilder
                .create(searchUrl)
                .param("sort", "release_date")
                .param("genre", "pc_soft")
                .param("search_keyword", URLEncoder.encode(title, Charset.forName("shift-jis")))
                .build();

    }

    public static String getListByBrand(int brandId) {
        return UrlBuilder
                .create(searchUrl)
                .param("search", "1")
                .param("sort", "release_date")
                .param("genre", "pc_soft")
                .param("search_brand_id", String.valueOf(brandId))
                .param("list_count", "1000")
                .build();
    }

    public static String gameListFromDateRange(LocalDate from, LocalDate to) {
        return UrlBuilder
                .create(searchUrl)
                .param("search", "1")
                .param("sort", "release_date")
                .param("genre", "pc_soft")
                .param("start_date", from.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .param("end_date", to.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .param("list_count", "1000")
                .build();
    }

    public static String getFromId(int gameId) {

        return UrlBuilder
                .create("http://www.getchu.com/soft.phtml")
                .param("id", String.valueOf(gameId))
//                .param("gc", "gc")
                .build();

    }


    public static String getImgSmallFromId(int gameId) {

        return String.format("http://www.getchu.com/brandnew/%d/rc%dpackage.jpg", gameId, gameId);
    }

    public static String getImgBigFromId(int gameId) {

        return String.format("http://www.getchu.com/brandnew/%d/c%dpackage.jpg", gameId, gameId);
    }

    public static String getSimpleImgBigFromSrc(String src) {

        return getUrlFromSrc(src);
    }

    public static String getSimpleImgSmallFromSrc(String src) {

        var ts = src.replace(".jpg", "_s.jpg");

        return getUrlFromSrc(ts);
    }

    public static String getUrlFromSrc(String src) {

        if (src.startsWith("http") || src.startsWith("https"))
            return src;

        return String.format("http://www.getchu.com%s", src.replaceFirst("\\.", ""));
    }

//    public static String getCharSmallFromId(int gameId, int charIndex) {
//
//        return String.format("http://www.getchu.com/brandnew/%d/c%dchara%d.jpg", gameId, gameId, charIndex);
//    }

    public static class RequestBuilder {
        private HttpRequest.Builder builder;


        private RequestBuilder(String url) {

            builder = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
            ;
        }

        public static RequestBuilder create(String url) {
            return new RequestBuilder(url);
        }


        public RequestBuilder adaltFlag() {
            builder = builder.header("Cookie", "getchu_adalt_flag=getchu.com");
            return this;
        }


        private RequestBuilder webClientParam() {
            builder = builder
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "ja,en-US;q=0.7,en;q=0.3")
                    .header("Cache-Control", "max-age=0")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
            ;

            return this;
        }


        public HttpRequest build() {

            webClientParam();
            return builder.GET().build();
        }


    }

}
