package com.goexp.common.util;

import org.w3c.dom.Document;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Objects;

public class URLUtil {

    public static String doc2String(final Document doc) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        TransformerFactory.newInstance()
                .newTransformer()
                .transform(new DOMSource(doc), new StreamResult(bos));


        return bos.toString();
    }


    public static String favUrl(final String url) {
        Objects.requireNonNull(url);

        var uri = URI.create(url);
        return String.format("%s://%s/%s", uri.getScheme(), uri.getHost(), "favicon.ico");
    }


}
