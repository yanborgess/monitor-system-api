package com.yan.security.monitorSystem.controllers;

import com.yan.security.monitorSystem.controllers.dtos.AuthenticationDTO;
import com.yan.security.monitorSystem.controllers.dtos.LoginResponseDTO;
import com.yan.security.monitorSystem.controllers.dtos.RegisterDTO;
import com.yan.security.monitorSystem.infra.security.TokenService;
import com.yan.security.monitorSystem.models.user.User;
import com.yan.security.monitorSystem.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        // 1. Cria um "envelope" com login e senha para o Spring validar
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        // 2. O motor tenta autenticar. Ele vai chamar o AuthorizationService e o BCrypt automaticamente
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Se deu certo, gera o Token para o usuário que foi autenticado
        var token = tokenService.generateToken((User) auth.getPrincipal());

        // 4. Devolve o token para o React dentro do nosso DTO
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        // Verifica se o login já existe
        if (this.userRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        // CRIPTOGRAFIA: Nunca salvamos a senha pura!
        // O BCrypt gera um hash aleatório da senha
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        User newUser = new User(data.login(), encryptedPassword, data.role());

        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }





}
