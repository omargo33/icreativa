package com.aplicaciones13.downride.servicio;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aplicaciones13.downride.cliente.utilidades.Archivo;
import com.aplicaciones13.downride.jpa.model.Documento;
import com.aplicaciones13.downride.jpa.model.Ruc;
import com.aplicaciones13.downride.jpa.queries.DocumentoRepositorio;
import com.aplicaciones13.downride.jpa.queries.RucRepositorio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase para el servicio RUC
 * 
 * @author omargo33@gmail.com
 * @version 2023-10-21
 *
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RucServicio {

    @Autowired
    private final RucRepositorio rucRepositorio;

    @Autowired
    private final DocumentoRepositorio documentoRepositorio;

    /**
     * Metodo para buscar todos los rucs, pero que estado sea A
     */
    public List<Ruc> getRucs() {
        return rucRepositorio.findAll();
    }

    /**
     * Metodo para buscar todos los rucs, pero que estado sea P para validar el login
     */
    public void validarLogin() {
        List<Ruc> rucs = rucRepositorio.findAllPendientes();
        for (Ruc ruc : rucs) {
            
            
            
            ruc.setEstado("A");
            rucRepositorio.save(ruc);
        }
    }

    /**
     * Metodo para buscar por id_rucs
     * 
     * @param idRucs
     * 
     */
    public Ruc findByIdRucs(Integer idRucs) {
        return rucRepositorio.findByIdRucs(idRucs);
    }

    /**
     * Metodo para buscar por ruc y que estado sea A
     * 
     * @param ruc
     */
    public Ruc findByRuc(String ruc) {
        return rucRepositorio.findByRuc(ruc);
    }

    /**
     * Metodo para actualizar la contrasena con un metodo nativo de postgresql
     *
     * @param idRucs
     * @param constrasena
     */
    public void updateContrasena(Integer idRucs, String contrasena) {
        rucRepositorio.updateContrasena(idRucs, contrasena);
    }

    /**
     * Metodo para crear un ruc
     * 
     * @param ruc
     * @param programaUsuario
     */
    public Ruc createRuc(Ruc ruc, String programaUsuario) {
        Ruc rucbusqueda = rucRepositorio.findByRuc(ruc.getNoRuc());
        if (rucbusqueda != null) {
            ruc.setIdRucs(rucbusqueda.getIdRucs());
        }

        ruc.setUsuarioFecha(new Date());
        ruc.setEstado("P");
        ruc.setUsuarioPrograma(programaUsuario);
        return rucRepositorio.save(ruc);
    }

    /**
     * Metodo para borrar un ruc
     * 
     * @param rucIdentificacion
     */
    public void deleteRuc(String rucIdentificacion) {
        Ruc ruc = rucRepositorio.findByRuc(rucIdentificacion);
        ruc.setEstado("X");
        rucRepositorio.save(ruc);
    }



    /**
     * Metodo para cargar los documentos
     * 
     * @param idRucs
     * @param pathFile
     * @param programaUsuario
     */
    public void cargarDocumentos(int idRucs, String pathFile, String programaUsuario) {

        Map<String, String> mapa = new HashMap<>();

        Archivo archivo = new Archivo() {
            @Override
            public void leerFilaCVS(String linea) {
                String[] campos = linea.split("\t");

                if (campos[0].equals("COMPROBANTE")) {
                    return;
                }

                Documento documento = documentoRepositorio.findByIdRucsAndNumeroAutorizacion(idRucs, campos[10]);
                if (documento != null) {
                    documentoRepositorio.updateEstado(idRucs, campos[10]);
                }

                documentoRepositorio.insertDocumento(
                        campos[0], // comprobante
                        campos[1], // serie_comprobante
                        campos[2], // ruc
                        campos[3], // razon_social
                        convertirStringSimple(campos[4]), // fecha_emision
                        convertirStringFull(campos[5]), // fecha_autorizacion
                        campos[6], // tipo
                        campos[7], // numero_documento_modificado
                        campos[8], // identificacion_receptor
                        campos[9], // clave_acceso
                        campos[10], // numero_autorizacion
                        convertirStringDouble((campos.length > 11) ? campos[11] : "0"), // importe_total
                        programaUsuario,
                        idRucs,
                        "A");
                mapa.put(campos[5].substring(0, 10), campos[5]);
            }
        };
        archivo.setPathFile(pathFile);
        archivo.leerAchivos();
        log.error(archivo.getError());
    }
    

    /**
     * Metodo para convertir un string a fecha
     * 
     * @param fechaString
     * @return
     */
    private Date convertirStringFull(String fechaString) {
        Date fechaJava = new Date();
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            fechaJava = formato.parse(fechaString);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }

        return fechaJava;
    }

    private double convertirStringDouble(String importeTotal) {
        double importe = 0;
        try {
            importe = Double.parseDouble(importeTotal);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
        return importe;
    }

    /**
     * Metodo para convertir un string a fecha
     * 
     * @param fechaString
     * @return
     */
    private Date convertirStringSimple(String fechaString) {
        Date fechaJava = new Date();

        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            fechaJava = formato.parse(fechaString);
        } catch (ParseException e1) {
            log.error(e1.getMessage());
        }

        return fechaJava;
    }
}
