package name.ixr.nano.common.utils;

import org.springframework.util.AntPathMatcher;

import java.util.Map;

/**
 * Created by IXR on 13-12-24.
 */
public class AntPathMatcherUtils {

    public static boolean match(String route, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(route, url);
    }

    public static String getUrlParam(String route, String url, String name) {
        AntPathMatcher matcher = new AntPathMatcher();
        Map<String, String> params = matcher.extractUriTemplateVariables(route, url);
        return params.get(name);
    }
}
