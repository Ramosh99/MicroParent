package org.example.DTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Auth {
    public String username;
    public String password;
    public String grant_type;
    public String client_id;
    public Auth(){
        this.grant_type = "password";
        this.client_id = "demoSpring";
    }

}
