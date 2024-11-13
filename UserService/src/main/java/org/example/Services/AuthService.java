package org.example.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


@Service
public class AuthService{

    private final WebClient.Builder webClient;
    private final String clientId;
    private final String clientSecret ;
    private final String clientLogEndpoint;

    @Autowired
    public AuthService(
            WebClient.Builder webClient,
            @Value("${keycloak.clientId}") String clientId,
            @Value("${keycloak.clientSecret}") String clientSecret,
            @Value("${keycloak.endpoint.clientLog}") String clientLogEndpoint
    ) {
        this.webClient = webClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientLogEndpoint = clientLogEndpoint;
    }

    public Mono<String> logClient() {
        return webClient.build().post()
                .uri(clientLogEndpoint)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseMap -> (String) responseMap.get("access_token"));
    }

}
