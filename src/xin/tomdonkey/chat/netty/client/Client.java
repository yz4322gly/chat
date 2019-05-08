package xin.tomdonkey.chat.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xin.tomdonkey.chat.netty.protocol.MspDecoder;
import xin.tomdonkey.chat.netty.protocol.MspEncoder;

import java.nio.charset.StandardCharsets;

public class Client extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static ClientChannelHandler mClientChannelHandle = new ClientChannelHandler();

    public static void main(String[] args)
    {
        new Thread(() -> launch(args)).start();


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
                                .addLast(mClientChannelHandle);
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
