package com.aplicaciones13.downride.jpa.model;

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
 * Clase para expresar en jpa el SELECT id_dias, id_rucs, usuario_fecha, dia,
 * mes, anio, estado, observacion FROM public.dias;
 * 
 * Con el indice serial de postgresql en el campo id_dias
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-13
 * 
 */
@Entity
@Table(name = "dias", schema = "public")
@Getter
@Setter
@JsonIgnoreProperties({ "idDias", "usuarioFecha" })
@ToString
public class Dia implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dias")
    private Integer idDias;

    @Column(name = "id_rucs")
    private Integer idRucs;

    @Column(name = "usuario_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usuarioFecha;

    @NotBlank
    @Column(name = "dia", length = 8)
    private String dia;

    @NotBlank
    @Column(name = "mes", length = 16)
    private String mes;

    @NotBlank
    @Column(name = "anio", length = 4)
    private String anio;

    @Column(name = "estado", length = 4)
    private String estado;

    @Column(name = "observacion", length = 256)
    private String observacion;
}
