package com.aplicaciones13.placas.jpa.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Temporal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase para expresar en jpa el SELECT id_placas, placa, cliente, usuario,
 * usuario_fecha, usuario_programa, estado, cliente_nombre, cliente_correo FROM
 * public.placas;
 * 
 * Con el indice serial de postgresql en el campo id_placas
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-13
 * 
 */
@Entity
@Table(name = "placas", schema = "public")
@Getter
@Setter
@ToString
@JsonIgnoreProperties({ "usuarioFecha" })
public class Placa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_placas")
    private Integer idPlacas;

    @NotBlank
    @Column(name = "placa", length = 32)
    private String ramvPlaca;

    @NotBlank
    @Column(name = "cliente", length = 16)
    private String cliente;

    @NotBlank
    @Column(name = "usuario", length = 32)
    private String usuario;

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
    @Column(name = "cliente_nombre", length = 128)
    private String clienteNombre;

    @NotBlank
    @Column(name = "cliente_correo", length = 128)
    private String clienteCorreo;
}
