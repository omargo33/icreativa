package com.aplicaciones13.placas.cliente;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.ReCaptcha;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;

/**
 * Clase para consumir una pagina web mediante selenium y conocer el estado de
 * la placa de un vehiculo.
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-20
 * 
 * 
 *        <iframe title="reCAPTCHA" width="256" height="60" role="presentation"
 *        name="a-kpezz94ocdfy" frameborder="0" scrolling="no" sandbox=
 *        "allow-forms allow-popups allow-same-origin allow-scripts
 *        allow-top-navigation allow-modals allow-popups-to-escape-sandbox
 *        allow-storage-access-by-user-activation" src=
 *        "https://www.google.com/recaptcha/api2/anchor?ar=1&amp;k=6Lc6rokUAAAAAJBG2M1ZM1LIgJ85DwbSNNjYoLDk&amp;co=aHR0cHM6Ly9zcmllbmxpbmVhLnNyaS5nb2IuZWM6NDQz&amp;hl=es&amp;v=MHBiAvbtvk5Wb2eTZHoP1dUd&amp;size=invisible&amp;cb=433g0swkmfne"></iframe>
 */
@Getter
@Setter
@Slf4j
public class ConsumoWebCliente2Captcha extends ConsumoWebCliente {
  private String apiKey = "bd7a2552ab5ad2bbd5e2f9f2e166ab24";
  private String siteKey = "6Lc6rokUAAAAAJBG2M1ZM1LIgJ85DwbSNNjYoLDk";

  public static void main(String[] args) {
    ConsumoWebCliente2Captcha consumoWebCliente2Captcha = new ConsumoWebCliente2Captcha();
    consumoWebCliente2Captcha.ejecutar();
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
    listaOpciones.add("--enable-javascript");
    listaOpciones.add("--disable-popup-blocking");
    listaOpciones.add("--user-agent=" + getUserAgent());

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
   * @param apiKey
   * @param siteKey
   */
  public void cargarParametros(
      Duration timeout,
      String placa,
      String urlSRI,
      String userAgent,
      String chromeDriver,
      String apiKey,
      String siteKey) {
    cargarParametros(timeout, placa, urlSRI, userAgent, chromeDriver);
    this.apiKey = apiKey;
    this.siteKey = siteKey;
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
    setRespuesta("No se encontro la Marca del vehiculo por 2 Captcha.");
    if (configurarDriver(null)) {
      if (ingresarPlaca()) {
        if (presionarBotonBusqueda()) {
          if (isCaptchaActivated()) {
            eludirReCaptcha();
          } else {
            estado = analizarRespuesta();
          }
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
   * Metodo para resolver el captcha.
   * revisar aca para mas informacion
   * https://2captcha.com/p/selenium-captcha-solver
   */
  private String eludirReCaptcha() {
    log.info("Captcha consultado");
    TwoCaptcha solver = new TwoCaptcha(apiKey);
    ReCaptcha captcha = new ReCaptcha();
    captcha.setSiteKey(siteKey);
    captcha.setUrl(getUrlSRI());
    try {
      solver.solve(captcha);
      String codeString = captcha.getCode();
      log.warn("Captcha solved: " + codeString);
      return codeString;
    } catch (Exception e) {
      log.error("Error occurred: " + e.getMessage());
    }

    return null;
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

      if (isCaptchaActivated) {
        log.info("Captcha activado");
        String code = eludirReCaptcha();
        // String code = "jfsadklfj";
        if (code == null) {
          return false;
        }

        // https://github.com/michaelkitas/Python-Selenium-Tutorial/tree/master
        // https://github.com/2captcha/2captcha-java/blob/master/src/main/java/examples/ReCaptchaV2OptionsExample.java
        getDriver().switchTo().defaultContent();

        WebDriverWait wait = new WebDriverWait(getDriver(), getTimeout());
        WebElement recaptchaResponseElement = wait
            .until(ExpectedConditions.presenceOfElementLocated(By.id("g-recaptcha-response")));

        ((JavascriptExecutor) getDriver()).executeScript(
            "document.getElementById('g-recaptcha-response').innerHTML = " + "'" + code + "'",
            recaptchaResponseElement);


/*             ((JavascriptExecutor) getDriver()).executeScript(
              "document.getElementById('g-recaptcha-response').innerHTML = " + "'" + code + "'",
              recaptchaResponseElement);
  */

            ((JavascriptExecutor) getDriver()).executeScript(
              "arguments[0].value = '" + code + "';", 
              recaptchaResponseElement);
            

        WebElement iframe2 = getDriver()
            .findElement(By.cssSelector("iframe[src^='https://www.google.com/recaptcha/api2/bframe']"));
        getDriver().switchTo().frame(iframe2);
        

        WebDriverWait wait2 = new WebDriverWait(getDriver(), getTimeout());

        WebElement botonSaltar = wait2
            .until(ExpectedConditions.visibilityOfElementLocated(By.id("recaptcha-verify-button")));
        botonSaltar.click();

      } else {
        log.info("Captcha desactivado");
      }

      getDriver().switchTo().defaultContent();
      return isCaptchaActivated;
    } catch (NoSuchElementException e) {
      log.error(apiKey + "No se puede eludir el reCaptcha {}", e.toString());
      getDriver().switchTo().defaultContent();
      return false;
    }
  }
}
