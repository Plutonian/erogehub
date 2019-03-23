package com.goexp.galgame.data.task.client;

import com.goexp.common.util.WebUtil;
import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.data.Config;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.parser.ParseException;
import com.goexp.galgame.data.parser.game.DetailPageParser;
import com.goexp.galgame.data.parser.game.ListPageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;

import static java.net.http.HttpResponse.BodyHandlers.ofByteArray;
import static java.net.http.HttpResponse.BodyHandlers.ofFile;

public class GetChu {

    public final static Charset DEFAULT_CHARSET = Charset.forName("EUC-JP");
    final private static Logger logger = LoggerFactory.getLogger(GetChu.class);

    public static String getHtml(HttpRequest request) {
        try {
            var bytes = WebUtil.httpClient.send(request, ofByteArray()).body();
            return WebUtil.decodeGzip(bytes, DEFAULT_CHARSET);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class GameService {

        public static void download(int gameId) throws IOException, InterruptedException {
            final var localPath = Config.GAME_CACHE_ROOT.resolve(String.format("%d.bytes", gameId));

            final var tempPath = Path.of(localPath.toString() + "_");
            logger.debug("Download:Game: {}", gameId);

            WebUtil.httpClient.send(GetchuURL.RequestBuilder
                            .create(GetchuURL.getFromId(gameId))
                            .adaltFlag()
                            .build()
                    , ofFile(tempPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE));

            Files.move(tempPath, localPath, StandardCopyOption.REPLACE_EXISTING);

        }


        public static Game getFrom(int gameId, String html) throws ParseException {
            var parser = new DetailPageParser();

            try {
                return parser.parse(gameId, html);
            } catch (Exception e) {
                throw new ParseException();
            }
        }

    }

    public static class BrandService {

        public static List<Game> gamesFrom(int brandId) {

            try {

                var bytes = (byte[]) ((HttpResponse) WebUtil.httpClient.send(GetchuURL.RequestBuilder
                        .create(GetchuURL.getListByBrand(brandId))
                        .adaltFlag()
                        .build(), ofByteArray())).body();
                var html = WebUtil.decodeGzip(bytes, DEFAULT_CHARSET);

                return new ListPageParser().parse(html);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                logger.error("<error> BrandId:{}", brandId);
            }

            return null;
        }

        public static List<Game> gamesFrom(LocalDate from, LocalDate to) {

            try {

                var bytes = (byte[]) ((HttpResponse) WebUtil.httpClient.send(GetchuURL.RequestBuilder
                        .create(GetchuURL.gameListFromDateRange(from, to))
                        .adaltFlag()
                        .build(), ofByteArray())).body();
                var html = WebUtil.decodeGzip(bytes, DEFAULT_CHARSET);

                return new ListPageParser().parse(html);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                logger.error("<error> From:{} to:{}", from, to);
            }

            return null;
        }

        public static List<Game> gamesFrom(byte[] bytes) {

            var html = WebUtil.decodeGzip(bytes, DEFAULT_CHARSET);

            return new ListPageParser().parse(html);
        }

        public static void download(final int brandId) throws IOException, InterruptedException {
            try {

                final var localPath = Config.BRAND_CACHE_ROOT.resolve(String.format("%d.bytes", brandId));
                final var tempPath = Path.of(localPath.toString() + "_");

                var request = GetchuURL.RequestBuilder.create(GetchuURL.getListByBrand(brandId))
                        .adaltFlag()
                        .build();

                WebUtil.httpClient.send(request, ofFile(tempPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE));


                Files.move(tempPath, localPath, StandardCopyOption.REPLACE_EXISTING);

                logger.debug("Download: Brand:{}", brandId);

            } catch (IOException | InterruptedException e) {
                logger.error("<Brand> {} Mes:{}", brandId, e.getMessage());
                throw e;
            }
        }

    }

}
