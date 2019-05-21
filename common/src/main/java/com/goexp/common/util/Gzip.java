package com.goexp.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class Gzip {
    public static byte[] unGzip(byte[] compressedBytes) {
        try (var s = new GZIPInputStream(new ByteArrayInputStream(compressedBytes))) {
            return s.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decode(byte[] compressedBytes, Charset charset) {

        Objects.requireNonNull(compressedBytes);
        Objects.requireNonNull(charset);

        byte[] bytes = unGzip(compressedBytes);
        Objects.requireNonNull(bytes, "Unzip error");

        return charset.decode(ByteBuffer.wrap(bytes)).toString();
    }
}
