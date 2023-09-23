package com.aplicaciones13.placas.http.request;

import com.aplicaciones13.placas.cliente.EnviarCorreo;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.placas.jpa.model.Parametro;
import com.aplicaciones13.placas.servicio.ParametroServicio;


@RestController
@RequestMapping(value = "/v1/mail")
public class Correo {

    
    @GetMapping("/")
    public void sendMail() {
         EnviarCorreo enviarCorreo = new EnviarCorreo();
        
        enviarCorreo.setPropiedades(
                "13a.usuario=notificacionesramv@icreativa.com.ec "
                +"13a.clave=23Ttb0s&2K4b "
                +"13a.instancia.servidor=false "
                +"13a.acceso.ssl=true "
                +"mail.smtp.host=mail.icreativa.com.ec "
                +"mail.smtp.port=587 "
                +"mail.smtp.auth=true "
                +"mail.smtp.starttls.enable=true "
                //+"mail.smtp.ssl.protocols=TLSv1.2"+
                //+"mail.smtp.socketFactory.port=465"+
                +"mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory"
        );

        enviarCorreo.setCorreo("omargo33@hotmail.com");
        enviarCorreo.setAsunto("Placa -test-");
        enviarCorreo.setCuerpo("body");

        if (enviarCorreo.enviarCorreo()) {
            System.out.println("Correo enviado");
            
        } else {
            System.out.println("Correo no enviado " + enviarCorreo.getDescripcionEstado());
        }
    }

}
