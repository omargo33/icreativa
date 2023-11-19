package com.aplicaciones13.downride.cliente;

import java.util.ArrayList;
import java.util.List;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import com.aplicaciones13.downride.cliente.utilidades.ConsultaRide;
import com.aplicaciones13.downride.cliente.utilidades.Generador;

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

  private String chromeDriver = "/home/ovelez/Documentos/clientes/iCreativa/recursos/chromeDrivers/chromedriver";
  private String clave = "ANDREITA2402";
  private String respuesta;
  private String urlSRI = "https://srienlinea.sri.gob.ec/sri-en-linea/inicio/NAT";
  private String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";
  private String usuario = "0102581709";
  private Duration timeout;
  private WebDriver driver;

  public static void main(String[] args) {
    ConsumoWebCliente consumoWebCliente = new ConsumoWebCliente();
    consumoWebCliente.ejecutarTest();
  }

  /**
   * Metodo para crear el objeto.
   * 
   * Para ejecuar el proceso String[] args
   */
  public void ejecutarTest() {
    List<ConsultaRide> listaConsultaRide = new ArrayList<>();
    ConsultaRide consultaRide = new ConsultaRide("Todos", "Enero", "2022");
    listaConsultaRide.add(consultaRide);

    ConsumoWebCliente consumoWebCliente = new ConsumoWebCliente();
    boolean estado = consumoWebCliente.ejecutar(listaConsultaRide);

    if(!estado) {
      log.error(String.valueOf(respuesta));  
    }    
  }

  /**
   * Metodo para ejecutar la consulta.
   * a posterior puede ver el resultado en la variable respuesta.
   * 
   * getRespuesta()
   * 
   */
  public boolean ejecutar(List<ConsultaRide> listaConsultaRide) {
    boolean estado = false;
    configurarDriver();

    estado = login();

    if (estado) {
      estado = abrirFormularioComprobantes();
    }

    for (ConsultaRide consultaRide : listaConsultaRide) {
      if (estado) {
        estado = descargarMesAnio(consultaRide.getDia(), consultaRide.getMes(), consultaRide.getAnio());
      }
    }

    if (estado) {
      estado = cerrarSession();
    }

    tearDown();
    return estado;
  }

  /**
   * Metodo para abrir el formulario de consulta de comprobantes electronicos.
   * 
   * Y quitar mensaje pop up de "Tu opinion nos permite mejorar" noviembre 2023
   * este menu puede cambiar por eso no se sale si este da error.
   * 
   * @return
   */
  private boolean abrirFormularioComprobantes() {
    try {
      driver.findElement(By.xpath("//*[@id='mat-dialog-0']/sri-modal-perfil/sri-titulo-modal-mat/div/div[2]/button"))
          .click();
    } catch (Exception e) {
      log.error("Error al quitar pop up: " + e.getMessage());
    }

    try {
      driver.findElement(By.cssSelector(".tamano-icono-hamburguesa")).click();
      driver
          .findElement(
              By.cssSelector(".ui-panelmenu-panel:nth-child(5) .ui-panelmenu-header-link > .ui-panelmenu-icon"))
          .click();
      driver.findElement(By.xpath("//*[@id='mySidebar']/p-panelmenu/div/div[5]/div[2]/div/p-panelmenusub/ul/li[2]/a"))
          .click();
    } catch (Exception e) {
      respuesta = "Error al abrir menu: " + e.getMessage();
      log.error(respuesta);
      return false;
    }

    return true;
  }

  /**
   * Metodo para descargar el archivo con los documentos electronicos.
   * 
   * Baja de acuerdo al dia, mes y anio.
   * Baja todos los tipos de documentos del mes y anio seleccionado.\
   * Se guarda el archivo en la carpeta de descargas del usuario.
   * 
   * @param dia  se puede poner: Todos, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, ....
   * @param mes  se pone: Enero, Febrero, Marzo, Abril, Mayo, Junio, Julio....
   * @param anio se pone: 2021, 2022, 2023, 2024 ....
   * 
   * @return
   */
  private boolean descargarMesAnio(String dia, String mes, String anio) {
    String puntoQuiebre = "";
    try {
      puntoQuiebre = "anio";
      Generador.generarEsperaAleatoria(900, 1500);
      {
        WebElement dropdown = driver.findElement(By.id("frmPrincipal:ano"));
        dropdown.findElement(By.xpath("//option[. = '" + anio + "']")).click();
      }

      puntoQuiebre = "mes";
      Generador.generarEsperaAleatoria(900, 1500);
      {
        WebElement dropdown = driver.findElement(By.id("frmPrincipal:mes"));
        dropdown.findElement(By.xpath("//option[. = '" + mes + "']")).click();
      }

      puntoQuiebre = "dia";
      Generador.generarEsperaAleatoria(900, 1500);
      {
        WebElement dropdown = driver.findElement(By.id("frmPrincipal:dia"));
        dropdown.findElement(By.xpath("//option[. = '" + dia + "']")).click();
      }

      puntoQuiebre = "consultar documentos";
      Generador.generarEsperaAleatoria(900, 2500);
      driver.findElement(By.id("btnRecaptcha")).click();
      {
        WebElement element = driver.findElement(By.id("btnRecaptcha"));
        Actions builder = new Actions(driver);
        builder.moveToElement(element).perform();
      }

      puntoQuiebre = "descargar archivo";
      {
        WebElement element = driver.findElement(By.tagName("body"));
        Actions builder = new Actions(driver);
        builder.moveToElement(element, 0, 0).perform();
      }
      Generador.generarEsperaAleatoria(900, 2500);
      driver.findElement(By.xpath("//*[@id='frmPrincipal:lnkTxtlistado']")).click();
    } catch (Exception e) {
      respuesta = "Error al descargar: " + puntoQuiebre + " " + e.getMessage();
      log.error(respuesta);
      return false;
    }
    return true;
  }

  /**
   * Metodo para cerrar la session y que quede de manera estable el usuario.
   * 
   * @return
   */
  private boolean cerrarSession() {
    try {
      driver.findElement(By.cssSelector(".sri-centrado")).click();
      driver.findElement(By.cssSelector(".sri-icon-cerrar-sesion")).click();
      driver.findElement(By.name("btnContinuar")).click();
    } catch (Exception e) {
      respuesta = "Error al cerrar sesion: " + e.getMessage();
      log.error(respuesta);
      return false;
    }
    return true;
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
    timeout = Duration.ofSeconds(10);

    List<String> listaOpciones = new ArrayList<>();
    listaOpciones.add("--ignore-certificate-errors");
    listaOpciones.add("--disk-cache-size=0");
    listaOpciones.add("--window-size=1600,868");
    listaOpciones.add("--disable-extensions");
    listaOpciones.add("--disable-popup-blocking");
    listaOpciones.add("user-agent=" + userAgent);

    ChromeOptions options = new ChromeOptions();
    options.addArguments(listaOpciones);
    options.setPageLoadStrategy(PageLoadStrategy.EAGER);
    options.setHeadless(false);

    driver = new ChromeDriver(options);
    driver.manage().window().setSize(new Dimension(1600, 862));
    driver.manage().timeouts().implicitlyWait(timeout);
    driver.manage().deleteAllCookies();
  }

  /**
   * Metodo para hacer el login.
   * 
   * Y devuelve si la clave esta incorrecta.
   * 
   * @return
   */
  private boolean login() {
    boolean estado = true;
    driver.get(urlSRI);

    driver.manage().window().setSize(new Dimension(1600, 868));
    {
      WebElement element = driver.findElement(By.cssSelector(".float-left"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".sri-iniciar-sesion"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).release().perform();
    }

    driver.findElement(By.id("usuario")).click();
    driver.findElement(By.id("usuario")).sendKeys(usuario);
    driver.findElement(By.id("password")).click();
    driver.findElement(By.id("password")).sendKeys(clave);

    Generador.generarEsperaAleatoria(1000, 2500);
    driver.findElement(By.id("kc-login")).click();
    Generador.generarEsperaAleatoria(1000, 2500);

    try {
      WebElement elemento = driver.findElement(By.xpath("//*[@id='kc-content-wrapper']/div[1]/div"));
      String mensaje = elemento.getText();
      if (mensaje.startsWith("Usuario o clave inválidos/inactivos.")) {
        respuesta = "Error al iniciar sesion: " + mensaje;
        log.error(respuesta);
        estado = false;
      }
    } catch (Exception e) {
      estado = true;
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
