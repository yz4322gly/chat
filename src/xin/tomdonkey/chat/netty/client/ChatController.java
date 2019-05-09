package xin.tomdonkey.chat.netty.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import xin.tomdonkey.chat.netty.protocol.Msp;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

@Component
@Scope("singleton")
public class ChatController implements Initializable
{
    @FXML
    private Button sendButton;

    @FXML
    private TextArea chatBoxTextArea;

    @FXML
    private TextField sendTextField;

    @Autowired
    ClientChannelHandler handler;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    public void textFieldKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.ENTER))
        {
           sendMessage();
        }
    }

    public void sendMessage()
    {
        String text = sendTextField.getText();
        sendTextField.clear();
        chatBoxTextArea.appendText(text + "\n");

        HashMap<String,String> broadcastMap = new HashMap<>(2);
        broadcastMap.put("fromId","1236");
        broadcastMap.put("type","broadcast");
        handler.ctx.writeAndFlush(new Msp(broadcastMap,text));
    }



    public void appendToChatBoxTextArea(String text)
    {
        chatBoxTextArea.appendText(text + "\n");
    }

}
