package xin.tomdonkey.chat.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class MspEncoder extends MessageToByteEncoder<Msp>
{

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Msp msp, ByteBuf byteBuf) throws Exception
    {
        byteBuf.writeBytes((Msp.mspToString(msp) + "*&*&").getBytes(StandardCharsets.UTF_8));
    }
}
