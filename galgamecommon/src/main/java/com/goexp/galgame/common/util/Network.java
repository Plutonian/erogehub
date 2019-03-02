package com.goexp.galgame.common.util;


import com.goexp.galgame.common.Config;

public class Network {
    public static void initProxy() {
        if (Config.proxy) {

            // HTTP 代理，只能代理 HTTP 请求
            System.setProperty("http.proxyHost", "127.0.0.1");
            System.setProperty("http.proxyPort", "8087");

            // HTTPS 代理，只能代理 HTTPS 请求
            System.setProperty("https.proxyHost", "127.0.0.1");
            System.setProperty("https.proxyPort", "4003");

            // SOCKS 代理，支持 HTTP 和 HTTPS 请求
            // 注意：如果设置了 SOCKS 代理就不要设 HTTP/HTTPS 代理
            //        System.setProperty("socksProxyHost", "127.0.0.1");
            //        System.setProperty("socksProxyPort", "53758");

        }
    }
}
