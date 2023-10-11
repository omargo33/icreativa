package com.aplicaciones13.placas.servicio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aplicaciones13.placas.jpa.model.Usuario;

public class UserDetailsImpl implements UserDetails {

    List<GrantedAuthority> authorities = new ArrayList<>();

    int idUsuario;
    String usuario;
    String contrasenia;
    String estado;

    public UserDetailsImpl(Integer idUsuario, String usuario, String contrasenia, String estado,
            List<GrantedAuthority> authorities) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.estado = estado;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Usuario user) {
        List<String> listaRoles = new ArrayList<>();
        String roles[] = user.getRol().split(" ");

        for (String rol : roles) {
            listaRoles.add(rol);
        }
    
        List<GrantedAuthority> authorities = listaRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getIdUsuario(),
                user.getUsuario(),
                user.getContrasenia(),
                user.getEstado(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return contrasenia;
    }

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (!Objects.equals(estado, "E")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (!Objects.equals(estado, "L")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (!Objects.equals(estado, "E")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        if (Objects.equals(estado, "A")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(idUsuario, user.idUsuario);
    }
}
