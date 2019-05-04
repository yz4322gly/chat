package xin.tomdonkey.chat.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.net.StandardSocketOptions.SO_RCVBUF;

public class Server
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8844));

        asynchronousServerSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>()
        {
            @Override
            public void completed(AsynchronousSocketChannel result, Void attachment)
            {
                asynchronousServerSocketChannel.accept(null,this);

                ByteBuffer byteBuffer = ByteBuffer.allocate(64);

                result.read(byteBuffer, null, new CompletionHandler<Integer, Void>()
                {
                    @Override
                    public void completed(Integer len, Void attachment)
                    {
                        if(len != -1)
                        {
                            System.out.println(new String(byteBuffer.array(), 0, len));
                        }

                        byteBuffer.flip();
                        byteBuffer.clear();

                        byteBuffer.put("hello client,i`m Server\n".getBytes());
                        byteBuffer.flip();

                        result.write(byteBuffer);
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment)
                    {

                    }
                });
            }

            @Override
            public void failed(Throwable exc, Void attachment)
            {
                exc.printStackTrace();
            }
        });
        while(true)
        {
            Thread.sleep(1000);
        }
    }



}
