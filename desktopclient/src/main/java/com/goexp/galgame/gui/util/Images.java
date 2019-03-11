package com.goexp.galgame.gui.util;

import com.goexp.galgame.Config;
import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.gui.model.Game;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class Images {

    public static class GameImage {

        public static Image tiny(Game game) {

            return Util.getImage(new CacheKey(game.id + "/game_t", game.smallImg));
        }

        public static Image small(final Game game) {
            var url = GetchuURL.getImgSmallFromId(game.id);

            return Util.getImage(new CacheKey(game.id + "/game_s", url));
        }

        public static class Simple {

            public static Image small(int gameId, int index, String src) {
                var url = GetchuURL.getSimpleImgSmallFromSrc(src);

                return Util.getImage(new CacheKey(gameId + "/simple_s_" + index, url));
            }

            public static Image large(int gameId, int index, String src) {
                var url = GetchuURL.getSimpleImgBigFromSrc(src);

                return Util.getImage(new CacheKey(gameId + "/simple_l_" + index, url));
            }
        }

        public static class GameChar {

            public static Image small(int gameId, int index, String src) {
                var url = GetchuURL.getUrlFromSrc(src);

                return Util.getImage(new CacheKey(gameId + "/char_s_" + index, url));
            }
        }

    }


    private static class Util {
        private static final Logger logger = LoggerFactory.getLogger(Util.class);

        private static Image getImage(CacheKey cacheKey) {
            Objects.requireNonNull(cacheKey);
            Objects.requireNonNull(cacheKey.getDiskCacheKey());
            Objects.requireNonNull(cacheKey.getMemCacheKey());

            logger.debug("LocalKey={},memCacheKey={}", cacheKey.getDiskCacheKey(), cacheKey.getMemCacheKey());

            var imageCache = AppCache.imageCache;

            //try heat cache
            return Optional.ofNullable(imageCache.get(cacheKey.getMemCacheKey()))
                    //not heat cache
                    .or(() -> {
                        final var localPath = Config.IMG_PATH.resolve(cacheKey.getDiskCacheKey() + ".jpg");

                        logger.debug("localPath={}", localPath);

                        Consumer<Image> callback = image1 -> {

                            try {
                                Files.createDirectories(localPath.getParent());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            saveImage(image1, localPath);
                        };


                        //heat disk cache or load from remote
                        var image = Files.exists(localPath) ? fromDisk(localPath) : loadRemote(cacheKey.getMemCacheKey(), callback);

                        //memCacheKey as cache key
                        imageCache.put(cacheKey.getMemCacheKey(), image);
                        return Optional.ofNullable(image);
                    }).get();
        }

        private static void saveImage(Image image, Path path) {

            if (image == null)
                return;

            var bufferImage = SwingFXUtils.fromFXImage(image, null);

            if (bufferImage == null)
                return;

            try {
                ImageIO.write(bufferImage, "jpg", path.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private static Image fromDisk(Path path) {

            logger.debug("Local:{}", path);
            return new Image("file:" + path.toString());
        }

        private static Image loadRemote(final String url, final Consumer<Image> callback) {

            logger.debug("Remote:{}", url);

            var imageCache = AppCache.imageCache;

            var image = new Image(url, true);
            image.progressProperty().addListener((o, old, newValue) ->
                    Optional.ofNullable(callback)
                            .ifPresent(b -> {
                                if (newValue != null && newValue.doubleValue() == 1) {
                                    b.accept(image);
                                }
                            }));
            image.exceptionProperty().addListener((o, old, newValue) -> {
                if (newValue != null) {
                    newValue.printStackTrace();

                    if (!(newValue instanceof FileNotFoundException))
                        imageCache.remove(url);
                }

            });
            return image;
        }

        private static byte[] getImageBytes(Image image) {

            if (image == null)
                return null;

            var bufferImage = SwingFXUtils.fromFXImage(image, null);

            if (bufferImage == null)
                return null;

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(bufferImage, "jpg", baos);

                return baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class Local {

        public static Image getLocal(String name) {
            var imageCache = AppCache.imageCache;

            return getLocalImage(name, imageCache);
        }

        private static Image getLocalImage(String name, ImageCache imageCache) {
            var img = imageCache.get(name);
            if (img != null) {
                return img;
            } else {
                var image = new Image(Images.class.getResource(name).toExternalForm());

                imageCache.put(name, image);
                return image;
            }
        }
    }

    private static class CacheKey {
        private final String diskCacheKey;
        private final String memCacheKey;

        CacheKey(String diskCacheKey, String memCacheKey) {
            this.diskCacheKey = diskCacheKey;
            this.memCacheKey = memCacheKey;
        }

        String getDiskCacheKey() {
            return diskCacheKey;
        }

        String getMemCacheKey() {
            return memCacheKey;
        }
    }
}
