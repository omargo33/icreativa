package com.aplicaciones13.downride.jpa.model;

import java.io.Serializable;
import java.math.BigDecimal;
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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase para expresar en jpa el SELECT id_documentos, comprobante, serie_comprobante, ruc, razon_social, fecha_emision, fecha_autorizacion, tipo, numero_documento_modificado, identificacion_receptor, clave_acceso, numero_autorizacion, importe_total, descargado, ride, usuario_fecha, usuario_programa, id_rucs, estado FROM public.documentos;
 * 
 * Con el indice serial de postgresql en el campo id_documentos
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-13
 * 
 * 
 */
@Entity
@Table(name = "documentos", schema = "public")
@Getter
@Setter
@ToString
public class Documento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documentos")
    private Integer idDocumentos;

    @NotBlank
    @Column(name = "comprobante", length = 64)
    private String comprobante;

    @NotBlank
    @Column(name = "serie_comprobante", length = 32)
    private String serieComprobante;

    @NotBlank
    @Column(name = "ruc", length = 16)
    private String ruc;

    @NotBlank
    @Column(name = "razon_social", length = 512)
    private String razonSocial;

    @NotBlank
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;

    @NotBlank
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;

    @NotBlank
    @Column(name = "tipo", length = 16)
    private String tipo;

    @NotBlank
    @Column(name = "numero_documento_modificado", length = 64)
    private String numeroDocumentoModificado;

    @NotBlank
    @Column(name = "identificacion_receptor", length = 16)
    private String identificacionReceptor;

    @NotBlank
    @Column(name = "clave_acceso", length = 64)
    private String claveAcceso;

    @NotBlank
    @Column(name = "numero_autorizacion", length = 64)
    private String numeroAutorizacion;

    @NotBlank
    @Column(name = "importe_total")
    private BigDecimal importeTotal;

    @NotBlank
    @Column(name = "descargado", length = 2)
    private String descargado;

    @NotBlank
    @Column(name = "ride", length = 2)
    private String ride;

    @NotBlank
    @Column(name = "usuario_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usuarioFecha;

    @NotBlank
    @Column(name = "usuario_programa", length = 128)
    private String usuarioPrograma;

    @NotBlank
    @Column(name = "id_rucs")
    private Integer idRucs;   

    @NotBlank
    @Column(name = "estado", length = 2)
    private String estado;
}