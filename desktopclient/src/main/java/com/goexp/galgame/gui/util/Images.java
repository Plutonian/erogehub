package com.goexp.galgame.gui.util;

import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.gui.Config;
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
import java.util.function.Consumer;

public class Images {

    public static class GameImage {

        public static Image tiny(Game game) {
            return Util.getImage(new CacheKey(game.id + "/game_t", game.smallImg));
        }

        public static Image small(Game game) {
            final var url = GetchuURL.Game.SmallImg(game.id);

            return Util.getImage(new CacheKey(game.id + "/game_s", url));
        }

        public static Image large(Game game) {
            final var url = GetchuURL.Game.LargeImg(game.id);

            return Util.getImage(new CacheKey(game.id + "/game_l", url));
        }

        public static class Simple {

            public static Image small(final int gameId, final int index, final String src) {
                final var url = GetchuURL.Game.smallSimpleImg(src);

                return Util.getImage(new CacheKey(gameId + "/simple_s_" + index, url));
            }

            public static Image large(final int gameId, final int index, final String src) {
                final var url = GetchuURL.Game.largeSimpleImg(src);

                return Util.getImage(new CacheKey(gameId + "/simple_l_" + index, url));
            }
        }

        public static class GameChar {

            public static Image small(final int gameId, final int index, final String src) {
                final var url = GetchuURL.Game.getUrlFromSrc(src);

                return Util.getImage(new CacheKey(gameId + "/char_s_" + index, url));
            }
        }

    }

    private static class Util {
        private static final Logger logger = LoggerFactory.getLogger(Util.class);

        private static Image getImage(final CacheKey cacheKey) {
            Objects.requireNonNull(cacheKey);
            Objects.requireNonNull(cacheKey.getDiskCacheKey());
            Objects.requireNonNull(cacheKey.getMemCacheKey());

            logger.debug("LocalKey={},memCacheKey={}", cacheKey.getDiskCacheKey(), cacheKey.getMemCacheKey());

            final var imageCache = AppCache.imageCache;

            //try heat cache
            return imageCache.get(cacheKey.getMemCacheKey())
                    //not heat cache
                    .orElseGet(() -> {
                        final var localPath = Config.IMG_PATH.resolve(cacheKey.getDiskCacheKey() + ".jpg");

                        logger.debug("localPath={}", localPath);


                        //heat disk cache or load from remote
                        final var image = Files.exists(localPath) ?
                                fromDisk(localPath) :
                                loadRemote(cacheKey.getMemCacheKey(), image1 -> {

                                    try {
                                        Files.createDirectories(localPath.getParent());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    saveImage(image1, localPath);
                                });

                        //memCacheKey as cache key
                        imageCache.put(cacheKey.getMemCacheKey(), image);
                        return image;
                    });
        }

        private static void saveImage(final Image image, final Path path) {
            Objects.requireNonNull(image);
            Objects.requireNonNull(path);

            final var bufferImage = SwingFXUtils.fromFXImage(image, null);
            if (bufferImage == null)
                return;

            try {
                ImageIO.write(bufferImage, "jpg", path.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private static Image fromDisk(final Path path) {
            Objects.requireNonNull(path);

            logger.debug("Local:{}", path);
            return new Image("file:" + path.toString());
        }


        private static Image loadRemote(final String url, final Consumer<Image> callback) {
            Objects.requireNonNull(url);

            logger.debug("Remote:{}", url);

            final var image = new Image(url, true);

            if (callback != null)
                image.progressProperty().addListener((o, old, newValue) -> {
                    if (newValue != null && newValue.doubleValue() == 1) {
                        callback.accept(image);
                    }
                });

            image.exceptionProperty().addListener((o, old, newValue) -> {
                if (newValue != null) {
                    newValue.printStackTrace();

                    if (!(newValue instanceof FileNotFoundException))
                        AppCache.imageCache.remove(url);
                }

            });
            return image;
        }

        private static byte[] getImageBytes(final Image image) {
            Objects.requireNonNull(image);

            final var bufferImage = SwingFXUtils.fromFXImage(image, null);
            if (bufferImage == null)
                return null;

            try (final var stream = new ByteArrayOutputStream()) {
                ImageIO.write(bufferImage, "jpg", stream);

                return stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class Local {

        public static Image getLocal(final String name) {
            return getLocal(name, AppCache.imageCache);
        }

        private static Image getLocal(final String name, final ImageCache imageCache) {
            return imageCache.get(name).orElseGet(() -> {
                final var image = new Image(Images.class.getResource(name).toExternalForm());

                imageCache.put(name, image);
                return image;

            });
        }
    }

    private static class CacheKey {
        private final String diskCacheKey;
        private final String memCacheKey;

        private CacheKey(final String diskCacheKey, final String memCacheKey) {
            this.diskCacheKey = diskCacheKey;
            this.memCacheKey = memCacheKey;
        }

        private String getDiskCacheKey() {
            return diskCacheKey;
        }

        private String getMemCacheKey() {
            return memCacheKey;
        }
    }
}
