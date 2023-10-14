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
 * Clase para expresar en jpa el select SELECT id_web_client, version, path FROM public.web_client;
 * 
 * Con el indice serial de postgresql en el campo id_web_client
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-31
 * 
 * 
 */
@Entity
@Table(name = "web_client", schema = "public")
@Getter
@Setter
@ToString
public class WebClient implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_web_client")
    private Integer idWebClient;

    @NotBlank
    @Column(name = "version", length = 64)
    private String version;

    @NotBlank
    @Column(name = "path", length = 512)
    private String path;
}
