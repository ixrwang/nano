package com.alibaba.base;

import com.alibaba.spi.RequestDispatcherSPI;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

/**
 * HttpServerHandler : TODO: yuuji
 * yuuji 3:23 PM 11/20/13
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
//            for (String name : req.headers().names()) {
//                System.out.println(name + " : " + req.headers().get(name));
//            }
            new RequestDispatcherSPI().invoke(new com.alibaba.utils.HttpRequest(req)).writeContext(ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
