package com.yan.security.monitorSystem.infra.security;

import com.yan.security.monitorSystem.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Indica que o Spring deve carregar este filtro automaticamente
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Tenta extrair o token do cabeçalho da requisição
        var token = this.recoverToken(request);

        if (token != null) {
            // 2. Valida o token e recupera o login (subject) do usuário
            var login = tokenService.validateToken(token);

            // 3. Se o login existir, buscamos os detalhes do usuário no banco
            UserDetails user = userRepository.findByLogin(login);

            if (user != null) {
                // 4. Cria um objeto de autenticação que o Spring Security entende
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // 5. Salva essa autenticação no contexto do Spring para essa requisição específica
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 6. Continua o fluxo para o próximo filtro ou para o Controller
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        // O token geralmente vem no formato: "Authorization: Bearer <token>"
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}