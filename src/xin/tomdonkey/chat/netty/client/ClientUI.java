package xin.tomdonkey.chat.netty.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author guolinyuan
 */
@Component
@Scope("singleton")
public class ClientUI extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        AnchorPane root =(AnchorPane) new SpringFxmlLoader().springLoad("sample.fxml",ClientUI.class);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void start()
    {
        ClientUI.launch();
    }
}
