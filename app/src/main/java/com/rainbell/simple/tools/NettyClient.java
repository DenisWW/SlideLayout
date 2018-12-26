package com.rainbell.simple.tools;

import java.util.concurrent.ArrayBlockingQueue;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyClient {

    private Bootstrap bootstrap = null;
    private ArrayBlockingQueue<String> sendQueue = new ArrayBlockingQueue<String>(5000);
    private boolean flag = true;

    public static NettyClient getInstance() {
        return InstanceEnum.INSTANCE_ENUM.getIntance();
    }

    private NettyClient() {
        init();
    }

    private void init() {
        bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                //心跳包的添加
//                pipeline.addLast("idleStateHandler", new IdleStateHandler(60, 60, 0));
//对消息格式进行验证（MessageDecoder为自定义的解析验证类因协议规定而定）
//                pipeline.addLast("messageDecoder", new MessageDecoder());
                pipeline.addLast("messageDecoder", new StringDecoder());
//                pipeline.addLast("clientHandler", new NettyClientHandler(nettyClient));
                pipeline.addLast("clientHandler", new SimpleChatClientHandler());
            }
        });
        bootstrap.connect();
        startSendThread();
    }

    private void startSendThread() {
        sendQueue.clear();
//        sendFlag = true;
//        sendThread.start();
    }

    private enum InstanceEnum {
        INSTANCE_ENUM;
        private NettyClient mNettyClient;

        InstanceEnum() {
            mNettyClient = new NettyClient();
        }

        public NettyClient getIntance() {
            return mNettyClient;
        }
    }
}
