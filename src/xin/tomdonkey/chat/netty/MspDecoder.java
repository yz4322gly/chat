package xin.tomdonkey.chat.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

class MspDecoder extends MessageToMessageDecoder<String>
{
    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception
    {
        try
        {
            out.add(Msp.StringToMsp(msg));
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("不合规范，丢弃消息");
            ReferenceCountUtil.release(msg);
        }
    }
}
