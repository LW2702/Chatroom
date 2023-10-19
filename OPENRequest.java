package Chatroom;

import org.json.simple.JSONObject;

public class OPENRequest extends Request {
    // class name to be used as tag in JSON representation
    private static final String _class =
            OPENRequest.class.getSimpleName();

    private static String identity;

    // Constructor; throws NullPointerException if name is null.
    public OPENRequest(String identity) {
        // check for null
        if (identity == null)
            throw new NullPointerException();
        this.identity = identity;
    }

    String getIdentity() { return identity; }

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("identity", identity);
        return obj;
    }

    public static OPENRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            if (!_class.equals(obj.get("_class")))
                return null;
            String identity = (String)obj.get("identity");
            return new OPENRequest(identity);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
