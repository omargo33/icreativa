package com.aplicaciones13.placas.cliente.utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/** Objeto para ejecutar un archivo de log.
 *
 * @author omar velez - omargo33@JeremiasSoft.com
 *
 */
public class MainFiles {
    private static final String SLASH = System.getProperty("file.separator");
    private static final String USUARIO_PATH = System.getProperty("user.home");
    private static final String FECHA_NOMBRE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    //Path del archivo
    private String path;

    /** Creates new mainFiles
     * @param Path Path para el archivo de usuario para win c:user.txt
     */
    public MainFiles(String path) {
        this.path = path;
    }

    /** Metodo para crear el objeto.
     *
     */
    public MainFiles() {
        super();
    }

    /** Metodo para escribir un archivo de rastreo
     *
     * Solo llama a un metodo existente.
     *
     * @param palabras
     * @return
     */
    public static String escribirLogDefault(Object... palabras) {
        return escribirLogJS(true, palabras);
    }
    
    /** propiedad texto
     * recupera un archivo de texto a partir de su path
     * @return contenido de user.txt
     */
    public String getText() {
        try {
            File fichaEntrada = new File(path);
            FileInputStream canalEntrada = new FileInputStream(fichaEntrada);
            byte bt[] = new byte[(int) fichaEntrada.length()];
            canalEntrada.read(bt);
            canalEntrada.close();
            String cadena = new String(bt);
            return cadena;
        } catch (IOException e) {
            escribirLogDefault(this.getClass().getName(), ".getText()", e.toString());
            return "";
        }
    }

    /** propiedad texto
     * Guarda un archivo de texto a partir de su path
     * @param text cid a guardar
     */
    public void setText(String text) {
        try {
            byte b[] = text.getBytes();
            File fichaSalida = new File(path);
            FileOutputStream canalSalida = new FileOutputStream(fichaSalida);
            canalSalida.write(b);
            canalSalida.close();
        } catch (IOException e) {
            escribirLogDefault(this.getClass().getName(), ".setText()", e.toString());
        }
    }

    /** Get texto de forma estandar
     *
     */
    public String toString() {
        return getText();
    }

    /** Metodo para escribir un archivo de rastreo.
     *
     * Puede escribir sobre un servidor windows o un servidor Linux/AIX, no probado en AS400
     *
     * @param interruptor si esta en 0 no escribe
     * @param palabras mensaje a escribir
     */
    public static String escribirLogJS(boolean interruptor, Object... palabras) {
        if (interruptor)
            try {
                FileWriter fileWriter = new FileWriter(USUARIO_PATH + SLASH + FECHA_NOMBRE + "-JS.log", true);
                fileWriter.write(addSt(palabras) + "\n");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
                    .log(Level.SEVERE, addSt("Error al escribir logger:", e.toString(), " Archivo temporal"));
                return "ko";
            }
        return "ok";
    }

    /** Metodo para aglomerar los datos.
     *
     * @param palabras
     * @return
     */
    public static String addSt(Object... palabras) {
        if (palabras == null)
            return "null";

        int iMax = palabras.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0;; i++) {
            b.append(String.valueOf(palabras[i]));

            if (i == iMax)
                return b.toString();
            b.append("\t");
        }
    }
}
