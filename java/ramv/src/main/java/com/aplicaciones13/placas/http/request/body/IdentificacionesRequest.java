package com.aplicaciones13.placas.http.request.body;

import java.util.List;

import lombok.Data;

/**
 * Metodo para obtener la informacion de la placa
 * 
 * @author omargo33@gmail.com
 * @since 2023-10-12
 * 
 */
@Data
public class IdentificacionesRequest {
    private List<String> identificaciones; 
}
