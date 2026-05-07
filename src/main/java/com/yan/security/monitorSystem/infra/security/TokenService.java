package com.yan.security.monitorSystem.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.yan.security.monitorSystem.models.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret; // Chave secreta que virá do seu application.properties

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); // Define o algoritmo e a chave
            String token = JWT.create()
                    .withIssuer("auth-api") // Identifica quem gerou o token (sua app)
                    .withSubject(user.getLogin()) // Guarda o login do usuário dentro do token
                    .withExpiresAt(genExpirationDate()) // Define quando o token vence
                    .sign(algorithm); // Assina digitalmente
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token) // Verifica se a assinatura é válida e não expirou
                    .getSubject(); // Devolve o login do usuário se estiver tudo ok
        } catch (JWTVerificationException exception) {
            return ""; // Se o token for falso ou expirado, retorna vazio
        }
    }

    private Instant genExpirationDate() {
        // Define que o token vale por 2 horas, ajustado ao fuso horário de Brasília (-03:00)
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}