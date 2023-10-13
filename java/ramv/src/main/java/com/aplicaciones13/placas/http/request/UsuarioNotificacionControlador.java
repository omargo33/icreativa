package com.aplicaciones13.placas.http.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.placas.http.request.body.UsuarioNotificacionDeleteRequest;
import com.aplicaciones13.placas.http.request.body.UsuarioNotificacionRequest;
import com.aplicaciones13.placas.http.request.body.UsuarioNotificacionResponse;
import com.aplicaciones13.placas.jpa.model.Correo;
import com.aplicaciones13.placas.jpa.model.Placa;
import com.aplicaciones13.placas.security.JwtUtils;
import com.aplicaciones13.placas.servicio.CorreoServicio;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Clase para consumir el servicio de usuarios notificacion.
 * 
 * @author omargo33@gmail.com
 * @since 2023-10-11
 * 
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Tag(name = "usuario-notificacion", description = "Servicio para Agregar, Eliminar y Listar correos para notificar usuarios")
@RequestMapping("/usuario-notificacion")
public class UsuarioNotificacionControlador extends ComonControlador {

    @Autowired
    private CorreoServicio correoServicio;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Metodo para obtener los usuarios notificacion.
     * 
     * @param request
     * @return ResponseEntity
     */
    @GetMapping("/")
    public ResponseEntity<?> getUsuariosNotificacion(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromHeader(request);
        String userName = jwtUtils.getUserNameFromJwtToken(token);

        List<Correo> listCorreos = correoServicio.getUsuariosNotificacion(userName);
        List<UsuarioNotificacionResponse> listadoCorreos = new ArrayList<UsuarioNotificacionResponse>();

        for (Correo correo : listCorreos) {
            UsuarioNotificacionResponse usuarioNotificacionResponse = new UsuarioNotificacionResponse();

            usuarioNotificacionResponse.setId(String.valueOf(correo.getIdCorreos()));
            usuarioNotificacionResponse.setCorreo(correo.getCorreo());

            listadoCorreos.add(usuarioNotificacionResponse);
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("listadoCorreos", listadoCorreos);
        return ResponseEntity.ok(response);
    }

    /**
     * Metodo para crear un correo usuario de notificacion.
     * 
     * @param request
     * @param usuarioNotificacionRequest
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<?> postUsuariosNotificacion(HttpServletRequest request,
            @Valid @RequestBody UsuarioNotificacionRequest usuarioNotificacionRequest) {
        String token = jwtUtils.getJwtFromHeader(request);
        String userName = jwtUtils.getUserNameFromJwtToken(token);
        String ipAplicacion = getIpClient(request);

        if (!correoServicio.existsByCorreoAndUsuario(usuarioNotificacionRequest.getCorreo(), userName)) {
            correoServicio.crearUsuarioNotificacion(usuarioNotificacionRequest.getCorreo(), userName, ipAplicacion);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Metodo para eliminar un correo usuario de notificacion.
     * 
     * @param request
     * @param usuarioNotificacionDeleteRequest
     * @return
     */
    @DeleteMapping("/")
    public ResponseEntity<?> deleteUsuariosNotificacion(HttpServletRequest request,
            @Valid @RequestBody UsuarioNotificacionDeleteRequest usuarioNotificacionDeleteRequest) {

        Integer id = new Integer(usuarioNotificacionDeleteRequest.getId());
        correoServicio.eliminarUsuarioNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
