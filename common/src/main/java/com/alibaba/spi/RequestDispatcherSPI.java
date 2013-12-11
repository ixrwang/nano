package com.alibaba.spi;

import com.alibaba.base.RequestContext;
import com.alibaba.utils.HttpRequest;
import com.alibaba.utils.HttpResponse;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;

/**
 * RequestDispatcherSPI : TODO: yuuji
 * yuuji 10:18 AM 11/21/13
 */
public class RequestDispatcherSPI {

    public HttpResponse invoke() {
        HttpRequest req = RequestContext.request();
        if ("/bad-request".equals(req.getUri())) {
            HttpResponse res = new HttpResponse(BAD_REQUEST, null);
            return res;
        }else if ("pages".equals(req.getUri(0))) {
            return new PageSPI().invoke();
        } else if ("static".equals(req.getUri(0))) {
            return new StaticSPI().invoke();
        } else {
            HttpResponse res = new HttpResponse(NOT_FOUND, null);
            return res;
        }
    }
}
