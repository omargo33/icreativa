package com.aplicaciones13.downride.servicio;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import com.aplicaciones13.downride.jpa.model.Documento;
import com.aplicaciones13.downride.jpa.model.Ruc;
import com.aplicaciones13.downride.jpa.queries.DocumentoRepositorio;
import com.aplicaciones13.downride.jpa.queries.RucRepositorio;
import com.aplicaciones13.downride.utilidad.Archivo;

import lombok.RequiredArgsConstructor;

/**
 * Clase para el servicio RUC
 * 
 * @author omargo33@gmail.com
 * @version 2023-10-21
 *
 */
@Service
@Transactional
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
     * Metodo para actualizar fecha inicio con un metodo nativco de postgresql
     *
     * @param idRucs
     * @param fechaInicio
     */
    public void updateFechaInicio(Integer idRucs, String fechaInicio) {
        rucRepositorio.updateFechaInicio(idRucs, fechaInicio);
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
        ruc.setEstado("A");
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
        Archivo archivo = new Archivo() {
            @Override
            public void leerFilaCVS(String linea) {
                String[] campos = linea.split("\t");
                Documento documento = documentoRepositorio.findByIdRucsAndNumeroAutorizacion(idRucs, campos[10]);
                if (documento != null) {
                    documentoRepositorio.updateEstado(idRucs, campos[10]);
                }

                documentoRepositorio.insertDocumento(
                        campos[0], // comprobante
                        campos[1], // serie_comprobante
                        campos[2], // ruc
                        campos[3], // razon_social
                        campos[4], // fecha_emision
                        campos[5], // fecha_autorizacion
                        campos[6], // tipo
                        campos[7], // numero_documento_modificado
                        campos[8], // identificacion_receptor
                        campos[9], // clave_acceso
                        campos[10], // numero_autorizacion
                        campos[11], // importe_total
                        programaUsuario,
                        idRucs,
                        "A");
                // TODO
                // crear un mapa con las fechas para ingresar los dias. si el archivo viene de
                // un mes se tien que proporcionar anio y ultimo dia del mes
            }
        };
        archivo.setPathFile(pathFile);
        archivo.leerAchivos();
        archivo.borrarArchivo();
    }
}
