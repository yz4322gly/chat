package xin.tomdonkey.chat.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import xin.tomdonkey.chat.netty.protocol.MspDecoder;
import xin.tomdonkey.chat.netty.protocol.MspEncoder;

import java.nio.charset.StandardCharsets;

public class NettyServer
{
    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void serverStart(int port)
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(5);

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel socketChannel)
                    {
                        socketChannel.pipeline()
                                .addLast(new DelimiterBasedFrameDecoder(256, Unpooled.copiedBuffer(new byte[]{42, 38, 42, 38})))
                                .addLast(new StringDecoder(StandardCharsets.UTF_8))
                                .addLast(new MspDecoder())
                                .addLast(new MspEncoder())
                                .addLast(new ServerChannelHandler(channels));
                    }
                });

        try
        {
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}


