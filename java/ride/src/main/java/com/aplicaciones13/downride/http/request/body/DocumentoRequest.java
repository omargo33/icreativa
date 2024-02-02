package com.aplicaciones13.downride.http.request.body;

import lombok.Data;
import java.util.Date;

/**
 * Metodo para obtener los documentos autorizados
 * 
 * @author omargo33@gmail.com
 * @since 2023-10-12
 * 
 */
@Data
public class DocumentoRequest {
    private String ruc;

    private Date fecha;
}
