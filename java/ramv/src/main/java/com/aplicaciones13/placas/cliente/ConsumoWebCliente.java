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
public class ConsumoWebCliente {
  private WebDriver driver;
  private Duration timeout;
  private String respuesta;
  private String placa = "I0097902";
  private String urlSRI = "https://srienlinea.sri.gob.ec/sri-en-linea/SriVehiculosWeb/ConsultaValoresPagarVehiculo/Consultas/consultaRubros";
  private String userAgent = "Mozilla/5.0 (Linux; Android 10; CPH2239) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Mobile Safari/537.36";
  private String chromeDriver = "/home/ovelez/Descargas/chromedriver-106.0.5249.21";

  public static void main(String[] args) {
    ConsumoWebCliente consumoWebCliente = new ConsumoWebCliente();

    Duration timeout = Duration.ofSeconds(20);
    consumoWebCliente.setTimeout(timeout);
    consumoWebCliente.setUrlSRI(
        "https://srienlinea.sri.gob.ec/sri-en-linea/SriVehiculosWeb/ConsultaValoresPagarVehiculo/Consultas/consultaRubros");

    consumoWebCliente
        .setUserAgent("Mozilla/5.0 (Linux; Android 10; CPH2239) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Mobile Safari/537.36");
    consumoWebCliente.setPlaca("I0097902");

    consumoWebCliente.setChromeDriver("/home/ovelez/Descargas/chromedriver-106.0.5249.21");

    if (consumoWebCliente.ejecutar()) {
      log.info("Respuesta: {}", consumoWebCliente.getRespuesta());
    } else {
      log.warn("Respuesta Error: {}", consumoWebCliente.getRespuesta());
    }
  }

  /**
   * Metodo para crear el objeto.
   * 
   * Para ejecuar el proceso String[] args
   */
  public void ejecutarTest() {
    ConsumoWebCliente consumoWebCliente = new ConsumoWebCliente();
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
    configurarDriver();
    if (buscarPlaca()) {
      Generador.generarEsperaAleatoria(3000, 5500);
      estado = analizarRespuesta();
    }
    Generador.generarEsperaAleatoria(3000, 5500);
    tearDown();
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
  private void configurarDriver() {
    System.setProperty("webdriver.chrome.driver", chromeDriver);

    timeout = Duration.ofMillis(15000);

    List<String> listaOpciones = new ArrayList<>();
    listaOpciones.add("--remote-allow-origins=*");
    listaOpciones.add("ignore-certificate-errors");
    listaOpciones.add("--disk-cache-size=0");
    listaOpciones.add("--window-size=1600,862");
    listaOpciones.add("--user-agent=" + userAgent);

    ChromeOptions options = new ChromeOptions();
    options.addArguments(listaOpciones);
    options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

    options.setHeadless(true);

    driver = new ChromeDriver(options);
    driver.manage().window().setSize(new Dimension(1600, 862));
    driver.manage().timeouts().implicitlyWait(timeout);
    driver.manage().deleteAllCookies();
  }

  /**
   * Metodo para ejecutar la busqueda de la placa.
   * 
   * Carga la pagina del SRI.
   * Posiciona sobre el campo de busqueda
   * Ingresa la placa en el campo de busqueda
   * Espera un tiempo aleatorio entre 3 y 5 segundos
   * 
   * Busca y presiona el boton de busqueda
   * Si el boton de busqueda se ejecuto correctamente devuelve true
   * Caso contrario
   * Asigna el mensaje de error y retorna false
   * 
   */
  private boolean buscarPlaca() {
    driver.get(urlSRI);
    // driver.findElement(By.id("busqueda")).click();
    driver.findElement(By.id("busqueda")).sendKeys(placa);

    Generador.generarEsperaAleatoria(3000, 5300);

    if (isBotonBusquedaClick(driver)) {
      return true;
    }

    log.warn("La placa {} no habilita el pago de matricula", placa);
    respuesta = "Boton de busqueda no encontrado";
    return false;
  }

  /**
   * Metodo para buscar el boton de busqueda y ejecutarlo.
   * 
   * Busca todos los elementos en la pagina que son botones
   * y busca el boton que tiene el texto "Consultar"
   * Si encuentra el boton, lo ejecuta y devuelve true
   * Caso contrario devuelve false
   */
  private boolean isBotonBusquedaClick(WebDriver driver) {
    try {    
      driver.findElement(By.cssSelector(".cyan-btn > .ui-button-text")).click();
      return true;
    } catch (Exception e) {
      log.warn("No se puede hacer clic a la busqueda {}", e.toString());
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
    boolean estado = false;

    // Duration de 10 segundos
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    Generador.generarEsperaAleatoria(3000, 5300);
    try {
      WebElement sriMatricula = wait
          .until(ExpectedConditions.presenceOfElementLocated(By.tagName("sri-rutas-matriculacion")));
      respuesta = sriMatricula.getText();
      log.warn("A analizar respuesta: {}", respuesta);
      if (respuesta.indexOf("Marca") >= 0) {
        estado = true;
      } else {
        respuesta = "No se encontro la Marca";
      }
    } catch (Exception e2) {
      respuesta = e2.toString();
      log.warn("No se puede leer la respuesta de la consulta captcha {}", e2.toString());
    }
    return estado;

  }

  /**
   * Metodo para cerrar el driver.
   */
  private void tearDown() {
    driver.quit();
  }
}
