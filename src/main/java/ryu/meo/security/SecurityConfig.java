package ryu.meo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Modification Logs:
 * DATE | AUTHOR | DESCRIPTION
 * -------------------------------------
 * 27-07-2025 | Ryu | Create
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {

        @Value("${spring.security.cors.allowed-origins}")
        List<String> corsOrigins;

        final ClientRegistrationRepository clientRegistrationRepository;
        final AuthenticationSuccessHandler oAuth2SuccessHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                return httpSecurity
                                .authorizeHttpRequests(
                                                authorize -> authorize
                                                                .requestMatchers("/oauth2/**", "/login/oauth2/code/**",
                                                                                "/user/access-token",
                                                                                "/public/**")
                                                                .permitAll()
                                                                .anyRequest().authenticated())
                                .oauth2Login(oauth2Login -> oauth2Login.successHandler(oAuth2SuccessHandler))
                                .oauth2Client(Customizer.withDefaults())
                                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf.disable())
                                .httpBasic(httpBasic -> httpBasic.disable())
                                .formLogin(formLogin -> formLogin.disable())
                                .build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowCredentials(corsOrigins.get(0).equals("*") ? false : true);
                corsConfiguration.setAllowedOrigins(corsOrigins);
                corsConfiguration.setAllowedMethods(List.of("*"));
                corsConfiguration.setAllowedHeaders(List.of("*"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", corsConfiguration);
                return source;
        }
}
