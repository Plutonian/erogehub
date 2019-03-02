package com.goexp.galgame.data.task.client;

import com.goexp.common.util.WebUtil;
import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.data.db.importor.Config;
import com.goexp.galgame.data.model.Game;
import com.goexp.galgame.data.parser.game.DetailPageParser;
import com.goexp.galgame.data.parser.ParseException;
import com.goexp.galgame.data.parser.game.ListPageParser;
import com.goexp.galgame.data.task.client.error.AbstractTaskErrorProcessor;
import com.goexp.galgame.data.task.client.error.BrandTaskErrorProcessor;
import com.goexp.galgame.data.task.client.error.GameTaskErrorProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
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

    final private static Logger logger = LoggerFactory.getLogger(GetChu.class);
    public final static Charset DEFAULT_CHARSET = Charset.forName("EUC-JP");

    public static String getHtml(HttpRequest request) {
        try {
            var bytes = WebUtil.httpClient.send(request, ofByteArray()).body();
            return WebUtil.decodeGzip(bytes, DEFAULT_CHARSET);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class GameService {

        private final static AbstractTaskErrorProcessor error = new GameTaskErrorProcessor();

        private static byte[] getBytes(int gameId) {
            try {

                var request = GetchuURL.RequestBuilder
                        .create(GetchuURL.getFromId(gameId))
                        .adaltFlag()
                        .build();

                return WebUtil.httpClient.send(request, ofByteArray()).body();

            } catch (IOException | InterruptedException e) {

                error.recordError(String.valueOf(gameId));
                e.printStackTrace();
                logger.error("<error> GameId:{}", gameId);
            } finally {
                logger.info("Download: {}", gameId);
            }
            return null;
        }

        public static void download(int gameId) throws IOException, InterruptedException {
            try {
                final var localPath = Config.GAME_CACHE_ROOT.resolve(String.format("%d.bytes", gameId));

                final var tempPath = Path.of(localPath.toString() + "_");

                WebUtil.httpClient.send(GetchuURL.RequestBuilder
                        .create(GetchuURL.getFromId(gameId))
                        .adaltFlag()
                        .build(), ofFile(tempPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE));


                Files.move(tempPath, localPath, StandardCopyOption.REPLACE_EXISTING);

                logger.debug("Download:Game: {}", gameId);

            } catch (IOException | InterruptedException e) {
                error.recordError(String.valueOf(gameId));
                logger.error("<Game> {} {}", gameId, e.getMessage());
                throw e;

            }
        }

        public static String downloadHTML(int gameId) {
            return WebUtil.decodeGzip(getBytes(gameId), DEFAULT_CHARSET);
        }


        public static Game getFrom(int gameId, byte[] bytes) throws ParseException {
            return getFrom(gameId, WebUtil.decodeGzip(bytes, DEFAULT_CHARSET));
        }

        public static Game getFrom(int gameId) throws ParseException {
            return getFrom(gameId, downloadHTML(gameId));
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

        private final static AbstractTaskErrorProcessor error = new BrandTaskErrorProcessor();

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
                error.recordError(String.valueOf(brandId));
                logger.error("<Brand> {} Mes:{}", brandId, e.getMessage());
                throw e;
            }
        }

        public static byte[] downloadBytesGameListFrom(int brandid) {
            try {

                logger.debug("Download: {}", brandid);

                return (byte[]) ((HttpResponse) WebUtil.httpClient.send(GetchuURL.RequestBuilder
                        .create(GetchuURL.getListByBrand(brandid))
                        .adaltFlag()
                        .build(), ofByteArray())).body();
            } catch (IOException | InterruptedException e) {

                error.recordError(String.valueOf(brandid));
                e.printStackTrace();
                logger.error("<error> Brand Id:{}", brandid);
            }
            return null;
        }

    }

    static class T {

        public static void main(String[] args) throws IOException {
//            var bytes = Files.readAllBytes(Path.of("e:\\2\\1000302.html"));
//            try {
//                var g = GameService.getFrom(1000302, GetchuURL.DEFAULT_CHARSET.decode(ByteBuffer.wrap(bytes)).toString());

//                g.gameCharacterList.forEach(gameCharacter -> {

            try {
                var byt = DEFAULT_CHARSET.encode("彧");

                System.out.println(byt.limit());

                var buffer = new byte[byt.limit()];

                byt.get(buffer);


                Files.write(Path.of("e:\\2\\cgar.txt"), buffer, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
//                });
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

        }
    }

    static class T1 {

        public static void main(String[] args) throws IOException {
            var bytes = Files.readAllBytes(Path.of("e:\\2\\cgar.txt"));
//            try {
            var g = DEFAULT_CHARSET.decode(ByteBuffer.wrap(bytes)).toString();
            System.out.println(g);

//                g.gameCharacterList.forEach(gameCharacter -> {
//
//                    try {
//                        var byt = GetchuURL.DEFAULT_CHARSET.encode("彧" + "\n");
//
//                        System.out.println(byt.limit());
//
//                        var buffer = new byte[byt.limit()];
//
//                        byt.get(buffer);
//
//
//                        Files.write(Path.of("e:\\2\\cgar.txt"), buffer, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//            catch (ParseException e) {
//                e.printStackTrace();
//            }

        }
    }

}
