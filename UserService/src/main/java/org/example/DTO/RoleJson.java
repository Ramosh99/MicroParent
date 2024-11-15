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
            obj.put("containerId", "b9302d05-f30b-447c-a490-32af934eea77");
            this.mainObject.put(obj);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    private String getId(String roleName){
        return switch (roleName) {
            case "admin" -> "49687e43-2a02-4149-9527-f1c2ec4b7959";
            case "buyer" -> "d45a782c-8413-4b64-b90a-8b2fa2ad2dcb";
            case "seller" -> "2829b8b2-d0bd-46bd-8314-1262d1f123da";
            case "courier" -> "68517736-5f5a-497d-a37e-c855af8a14c0";
            default -> "";
        };
    }
}
