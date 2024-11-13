package org.example.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;


@Service
public class AuthService{

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String clientId;
    private final String clientSecret ;
    private final String logEndpoint;
    private final String addUserEndpoint;

    @Autowired
    public AuthService(
            WebClient.Builder webClient,
            ObjectMapper objectMapper,
            @Value("${keycloak.clientId}") String clientId,
            @Value("${keycloak.clientSecret}") String clientSecret,
            @Value("${keycloak.endpoint.log}") String logEndpoint,
            @Value("${keycloak.endpoint.addUser}") String addUserEndpoint
    ) {
        this.webClient = webClient.build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.logEndpoint = logEndpoint;
        this.addUserEndpoint = addUserEndpoint;
        this.objectMapper = objectMapper;
    }

    public Mono<String> logClient() {
        return webClient.post()
                .uri(logEndpoint)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseMap -> (String) responseMap.get("access_token"));
    }

    //Add user to key cloak (sign up)----------------------------------->>>>>>>>>>>>>>>>>>>>>
    public Mono<Boolean> addUser(
            String email,
            String password,
            String firstName,
            String lastName
    ){
        return logClient()
                .flatMap(token -> {
                    String mainObject = createJson(email, password, firstName, lastName);
                    return webClient.post()
                            .uri(addUserEndpoint)
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .bodyValue(mainObject)
                            .retrieve()
                            .toBodilessEntity()
                            .map(response -> response.getStatusCode() == HttpStatus.CREATED);
                });
    }

    //manipulating json object
    private String createJson(String email, String password, String firstName, String lastName) {
        JSONObject mainObject = new JSONObject();
        try {
            String attributeKey = "attribute_key";
            String attributeValue = "test_value";
            boolean temporary = false;
            // Create JSON for "attributes"
            JSONObject attributes = new JSONObject();
            attributes.put(attributeKey, attributeValue);
            // Create JSON for "credentials" (an array with one object)
            JSONArray credentials = new JSONArray();
            JSONObject credentialObject = new JSONObject();
            credentialObject.put("temporary", temporary);
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
        return mainObject.toString();
    }

    //creat

    //User login--------------------------------------------------------------->>>>>>>>>>>>>>>>>>
    public Mono<String> logUser(String username, String password) {
        return webClient.post()
                .uri(logEndpoint)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("grant_type", "password")
                        .with("username", username)
                        .with("password", password)
                )
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseMap -> (String) responseMap.get("access_token"));
    }

}
