package com.aplicaciones13.downride.http.request;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.downride.jpa.model.UserAgent;
import com.aplicaciones13.downride.servicio.UserAgentServicio;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Tag(name = "user-agents", description = "Servicio para manejar los user-agents de chrome")
@RequestMapping(value = "/v1/useragents")
public class UserAgents {

    @Autowired
    private UserAgentServicio userAgentServicio;
    
    @GetMapping("/{idUserAgent}")
    public UserAgent getUserAgent(@PathVariable int idUserAgent) {
        return userAgentServicio.findByIdUserAgent(idUserAgent);
    }

    @PostMapping("/")
    public UserAgent postUserAgent(@Valid @RequestBody UserAgent userAgent) {
        return userAgentServicio.crearUserAgent(userAgent);
    }

    @DeleteMapping("/{idUserAgent}")
    public void deleteUserAgent(@PathVariable int idUserAgent) {
        userAgentServicio.borrarUserAgent(idUserAgent);
    }
}
