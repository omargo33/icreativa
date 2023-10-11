package com.aplicaciones13.placas.http.request.body;

import lombok.Data;

@Data
public class LoginRequest {

    String usuario;
    String contrasenia;
}
