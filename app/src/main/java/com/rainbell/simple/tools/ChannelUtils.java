package com.rainbell.simple.tools;

import android.util.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChannelUtils extends SimpleChannelInboundHandler<String> {
    //数据回调
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Log.e("channelRead0", "-----------" + s);
    }
}
