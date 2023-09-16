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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase para expresar en jpa el SELECT id_placas_envetos, descripcion, estado, fecha, id_placas FROM public.placas_eventos;
 * 
 * Con el indice serial de postgresql en el campo id_placas_envetos
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-13
 * 
 * 
 */
@Entity
@Table(name = "placas_eventos", schema = "public")
@Getter
@Setter
@ToString
public class PlacaEvento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_placas_eventos")
    private Integer idPlacasEnvetos;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha")    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "id_placas")
    private Integer idPlacas;
}
