package xin.tomdonkey.chat.netty.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("singleton")
public class Client
{
    @Autowired
    private ClientUI clientUI;
    @Autowired
    private ClientNetty clientNetty;

    @SuppressWarnings("all")
    public void start()
    {
        new Thread(() -> clientUI.start()).start();
        new Thread(() -> clientNetty.start()).start();
    }
}
