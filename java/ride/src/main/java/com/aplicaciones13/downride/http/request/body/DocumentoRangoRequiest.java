package com.aplicaciones13.downride.http.request.body;

import java.util.Date;


import lombok.Data;
import lombok.ToString;


/**
 * Clase para estructura de consulta.
 *
 * @author omargo33@hotmail.com
 * @since 2023-10-12
 */
@Data
@ToString
public class DocumentoRangoRequiest {
    private String ruc;

    private Date fechaInicio;

    private Date fechaFin;
}
