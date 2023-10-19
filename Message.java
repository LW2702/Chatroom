package Chatroom;

import org.json.simple.JSONObject;

public class Message
{
    private static final String _class = Message.class.getSimpleName();

    private final String body;
    private final String from;
    private long when;

    public Message( String from, long when, String body) {
        if (body == null || from == null)
            throw new NullPointerException();
        this.body = body;
        this.from = from;
        this.when = when;
    }

    public String getBody()      { return body; }
    public String getFrom()    { return from; }
    public long getWhen() { return when; }

    public void setWhen(long when) {this.when = when;}

    public String toString() {
        return from + ": " + body + " (" + when + ")";
    }

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class",    _class);
        obj.put("body",      body);
        obj.put("from", from);
        obj.put("when", when);
        return obj;
    }

    public static Message fromJSON(Object val) {
        try
        {
            JSONObject obj = (JSONObject)val;
            if (!_class.equals(obj.get("_class")))
                return null;
            String body = (String)obj.get("body");
            String from = (String)obj.get("from");
            long when = (long)obj.get("when");
            return new Message(from, when, body);
        }
        catch (ClassCastException | NullPointerException e)
        {
            return null;
        }
    }
}
