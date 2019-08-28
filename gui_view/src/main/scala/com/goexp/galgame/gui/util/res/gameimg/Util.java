package com.goexp.galgame.gui.util.res.gameimg;

import com.goexp.galgame.gui.Config;
import com.goexp.galgame.gui.model.Game;
import com.goexp.galgame.gui.util.cache.AppCache;
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

class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static Image getImage(Game game, CacheKey cacheKey) {
        Objects.requireNonNull(cacheKey);
        Objects.requireNonNull(cacheKey.getDiskCacheKey());
        Objects.requireNonNull(cacheKey.getMemCacheKey());

        logger.debug("LocalKey={},memCacheKey={}", cacheKey.getDiskCacheKey(), cacheKey.getMemCacheKey());

        final var imageCache = AppCache.imageMemCache();

        //try heat cache
        return imageCache.get(cacheKey.getMemCacheKey())
                //not heat cache
                .orElseGet(() -> {
                    final var localPath = Config.IMG_PATH().resolve(cacheKey.getDiskCacheKey() + ".jpg");

                    logger.debug("localPath={}", localPath);


                    //heat disk cache or load from remote
                    final var image = Files.exists(localPath) ?
                            fromDisk(localPath) :
                            loadRemote(cacheKey.getMemCacheKey(), image1 -> {


                                // only save not blocked
                                if (game.isOkState()) {
                                    try {
                                        Files.createDirectories(localPath.getParent());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    saveImage(image1, localPath);
                                }

                            });

                    //memCacheKey as cache key
                    imageCache.put(cacheKey.getMemCacheKey(), image);
                    return image;
                });
    }

    public static void preLoadRemoteImage(CacheKey cacheKey) {
        Objects.requireNonNull(cacheKey);
        Objects.requireNonNull(cacheKey.getDiskCacheKey());
        Objects.requireNonNull(cacheKey.getMemCacheKey());

        logger.debug("LocalKey={},memCacheKey={}", cacheKey.getDiskCacheKey(), cacheKey.getMemCacheKey());

        final var imageCache = AppCache.imageMemCache();

        //try heat cache

        var cachedImage = imageCache.get(cacheKey.getMemCacheKey());

        if (cachedImage.isEmpty()) {
            //not heat cache
            final var localPath = Config.IMG_PATH().resolve(cacheKey.getDiskCacheKey() + ".jpg");

            logger.debug("localPath={}", localPath);


            //heat disk cache or load from remote

            if (!Files.exists(localPath))
                loadRemote(cacheKey.getMemCacheKey(), image1 -> {

                    try {
                        Files.createDirectories(localPath.getParent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    saveImage(image1, localPath);

                    //memCacheKey as cache key
                    imageCache.put(cacheKey.getMemCacheKey(), image1);
                });
        }

    }

    public static void saveImage(Image image, Path path) {
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

    public static Image fromDisk(Path path) {
        Objects.requireNonNull(path);

        logger.debug("Local:{}", path);
        return new Image("file:" + path.toString());
    }


    public static Image loadRemote(String url, Consumer<Image> callback) {
        Objects.requireNonNull(url);

        logger.debug("Remote:{}", url);

        final var image = new Image(url, true);

        if (callback != null)
            image.progressProperty().addListener((o, old, newValue) -> {
                if (newValue != null && newValue.doubleValue() == 1) {
                    callback.accept(image);
                }
            });

        image.exceptionProperty().addListener((o, old, e) -> {
            if (e != null) {

                if (!(e instanceof FileNotFoundException))
                    AppCache.imageMemCache().remove(url);
                else {
                    logger.error(e.getMessage());
                }
            }

        });
        return image;
    }

    public static byte[] getImageBytes(Image image) {
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
