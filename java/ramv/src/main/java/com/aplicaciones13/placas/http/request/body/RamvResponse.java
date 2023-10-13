package com.aplicaciones13.placas.http.request.body;

import lombok.Data;

/**
 * Metodo para obtener la informacion de la placa ingresada y su estado
 * 
 * @author omargo33@gmail.com
 * @since 2023-10-12
 */
@Data
public class RamvResponse {
    String indentificacion;
    String estado;
    String fechaSolicitud;
    String fechaRespuesta;    
}
