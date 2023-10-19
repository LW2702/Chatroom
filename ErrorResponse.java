package Chatroom;

import org.json.simple.JSONObject;

public class ErrorResponse extends Response
{

    private static final String _class =
            ErrorResponse.class.getSimpleName();

    private String error;

    public ErrorResponse(String error) {
        // check for null
        if (error == null)
            throw new NullPointerException();
        this.error = error;
    }

    public String getError() { return error; }

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("error", error);
        return obj;
    }


    public static ErrorResponse fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            if (!_class.equals(obj.get("_class")))
                return null;
            String error = (String)obj.get("error");
            return new ErrorResponse(error);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
