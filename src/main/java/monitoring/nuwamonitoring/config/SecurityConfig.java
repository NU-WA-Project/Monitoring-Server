package monitoring.nuwamonitoring.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AdminServerProperties adminServerProperties;

    public SecurityConfig(AdminServerProperties adminServerProperties) {
        this.adminServerProperties = adminServerProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        loginSuccessHandler.setTargetUrlParameter("redirectTo");
        loginSuccessHandler.setDefaultTargetUrl(this.adminServerProperties.path("/"));

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(this.adminServerProperties.path("/assets/**")).permitAll()
                        .requestMatchers(this.adminServerProperties.path("/login")).permitAll()
                        .anyRequest().authenticated())
                .formLogin(login -> login.loginPage(this.adminServerProperties.path("/login")).successHandler(loginSuccessHandler))
                .logout(logout -> logout.logoutUrl(this.adminServerProperties.path("/logout")))
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                this.adminServerProperties.path("/instances"),
                                this.adminServerProperties.path("/actuator/**")));

        return http.build();
    }
}
