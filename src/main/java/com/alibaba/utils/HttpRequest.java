package com.alibaba.utils;

import io.netty.handler.codec.http.HttpHeaders;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * HttpRequest : TODO: yuuji
 * yuuji 4:38 PM 11/28/13
 */
public class HttpRequest {

    private static final String HTML_SPLIT = ".";
    private static final String URI_SPLIT = "/";
    private static final String PARAM_BEGIN = "?";

    private io.netty.handler.codec.http.HttpRequest source;
    private String host;
    private String uri;
    private String[] uris;
    private String referer;

    public HttpRequest(io.netty.handler.codec.http.HttpRequest source) {
        this.source = source;
        this.host = source.headers().get(HttpHeaders.Names.HOST);
        this.referer = source.headers().get(HttpHeaders.Names.REFERER);
        this.referer = StringUtils.substringAfter(this.referer, this.host);
        setUri(source.getUri());
    }

    public void setUri(String uri) {
        this.uri = uri;
        uris = uri.split(URI_SPLIT);
        uris = ArrayUtils.removeElement(uris, StringUtils.EMPTY);
        if ("/".equals(getUri()) || "index".equals(getUriBefore(0))) {
            setUri("/pages/index.htm");
        }
    }

    public io.netty.handler.codec.http.HttpRequest getSource() {
        return source;
    }

    public String getHost() {
        return host;
    }

    public String getReferer() {
        return referer;
    }

    public String getUri() {
        return uri;
    }

    public String getUriSuffix(int idx) {
        String suffix = getUriAfter(idx);
        suffix = StringUtils.substringBefore(suffix, PARAM_BEGIN);
        return suffix;
    }

    public String getUriAfter(int idx) {
        return StringUtils.substringAfter(getUri(idx), HTML_SPLIT);
    }

    public String getUriBefore(int idx) {
        return StringUtils.substringBefore(getUri(idx), HTML_SPLIT);
    }

    public String getUri(int idx) {
        if (idx < uris.length) {
            return uris[idx];
        } else {
            return null;
        }

    }

    public String[] getUris() {
        return uris;
    }
}
