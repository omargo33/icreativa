package com.aplicaciones13.placas.jpa.queries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.aplicaciones13.placas.jpa.model.Placa;

import java.util.List;

public interface PlacaRepositorio extends JpaRepository<Placa, Integer> {

    /**
     * Metodo para buscar por el campo placa con query nativo
     * 
     * @param placa
     */
    @Query(value = "SELECT id_placas, placa, cliente, usuario, usuario_fecha, usuario_programa, estado, cliente_nombre, cliente_correo FROM public.placas WHERE placa = :placa", nativeQuery = true)
    Placa findByPlaca(String placa);

    /**
     * Metodo para buscar las placas por estado
     */
    List<Placa> findByEstado(String estado);

    /**
     * Metodo para actualizar el estado con sobre el id de la placa con un query nativo
     */    
    @Modifying
    @Query(value = "UPDATE public.placas SET estado = :estado WHERE id_placas = :idPlacas", nativeQuery = true)
    void actualizarEstado(Integer idPlacas, String estado);
}
