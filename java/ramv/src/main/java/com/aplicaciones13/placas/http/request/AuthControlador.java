package com.aplicaciones13.placas.http.request;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.placas.http.request.body.LoginRequest;
import com.aplicaciones13.placas.http.request.body.TokenResponse;
import com.aplicaciones13.placas.jpa.queries.UsuarioRepositorio;
import com.aplicaciones13.placas.security.JwtUtils;
import com.aplicaciones13.placas.servicio.UserDetailsImpl;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Clase para consumir el servicio de autorizacion para los usuarios.
 * 
 * @author omargo33@gmail.com
 * @since 2023-10-11
 * 
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Tag(name = "auth", description = "Servicio para autenticar usuarios")
@RequestMapping("/auth")
public class AuthControlador extends ComonControlador{
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepositorio userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Metodo para autenticar usuarios.
     * 
     * @param loginRequest
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsuario(),
                        loginRequest.getContrasenia()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token =jwtUtils.generateTokenFromUsername(userDetails.getUsername(), userDetails.getAuthorities());
    
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setTokenAccess(token);

        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * Metodo para crear una clave.
     * 
     * @param clave
     * @return
     */
    @PostMapping("/{clave}")
    public ResponseEntity<?> crearClave(@PathVariable String clave) {
        String password = encoder.encode(clave); 
        return ResponseEntity.ok(password);
    }
}