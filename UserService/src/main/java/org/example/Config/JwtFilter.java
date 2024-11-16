package org.example.Config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Component
public class JwtFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    public JwtFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String[] chunks = token.split("\\.");
            String payload = new String(Base64.getDecoder().decode(chunks[1]));

            JsonNode rootNode = objectMapper.readTree(payload);
            JsonNode resourceAccess = rootNode.path("resource_access");
            JsonNode demoClient = resourceAccess.path("demoClient");
            JsonNode roles = demoClient.path("roles");

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.asText().toUpperCase()));
            });

            String username = rootNode.path("preferred_username").asText();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}