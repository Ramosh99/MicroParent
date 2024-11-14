package org.example.DTO;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserJson {
    public JSONObject mainObject;

    public UserJson(String firstName, String lastName, String email, String password) {
        this.mainObject= new JSONObject();
        try {
            // Create JSON for "attributes"
            String attributeKey = "attribute_key";
            String attributeValue = "test_value";
            JSONObject attributes = new JSONObject();
            attributes.put(attributeKey, attributeValue);

            // Create JSON for "credentials" (an array with one object)
            JSONArray credentials = new JSONArray();
            JSONObject credentialObject = new JSONObject();
            credentialObject.put("temporary", false);
            credentialObject.put("type", "password");
            credentialObject.put("value", password);
            credentials.put(credentialObject);

            // Create the main JSON object and assign all properties
            mainObject.put("attributes", attributes);
            mainObject.put("credentials", credentials);
            mainObject.put("username", firstName + lastName);
            mainObject.put("firstName", firstName);
            mainObject.put("lastName", lastName);
            mainObject.put("email", email);
            mainObject.put("emailVerified", false);
            mainObject.put("enabled", true);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }


}
