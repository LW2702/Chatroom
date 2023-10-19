package Chatroom;

import org.json.simple.JSONObject;

public class KeywordRequest extends Request
{
    private static final String _class = KeywordRequest.class.getSimpleName();

    private String keyword;

    public KeywordRequest(String keyword)
    {
        // check for null
        if (keyword == null)
            throw new NullPointerException();
        else
            this.keyword = keyword;
    }



    String getKeyword() { return keyword; }

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("keyword", keyword);
        return obj;
    }

    public static KeywordRequest fromJSON(Object val)
    {
        try
        {
            JSONObject obj = (JSONObject) val;

            if (!_class.equals(obj.get("_class")))
                return null;

            String keyword = (String) obj.get("keyword");
            return new KeywordRequest(keyword);
        }
        catch (ClassCastException | NullPointerException e)
        {
            return null;
        }
    }
}
