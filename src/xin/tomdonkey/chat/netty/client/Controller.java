package xin.tomdonkey.chat.netty.client;

import io.netty.channel.ChannelHandlerContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller  implements Initializable, OnChannelReadListener
{
    @FXML
    private Button sendButton;

    @FXML
    private TextArea chatBoxTextArea;

    @FXML
    private TextField sendTextField;



    private void init(){
        Client.mClientChannelHandle.setListener(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        init();
        sendTextField.setOnKeyPressed(event ->
        {
            if (event.getCode().equals(KeyCode.ENTER))
            {
                String text = sendTextField.getText();
                sendTextField.clear();
                chatBoxTextArea.appendText(text + "\n");
            }
        });
    }

    public void sendMessage(ActionEvent event)
    {
        String text = sendTextField.getText();
        sendTextField.clear();
        chatBoxTextArea.appendText(text + "\n");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        chatBoxTextArea.appendText(msg + "\n");
    }
}
