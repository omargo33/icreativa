package com.aplicaciones13.downride.jpa.queries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.aplicaciones13.downride.jpa.model.UserAgent;

/**
 * Repositorio para la tabla user_agent
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-31
 * 
 */
public interface UserAgentRepositorio extends JpaRepository<UserAgent, Integer> {

    /**
     * Metodo para buscar el maximo id de la tabla
     */
    UserAgent findTopByOrderByIdUserAgentDesc();

    /**
     * Metodo para buscar por id_user_agent
     */
    UserAgent findByIdUserAgent(Integer id);

    /**
     * Metodo incrementar el contador de ejecucion_correcta con un query nativo
     *
     */
    @Modifying
    @Query(value = "UPDATE public.user_agent SET ejecucion_correcta = ejecucion_correcta + 1 WHERE id_user_agent = :idUserAgent", nativeQuery = true)
    void incrementarEjecucionCorrecta(Integer idUserAgent);
    
    /**
     * Metodo incrementar el contador de ejecucion_incorrecta con un query nativo
     *
     */
    @Modifying
    @Query(value = "UPDATE public.user_agent SET ejecucion_incorrecta = ejecucion_incorrecta + 1 WHERE id_user_agent = :idUserAgent", nativeQuery = true)
    void incrementarEjecucionIncorrecta(Integer idUserAgent);

    
}
