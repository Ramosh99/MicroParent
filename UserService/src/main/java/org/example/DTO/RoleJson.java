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
            obj.put("containerId", "b719b0aa-059d-40c7-bee4-1c8619952e9c");
            this.mainObject.put(obj);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    private String getId(String roleName){
        return switch (roleName) {
            case "admin" -> "";
            case "buyer" -> "ea8b14d3-cc78-444c-8cf4-54596b6ae198";
            case "seller" -> "cd0ff57d-bbd0-4736-9ba6-8e851b944176";
            case "courier" -> "440ee004-b546-49e7-9d94-7e105a218ea7";
            default -> "";
        };
    }
}
