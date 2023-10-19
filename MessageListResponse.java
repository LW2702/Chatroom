package Chatroom;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageListResponse extends Response
{
    private static final String _class = MessageListResponse.class.getSimpleName();

    private List<Message> messages;

    public MessageListResponse(List<Message> messages)
    {

        if (messages == null || messages.contains(null))
            throw new NullPointerException();
        this.messages = messages;
    }

    List<Message> getMessages() { return messages; }

    @SuppressWarnings("unchecked")
    public Object toJSON() {

        JSONArray arr = new JSONArray();
        for (Message msg : messages)
            arr.add(msg.toJSON());

        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("messages", arr);
        return obj;
    }

    public static MessageListResponse fromJSON(Object val)
    {
        try
        {
            JSONObject obj = (JSONObject)val;
            if (!_class.equals(obj.get("_class")))
                return null;
            JSONArray arr = (JSONArray)obj.get("messages");
            List<Message> messages = new ArrayList<>();
            for (Object msg_obj : arr)
                messages.add(Message.fromJSON(msg_obj));

            return new MessageListResponse(messages);
        } catch (ClassCastException | NullPointerException e)
        {
            return null;
        }
    }
}
