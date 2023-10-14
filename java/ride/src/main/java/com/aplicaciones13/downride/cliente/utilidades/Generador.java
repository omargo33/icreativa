package com.aplicaciones13.downride.cliente.utilidades;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase para funciones de utilidad. en las creaciones de placas.
 * 
 * @author: omargo33@gmail.com
 * @since: 2023-08-15
 */

@Slf4j
public class Generador {

    /**
     * Constructor privado para evitar instancias
     */
    private Generador() {
        super();
    }

    /**
     * Metodo para generar milisegundos aleatorios entre dos rangos
     */
    public static int generarMilisegundosAleatorios(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    /**
     * Metodo para generar espera aleatoria entre dos rangos
     */
    public static void generarEsperaAleatoria(int min, int max) {
        try {
            Thread.sleep(generarMilisegundosAleatorios(min, max));
        } catch (InterruptedException e) {
            log.error("Error en la espera aleatoria: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}