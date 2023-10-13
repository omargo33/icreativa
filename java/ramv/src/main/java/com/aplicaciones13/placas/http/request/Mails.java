package com.aplicaciones13.placas.http.request;

import com.aplicaciones13.placas.http.request.body.CorreoResponse;
import com.aplicaciones13.placas.security.JwtUtils;
import com.aplicaciones13.placas.servicio.CorreoServicio;

import io.swagger.v3.oas.annotations.Hidden;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Clase para consumir el servicio de correos, para testear
 * 
 * @author omargo33@gmail.com
 * @since 2023-10-11
 * 
 */
@Hidden
@RestController
@RequestMapping(value = "/v1/mails")
public class Mails {

    @Autowired
    private CorreoServicio mailControlServicio;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Metodo para enviar correos de prueba de configuracion.
     * 
     * @param mail
     * @return
     */
    @PostMapping("/{mail}")
    public ResponseEntity<CorreoResponse> sendMail(HttpServletRequest request, @PathVariable("mail") String mail) {
        String token = jwtUtils.getJwtFromHeader(request);
        String userName = jwtUtils.getUserNameFromJwtToken(token);

        CorreoResponse correoResponse = new CorreoResponse();
        correoResponse.setRespuesta(mailControlServicio.enviarTest(mail, userName));

        return ResponseEntity.ok(correoResponse);
    }
}
