package com.aplicaciones13.downride.jpa.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase para expresar en jpa el select SELECT id_parametro, texto_1, texto_2,
 * valor_1, valor_2, descripcion FROM public.parametros;
 * 
 * Con el indice serial de postgresql en el campo id_parametro
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-13
 * 
 * 
 */
@Entity
@Table(name = "parametros", schema = "public")
@Getter
@Setter
@ToString
public class Parametro implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_parametros")
    private Integer idParametros;

    @Column(name = "texto_1", length = 512)
    private String texto1;

    @Column(name = "texto_2", length = 512)
    private String texto2;

    @Column(name = "valor_1")
    private BigDecimal valor1;

    @Column(name = "valor_2")
    private BigDecimal valor2;

    @NotBlank
    @Column(name = "descripcion", length = 256)
    private String descripcion;
}