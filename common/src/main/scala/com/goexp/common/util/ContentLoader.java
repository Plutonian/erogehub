package com.goexp.common.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class ContentLoader {

    public static String loadFromFile(Path path, Charset charset) {
        try {
            var bytes = Files.readAllBytes(path);
            var html = charset.decode(ByteBuffer.wrap(bytes)).toString();

            return html;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
