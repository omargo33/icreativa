package com.aplicaciones13.placas.cliente;

import com.sun.mail.util.MailSSLSocketFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Objeto para enviar un correo de manera personalizada con las propiedades
 * como:
 *
 * 13a.usuario=test.portal@jardinazuayo.fin.ec
 * 13a.clave=Testportal.2022
 * 13a.instancia.servidor=false
 * 13a.acceso.ssl=true
 * mail.smtp.host=smt
 * mail.smtp.host=172.18.34.9
 * mail.smtp.socketFactory.port=465
 * mail.smtp.ssl.enable=true
 * mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
 * mail.smtp.auth=true
 * mail.smtp.port=465
 * mail.debug=false
 * mail.smtp.starttls.enable=true
 *
 * @author omargo33@gmail.com
 * @since 2023-08-20
 * 
 */
@Getter
@Setter
@Slf4j
public class EnviarCorreo {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private boolean accesoSSL;
    private boolean instaciaServidorAplicacion;

    private String correo;
    private String asunto;
    private String cuerpo;
    private String userName;
    private String clave;
    private String descripcionEstado;

    private Date fechaInicio;
    private Date fechaFin;

    private Properties propiedades;

    private List<String> adjuntos;
    private List<String> adjuntosRespuesta;

    /**
     * Metodo para crear el objeto.
     *
     */
    public EnviarCorreo() {
        super();
        limpiar();
    }

    /**
     * Metodo para limpiar los datos del proceso de envio del correo.
     *
     */
    private void limpiar() {
        this.accesoSSL = false;
        this.instaciaServidorAplicacion = false;
        this.clave = "";
        this.userName = "";
        this.asunto = "";
        this.correo = "";
        this.cuerpo = "";
        this.descripcionEstado = "";
        this.fechaInicio = new Date();
        this.fechaFin = new Date();
        this.propiedades = new Properties();
        this.adjuntos = new ArrayList<>();
        this.adjuntosRespuesta = new ArrayList<>();
    }

    /**
     * Metodo para enviar el correo con utilizacion de exception para facilitar
     * el uso.
     *
     * @return
     */
    public boolean enviarCorreo() {
        boolean estado = false;

        if (validarCorreo()) {
            try {
                enviarMail();
                estado = true;
                descripcionEstado = "Correo enviado";
            } catch (Exception e) {
                log.error("Correo {} no se pudo enviar por {}", this.correo, e.toString());
                descripcionEstado = "Tiene el siguiente error:" + e.toString();
            }
        } else {
            log.error("Correo {} no tiene el formato correcto", this.correo);
            descripcionEstado = "Correo invalido";
        }
        return estado;
    }

    /**
     * Metodo para enviar un correo.
     *
     * Valida que el correo este con el formato adecuado Genera la autorizacion
     * del servidor de correo. Toma los datos de la session. Inserta
     * destinatario. Inserta emisor Agrega asunto Agrega mensaje Agrega los
     * adjuntos y corrige el mensaje en cuerpos multipart De ser necesario hace
     * cambios en los documentos electronicos en los nombres de los documentos
     * Envia el mail.
     *
     * @throws AddressException
     * @throws MessagingException
     * @throws IOException
     */
    private void enviarMail() throws AddressException, MessagingException, Exception {
        Authenticator autentificacion = new SMTPAuthenticator();

        if (accesoSSL) {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            this.propiedades.put("mail.smtp.ssl.socketFactory", sf);
        }

        Session session = Session.getDefaultInstance(this.propiedades, autentificacion);
        if (!instaciaServidorAplicacion) {
            session = Session.getInstance(this.propiedades, autentificacion);
        }

        MimeMessage message = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(this.userName);
        message.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[1];
        addressTo[0] = new InternetAddress(this.correo);
        message.setRecipients(Message.RecipientType.TO, addressTo);

        message.setSubject(this.asunto);
        message.setContent(this.cuerpo, "text/html; charset=utf-8");

        if (!this.adjuntos.isEmpty()) {
            message.setContent(crearAdjungos());
        }

        Transport.send(message);
    }

    /**
     * Metodo para crear los adjuntos.
     *
     * @return
     */
    private MimeMultipart crearAdjungos() throws MessagingException {
        MimeMultipart multipart = new MimeMultipart();

        BodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(this.cuerpo, "text/html; charset=utf-8");

        validaAdjuntos();
        for (String a : this.adjuntos) {
            if (a != null) {
                try {
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.attachFile(a);
                    messageBodyPart.setFileName(messageBodyPart.getFileName());
                    multipart.addBodyPart(messageBodyPart);
                } catch (IOException | MessagingException e) {
                    this.adjuntosRespuesta.add(a + " " + e.toString());
                    log.error("enviarMail() - add attachment {}", e.toString());
                }
            }
        }

        multipart.addBodyPart(htmlPart);
        return multipart;
    }

    /**
     * Metodo para hacer una validacion de correo electronico.
     *
     * @return
     */
    private boolean validarCorreo() {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(this.correo);
        return matcher.matches();
    }

    /**
     * Metodo para validar los adjuntos
     *
     */
    private void validaAdjuntos() {
        List<String> listaTemporal = new ArrayList<>();
        for (String a : this.adjuntos) {
            File fichero = new File(a);
            if (fichero.exists()) {
                listaTemporal.add(a);
            }
        }
        if (listaTemporal.isEmpty()) {
            this.descripcionEstado = String.format("Se esperaban %s adjunto(s), y se validaron %s",
                    this.adjuntos.size(),
                    listaTemporal.size());
        }
        setAdjuntos(listaTemporal);
    }

    /**
     * Metodo para cambiar de forma adecuado los ascentos latinos y la eñe
     *
     * Si se desea que el asunto tenga las eñes, varia en los navegadores y se
     * decidio quitarlas para evitar complicaciones.
     *
     * @param textoACambiar
     * @return
     */
    private String changeCharSet(String textoACambiar, boolean asunto) {
        if (textoACambiar == null) {
            return "";
        }
        if (asunto) {
            textoACambiar = textoACambiar.replace("á", "a");
            textoACambiar = textoACambiar.replace("é", "e");
            textoACambiar = textoACambiar.replace("í", "i");
            textoACambiar = textoACambiar.replace("ó", "o");
            textoACambiar = textoACambiar.replace("ú", "u");
            textoACambiar = textoACambiar.replace("ñ", "n");
        } else {
            textoACambiar = textoACambiar.replace("á", "&aacute;");
            textoACambiar = textoACambiar.replace("é", "&eacute;");
            textoACambiar = textoACambiar.replace("í", "&iacute;");
            textoACambiar = textoACambiar.replace("ó", "&oacute;");
            textoACambiar = textoACambiar.replace("ú", "&uacute;");
            textoACambiar = textoACambiar.replace("ñ", "&ntilde;");

            textoACambiar = textoACambiar.replace("\\\\u00E1", "&aacute;");
            textoACambiar = textoACambiar.replace("\\\\u00E9", "&eacute;");
            textoACambiar = textoACambiar.replace("\\\\u00ED", "&iacute;");
            textoACambiar = textoACambiar.replace("\\\\u00F3", "&oacute;");
            textoACambiar = textoACambiar.replace("\\\\u00FA", "&uacute;");
            textoACambiar = textoACambiar.replace("\\\\u00F1", "&ntilde;");

            textoACambiar = textoACambiar.replace("\\\\n", "<br>");
        }

        try {
            byte[] latin1 = textoACambiar.getBytes(StandardCharsets.UTF_8);
            return new String(latin1);
        } catch (Exception e) {
            return textoACambiar;
        }
    }

    /**
     * Metodo para poner las propiedades a partir de una cadna de String
     *
     * Tomar las propiedades especificas del sistema Borrara las propiedades
     * especificas.
     *
     * @param propiedadesTexto
     */
    public void setPropiedades(String propiedadesTexto) {
        try {
            String valorTrueString = "";
            this.propiedades = new Properties();
            this.propiedades.load(new StringReader(propiedadesTexto.trim().replace(" ", "\n")));
            this.userName = this.propiedades.getProperty("13a.usuario");
            this.clave = this.propiedades.getProperty("13a.clave");

            valorTrueString = this.propiedades.getProperty("13a.instancia.servidor");

            this.instaciaServidorAplicacion = valorTrueString.equalsIgnoreCase("true");

            valorTrueString = this.propiedades.getProperty("13a.acceso.ssl");
            this.accesoSSL = valorTrueString.equalsIgnoreCase("true");

            this.propiedades.remove("13a.usuario");
            this.propiedades.remove("13a.clave");
            this.propiedades.remove("13a.instancia.servidor");
            this.propiedades.remove("13a.acceso.ssl");
        } catch (IOException ex) {
            log.error("No se puede procesar las propiedades {}", ex.toString());
        }
    }

    public void setAsunto(String asunto) {
        this.asunto = changeCharSet(asunto, true);
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = changeCharSet(cuerpo, false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Correo{accesoSSL=").append(accesoSSL);
        sb.append(", instaciaServidorAplicacion=").append(instaciaServidorAplicacion);
        sb.append(", correo=").append(correo);
        sb.append(", asunto=").append(asunto);
        sb.append(", cuerpo=").append(cuerpo);
        sb.append(", userName=").append(userName);
        sb.append(", clave=").append(clave);
        sb.append(", descripcionEstado=").append(descripcionEstado);
        sb.append(", fechaInicio=").append(fechaInicio);
        sb.append(", fechaFin=").append(fechaFin);
        sb.append(", propiedades=").append(propiedades);
        sb.append(", adjuntos=").append(adjuntos);
        sb.append(", adjuntosRespuesta=").append(adjuntosRespuesta);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Objeto para redifinir al autenticacion al sito.
     *
     * @author omarv omargo33@hotmail.com
     *
     */
    private class SMTPAuthenticator extends javax.mail.Authenticator {

        /**
         * Metodo para sobrecargar la obtencion del password.
         *
         * @return
         */
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(getUserName(), getClave());
        }
    }
}
