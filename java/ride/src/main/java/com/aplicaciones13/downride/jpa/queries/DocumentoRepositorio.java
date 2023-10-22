package com.aplicaciones13.downride.jpa.queries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.aplicaciones13.downride.jpa.model.Documento;

/**
 * Clase para el repositorio Documento
 * 
 * @author omargo33@gmail.com
 * @version 2023-10-21
 */
public interface DocumentoRepositorio extends JpaRepository<Documento, Integer> {

    /**
     * Metodo para buscar por id_documentos cuyo estado es A
     */
    @Query(value = "SELECT id_documentos, comprobante, serie_comprobante, ruc, razon_social, fecha_emision, fecha_autorizacion, tipo, numero_documento_modificado, identificacion_receptor, clave_acceso, numero_autorizacion, importe_total, usuario_programa, id_rucs, estado FROM public.documentos WHERE id_documentos = ?1 AND estado = 'A'", nativeQuery = true)
    Documento findByIdDocumentos(Integer idDocumentos);

    /**
     * Metodo para buscar por idRucs y numeroAutorizacion cuyo estado es A
     *
     * @param idRucs
     * @param numeroAutorizacion
     * 
     */
    @Query("SELECT d FROM Documento d WHERE d.idRucs = ?1 AND d.numeroAutorizacion = ?2 AND d.estado = 'A'")
    Documento findByIdRucsAndNumeroAutorizacion(Integer idRucs, String numeroAutorizacion);

    /**
     * Metodo para actualizar estado buscando por numeroAutorizacion y dar un
     * borrado al sistema.
     * 
     * @param idRucs
     * @param numeroAutorizacion
     * 
     */
    @Query(value = "UPDATE public.documentos SET estado = 'X' WHERE numero_autorizacion = ?1 AND id_rucs = ?2", nativeQuery = true)
    void updateEstado(Integer idRucs, String numeroAutorizacion);

    /**
     * Metodo para insertar un documento directamente con codigo nativo.
     * 
     */
    @Modifying
    @Query(value = "INSERT INTO public.documentos ( comprobante, serie_comprobante, ruc, razon_social, fecha_emision, fecha_autorizacion, tipo, numero_documento_modificado, identificacion_receptor, clave_acceso, numero_autorizacion, importe_total, usuario_programa, id_rucs, estado) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", nativeQuery = true)
    void insertDocumento(String comprobante, String serieComprobante, String ruc, String razonSocial,
            String fechaEmision, String fechaAutorizacion, String tipo, String numeroDocumentoModificado,
            String identificacionReceptor, String claveAcceso, String numeroAutorizacion, String importeTotal,
            String usuarioPrograma, Integer idRucs, String estado);
}