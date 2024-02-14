package com.aplicaciones13.placas.cliente;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.NoSuchElementException;

/**
 * Clase para consumir una pagina web mediante selenium y conocer el estado de
 * la placa de un vehiculo.
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-20
 * 
 */
@Getter
@ToString
@Slf4j
public class ConsumoWebClienteProfile extends ConsumoWebCliente {
  private String userPath = "/home/ovelez/.config/google-chrome/";
  private String userProfile = "Default";

  public static void main(String[] args) {
    ConsumoWebClienteProfile consumoWebClienteProfile = new ConsumoWebClienteProfile();
    consumoWebClienteProfile.ejecutar();
    log.info(consumoWebClienteProfile.toString());
  }

  /**
   * Metodo para crear el objeto con parametros.
   * 
   * @param timeout
   * @param placa
   * @param urlSRI
   * @param userAgent
   * @param chromeDriver
   * @param userPath
   * @param userProfile
   * @param apiKey
   * @param siteKey
   */
  public void cargarParametros(
      Duration timeout,
      String placa,
      String urlSRI,
      String userAgent,
      String chromeDriver,
      String userPath,
      String userProfile) {
    cargarParametros(timeout, placa, urlSRI, userAgent, chromeDriver);
    this.userPath = userPath;
    this.userProfile = userProfile;
  }

  /**
   * Metodo para cargar la lista de opciones.
   */
  public List<String> cargarListaOpciones() {
    List<String> listaOpciones = new ArrayList<>();
    listaOpciones.add("--remote-allow-origins=*");
    listaOpciones.add("--ignore-certificate-errors");
    listaOpciones.add("--disable-web-security");
    listaOpciones.add("--window-size=1600,862");
    listaOpciones.add("--disable-popup-blocking");
    listaOpciones.add("--user-agent=" + getUserAgent());
    listaOpciones.add("--user-data-dir=" + userPath);
    listaOpciones.add("--profile-directory=" + userProfile);
    return listaOpciones;
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
    setRespuesta("No se encontro la Marca del vehiculo por Perfil.");
    if (configurarDriver(null)) {
      if (ingresarPlaca()) {
        if (presionarBotonBusqueda()) {
          if (!isCaptchaActivated()) {
            estado = analizarRespuesta();
          }
        }
      }
      tearDown();
    }
    return estado;
  }

  /**
   * Metodo para verificar si el reCAPTCHA de validación de imágenes está
   * activado.
   * 
   * Toma
   */
  public boolean isCaptchaActivated() {
    try {
      WebElement iframe = getDriver()
          .findElement(By.cssSelector("iframe[src^='https://www.google.com/recaptcha/api2/bframe']"));

      getDriver().switchTo().frame(iframe);

      // Verifica si el reCAPTCHA de validación de imágenes está presente
      WebElement imageCaptcha = getDriver().findElement(By.cssSelector(".rc-imageselect-challenge"));
      boolean isCaptchaActivated = imageCaptcha.isDisplayed();

      getDriver().switchTo().defaultContent();
      return isCaptchaActivated;
    } catch (NoSuchElementException e) {
      getDriver().switchTo().defaultContent();
      return false;
    }
  }
}
