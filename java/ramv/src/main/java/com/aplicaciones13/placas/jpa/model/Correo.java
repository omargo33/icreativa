package com.aplicaciones13.placas.jpa.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase para expresar en jpa el SELECT id_correos, correo, usuario,
 * usuario_fecha, usuario_programa FROM public.correos;
 * 
 * Con el indice serial de postgresql en el campo id_correos
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-13
 * 
 */
@Entity
@Table(name = "correos", schema = "public")
@Getter
@Setter
@ToString
@JsonIgnoreProperties({ "usuarioFecha" })
public class Correo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_correos")
    private Integer idCorreos;

    @NotBlank
    @Column(name = "correo", length = 128)
    private String correo;

    @NotBlank
    @Column(name = "usuario", length = 32)
    private String usuario;

    @Column(name = "usuario_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usuarioFecha;

    @NotBlank
    @Column(name = "usuario_programa", length = 128)
    private String usuarioPrograma;
}
