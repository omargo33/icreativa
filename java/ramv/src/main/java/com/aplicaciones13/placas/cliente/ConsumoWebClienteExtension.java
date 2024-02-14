package com.aplicaciones13.placas.cliente;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import org.openqa.selenium.By;

import com.aplicaciones13.placas.cliente.utilidades.Generador;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase para consumir una pagina web mediante selenium y conocer el estado de
 * la placa de un vehiculo.
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-20
 * 
 */
@Getter
@Setter
@Slf4j
public class ConsumoWebClienteExtension extends ConsumoWebCliente {
  private String extensionPath = "/home/ovelez/Descargas/Buster-Captcha-Solver-for-Humans.crx";

  public static void main(String[] args) {
    ConsumoWebClienteExtension consumoWebCliente = new ConsumoWebClienteExtension();
    consumoWebCliente.ejecutarTest();
  }

  /**
   * Metodo para cargar la lista de opciones.
   */
  public List<String> cargarListaOpciones() {
    List<String> listaOpciones = new ArrayList<>();
    listaOpciones = new ArrayList<>();
    listaOpciones.add("--remote-allow-origins=*");
    listaOpciones.add("--ignore-certificate-errors");
    listaOpciones.add("--disable-web-security");
    listaOpciones.add("--window-size=1600,862");
    listaOpciones.add("--disable-popup-blocking");
    listaOpciones.add("--user-agent=" + getUserAgent());
    listaOpciones.add("--no-sandbox");
    return listaOpciones;
  }

  /**
   * Metodo para crear el objeto con parametros.
   * 
   * @param timeout
   * @param placa
   * @param urlSRI
   * @param userAgent
   * @param chromeDriver
   */
  public void cargarParametros(
      Duration timeout,
      String placa,
      String urlSRI,
      String userAgent,
      String chromeDriver,
      String extensionPath) {
    cargarParametros(timeout, placa, urlSRI, userAgent, chromeDriver);
    this.extensionPath = extensionPath;
  }

  /**
   * Metodo para ejecutar la consulta.
   * a posterior puede ver el resultado en la variable respuesta.
   * 
   * getRespuesta()
   * 
   */
  public boolean ejecutar() {
    boolean estado = false;
    setRespuesta("No se encontro la Marca del vehiculo por Extension.");
        if (configurarDriver(extensionPath)) {
      if (ingresarPlaca()) {
        if (presionarBotonBusqueda()) {
          eludirReCaptcha();
          estado = analizarRespuesta();
        }
      }
      tearDown();
    }
    return estado;
  }

  /**
   * Metodo para eludir el reCaptcha.
   * 
   * Cambia al frame del reCaptcha
   * Pone tiempo de espera aleatorio
   * busca el boton de ayuda de paso de reCaptcha
   * Pone tiempo de espera aleatorio
   * Cambia al frame por defecto
   */
  private void eludirReCaptcha() {
    try {
      getDriver().switchTo().frame(2);
      Generador.generarEsperaAleatoria(900, 2000);
      getDriver().findElement(By.cssSelector(".help-button-holder")).click();
      Generador.generarEsperaAleatoria(900, 2000);
      getDriver().switchTo().defaultContent();
    } catch (Exception e) {
      log.warn("No se puede eludir el reCaptcha {}", e.toString());
    }
  }
}
