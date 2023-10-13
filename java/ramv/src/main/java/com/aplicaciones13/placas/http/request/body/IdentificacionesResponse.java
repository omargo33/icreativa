package com.aplicaciones13.placas.http.request.body;

import lombok.Data;


/**
 * Metodo para obtener la informacion de la placa ingresada
 * 
 * @author omargo33@gmail.com
 * @since 2023-10-12
 * 
 */
@Data
public class IdentificacionesResponse {
    String indentificacio;
    String mensaje;
}
