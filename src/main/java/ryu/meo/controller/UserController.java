package ryu.meo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Modification Logs:
 * DATE | AUTHOR | DESCRIPTION
 * -------------------------------------
 * 01-08-2025 | Ryu | Create
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    private final RestClient restClient = RestClient.create();

    @GetMapping("/info")
    public ResponseEntity<String> getUserInfo(@AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(restClient.get()
                .uri(issuerUri + "/protocol/openid-connect/userinfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class));
    }
}
