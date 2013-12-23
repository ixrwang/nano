package name.ixr.nano.common.context;

import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.HttpHeaders;
import name.ixr.nano.common.utils.AntPathMatcherUtils;
import name.ixr.nano.common.utils.I18nUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
    private String ref;
    private Locale locale;
    private Map<String, Cookie> cookies = new HashMap<>();

    public Cookie getCookie(String name) {
        return cookies.get(name);
    }

    public HttpRequest(io.netty.handler.codec.http.HttpRequest source) {
        String language = source.headers().get(HttpHeaders.Names.ACCEPT_LANGUAGE);
        this.locale = I18nUtils.toLocale(language);
        this.source = source;
        this.host = source.headers().get(HttpHeaders.Names.HOST);
        this.ref = source.headers().get(HttpHeaders.Names.REFERER);
        this.ref = StringUtils.substringAfter(this.ref, this.host);
        setUri(source.getUri());
        String cookies = source.headers().get(HttpHeaders.Names.COOKIE);
        if (StringUtils.isNotBlank(cookies)) {
            Set<Cookie> cookieSet = CookieDecoder.decode(cookies);
            for (Cookie cookie : cookieSet) {
                this.cookies.put(cookie.getName(), cookie);
            }
        }
    }

    public HttpRequest(String url) {
        setUri(url);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setUri(String uri) {
        this.uri = url(uri);
    }

    public io.netty.handler.codec.http.HttpRequest getSource() {
        return source;
    }

    public String getHost() {
        return host;
    }

    public String getRef() {
        return url(ref);
    }

    private String url(String url) {
        if ("/".equals(url) || AntPathMatcherUtils.match("/index.*", url)) {
            return "/pages/index.htm";
        } else {
            return url;
        }
    }

    public String getUri() {
        return uri;
    }

}
