package ryu.meo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ryu.meo.dto.RefreshTokenDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Modification Logs:
 * DATE | AUTHOR | DESCRIPTION
 * -------------------------------------
 * 01-08-2025 | Ryu | Create
 */
@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    String issuerUri;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    String clientSecret;

    final RestClient restClient = RestClient.create();

    @GetMapping("/info")
    public ResponseEntity<String> getUserInfo(@AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(restClient.get()
                .uri(issuerUri + "/protocol/openid-connect/userinfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class));
    }

    @PostMapping("/access-token")
    public ResponseEntity<String> getAccessToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshTokenDTO.getRefreshToken());
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);

        return ResponseEntity.ok(restClient.post()
                .uri(issuerUri + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(String.class));

    }
}
