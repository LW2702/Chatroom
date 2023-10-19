package Chatroom;

import org.json.simple.JSONObject;

public class SubscribeRequest extends Request
{
    private static final String _class =
            SubscribeRequest.class.getSimpleName();

    private String channel;

    public SubscribeRequest(String channel)
    {
        if (channel == null) {throw new NullPointerException();}
        this.channel = channel;
    }

    public String getChannel() {return channel;}

    public Object toJSON()
    {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("channel", channel);
        return obj;
    }

    public static SubscribeRequest fromJSON(Object val)
    {
        try
        {
            JSONObject obj = (JSONObject)val;
            if (!_class.equals(obj.get("_class")))
                return null;
            // deserialize login name
            String channel = (String)obj.get("channel");
            // construct the object to return (checking for nulls)
            return new SubscribeRequest(channel);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
