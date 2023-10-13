package com.aplicaciones13.placas.http.request;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.placas.http.request.body.IdentificacionesRequest;
import com.aplicaciones13.placas.http.request.body.IdentificacionesResponse;
import com.aplicaciones13.placas.http.request.body.RamvResponse;
import com.aplicaciones13.placas.jpa.model.Placa;
import com.aplicaciones13.placas.jpa.model.PlacaEvento;
import com.aplicaciones13.placas.security.JwtUtils;
import com.aplicaciones13.placas.servicio.PlacaEventoServicio;
import com.aplicaciones13.placas.servicio.PlacaServicio;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Clase para consumir el servicio de identificacion vehicular.
 * 
 * @author omargo33@gmail.com
 * @since 2023-10-12
 * 
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Tag(name = "identificacion_vehicular", description = "Servicio para agregar RAMV, y listar los estados de los mismos")
@RequestMapping("/identificacion_vehicular")
public class IdentificacionVehicularControlador extends ComonControlador {

    @Autowired
    PlacaServicio placaServicio;

    @Autowired
    PlacaEventoServicio placaEventoServicio;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Metodo para agregar una identificacion vehicular
     * 
     * @param request
     * @param identificacionesRequest
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<?> agregarIdentificacionVehicular(HttpServletRequest request,
            @Valid @RequestBody IdentificacionesRequest identificacionesRequest) {

        String token = jwtUtils.getJwtFromHeader(request);
        String userName = jwtUtils.getUserNameFromJwtToken(token);

        List<IdentificacionesResponse> identificacionesResponseLista = new ArrayList<>();

        identificacionesRequest.getIdentificaciones().forEach(ramv -> {
            Placa placa = new Placa();
            placa.setEstado("C");
            placa.setRamvPlaca(ramv);
            placa.setUsuario(userName);
            placa.setUsuarioFecha(new Date());
            placa.setUsuarioPrograma(this.getClass().getName());
            placaServicio.crearPlaca(placa);

            IdentificacionesResponse identificacionesResponse = new IdentificacionesResponse();
            identificacionesResponse.setIndentificacio(ramv);
            identificacionesResponse.setMensaje("Agregado");
            identificacionesResponseLista.add(identificacionesResponse);
        });

        Map<String, List<IdentificacionesResponse>> response = new HashMap<>();
        response.put("agregados", identificacionesResponseLista);
        return ResponseEntity.ok(response);
    }

    /**
     * Metodo para obtener el estado de una identificacion vehicular
     * 
     * @param ramv
     * @return
     */
    @GetMapping("/{ramv}")
    public ResponseEntity<?> getEstadoRamv(@PathVariable String ramv) {
        String estado = "PENDIENTE";
        String fechaRespuesta = "";
        Placa placa = placaServicio.findByPlaca(ramv);

        if (placa.getEstado().equals("M")) {
            estado = "ENCONTRADO";

            PlacaEvento placaEvento = placaEventoServicio.findByIdPlacaAndEstadoM(placa.getIdPlacas());
            fechaRespuesta = generarFechaFormato(placaEvento.getFecha());
        }

        RamvResponse ramvResponse = new RamvResponse();
        ramvResponse.setIndentificacion(ramv);
        ramvResponse.setEstado(estado);
        ramvResponse.setFechaSolicitud(generarFechaFormato(placa.getUsuarioFecha()));
        ramvResponse.setFechaRespuesta(fechaRespuesta);

        List<RamvResponse> listaRamv = new ArrayList<>();
        listaRamv.add(ramvResponse);

        return ResponseEntity.ok(listaRamv);
    }

    /**
     * Metodo para obtener por identificacion vehicular por usuario y estado.
     * 
     * @param request
     * @param estado
     * @return
     */
    @GetMapping("?estado={estado}")
    public ResponseEntity<?> getEstadoRamvPorEstado(HttpServletRequest request, @PathVariable String estado) {
        String token = jwtUtils.getJwtFromHeader(request);
        String userName = jwtUtils.getUserNameFromJwtToken(token);
        List<RamvResponse> listaRamv = new ArrayList<>();

        if (estado.compareToIgnoreCase("PENDIENTE") == 0) {
            List<Placa> listaPlacas = placaServicio.findByUsuarioPendiente(userName);

            for (Placa placa : listaPlacas) {
                RamvResponse ramvResponse = new RamvResponse();
                ramvResponse.setIndentificacion(placa.getRamvPlaca());
                ramvResponse.setEstado(estado);
                ramvResponse.setFechaSolicitud(generarFechaFormato(placa.getUsuarioFecha()));
                ramvResponse.setFechaRespuesta("");
                listaRamv.add(ramvResponse);
            }
        } else {
            List<Placa> listaPlacas = placaServicio.findByUsuarioEncontrado(userName);

            for (Placa placa : listaPlacas) {
                RamvResponse ramvResponse = new RamvResponse();
                ramvResponse.setIndentificacion(placa.getRamvPlaca());
                ramvResponse.setEstado("ENCONTRADO");
                ramvResponse.setFechaSolicitud(generarFechaFormato(placa.getUsuarioFecha()));

                if (placa.getEstado().equals("M")) {
                    PlacaEvento placaEvento = placaEventoServicio.findByIdPlacaAndEstadoM(placa.getIdPlacas());
                    ramvResponse.setFechaRespuesta(generarFechaFormato(placaEvento.getFecha()));
                }
                listaRamv.add(ramvResponse);
            }
        }
        return ResponseEntity.ok(listaRamv);
    }

    /**
     * Metodo para generar la fecha en formato UTC
     * 
     * @param fecha
     * @return
     */
    private String generarFechaFormato(Date fecha) {
        Instant instant = fecha.toInstant();
        ZoneId zoneId = ZoneId.systemDefault(); // Zona horaria del sistema
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDateTime = localDateTime.atOffset(zoneOffset).format(formatter);

        return formattedDateTime;
    }
}
