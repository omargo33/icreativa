package com.aplicaciones13.placas.cliente.utilidades;

/**
 * Clase para funciones de utilidad. en las creaciones de placas.
 * 
 * @author: omargo33@gmail.com
 * @since: 2023-08-15
 */
public class Generador {
    /**
     * Metodo para generar milisegundos aleatorios entre dos rangos
     */
    public static int generarMilisegundosAleatorios(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    /**
     * Metodo para generar espera aleatoria entre dos rangos
     */
    public static void generarEsperaAleatoria(int min, int max) {
        try {
            Thread.sleep(generarMilisegundosAleatorios(min, max));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}