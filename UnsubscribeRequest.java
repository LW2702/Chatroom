package Chatroom;

import org.json.simple.JSONObject;

public class UnsubscribeRequest extends Request
{
    private static final String _class = UnsubscribeRequest.class.getSimpleName();

    private String channel;
    public UnsubscribeRequest(String channel)
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

    public static UnsubscribeRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            if (!_class.equals(obj.get("_class")))
                return null;

            String channel = (String)obj.get("channel");
            return new UnsubscribeRequest(channel);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }

}
