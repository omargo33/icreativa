package com.aplicaciones13.downride.utilidad;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase para leer archivos
 * 
 * @author omargo33@gmail.com
 * @version 2023-10-21
 * 
 */
@Getter
@Setter
@Slf4j
public class Archivo {
    private String error;
    private String pathFile;

    /**
     * 
     * Leer archivo
     * codigo para leer un archivo y imprimir cada linea
     * 
     */
    public void leerAchivos() {
        try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                leerFilaCVS(linea);
            }
        } catch (FileNotFoundException e) {
            error = "Archivo no encontrado: " + e.getMessage();
        } catch (IOException e) {
            error = "Error al leer el archivo: " + e.getMessage();
        }
    }

    /**
     * Metodo para leer una fila en CVS
     * 
     * @param linea
     */
    public void leerFilaCVS(String linea) {
        String[] campos = linea.split("\t");
        for (String campo : campos) {
            log.info(campo);
        }
    }

    /**
     * Metodo para borrar un archivo
     * 
     * @param pathFile
     */
    public void borrarArchivo() {
        Path path = Paths.get(pathFile);
        try {
            Files.delete(path);
        } catch (IOException e) {
            error = "Error al borrar el archivo: " + e.getMessage();
        }
    }
}
