package xin.tomdonkey.chat.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import xin.tomdonkey.chat.netty.protocol.Msp;

import java.util.HashMap;

/**
 * @author guolinyuan
 */
public class ClientChannelHandler extends ChannelInboundHandlerAdapter
{
    OnChannelReadListener mListener;

    public void setListener(OnChannelReadListener mListener)
    {
        this.mListener = mListener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        HashMap map = new HashMap<>();
        map.put("init","true");
        map.put("fromId","1236");
        ctx.writeAndFlush(new Msp(map,""));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println(msg);
        if(mListener != null)
        {
            mListener.channelRead(ctx, msg);
        }
    }
}
