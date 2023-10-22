package com.aplicaciones13.downride.http.request;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.downride.jpa.model.WebClient;
import com.aplicaciones13.downride.servicio.WebClientServicio;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Clase para consumir el servicio de web clientes.
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-20
 */

@RestController
@Tag(name = "webclients", description = "Servicio para manejar los clientes-web de chrome")
@RequestMapping(value = "/v1/webclients")
public class WebClients extends ComonControlador {

    @Autowired
    WebClientServicio webClientServicio;

    @GetMapping("/")
    public List<WebClient> getWebClients() {
        return webClientServicio.findAll();
    }

    @GetMapping("/{id}")
    public WebClient getWebClient(@PathVariable Integer id) {
        return webClientServicio.findByIdWebClient(id);
    }

    @PostMapping("/")
    public WebClient postUserAgent(@Valid @RequestBody WebClient webClient) {
        return webClientServicio.crearWebClient(webClient);
    }

    @DeleteMapping("/{id}")
    public void deleteUserAgent(@PathVariable Integer id) {
        webClientServicio.borrarWebClient(id);
    }
}
