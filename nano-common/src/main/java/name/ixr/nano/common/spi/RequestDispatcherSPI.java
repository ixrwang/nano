package name.ixr.nano.common.spi;

import name.ixr.nano.common.context.RequestContext;
import name.ixr.nano.common.context.HttpRequest;
import name.ixr.nano.common.context.HttpResponse;

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
