package com.aplicaciones13.placas.servicio;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicaciones13.placas.cliente.utilidades.Generador;
import com.aplicaciones13.placas.jpa.model.UserAgent;
import com.aplicaciones13.placas.jpa.queries.UserAgentRepositorio;

import lombok.RequiredArgsConstructor;

/**
 * Clase para el servicio userAgent
 * 
 * @author omargo33@gmail.com
 * @version 2023-08-20
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserAgentServicio {

    @Autowired
    private final UserAgentRepositorio userAgentRepositorio;

    /**
     * Metodo para buscar un user agent por id.
     * 
     * @param idUserAgent
     * @return
     */
    public UserAgent findByIdUserAgent(int idUserAgent) {
        return userAgentRepositorio.findByIdUserAgent(idUserAgent);
    }

    public List<UserAgent> findByAll() {
        return userAgentRepositorio.findAll();
    }

    /**
     * Metodo para buscar una descripcion user agent random.
     * 
     * @return
     */
    public UserAgent buscarDescripcionRandom(){
        UserAgent  userAgent =userAgentRepositorio.findTopByOrderByIdUserAgentDesc();

        int idUserAgent = Generador.generarMilisegundosAleatorios(1, userAgent.getIdUserAgent());
        return userAgentRepositorio.findByIdUserAgent(idUserAgent);
    }

    /**
     * Metodo para crear un user agent.
     * 
     * @param userAgent
     * @return
     */
    public UserAgent crearUserAgent(UserAgent userAgent) {
        return userAgentRepositorio.save(userAgent);
    }

    /**
     * Metodo para incrementar el contador de ejecucion_correcta.
     * 
     * @param idUserAgent
     */
    public void incrementarEjecucionCorrecta(Integer idUserAgent) {
        userAgentRepositorio.incrementarEjecucionCorrecta(idUserAgent);
    }

    /**
     * Metodo para incrementar el contador de ejecucion_incorrecta.
     * 
     * @param idUserAgent
     */
    public void incrementarEjecucionIncorrecta(Integer idUserAgent) {
        userAgentRepositorio.incrementarEjecucionIncorrecta(idUserAgent);
    }

    /**
     * Metodo para borrar un user agent.
     * 
     * @param idUserAgent
     */
    public void borrarUserAgent(Integer idUserAgent) {
        userAgentRepositorio.deleteById(idUserAgent);
    }
}
