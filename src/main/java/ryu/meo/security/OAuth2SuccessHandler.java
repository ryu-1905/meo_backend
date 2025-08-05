package ryu.meo.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Modification Logs:
 * DATE | AUTHOR | DESCRIPTION
 * -------------------------------------
 * 01-08-2025 | Ryu | Create
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

        private final OAuth2AuthorizedClientService authorizedClientService;

        @Value("${spring.security.oauth2.client.registration.keycloak.redirect-frontend-uri}")
        private String redirectFrontendUri;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {

                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                                oauthToken.getAuthorizedClientRegistrationId(),
                                oauthToken.getName());
                OAuth2AccessToken accessToken = client.getAccessToken();
                OAuth2RefreshToken refreshToken = client.getRefreshToken();
                response.sendRedirect(
                                redirectFrontendUri + "?token="
                                                + URLEncoder.encode(accessToken.getTokenValue(), StandardCharsets.UTF_8)
                                                + "&refresh-token="
                                                + URLEncoder.encode(refreshToken.getTokenValue(),
                                                                StandardCharsets.UTF_8)
                                                + "&access-token-expires-at="
                                                + URLEncoder.encode(accessToken.getExpiresAt().atZone(ZoneId.of("UTC"))
                                                                .toString(), StandardCharsets.UTF_8));
        }

}
