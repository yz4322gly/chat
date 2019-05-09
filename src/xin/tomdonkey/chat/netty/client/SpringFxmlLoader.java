package xin.tomdonkey.chat.netty.client;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author guolinyuan
 */
public class SpringFxmlLoader extends FXMLLoader
{
    @SuppressWarnings("rawtypes")
    public Object springLoad(String url, Class cl)
    {
        try
        {
            InputStream fxmlStream = cl.getResourceAsStream(url);
            FXMLLoader loader = this;
            loader.setControllerFactory(clazz -> Chat.context.getBean(clazz));
            return loader.load(fxmlStream);
        }
        catch (IOException ioException)
        {
            throw new RuntimeException(ioException);
        }
    }
}