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
            this.mainObject.put("attributes", attributes);
            this.mainObject.put("credentials", credentials);
            this.mainObject.put("username", firstName + lastName);
            this.mainObject.put("firstName", firstName);
            this.mainObject.put("lastName", lastName);
            this.mainObject.put("email", email);
            this.mainObject.put("emailVerified", false);
            this.mainObject.put("enabled", true);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }


}
