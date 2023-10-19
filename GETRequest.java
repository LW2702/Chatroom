package Chatroom;

import org.json.simple.JSONObject;

public class GETRequest extends Request {
    // the class name is used as a tag in JSON representation
    private static final String _class =
            GETRequest.class.getSimpleName();


    public GETRequest() {}

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        return obj;
    }


    //fromJSOM attempts to deserialize a request from a JSONObject.
    //In this case, a GETRequest. null is returned if the attempt is
    //unsuccessful
    public static GETRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // checks if the name of this class matches the name
            //in the object.
            //if it doesn't it will return null.
            if (!_class.equals(obj.get("_class")))
                return null;
            // construct the new object to return
            return new GETRequest();
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
