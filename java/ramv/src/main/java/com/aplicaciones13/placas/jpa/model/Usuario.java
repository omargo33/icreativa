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
 * Clase para expresar en jpa el SELECT id_usuario, usuario, contrasenia,
 * usuario_fecha, usuario_programa, estado, rol
 * FROM public.usuario;
 * 
 * Con el indice serial de postgresql en el campo id_usuario
 * 
 * @author omargo33@gmail.com
 * @since 2023-10-07
 * 
 */
@Entity
@Table(name = "usuario", schema = "public")
@Getter
@Setter
@ToString
@JsonIgnoreProperties({ "id_usuario", "usuarioFecha", "usuario_programa" })
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank
    @Column(name = "usuario", length = 128)
    private String usuario;

    @NotBlank
    @Column(name = "contrasenia", length = 256)
    private String contrasenia;

    @Column(name = "usuario_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usuarioFecha;

    @NotBlank
    @Column(name = "usuario_programa", length = 128)
    private String usuarioPrograma;

    @NotBlank
    @Column(name = "estado", length = 8)
    private String estado;

    @NotBlank
    @Column(name = "rol", length = 64)
    private String rol;
} 
