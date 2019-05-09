package xin.tomdonkey.chat.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.ReferenceCountUtil;
import xin.tomdonkey.chat.netty.protocol.Msp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author guolinyuan
 */
public class ServerChannelHandler extends ChannelInboundHandlerAdapter
{
    private ChannelGroup channels;

    public ServerChannelHandler(ChannelGroup channels)
    {
        this.channels = channels;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        try
        {
            Msp massage = (Msp)msg;
            System.out.println("收到消息："+massage);

            //如果这个消息是一个初始化消息，及一个·我在线上·的操作，就注册到channels中
            if (massage.isInit())
            {
                channels.add(ctx.channel());
            }
            else if (massage.isBroadcast())
            {
                channels.writeAndFlush(massage);
            }

            ctx.writeAndFlush(Msp.OK_MSP);
        }
        finally
        {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        channels.remove(ctx.channel());
        cause.printStackTrace();
        ctx.close();
    }
}
