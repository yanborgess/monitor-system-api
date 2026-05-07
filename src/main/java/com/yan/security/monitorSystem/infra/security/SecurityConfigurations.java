package com.yan.security.monitorSystem.infra.security;

import com.yan.security.monitorSystem.models.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Habilita a segurança personalizada no projeto
public class SecurityConfigurations {

    @Bean // O Spring vai gerenciar o retorno deste método como um componente
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, SecurityFilter securityFilter) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Desabilita o CSRF, pois usaremos JWT (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define que não teremos sessões guardadas no servidor
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Libera o acesso para o endpoint de login
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Libera a criação de usuários (por enquanto)
                        .requestMatchers(HttpMethod.POST, "/api/devices").hasRole("ADMIN") // Só admin cadastra dispositivos
                        .anyRequest().authenticated() // Qualquer outra rota exige estar logado
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Este método permite que o Spring injete o gerenciador de autenticação em outros lugares
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define o algoritmo de criptografia para as senhas
        return new BCryptPasswordEncoder();
    }
}