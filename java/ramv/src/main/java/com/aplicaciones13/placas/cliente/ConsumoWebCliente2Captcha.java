package com.aplicaciones13.placas.cliente;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aplicaciones13.placas.cliente.utilidades.Generador;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.ReCaptcha;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;

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
public class ConsumoWebCliente2Captcha {
  private WebDriver driver;
  private Duration timeout;
  private String respuesta;
  private String placa = "I0098705";
  private String urlSRI = "https://srienlinea.sri.gob.ec/sri-en-linea/SriVehiculosWeb/ConsultaValoresPagarVehiculo/Consultas/consultaRubros";
  private String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.90 Safari/537.36";
  private String chromeDriver = "/home/ovelez/Descargas/chromedriver-114.0.5735.90";
  // private String extensionPath =
  // "/home/ovelez/Descargas/Buster-Captcha-Solver-for-Humans.crx";
  private String apiKey = "bd7a2552ab5ad2bbd5e2f9f2e166ab24";
  private String siteKey = "6Lc6rokUAAAAAJBG2M1ZM1LIgJ85DwbSNNjYoLDk";

  public static void main(String[] args) {
    ConsumoWebCliente2Captcha consumoWebCliente2Captcha = new ConsumoWebCliente2Captcha();
    consumoWebCliente2Captcha.ejecutarTest();
  }

  /**
   * Metodo para crear el objeto.
   * 
   * Para ejecuar el proceso String[] args
   */
  private void ejecutarTest() {
    ConsumoWebCliente2Captcha consumoWebCliente2Captcha = new ConsumoWebCliente2Captcha();
    consumoWebCliente2Captcha.setTimeout(Duration.ofSeconds(20));
    boolean estado = consumoWebCliente2Captcha.ejecutar();
    log.info("Estado: {}", estado);
    log.info("Respuesta: {}", consumoWebCliente2Captcha.getRespuesta());
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
    if (configurarDriver()) {
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
   * Metodo para configurar el driver.
   * 
   * Selecciona el webDriver en Chrome.
   * Crea la lista de argumentos de opciones para el driver.
   * Crea el chrome driver con las la lista de opciones de ejecucion y
   * agrega los argumentos, la estrategia de carga de la pagina y el modo de
   * lanzamiento (Headless)
   * Crea el driver
   * Administra tamaño de la ventana
   * Administra el tiempo de espera implicito
   * Elimina las cookies
   */
  private boolean configurarDriver() {
    try {
      System.setProperty("webdriver.chrome.driver", chromeDriver);
      timeout = Duration.ofMillis(15000);

      List<String> listaOpciones = new ArrayList<>();
      listaOpciones.add("--remote-allow-origins=*");
      listaOpciones.add("--ignore-certificate-errors");
      // listaOpciones.add("--disk-cache-size=0");
      listaOpciones.add("--disable-web-security");
      listaOpciones.add("--window-size=1600,862");
      // listaOpciones.add("--enable-javascript");
      listaOpciones.add("--disable-popup-blocking");
      listaOpciones.add("--user-agent=" + userAgent);
      listaOpciones.add("--no-sandbox");
      // listaOpciones.add("--user-data-dir=/home/ovelez/.config/google-chrome");
      // listaOpciones.add("--profile-directory=Profile 1");

      ChromeOptions options = new ChromeOptions();
      options.addArguments(listaOpciones);
      options.setPageLoadStrategy(PageLoadStrategy.EAGER);
      //options.setHeadless(true);

      /*
       * try {
       * options.addExtensions(new File(extensionPath));
       * } catch (Exception e) {
       * log.warn("No se puede cargar la extension {}", e.toString());
       * }
       */

      driver = new ChromeDriver(options);
      driver.manage().window().setSize(new Dimension(1600, 862));
      driver.manage().timeouts().implicitlyWait(timeout);
      driver.manage().deleteAllCookies();
      driver.get(urlSRI);
    } catch (Exception e) {
      respuesta = String.format("No se ha podido crear la configuraicon del driver %s", e.toString());
      return false;
    }
    return true;
  }

  /**
   * Metodo para ingresar la placa.
   * 
   * @return
   */
  private boolean ingresarPlaca() {
    try {
      Generador.generarEsperaAleatoria(1000, 4000);
      driver.findElement(By.id("busqueda")).sendKeys(placa);
      Generador.generarEsperaAleatoria(400, 1300);
      driver.findElement(By.id("busqueda")).sendKeys(Keys.ENTER);
      return true;
    } catch (Exception e) {
      respuesta = String.format("No se ha podido ingresar la placa %s", placa);
    }
    return false;
  }

  /**
   * Metodo para presionar el boton de busqueda.
   * 
   * Intenta presionar el boton de busqueda 3 veces
   * Si el boton de busqueda se ejecuto correctamente devuelve true
   * Caso contrario
   * Asigna el mensaje de error y retorna false
   * 
   */
  private boolean presionarBotonBusqueda() {
    for (int i = 0; i < 3; i++) {
      if (isBotonBusquedaObligadoClick()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Metodo para ejecutar el boton de busqueda.
   * 
   * 
   * @param driver
   * @return
   */
  private boolean isBotonBusquedaObligadoClick() {
    Generador.generarEsperaAleatoria(1000, 4000);
    try {
      List<WebElement> buttons = driver.findElements(By.tagName("button"));
      for (int i = 0; i < buttons.size(); i++) {
        String texto = buttons.get(i).getAttribute("outerHTML");
        int busqueda = texto.indexOf("Consultar");
        if (busqueda != -1) {
          WebElement yourButton = buttons.get(i);
          if (yourButton.isEnabled()) {
            yourButton.click();
            return true;
          }
        }
      }
    } catch (Exception e) {
      respuesta = String.format("No se puede ejecutar el boton de busqueda %s", e.toString());
    }
    return false;
  }

  /**
   * Metodo para analizar la respuesta de la consulta.
   * 
   * Analiza si no esta activo el captcha de google
   * 
   * @return
   */
  private boolean analizarRespuesta() {
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    Generador.generarEsperaAleatoria(2000, 5300);
    try {
      WebElement sriMatricula = wait
          .until(ExpectedConditions.presenceOfElementLocated(By.tagName("sri-rutas-matriculacion")));
      respuesta = sriMatricula.getText();
      log.warn("Respuesta a analizar: {}", respuesta);
      if (respuesta.indexOf("Marca") >= 0) {
        return true;
      } else {
        respuesta = "No se encontro la Marca del vehiculo";

        
      }
    } catch (Exception e2) {
      respuesta = String.format("No se encontro la Marca del vehiculo %s", e2.toString());
    }
    return false;
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
    log.info("Captcha consultado");
    resolverCaptcha();
  }

  /**
   * Metodo para resolver el captcha.
   * revisar aca para mas informacion
   * https://2captcha.com/p/selenium-captcha-solver
   */
  private void resolverCaptcha() {
    TwoCaptcha solver = new TwoCaptcha(apiKey);

    ReCaptcha captcha = new ReCaptcha();
    captcha.setSiteKey(siteKey);
    captcha.setUrl(urlSRI);
    captcha.setVersion("v3");
    try {
      solver.solve(captcha);
      log.warn("Captcha solved: " + captcha.getCode());
    } catch (Exception e) {
      log.error("Error occurred: " + e.getMessage());
    }

    //driver. 
  }

  /**
   * Metodo para cerrar el driver.
   */
  private void tearDown() {
    driver.quit();
  }
}
