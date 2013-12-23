package name.ixr.nano.common.context;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

/**
 * RequestContext : TODO: yuuji
 * yuuji 3:40 PM 12/6/13
 */
public class RequestContext {

    private static final ThreadLocal<HttpRequest> requestThreadLocal = new ThreadLocal<HttpRequest>();
    private static final ThreadLocal<HttpResponse> responseThreadLocal = new ThreadLocal<HttpResponse>();
    private static final ThreadLocal<Context> contextThreadLocal = new ThreadLocal<Context>();

    public static HttpRequest request() {
        return requestThreadLocal.get();
    }

    public static HttpResponse response() {
        return responseThreadLocal.get();
    }

    public static Context context() {
        return contextThreadLocal.get();
    }

    public static void init(HttpRequest request) {
        requestThreadLocal.set(request);
        responseThreadLocal.set(new HttpResponse());
        contextThreadLocal.set(new VelocityContext());
    }
}
