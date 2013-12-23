package name.ixr.nano.common.context;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.ServerCookieEncoder;

import java.util.Set;
import java.util.TreeSet;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * HttpResponse : TODO: yuuji
 * yuuji 3:51 PM 11/20/13
 */
public class HttpResponse extends DefaultFullHttpResponse {

    private HttpHeaders headers = new DefaultHttpHeaders();

    private boolean keepAlive = false;

    private ContentType contentType = ContentType.HTML;


    private Set<Cookie> cookies = new TreeSet<>();

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public HttpResponse() {
        this(OK);
    }

    public HttpResponse(HttpResponseStatus status) {
        this(HttpVersion.HTTP_1_1, status);
    }

    public HttpResponse(HttpResponseStatus status, ContentType contentType) {
        this(HttpVersion.HTTP_1_1, status);
        this.contentType = contentType;
    }

    public HttpResponse(HttpVersion version, HttpResponseStatus status) {
        super(version, status);
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void writeContext(ChannelHandlerContext ctx) {
        writeContext(ctx, contentType);
    }
    public void mark(HttpResponseStatus status) {
        super.setStatus(status);
    }

    public void mark(HttpResponseStatus status, ContentType contentType) {
        super.setStatus(status);
        this.contentType = contentType;
    }

    public void writeContext(ChannelHandlerContext ctx, ContentType contentType) {
        writeContext(ctx, contentType, keepAlive);
    }

    public void writeContext(ChannelHandlerContext ctx, boolean keepAlive) {
        writeContext(ctx, contentType, keepAlive);
    }

    public void writeContext(ChannelHandlerContext ctx, ContentType contentType, boolean keepAlive) {
        headers().set("Server", "ixr");
        headers().set("Email", "mail@ixr.name");
        if (contentType != null) {
            headers().set(CONTENT_TYPE, contentType.value());
        }
        int length = content().readableBytes();
        if (length > 0) {
            headers().set(CONTENT_LENGTH, length);
        }
        if (keepAlive) {
            headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            addHeaders();
            ctx.write(this);
        } else {
            addHeaders();
            ctx.write(this).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void addHeaders() {
        headers().add(headers);
        for (Cookie cookie : cookies) {
            headers().add(SET_COOKIE, ServerCookieEncoder.encode(cookie));
        }
    }
}