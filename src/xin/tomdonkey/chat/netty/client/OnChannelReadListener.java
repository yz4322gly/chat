package xin.tomdonkey.chat.netty.client;

import io.netty.channel.ChannelHandlerContext;

public interface OnChannelReadListener
{
    void channelRead(ChannelHandlerContext ctx, Object msg);
}
