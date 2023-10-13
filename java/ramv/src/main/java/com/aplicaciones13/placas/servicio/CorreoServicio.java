package com.aplicaciones13.placas.servicio;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicaciones13.placas.cliente.EnviarCorreo;
import com.aplicaciones13.placas.jpa.model.Correo;
import com.aplicaciones13.placas.jpa.model.Parametro;
import com.aplicaciones13.placas.jpa.queries.CorreoRepositorio;

import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Clase para enviar correos de prueba de configuracion.
 * 
 * @autor: omargo33@gmail.com
 * @since: 2023-10-09
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CorreoServicio {

    @Autowired
    private ParametroServicio parametroServicio;

    @Autowired
    private PlacaEventoServicio placaEventoServicio;

    @Autowired
    private CorreoRepositorio correoRepositorio;


    /**
     * Metodo para obtener los correos de los usuarios.
     * 
     * @param userName
     * @return
     */
    public List<Correo> getUsuariosNotificacion(String userName) {
        return correoRepositorio.findByUsuario(userName);
    }

    /**
     * Metodo para crear un usuario de notificacion.
     *  
     * @param correo
     * @param userName
     * @param usuarioPrograma
     */
    public void crearUsuarioNotificacion(String correo, String userName, String usuarioPrograma) {
        Correo correoUsuario = new Correo();
        correoUsuario.setCorreo(correo);
        correoUsuario.setUsuario(userName);
        correoUsuario.setUsuarioFecha(new Date());
        correoUsuario.setUsuarioPrograma(usuarioPrograma);
        correoRepositorio.save(correoUsuario);
    }

    /**
     * Metodo para eliminar un usuario de notificacion.
     * 
     * @param correo
     * @param userName
     * @param usuarioPrograma
     */
    public void eliminarUsuarioNotificacion(int idCorreos) {
        Correo correoUsuario = correoRepositorio.findByIdCorreos(idCorreos);
        correoRepositorio.delete(correoUsuario);
    }

    /**
     * Metodo para si existe un usuario de notificacion.
     * 
     * @param correo
     * @param userName
     */
    public boolean existsByCorreoAndUsuario(String correo, String userName) {
        return correoRepositorio.existsByCorreoAndUsuario(correo, userName);
    }

    /**
     * Metodo para enviar correos de placas encontradas.
     * 
     * @param idPlaca
     * @param placa
     * @param novedad
     * @param userName
     */
    public void enviarCorreoEncontrado(int idPlaca, String placa, String novedad, String userName, String propiedades) {
        String estado = "FB";
        List<com.aplicaciones13.placas.jpa.model.Correo> listaEmail = correoRepositorio.findByUsuario(userName);

        if (listaEmail.isEmpty()) {
            placaEventoServicio.crearPlacaEvento(idPlaca, "Correos no disponibles", estado);
            return;
        }

        for (com.aplicaciones13.placas.jpa.model.Correo email : listaEmail) {
            EnviarCorreo enviarCorreo = new EnviarCorreo();
            enviarCorreo.setPropiedades(propiedades);
            enviarCorreo.setCorreo(email.getCorreo());
            enviarCorreo.setAsunto("Placa " + placa + " Encontrada");
            enviarCorreo.setCuerpo("Estimad@s <br/><br/>" +
                    "Su placa " + placa + " tiene la siguietne novedad:<br/><br/> " + novedad
                    + "<br/><br/>Atentamente\n");
            if (enviarCorreo.enviarCorreo()) {
                estado = "F";
            } else {
                estado = "FB";
            }

            placaEventoServicio.crearPlacaEvento(idPlaca, email.getCorreo() + " " + enviarCorreo.getDescripcionEstado(),
                    estado);
        }
    }

    /**
     * Metodo para enviar correos de placas antiguas.
     * 
     * @param idPlaca
     * @param placa
     * @param novedad
     * @param userName
     */
    public void enviarCorreoAntiguas(int idPlaca, String placa, String userName, String propiedades) {
        String estado = "FB";
        List<com.aplicaciones13.placas.jpa.model.Correo> listaEmail = correoRepositorio.findByUsuario(userName);

        if (listaEmail.isEmpty()) {
            placaEventoServicio.crearPlacaEvento(idPlaca, "Correos no disponibles", estado);
            return;
        }

        for (com.aplicaciones13.placas.jpa.model.Correo email : listaEmail) {
            EnviarCorreo enviarCorreo = new EnviarCorreo();
            enviarCorreo.setPropiedades(propiedades);
            enviarCorreo.setCorreo(email.getCorreo());
            enviarCorreo.setAsunto("Placa " + placa + " Descartada por Antiguedad en el sistema");
            enviarCorreo.setCuerpo("Estimad@s <br/><br/>Su placa " + placa
                    + " tiene la siguiente novedad:<br/><br/>Ha sido Descartada por Antiguedad en el sistema<br/><br/>Atentamente<br/>");
            if (enviarCorreo.enviarCorreo()) {
                estado = "F";
            } else {
                estado = "FB";
            }

            placaEventoServicio.crearPlacaEvento(idPlaca, email.getCorreo() + " " + enviarCorreo.getDescripcionEstado(),
                    estado);
        }
    }

    /**
     * Metodo para enviar correos de prueba de configuracion.
     * 
     * @param correo
     * @param userName
     * @return
     */
    public String enviarTest(String correo, String userName) {
        Parametro parametro = parametroServicio.findByIdParametros(6);

        EnviarCorreo enviarCorreo = new EnviarCorreo();
        enviarCorreo.setPropiedades(parametro.getTexto1() + " " + parametro.getTexto2());

        enviarCorreo.setCorreo(correo);
        enviarCorreo.setAsunto("Configuracion de Correo");
        enviarCorreo.setCuerpo(
                "Estimad@" + userName
                        + ", personal de iCreativa<br/><br/>La configuracion ha sido satisfactoria<br/><br/>Atentamente<br/>");

        enviarCorreo.enviarCorreo();
        return enviarCorreo.getDescripcionEstado();
    }
}
