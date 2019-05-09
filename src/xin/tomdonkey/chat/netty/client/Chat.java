package xin.tomdonkey.chat.netty.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author guolinyuan
 */
@SpringBootApplication
public class Chat
{
    public static ConfigurableApplicationContext context ;

    public static void main(String[] args)
    {
        ConfigurableApplicationContext context = SpringApplication.run(Chat.class, args);
        Chat.context = context;
        context.getBean("client",Client.class).start();
    }
}
