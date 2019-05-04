package xin.tomdonkey.chat.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        Socket socket = new Socket("127.0.0.1",8844);
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        while(true)
        {
            out.write("hello Server,i`m Client\r\n\r\ndasdss*&*&".getBytes());

            //此操作阻塞，直到服务器发起消息，才会停止,设定\n是一条消息的终止符，即10
            int i = -1;
            System.out.print("Client received message:");
            while ( (i = in.read())!= -1)
            {
                System.out.print(i);
            }
            System.out.println();

            Thread.sleep(1000);
        }
    }
}
