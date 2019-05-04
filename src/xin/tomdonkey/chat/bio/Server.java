package xin.tomdonkey.chat.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(8844);
        while (true)
        {
            Socket socket = serverSocket.accept();
            new Thread(new ServerHandler(socket)).start();
        }
    }
}

class ServerHandler implements Runnable
{
    private Socket socket;

    public ServerHandler(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            while(true)
            {
                //此操作阻塞，直到客户端发起消息，才会停止,设定\n是一条消息的终止符，即10
                int i = -1;
                System.out.print("Server received message:");
                while ( (i = in.read())!= 10)
                {
                    System.out.print(i);
                }
                System.out.println();

                out.write("hello client,i`m Server\n".getBytes());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
