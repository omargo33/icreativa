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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase para expresar en jpa el SELECT id_dias, dia, id_rucs, usuario_fecha FROM public.dias;
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
 @ToString
public class Dia  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dias")
    private Integer idDias;

    @Column(name = "dia")
    @Temporal(TemporalType.DATE)
    private Date diaConsultado;

    @Column(name = "id_rucs")
    private Integer idRucs;

    @Column(name = "usuario_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usuarioFecha;
}
