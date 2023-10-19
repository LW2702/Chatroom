package Chatroom;

import org.json.simple.JSONObject;

public class TimeRequest extends Request
{
    private static final String _class = TimeRequest.class.getSimpleName();

    private String start;
    private String end;
    public TimeRequest(String start, String end)
    {
        this.start = start;
        this.end = end;
    }

    String getStart() { return start; }
    String getEnd() { return end; }

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("start", start);
        obj.put("end", end);
        return obj;
    }

    public static TimeRequest fromJSON(Object val)
    {
        try
        {
            JSONObject obj = (JSONObject) val;

            if (!_class.equals(obj.get("_class")))
                return null;

            String start = (String) obj.get("start");
            String end = (String) obj.get("end");

            return new TimeRequest(start, end);
        }
        catch (ClassCastException | NullPointerException e)
        {
            return null;
        }

    }

}
