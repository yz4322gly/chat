package xin.tomdonkey.chat.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import xin.tomdonkey.chat.netty.protocol.Msp;

import java.util.HashMap;

/**
 * @author guolinyuan
 */
@Component
@Scope("singleton")
public class ClientChannelHandler extends ChannelInboundHandlerAdapter
{
    @Autowired
    ChatController chatController;

    ChannelHandlerContext ctx;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        this.ctx=ctx;

        HashMap<String,String> map = new HashMap<>(2);
        map.put("type","init");
        map.put("fromId","1236");
        ctx.writeAndFlush(new Msp(map,""));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        try
        {
            chatController.appendToChatBoxTextArea(msg.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            ReferenceCountUtil.release(msg);
        }

    }
}
