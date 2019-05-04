package xin.tomdonkey.chat.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Client
{
    public static void main(String[] args)
    {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception
                    {
                        socketChannel.pipeline()
                                .addLast(new DelimiterBasedFrameDecoder(256, Unpooled.copiedBuffer(new byte[]{42,38,42,38})))
                                .addLast(new StringDecoder(StandardCharsets.UTF_8))
                                .addLast(new MspDecoder())
                                .addLast(new MspEncoder())
                                .addLast(new ClientChannelHandler());
                    }
                });
        try
        {
            ChannelFuture f = bootstrap.connect("127.0.0.1",8844).sync();

            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            workerGroup.shutdownGracefully();
        }

    }
}
class ClientChannelHandler extends ChannelInboundHandlerAdapter
{
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
    }
}
