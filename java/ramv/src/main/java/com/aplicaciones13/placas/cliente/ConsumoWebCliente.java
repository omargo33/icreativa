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
public class ConsumoWebCliente {
  private WebDriver driver;
  private Duration timeout;
  private String respuesta;
  private String placa = "I0098705";
  private String urlSRI = "https://srienlinea.sri.gob.ec/sri-en-linea/SriVehiculosWeb/ConsultaValoresPagarVehiculo/Consultas/consultaRubros";
  private String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.90 Safari/537.36";
  private String chromeDriver = "/home/ovelez/Descargas/chromedriver-114.0.5735.90";

  public static void main(String[] args) {
    ConsumoWebCliente consumoWebCliente = new ConsumoWebCliente();
    consumoWebCliente.ejecutarTest();
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
    listaOpciones.add("--user-agent=" + userAgent);
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
      String chromeDriver) {
    this.placa = placa;
    this.urlSRI = urlSRI;
    this.userAgent = userAgent;
    this.chromeDriver = chromeDriver;
  }

  /**
   * Metodo para crear el objeto.
   * 
   * Para ejecuar el proceso String[] args
   */
  public void ejecutarTest() {
    ConsumoWebCliente consumoWebCliente = new ConsumoWebCliente();
    timeout = Duration.ofSeconds(20);
    boolean estado = consumoWebCliente.ejecutar();
    log.info("Estado: {}", estado);
    log.info("Respuesta: {}", consumoWebCliente.getRespuesta());
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
    if (configurarDriver(null)) {
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
  public boolean configurarDriver(String extensionPath) {
    try {
      System.setProperty("webdriver.chrome.driver", chromeDriver);
      timeout = Duration.ofMillis(15000);

      ChromeOptions options = new ChromeOptions();
      options.addArguments(cargarListaOpciones());
      options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
      options.setHeadless(true);

      if (extensionPath != null && !extensionPath.isEmpty()) {
        try {
          options.addExtensions(new File(extensionPath));
        } catch (Exception e) {
          log.warn("No se puede cargar la extension {}", e.toString());
        }
      }

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
  public boolean ingresarPlaca() {
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
  public boolean presionarBotonBusqueda() {
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
  public boolean analizarRespuesta() {
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
    try {
      driver.switchTo().frame(2);
      Generador.generarEsperaAleatoria(900, 2000);
      driver.findElement(By.cssSelector(".help-button-holder")).click();
      Generador.generarEsperaAleatoria(900, 2000);
      driver.switchTo().defaultContent();
    } catch (Exception e) {
      log.warn("No se puede eludir el reCaptcha {}", e.toString());
    }
  }

  /**
   * Metodo para cerrar el driver.
   */
  public void tearDown() {
    driver.quit();
  }
}
