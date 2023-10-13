package com.aplicaciones13.placas.http.request.body;

import lombok.Data;

/**
 * Clase para manejar los correos de un usuario.
 * 
 * @autor: omargo33@gmail.com
 * @since: 2023-10-11
 */
@Data
public class UsuarioNotificacionResponse {
    String id;
    String correo;    
}
