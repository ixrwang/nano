package com.alibaba;

import com.alibaba.base.HttpServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * HttpServer : TODO: yuuji
 * yuuji 11:25 AM 11/20/13
 */
public class HttpServer {

    private static final int SO_BACKLOG = 1024 * 8;
    private int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .option(ChannelOption.SO_BACKLOG, SO_BACKLOG)
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpServerInitializer());
        // Bind and start to accept incoming connections.
        ChannelFuture future = bootstrap.bind(port).sync();
        // Wait until the server socket is closed.
        // In this example, this does not happen, but you can do that to gracefully
        // shut down your server.
        System.out.println("HttpServer Port " + port + " Start...");
        future.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new HttpServer(port).run();
    }
}
