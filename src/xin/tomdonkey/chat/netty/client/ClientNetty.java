package xin.tomdonkey.chat.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import xin.tomdonkey.chat.netty.protocol.MspDecoder;
import xin.tomdonkey.chat.netty.protocol.MspEncoder;

import java.nio.charset.StandardCharsets;

/**
 * @author guolinyuan
 */
@Component
@Scope("singleton")
public class ClientNetty
{
    @Autowired
    ClientChannelHandler clientChannelHandler;

    public void start()
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
                                .addLast(new DelimiterBasedFrameDecoder(256, Unpooled.copiedBuffer(new byte[]{42, 38, 42, 38})))
                                .addLast(new StringDecoder(StandardCharsets.UTF_8))
                                .addLast(new MspDecoder())
                                .addLast(new MspEncoder())
                                .addLast(clientChannelHandler);
                    }
                });
        try
        {
            ChannelFuture f = bootstrap.connect("192.168.1.28",8844).sync();
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
