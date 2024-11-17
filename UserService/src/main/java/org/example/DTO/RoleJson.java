package org.example.DTO;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoleJson {
    public JSONArray mainObject;

    public RoleJson(String roleName) {
        this.mainObject= new JSONArray();
        try{
            JSONObject obj = new JSONObject();
            obj.put("id", getId(roleName));
            obj.put("name", roleName);
            obj.put("description", "");
            obj.put("composite", false);
            obj.put("clientRole", true);
            obj.put("containerId", "7d300385-3863-4bc6-b3c8-3218d9598fb1");
            this.mainObject.put(obj);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    private String getId(String roleName){
        return switch (roleName) {
            case "admin" -> "015b8705-b544-4424-a08a-60e14d33ad42";
            case "buyer" -> "b9b054a7-f47e-4e9a-94ff-cbc2c9f0c094";
            case "seller" -> "189615e8-2d12-492c-adb5-086448e52f42";
            case "courier" -> "5e21d014-9bcd-4509-9cf6-e7631436289c";
            default -> "";
        };
    }
}
