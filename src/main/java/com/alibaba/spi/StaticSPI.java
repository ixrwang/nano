package com.alibaba.spi;

import com.alibaba.utils.HttpRequest;
import com.alibaba.utils.HttpResponse;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * StaticSPI : TODO: yuuji
 * yuuji 9:41 AM 11/26/13
 */
public class StaticSPI {

    public HttpResponse invoke(HttpRequest req) {
        if(req.getReferer() != null) {
            HttpRequest referer = new HttpRequest(req.getReferer());
            if ("apps".equals(req.getUriBefore(1)) && "pages".equals(referer.getUri(0))) {
                return new AppStaticSPI().invoke(req);
            }
        }
        return new HttpResponse(HttpResponseStatus.NOT_FOUND, null);
    }
}
