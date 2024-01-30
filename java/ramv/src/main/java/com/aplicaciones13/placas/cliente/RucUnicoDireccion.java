package com.aplicaciones13.placas.cliente;

import java.util.ArrayList;
import java.util.List;
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
public class RucUnicoDireccion {
  private WebDriver driver;
  private Duration timeout;
  private String respuesta;
  private String ruc = "0102581709001";
  private String urlSRI = "https://srienlinea.sri.gob.ec/facturacion-internet/consultas/publico/ruc-datos2.jspa?ruc=";
  private String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.90 Safari/537.36";
  private String chromeDriver = "/home/colaborador/Descargas/chromedriver_linux64/chromedriver-114.0.5735.90";
  
  public static void main(String[] args) {
    RucUnicoDireccion consumoWebCliente = new RucUnicoDireccion();
    consumoWebCliente.ejecutarTest();    
  }

  /**
   * Metodo para crear el objeto.
   * 
   * Para ejecuar el proceso String[] args
   */
  private void ejecutarTest() {
    RucUnicoDireccion consumoWebCliente = new RucUnicoDireccion();
    consumoWebCliente.setTimeout(Duration.ofSeconds(20));
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
    if (configurarDriver()) {
        if (presionarLink()) {        
          //estado = analizarRespuesta();       
          estado = true;
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
      //listaOpciones.add("--enable-javascript");
      listaOpciones.add("--disable-popup-blocking");
      listaOpciones.add("--user-agent=" + userAgent);
      listaOpciones.add("--no-sandbox");
      //listaOpciones.add("--user-data-dir=/home/ovelez/.config/google-chrome");
      //listaOpciones.add("--profile-directory=Profile 1");

      ChromeOptions options = new ChromeOptions();
      options.addArguments(listaOpciones);
      options.setPageLoadStrategy(PageLoadStrategy.EAGER);
      //options.setHeadless(true);      

      driver = new ChromeDriver(options);
      driver.manage().window().setSize(new Dimension(1600, 862));
      driver.manage().timeouts().implicitlyWait(timeout);
      driver.manage().deleteAllCookies();
      driver.get(urlSRI+ruc);
    } catch (Exception e) {
      respuesta = String.format("No se ha podido crear la configuraicon del driver %s", e.toString());
      return false;
    }
    return true;
  }

  /**
   * Metodo para ejecutar el boton de busqueda.
   * 
   * 
   * @param driver
   * @return
   */
  private boolean presionarLink() {
    Generador.generarEsperaAleatoria(1000, 4000);
    driver.findElement(By.cssSelector(".listaLinks > a")).click();    
    try {
      driver.findElement(By.cssSelector(".listaLinks > a")).click();
    } catch (Exception e) {
      respuesta = String.format("No se Ejecutar el link %s", e.toString());
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
   * Metodo para cerrar el driver.
   */
  private void tearDown() {
    driver.quit();
  }
}
