package Chatroom;

import org.json.simple.JSONObject;

public class PublishRequest extends Request {
    // class name to be used as tag in JSON representation
    private static final String _class =
            PublishRequest.class.getSimpleName();

    //this holds the message object
    private Message message;
    //this is the channel to publish on
    private static String identity;


    //the message and the channel to publish on are put in the constructor
    public PublishRequest(Message message, String identity)
    {
        if (message == null)
        {
            throw new NullPointerException();
        }
        this.message = message;
        this.identity = identity;
    }


    Message getMessage()
    {
        return message;
    }

    String getIdentity()
    {
        return identity;
    }


    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON()
    {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("message", message.toJSON());
        obj.put("identity", identity);
        return obj;
    }


    public static PublishRequest fromJSON(Object val)
    {
        try
        {
            JSONObject obj = (JSONObject)val;

            if (!_class.equals(obj.get("_class")))
                return null;

            Object m = (JSONObject) obj.get("message");
            Message message = Message.fromJSON(m);
            String identity = (String)obj.get("identity");
            // construct the object to return (checking for nulls)
            return new PublishRequest(message, identity);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
