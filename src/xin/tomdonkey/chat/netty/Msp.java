package xin.tomdonkey.chat.netty;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 传输的消息实体，前若干行为head，使用key:value标记，每一行使用\r\n隔开
 * 消息体body和头使用一个空行隔开，即头和体中间有\r\n\r\n隔开，消息可以没有消息体。
 * 注意，如果只有消息头的话，消息头结尾也必须有\r\n
 */
class Msp
{
    private Map<String,String> head;
    private String body;

    @Override
    public String toString()
    {
        return "Msp{" +
                "head=" + head +
                ", body='" + body + '\'' +
                '}';
    }

    public Msp()
    {
    }

    public Msp(Map<String, String> head, String body)
    {
        this.head = head;
        this.body = body;
    }



    public static Msp StringToMsp(String message) throws IllegalArgumentException
    {
        String[] msgs = message.split("\r\n");
        if (msgs.length <= 1)
        {
            throw new IllegalArgumentException("data format error,please discard");
        }
        else
        {
            //构造返回实体
            Msp result = new Msp();
            result.body = "";
            result.head = new LinkedHashMap<>(msgs.length);

            //如果有body的话，那么数组的倒数第二个一定是一个空的字符串
            if (msgs.length - 2 > 0 && msgs[msgs.length - 2].equals(""))
            {
                for (int i = 0 ; i < msgs.length -2 ;i++)
                {
                    //将每一个消息头注入，如果不合key:value规则，则直接丢弃
                    try
                    {
                        String[] entry = msgs[i].split(":");
                        result.head.put(entry[0],entry[1]);
                    }
                    catch (Exception ignored)
                    {

                    }
                }
                result.body = msgs[msgs.length - 1];
            }
            //此时没有消息体，构造消息体为空字符串
            else
            {
                for (String msg : msgs)
                {
                    //将每一个消息头注入，如果不合key:value规则，则直接丢弃
                    try
                    {
                        String[] entry = msg.split(":");
                        result.head.put(entry[0], entry[1]);
                    }
                    catch (Exception ignored)
                    {

                    }
                }
            }
            return result;
        }


    }

    public static String mspToString(Map<String,String> heads,String body)
    {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String,String> entry : heads.entrySet())
        {
            result.append(entry.getKey()).append(":").append(entry.getValue()).append("\r\n");
        }
        if (heads.size() <= 0)
        {
            result.append("\r\n");
        }
        result.append("\r\n").append(body);
        return result.toString();
    }

    public static String mspToString(Msp msp)
    {
        return Msp.mspToString(msp.head,msp.body);
    }


    public boolean isInit()
    {
        return head.containsKey("init") && head.containsKey("fromId");
    }


    public String getFromId()
    {
        return head.get("fromId");
    }
}
