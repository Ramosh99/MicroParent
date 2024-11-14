package org.example.Services;

import org.example.DTO.UserJson;
import org.example.Exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AuthService{

    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret ;
    private final String logEndpoint;
    private final String addUserEndpoint;

    @Autowired
    public AuthService(
            WebClient.Builder webClient,
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
    }

    //Client login (User management authorization)--------------- --------->>>>>>>>>>>>>>>>>>
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
                    UserJson jsonobj = new UserJson(firstName, lastName,email, password);

                    return webClient.post()
                            .uri(addUserEndpoint)
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .bodyValue(jsonobj.mainObject.toString())
                            .retrieve()
                            .toBodilessEntity()
                            .map(response -> response.getStatusCode() == HttpStatus.CREATED);
                });
    }


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
                .onStatus(status -> status == HttpStatus.UNAUTHORIZED, response -> Mono.error(new UnauthorizedException("Invalid credentials")))
                .bodyToMono(Map.class)
                .map(responseMap -> (String) responseMap.get("access_token"))
                .onErrorResume(ex -> {
                    if (ex instanceof UnauthorizedException) {
                        return Mono.error(ex);  // Pass 401 Unauthorized error to the controller
                    } else {
                        return Mono.error(new RuntimeException("Unexpected error")); // Handle other errors
                    }
                });
    }

}
