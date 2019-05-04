package xin.tomdonkey.chat.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server
{
    public static void main(String[] args) throws IOException
    {
        //初始化serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(8844));
        serverSocketChannel.configureBlocking(false);

        //初始化selector
        Selector selector = Selector.open();

        //将serverSocketChannel的接受事件绑定到selector上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true)
        {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator= keys.iterator();
            while (iterator.hasNext())
            {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable())
                {
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel channel = ssc.accept();
                    channel.configureBlocking(false);
                    channel.register(selector,SelectionKey.OP_READ);

                    System.out.println("建立一个新的连接");
                }
                else if (key.isReadable())
                {
                    System.out.println("收到一个新的消息");

                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer  = ByteBuffer.allocate(64);
                    byteBuffer.clear();

                    int len = sc.read(byteBuffer);

                    if(len != -1)
                    {
                        System.out.println(new String(byteBuffer.array(), 0, len));
                    }

                    byteBuffer.flip();
                    byteBuffer.clear();

                    byteBuffer.put("hello client,i`m Server\n".getBytes());
                    byteBuffer.flip();

                    sc.write(byteBuffer);
                }

            }
        }
    }
}
