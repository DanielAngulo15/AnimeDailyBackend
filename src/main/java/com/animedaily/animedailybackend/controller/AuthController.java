package com.animedaily.animedailybackend.controller;

import com.animedaily.animedailybackend.config.JwtUtil;
import com.animedaily.animedailybackend.dto.LoginRequest;
import com.animedaily.animedailybackend.dto.UserRegisterDTO;
import com.animedaily.animedailybackend.model.Usuario;
import com.animedaily.animedailybackend.repository.UsuarioRepository;
import com.animedaily.animedailybackend.service.UsuarioService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody UserRegisterDTO dto) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(dto));
    }

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContraseña())
        );
        
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtUtil.generarToken(usuario.getCorreo()));
        response.put("refreshToken", jwtUtil.generarRefreshToken(usuario.getCorreo()));
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", usuario.getId());
        userInfo.put("correo", usuario.getCorreo());
        userInfo.put("username", usuario.getUsername());
        
        response.put("user", userInfo);
        
        return ResponseEntity.ok(response);
    } catch (AuthenticationException e) {
        return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
    }
}

@PostMapping("/refresh")
public ResponseEntity<?> refreshToken(
        @RequestHeader("Refresh-Token") String refreshToken) {
    try {
        if (!jwtUtil.validarToken(refreshToken)) {
            return ResponseEntity.status(401).body(Map.of("error", "Refresh token inválido"));
        }
        
        String email = jwtUtil.obtenerUsername(refreshToken);
        
        Usuario usuario = usuarioRepository.findByCorreo(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtUtil.generarToken(usuario.getCorreo()));
        response.put("refreshToken", jwtUtil.generarRefreshToken(usuario.getCorreo()));
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", usuario.getId());
        userInfo.put("correo", usuario.getCorreo());
        userInfo.put("username", usuario.getUsername());
        
        response.put("user", userInfo);
        
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
    }
}
}