package com.aplicaciones13.placas.servicio;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicaciones13.placas.cliente.ConsumoPlaca;
import com.aplicaciones13.placas.cliente.ConsumoWebCliente;
import com.aplicaciones13.placas.jpa.model.Parametro;
import com.aplicaciones13.placas.jpa.model.Placa;
import com.aplicaciones13.placas.jpa.model.UserAgent;
import com.aplicaciones13.placas.jpa.model.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase para el servicio de consulta de placas
 * 
 * @author omargo33@gmail.com
 * @version 2023-08-20
 * 
 * @see para descargar webdriver de chrome en:
 *      https://chromedriver.chromium.org/downloads
 * 
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ConsultarPlacaServicio {
    ConsumoPlaca consumoPlaca = new ConsumoPlaca();
    ConsumoWebCliente consumoWebCliente = new ConsumoWebCliente();
    
    @Autowired
    ParametroServicio parametroServicio;

    @Autowired
    PlacaServicio placaServicio;

    @Autowired
    PlacaEventoServicio placaEventoServicio;

    @Autowired
    UserAgentServicio userAgentServicio;

    @Autowired
    WebClientServicio webClientServicio;

    @Autowired
    CorreoServicio correoServicio;

    /**
     * Metodo para consultar las placas encontradas en la base de datos.
     * 
     * Si no hay placas pendientes de trabajo se termina el proceso.
     * 
     * @param estado
     */
    public void consultarPlacasEncontradas(String estado) {
        List<Placa> listaPlacas = placaServicio.findByEstado(estado);

        if (listaPlacas.isEmpty()) {
            log.warn("No hay placas pendientes de trabajo");
            return;
        }

        if (estado.compareTo("C") == 0) {
            consumirPlacaExiste(listaPlacas);
        }

        if (estado.compareTo("E") == 0) {
            consumirPlacaWeb(listaPlacas);
        }

        if (estado.compareTo("M") == 0) {
            consumirMail(listaPlacas);
        }
    }

    /**
     * Metodo para consultar las placas muy antiguas.
     * 
     * Parametros del sistema.
     * 
     */
    public void consumirPlacaAntiguas() {
        List<Integer> listaIdParametros = List.of(6, 7);
        Map<Integer, Parametro> mapParametros = parametroServicio.findByIdParametrosIn(listaIdParametros);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, (mapParametros.get(6).getValor1().intValue() * (-1)));
        Date fecha = calendar.getTime();

        List<Placa> listaPlacas = placaServicio.findByEstadoAndFechaUsuario(fecha);

        for (Placa placa : listaPlacas) {
            correoServicio.enviarCorreoAntiguas(placa.getIdPlacas(), placa.getRamvPlaca(), placa.getUsuario(),
                    mapParametros.get(6).getTexto1() + " " + mapParametros.get(6).getTexto2());

            placaServicio.actualizarEstado(placa.getIdPlacas(), "D");
        }
    }

    /**
     * Metodo para enviar de correos indicando la terminacion de encuentro de la
     * placa.
     * 
     * Parametros del sistema.
     * 
     * Listas las placas pendientes de envio de correos.
     * Y se envia el correo.
     * Si sale correcto se actualiza el estado de la placa a F.
     * Caso contrario se crea un evento de placa con el error con FB.
     * 
     * @param listaPlacas
     */
    private void consumirMail(List<Placa> listaPlacas) {
        Parametro parametro = parametroServicio.findByIdParametros(6);

        for (Placa placa : listaPlacas) {
            String novedadPlaca = placaEventoServicio.findByIdPlacaAndEstadoM(placa.getIdPlacas()).getDescripcion();

            correoServicio.enviarCorreoEncontrado(placa.getIdPlacas(), placa.getRamvPlaca(), novedadPlaca,
                    placa.getUsuario(),
                    parametro.getTexto1() + " " + parametro.getTexto2());

            placaServicio.actualizarEstado(placa.getIdPlacas(), "F");
        }
    }

    /**
     * Metodo para consumir el servicio de consulta de placas del SRI en linea con
     * selenium.
     * 
     * Parametros del sistema.
     * 
     * Listas las placas pendientes de trabajo.
     * 
     * Y se busca el web client para el consumo.
     * 
     * Y se consume el proceso selenium.
     * Si sale correcto se actualiza el estado de la placa a M.
     * Caso contrario se crea un evento de placa con el error con P.
     * 
     * @param listaPlacas
     */
    private void consumirPlacaWeb(List<Placa> listaPlacas) {
        int i=0;
        int iMax=0;
        List<Integer> listaIdParametros = List.of(4, 5, 8);
        List<UserAgent> listaUserAgents = userAgentServicio.findByAll();
        iMax = listaUserAgents.size();
        Map<Integer, Parametro> mapParametros = parametroServicio.findByIdParametrosIn(listaIdParametros);


        Duration timeout = Duration.ofSeconds(mapParametros.get(5).getValor1().intValue());
        consumoWebCliente.setTimeout(timeout);
        consumoWebCliente.setUrlSRI(mapParametros.get(4).getTexto1());
        consumoWebCliente.setExtensionPath(mapParametros.get(8).getTexto1());

        for (Placa placa : listaPlacas) {
            UserAgent userAgent = listaUserAgents.get(i);
            consumoWebCliente.setUserAgent(userAgent.getDescripcion());
            consumoWebCliente.setPlaca(placa.getRamvPlaca());

            WebClient webClient = webClientServicio.findByIdWebClient(userAgent.getIdWebClient());

            consumoWebCliente.setChromeDriver(webClient.getPath());

            if (consumoWebCliente.ejecutar()) {
                placaServicio.actualizarEstado(placa.getIdPlacas(), "M");
                userAgentServicio.incrementarEjecucionCorrecta(userAgent.getIdUserAgent());
                placaEventoServicio.crearPlacaEvento(placa.getIdPlacas(), consumoWebCliente.getRespuesta(), "M");
            } else {
                userAgentServicio.incrementarEjecucionIncorrecta(userAgent.getIdUserAgent());
                placaEventoServicio.crearPlacaEvento(placa.getIdPlacas(), consumoWebCliente.getRespuesta(), "P");
            }
            i++;
            if (i==iMax) {
                i=0;
            }
        }
    }

    /**
     * Metodo para consumir el servicio de consulta de placas del SRI en linea.
     * 
     * Parametros del sistema.
     * 
     * Listas las placas pendientes de trabajo.
     * Y se consume el servicio.
     * Si sale correcto se actualiza el estado de la placa a E.
     * Caso contrario se crea un evento de placa con el error con P.
     * 
     * @param listaPlacas
     */
    private void consumirPlacaExiste(List<Placa> listaPlacas) {
        List<Integer> listaIdParametros = List.of(1, 2, 3);
        Map<Integer, Parametro> mapParametros = parametroServicio.findByIdParametrosIn(listaIdParametros);

        int timeOut = mapParametros.get(3).getValor1().intValue();
        consumoPlaca.setContexto(mapParametros.get(1).getTexto1());
        consumoPlaca.setPathCertificado(mapParametros.get(2).getTexto1());
        consumoPlaca.setClaveCertificado(mapParametros.get(2).getTexto2());

        for (Placa placa : listaPlacas) {
            consumoPlaca.setPlaca(placa.getRamvPlaca());

            if (consumoPlaca.load(timeOut)) {
                placaServicio.actualizarEstado(placa.getIdPlacas(), "E");
                placaEventoServicio.crearPlacaEvento(placa.getIdPlacas(), consumoPlaca.getRespuesta().getMensaje(),
                        "E");
            } else {
                placaEventoServicio.crearPlacaEvento(placa.getIdPlacas(), "No Presente", "P");
            }
        }
    }
}