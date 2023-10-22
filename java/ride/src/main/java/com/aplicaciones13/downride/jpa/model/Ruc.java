package com.aplicaciones13.downride.jpa.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase para expresar en jpa el select SELECT id_rucs, ruc, contrasena, estado,
 * usuario_fecha, usuario_programa, fecha_inicio FROM public.rucs;
 * 
 * Con el indice serial de postgresql en el campo id_rucs
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-13
 * 
 */
@Entity
@Table(name = "rucs", schema = "public", indexes = {
    @Index(name="idx_ruc", columnList = "ruc"),
})
@Getter
@Setter
@ToString
@JsonIgnoreProperties({ "usuarioFecha", "usuarioPrograma" })
public class Ruc implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rucs")
    private Integer idRucs;

    @NotBlank
    @Column(name = "ruc", length = 16)
    private String noRuc;

    @NotBlank
    @Column(name = "contrasena", length = 128)
    private String contrasena;

    @Column(name = "usuario_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usuarioFecha;

    @Column(name = "usuario_programa", length = 128)
    private String usuarioPrograma;

    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaIncio;

    @Column(name = "estado", length = 2)
    private String estado;    

}