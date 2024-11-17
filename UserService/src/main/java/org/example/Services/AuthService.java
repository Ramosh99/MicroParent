package org.example.Services;

import org.example.DTO.RoleJson;
import org.example.DTO.UserJson;
import org.example.Exceptions.AlreadyExistsException;
import org.example.Exceptions.InvalidFormatException;
import org.example.Exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Service
public class AuthService{

    private final WebClient webClient;
    private final String clientId;
    private final String clientIdNo;
    private final String clientSecret ;
    private final String logEndpoint;
    private final String addUserEndpoint;
    private final String roleAssignEndpoint1;
    private final String roleAssignEndpoint2;

    @Autowired
    public AuthService(
            WebClient.Builder webClient,
            @Value("${keycloak.clientId}") String clientId,
            @Value("${keycloak.clientIdNo}") String clientIdNo,
            @Value("${keycloak.clientSecret}") String clientSecret,
            @Value("${keycloak.endpoint.log}") String logEndpoint,
            @Value("${keycloak.endpoint.addUser}") String addUserEndpoint,
            @Value("${keycloak.endpoint.assignRole1}") String roleAssignEndpoint1,
            @Value("${keycloak.endpoint.assignRole2}") String roleAssignEndpoint2

    ) {
        this.webClient = webClient.build();
        this.clientId = clientId;
        this.clientIdNo = clientIdNo;
        this.clientSecret = clientSecret;
        this.logEndpoint = logEndpoint;
        this.addUserEndpoint = addUserEndpoint;
        this.roleAssignEndpoint1 = roleAssignEndpoint1;
        this.roleAssignEndpoint2 = roleAssignEndpoint2;
    }

    //Client login (User management authorization)--------------- --------->>>>>>>>>>>>>>>>>>
    private Mono<String> logClient() {
        return webClient.post()
                .uri(logEndpoint)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseMap -> (String) responseMap.get("access_token"))
                .onErrorResume(e ->{
                        return Mono.error(new RuntimeException("Unexpected error", e));
                    }
                );
    }

    //Add user to key cloak (sign up)----------------------------------->>>>>>>>>>>>>>>>>>>>>
    public Mono<Boolean> addUser(
            String email,
            String password,
            String firstName,
            String lastName,
            String role
    ){
        //Validation =================================
        if(role.isEmpty()||!(role.equals("admin")||role.equals("seller")||role.equals("buyer")||role.equals("courier"))){
            return Mono.error(new InvalidFormatException("Invalid role"));
        }

        //web client call ============================
        return logClient()
                .flatMap(token -> {
                    UserJson jsonobj = new UserJson(firstName, lastName,email, password);
                    return webClient.post()
                            .uri(addUserEndpoint)
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .bodyValue(jsonobj.mainObject.toString())
                            .retrieve()
                            .onStatus(status -> status == HttpStatus.CONFLICT, response -> Mono.error(new AlreadyExistsException("User already exists")))
                            .toBodilessEntity()
                            .publishOn(Schedulers.boundedElastic())
                            .mapNotNull(response -> {
                                if(response.getStatusCode() == HttpStatus.CREATED){
                                    String locationHeader = response.getHeaders().getFirst("Location");
                                    assert locationHeader != null;
                                    String userIdNo= locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
                                    return assignRole(userIdNo, role, token).block();
                                }else{
                                    return false;
                                }
                            })
                            .onErrorResume(ex -> {
                                if (ex instanceof AlreadyExistsException) {
                                    return Mono.error(ex);
                                } else {
                                    System.out.println(ex.getMessage());
                                    return Mono.error(new RuntimeException("Unexpected error"));
                                }
                            });
                });
    }

    //Assign role--------------------------------
    private Mono<Boolean> assignRole(
           String userIdNo,
           String role,
           String token
    ){
        RoleJson jsonobj = new RoleJson(role);
        System.out.println(jsonobj.mainObject.toString());
        return webClient.post()
                .uri(roleAssignEndpoint1+userIdNo+roleAssignEndpoint2+clientIdNo)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .bodyValue(jsonobj.mainObject.toString())
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode() == HttpStatus.NO_CONTENT)
                .onErrorResume(ex -> {
                        System.out.println(ex.getMessage());
                        return Mono.error(new RuntimeException("Unexpected error"));
                    }
                );
    }


    //User login--------------------------------------------------------------->>>>>>>>>>>>>>>>>>
    public Mono<String> logUser(String email, String password) {
        return webClient.post()
                .uri(logEndpoint)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("grant_type", "password")
                        .with("username", email)
                        .with("password", password)
                )
                .retrieve()
                .onStatus(status -> status == HttpStatus.UNAUTHORIZED, response -> Mono.error(new UnauthorizedException("Invalid credentials")))
                .bodyToMono(Map.class)
                .map(responseMap -> (String) responseMap.get("access_token"))
                .onErrorResume(ex -> {
                    if (ex instanceof UnauthorizedException) {
                        return Mono.error(ex);
                    } else {
                        System.out.println(ex.getMessage());
                        return Mono.error(new RuntimeException("Unexpected error"));
                    }
                });
    }

}
