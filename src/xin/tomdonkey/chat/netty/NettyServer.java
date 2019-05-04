package xin.tomdonkey.chat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

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
                    protected void initChannel(SocketChannel socketChannel) throws Exception
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


class ServerChannelHandler extends ChannelInboundHandlerAdapter
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

            ctx.writeAndFlush(new Msp( new HashMap<>(),"好的，我知道了"));


            channels.writeAndFlush(new Msp( new HashMap<>(),ctx.channel().remoteAddress() + "欢迎加入！"));
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

