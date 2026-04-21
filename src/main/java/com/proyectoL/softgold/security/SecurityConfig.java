package com.proyectoL.softgold.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//Configuracion de seguridad para la aplicacion, rutas permitidas y roles de usuario
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final CustomLoginPassword customLoginPassword;

        public SecurityConfig(CustomLoginPassword customLoginPassword) {
                this.customLoginPassword = customLoginPassword;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        // ...existing code...
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                                "/login", "/registro", "/registro/**", "/recuperar",
                                                "/recuperar/**", "/recuperar-password", "/enviar-token",
                                                "/cambiarPassword", "/cambiarPassword/**",
                                                "/cuenta-bloqueada",
                                                "/css/**", "/js/**", "/images/**", "/redirectByRole",
                                                "/soporte/reporte",
                                                "/foro", "/foro/crear", "/foro/{id:[\\d]+}", "/foro/{id:[\\d]+}/comentar",
                                                "/reporte_incidente.html", "/formulario_satisfaccion.html",
                                                "/service_level.html", "/control_cambios.html", "/mejor_continua.html"
                                )
                                .permitAll()
                                .requestMatchers("/foro/eliminar/**").hasRole("ADMINISTRADOR")
                                .requestMatchers("/soporte/tickets/**").hasRole("ADMINISTRADOR")
                                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                                .requestMatchers("/minero/**").hasRole("MINERO")
                                .requestMatchers("/empleado/**").hasRole("EMPLEADO")
                                .requestMatchers("/usuario/**").hasRole("USUARIO")
                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .usernameParameter("email")
                                                .defaultSuccessUrl("/redirectByRole", true)
                                                .failureHandler(customLoginPassword)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }
        // ...existing code...
}
