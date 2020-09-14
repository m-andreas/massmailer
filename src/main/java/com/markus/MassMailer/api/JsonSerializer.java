package com.markus.MassMailer.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonSerializer {
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keysItr = object.keys();

        while(keysItr.hasNext()) {
            String key = keysItr.next();
            String value = object.getString(key);
            map.put(key, value);
        }
        return map;
    }
}
