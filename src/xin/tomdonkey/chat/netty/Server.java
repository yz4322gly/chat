package xin.tomdonkey.chat.netty;

public class Server
{
    private int port;
    private NettyServer server;



    public Server(int port)
    {
        this.server = new NettyServer();
        this.port = port;
    }

    public void start()
    {
        server.serverStart(port);
    }

    public static void main(String[] args)
    {
        Server server = new Server(8844);
        server.start();
    }
}

