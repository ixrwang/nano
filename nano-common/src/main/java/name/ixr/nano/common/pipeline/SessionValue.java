package name.ixr.nano.common.pipeline;

import io.netty.handler.codec.http.Cookie;
import name.ixr.nano.common.context.HttpRequest;
import name.ixr.nano.common.context.PipelineContext;
import name.ixr.nano.common.context.RequestContext;
import org.springframework.stereotype.Component;

/**
 * init session
 * Created by IXR on 13-12-23.
 */
@Component
public class SessionValue extends PipelineValue {

    public static final String SESSION_KEY = "YSID";

    @Override
    public Result invoke(PipelineContext pipelineContext) {
        HttpRequest request = RequestContext.request();
        Cookie sessionCookie = request.getCookie(SESSION_KEY);
        if (sessionCookie != null) {
            RequestContext.context().put(SESSION_KEY, sessionCookie.getValue());
        }
        return Result.NEXT;
    }
}
