package com.alibaba.spi;

import com.alibaba.base.RequestContext;
import com.alibaba.utils.HttpRequest;
import com.alibaba.utils.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * StaticSPI : TODO: yuuji
 * yuuji 9:41 AM 11/26/13
 */
public class StaticSPI {

    public HttpResponse invoke() {
        HttpRequest req = RequestContext.request();
        if(req.getReferer() != null) {
            HttpRequest referer = new HttpRequest(req.getReferer());
            if ("page".equals(req.getUriBefore(1)) && "pages".equals(referer.getUri(0))) {
                return new PageStaticSPI().invoke();
            }
        }
        return new HttpResponse(HttpResponseStatus.NOT_FOUND, null);
    }
}