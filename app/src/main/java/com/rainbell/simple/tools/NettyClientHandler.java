package com.rainbell.simple.tools;

import android.util.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private NettyClient nettyClient = null;

    public NettyClientHandler(NettyClient nettyClient) {
        super();
        this.nettyClient = nettyClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String strMsg = (String) msg;
        Log.d("回复的消息：", strMsg);
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.d("ClientHandler", "-------重连回调------");
//        nettyClient.setConnectState(NettyClient.DISCONNECTION);
//        nettyClient.connect();
        super.channelInactive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Log.d("NettyClientHandl", "registered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.d("NettyClientHandler", "=====连接成功回调=====");
        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.d("NettyClientHandl", "网络异常!");
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }


}
