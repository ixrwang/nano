package name.ixr.nano.common.spi;

import name.ixr.nano.common.context.ContentType;
import name.ixr.nano.common.context.HttpRequest;
import name.ixr.nano.common.context.PipelineContext;
import name.ixr.nano.common.context.RequestContext;
import name.ixr.nano.common.context.SpringContext;
import name.ixr.nano.common.pipeline.PipelineValue;
import name.ixr.nano.common.utils.AntPathMatcherUtils;
import org.springframework.stereotype.Component;

import javax.swing.*;

import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE;

/**
 * RequestDispatcherSPI : TODO: yuuji
 * yuuji 10:18 AM 11/21/13
 */
@Component
public class RequestDispatcherSPI {

    public void invoke() {
        PipelineContext pipelineContext = new PipelineContext();
        Map<String, PipelineValue> pipelines = SpringContext.getContext().getBeansOfType(PipelineValue.class);
        for (PipelineValue pipelineValue : pipelines.values()) {
            PipelineValue.Result result = pipelineValue.invoke(pipelineContext);
            if (result == PipelineValue.Result.BREAK) {
                break;
            } else if (result == PipelineValue.Result.RETURN) {
                return;
            }
        }
        Map<String, UrlRoutingSPI> routes = SpringContext.getContext().getBeansOfType(UrlRoutingSPI.class);
        for (UrlRoutingSPI route : routes.values()) {
            if (AntPathMatcherUtils.match(route.route(), RequestContext.request().getUri())) {
                try {
                    route.invoke();
                } catch (Exception | Error ex) {
                    RequestContext.response().mark(INTERNAL_SERVER_ERROR);
                    RequestContext.response().content().writeBytes(ex.getMessage().getBytes());
                }
                return;
            }
        }
        RequestContext.response().mark(NOT_FOUND, null);
    }
}
