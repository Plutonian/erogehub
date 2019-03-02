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

        public static Image tiny(Game game, String url) {

            return Util.getImage(game.id + "\\game_t", url);
        }

        public static Image small(final Game game) {
            var url = GetchuURL.getImgSmallFromId(game.id);

            return Util.getImage(game.id + "\\game_s", url);
        }

        public static class Simple {

            public static Image small(int gameId, int index, String src) {
                var url = GetchuURL.getSimpleImgSmallFromSrc(src);

                return Util.getImage(gameId + "\\simple_s_" + index, url);
            }

            public static Image large(int gameId, int index, String src) {
                var url = GetchuURL.getSimpleImgBigFromSrc(src);

                return Util.getImage(gameId + "\\simple_l_" + index, url);
            }
        }

        public static class GameChar {

            public static Image small(int gameId, int index, String src) {
                var url = GetchuURL.getUrlFromSrc(src);

                return Util.getImage(gameId + "\\char_s_" + index, url);
            }
        }

    }


    private static class Util {
        private static final Logger logger = LoggerFactory.getLogger(Util.class);

        private static Image getImage(String localKey, String url) {
            Objects.requireNonNull(localKey);
            Objects.requireNonNull(url);

            logger.debug("LocalKey={},url={}", localKey, url);

            var imageCache = AppCache.imageCache;

            //heat cache
            var img = imageCache.get(url);
            if (img != null) {
                return img;
            } else {
                final var path = Config.IMG_PATH.resolve(Path.of(localKey + ".jpg"));

                logger.debug("path={}", path);

                Image image = null;

                if (Files.exists(path)) {
                    image = localImageLoader(path);
                } else {
                    image = remoteImageLoader(url, image1 -> {

                        try {
                            Files.createDirectories(path.getParent());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        saveImage2Path(image1, path);
                    });
                }

                imageCache.put(url, image);
                return image;
            }
        }

        private static void saveImage2Path(Image image, Path path) {

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

        private static Image localImageLoader(Path path) {

            logger.debug("Local:{}", path);
            return new Image("file:" + path.toString());
        }

        private static Image remoteImageLoader(final String url, final Consumer<Image> back) {

            logger.debug("Remote:{}", url);

            var imageCache = AppCache.imageCache;

            var image = new Image(url, true);
            image.progressProperty().addListener((o, old, newValue) -> Optional.ofNullable(back)
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
}
