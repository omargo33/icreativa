package com.aplicaciones13.downride.cliente;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

import com.aplicaciones13.downride.cliente.estructura.Respuesta;
import com.aplicaciones13.downride.cliente.utilidades.SolicitaRESTURL;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase para consumir el servicio de consulta de placas del SRI en linea.
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-20
 */
@Getter
@Setter
@Slf4j
public class ConsumoPlaca extends SolicitaRESTURL {

    private String placa = "";
    private String contexto = "";
    private Respuesta respuesta = new Respuesta();

    /**
     * Metodo para crear el objeto.
     */
    public ConsumoPlaca() {
        super();
        this.placa = "";
        this.contexto ="";
    }

    /**
     * Metodo para cargar el Json solicitado.
     * 
     * @param timeOut
     * @return
     */
    public boolean load(int timeOut) {
        setTimeOut(timeOut);
        return ejecutar();
    }

    /**
     * Metodo para sobrecargar la conexion.
     *
     * @return
     * @throws IOException
     */
    @Override
    public HttpURLConnection generarConexion() throws  IOException {
        URL url = new URL(getUrlConsulta());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        return connection;
    }

    /**
     * Metodo para ejecutar la consulta.
     *
     * @return
     */
    private boolean ejecutar() {
        setJsonConsulta(null);
        setUrlConsulta(getContexto() + this.placa);

        try {
            ejecutarConsultaWebService();
        } catch (Exception e) {
            log.error("No se pudo ejecutar el consumo por: {} {} {} {}",
                e.getMessage(),
                getHttpEstado(),
                getErrorRespuesta(),
                getJsonRespuesta()
                );
            
        }

        if (getHttpEstado() == 200) {
            return isRespuestaOrdenCompra(getJsonRespuesta());
        }

        if (getHttpEstado() == 204) {
            return false;
        }

        log.warn("El consumo tiene el siguiente estado: {}", getHttpEstado());
        return false;
    }

    /**
     * Metodo para conocer
     *
     * @param json
     * @return
     */
    private boolean isRespuestaOrdenCompra(String json) {
        respuesta = new Respuesta();
        respuesta.setMensaje("No encontrado");
        try {            
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            respuesta = objectMapper.readValue(json, Respuesta.class);
            return (respuesta.getMensaje().compareToIgnoreCase("El RAMV o CPN existe") == 0);
        } catch (JsonProcessingException ex) {
            log.error("isRespuestaOrdenCompra {}",  ex.getMessage());
        }
        return false;
    }
}