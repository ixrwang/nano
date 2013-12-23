package name.ixr.nano.common.spi;

import name.ixr.nano.common.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.rmi.Remote;
import java.util.Map;

/**
 * Created by IXR on 13-12-23.
 */
public abstract class UrlRoutingSPI {

    private static final String URL_PARAM_KEY = "URL_PARAM";

    public abstract void invoke() throws Exception;

    public abstract String route();

    public boolean match() {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(route(), RequestContext.request().getUri());
    }

    public String getUrlParam(String name) {
        AntPathMatcher matcher = new AntPathMatcher();
        if (RequestContext.context().containsKey(URL_PARAM_KEY)) {
            Map<String, String> params = (Map<String, String>) RequestContext.context().get(URL_PARAM_KEY);
            return params.get(name);
        } else {
            Map<String, String> params = matcher.extractUriTemplateVariables(route(), RequestContext.request().getUri());
            RequestContext.context().put(URL_PARAM_KEY, params);
            return params.get(name);
        }
    }
}
