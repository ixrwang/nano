package name.ixr.nano.common.context;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

/**
 * HttpResponse : TODO: yuuji
 * yuuji 3:51 PM 11/20/13
 */
public class HttpResponse extends DefaultFullHttpResponse {

    private HttpHeaders headers = new DefaultHttpHeaders();

    private boolean keepAlive = false;

    private ContentType contentType = ContentType.HTML;

    public HttpHeaders getHeaders() {
        return headers;
    }

    public HttpResponse() {
        this(OK);
    }

    public HttpResponse(HttpResponseStatus status) {
        this(HttpVersion.HTTP_1_1, status);
    }

    public HttpResponse(HttpResponseStatus status,ContentType contentType) {
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
            headers().add(headers);
            ctx.write(this);
        } else {
            headers().add(headers);
            ctx.write(this).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
