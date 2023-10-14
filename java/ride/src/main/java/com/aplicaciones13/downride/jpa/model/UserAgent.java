package com.aplicaciones13.downride.jpa.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase para expresar en jpa el select SELECT id_user_agent, descripcion, ejecucion_correcta, ejecucion_incorrecta, id_web_client FROM public.user_agent;
 * 
 * Con el indice serial de postgresql en el campo id_user_agent
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-31
 * 
 * 
 */
@Entity
@Table(name = "user_agent", schema = "public")
@Getter
@Setter
@ToString
public class UserAgent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_agent")
    private Integer idUserAgent;

    @NotBlank
    @Column(name = "descripcion", length = 1024)
    private String descripcion;

    @Column(name = "ejecucion_correcta")
    private int ejecucionCorrecta;

    @Column(name = "ejecucion_incorrecta")
    private int ejecucionIncorrecta;

    @Column(name = "id_web_client")
    private Integer idWebClient;
}
