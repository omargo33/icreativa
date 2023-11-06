package com.aplicaciones13.downride.cliente;

import java.util.ArrayList;
import java.util.List;

import java.time.Duration;

import org.hibernate.mapping.Map;
import org.hibernate.mapping.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aplicaciones13.downride.cliente.utilidades.Generador;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;

import org.openqa.selenium.Cookie;

//TODO: Cambiar para bajar el archivo con los RIDE
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
  private String respusta;
  private String usuario = "0102581709";
  private String clave = "ANDREITA2402";
  private String urlSRI = "https://srienlinea.sri.gob.ec/sri-en-linea/inicio/NAT";
  // private String urlSRI =
  // "https://www.whatismybrowser.com/es/detect/what-is-my-user-agent/";
  private String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";
  private String chromeDriver = "/home/ovelez/Documentos/clientes/iCreativa/recursos/chromeDrivers/chromedriver";

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
    ConsumoWebCliente consumoWebCliente = new ConsumoWebCliente();
    boolean estado = consumoWebCliente.ejecutar();
    log.info(String.valueOf(estado));
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

    if (isLogin()) {
      Generador.generarEsperaAleatoria(3000, 5500);
      // TODO se pone los datos de las fechas
      // y se hace la descarga
    }
    Generador.generarEsperaAleatoria(3000, 5500);

    tearDown();
    return estado;
  }

  /**
   * Metodo para hacer el login.
   * 
   * @return
   */
  private boolean isLogin() {
    driver.get(urlSRI);
    
    driver.manage().window().setSize(new Dimension(1600, 868));
    
    // iniciar sesion -----------------------------------
    Generador.generarEsperaAleatoria(3000, 5500);
    
    driver.findElement(By.cssSelector(".sri-iniciar-sesion")).click();

    
    Generador.generarEsperaAleatoria(3000, 5500);
    driver.findElement(By.id("usuario")).click();
    driver.findElement(By.id("usuario")).sendKeys(usuario);
    driver.findElement(By.id("password")).click();
    driver.findElement(By.id("password")).sendKeys(clave);

    Generador.generarEsperaAleatoria(1000, 5500);
    driver.findElement(By.id("kc-login")).click();

    // buscar formulario de documentos -----------------------------------
    {
      WebElement element = driver.findElement(By.cssSelector(".mat-expansion-panel-header-description"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".sri-menu-icon-declaraciones:nth-child(1)"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".sri-menu-icon-facturacion-electronica:nth-child(1)"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.cssSelector(".sri-menu-icon-facturacion-electronica:nth-child(1)")).click();
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.cssSelector(".ng-tns-c18-9:nth-child(2) > .ui-menuitem-link > .ui-menuitem-text")).click();
        
    
    // anio -----------------------------------
    Generador.generarEsperaAleatoria(3000, 5500);

    {
      WebElement dropdown = driver.findElement(By.id("frmPrincipal:ano"));
      dropdown.findElement(By.xpath("//option[. = '2022']")).click();
    }
    // mes -----------------------------------
    {
      WebElement dropdown = driver.findElement(By.id("frmPrincipal:mes"));
      dropdown.findElement(By.xpath("//option[. = 'Enero']")).click();
    }
    // dia -----------------------------------
    {
      WebElement dropdown = driver.findElement(By.id("frmPrincipal:dia"));
      dropdown.findElement(By.xpath("//option[. = 'Todos']")).click();
    }
    
    // tipo comprobantes -----------------------------------
    {
      WebElement dropdown = driver.findElement(By.id("frmPrincipal:cmbTipoComprobante"));
      dropdown.findElement(By.xpath("//option[. = 'Todos']")).click();
    }

    // consultar -----------------------------------
    Generador.generarEsperaAleatoria(3000, 5500);

    driver.findElement(By.id("btnRecaptcha")).click();
    {
      WebElement element = driver.findElement(By.id("btnRecaptcha"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }

    // descargar -----------------------------------
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.cssSelector("p")).click();


    // cerrar sesion -----------------------------------
    Generador.generarEsperaAleatoria(3000, 5500);

    driver.findElement(By.cssSelector(".sri-icon-cerrar-sesion")).click();
    driver.findElement(By.name("btnContinuar")).click();

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
    // listaOpciones.add("--remote-allow-origins=*");
    listaOpciones.add("--ignore-certificate-errors");
    listaOpciones.add("--disk-cache-size=0");
    listaOpciones.add("--window-size=1400,868");
    // listaOpciones.add("--debug-print=true");
    listaOpciones.add("user-agent=" + userAgent);
    listaOpciones.add("--disable-extensions");
    // listaOpciones.add("--incognito");
    listaOpciones.add("--disable-popup-blocking");
    // listaOpciones.add("--enable-javascript");

    ChromeOptions options = new ChromeOptions();
    options.addArguments(listaOpciones);
    options.setPageLoadStrategy(PageLoadStrategy.EAGER);
    options.setHeadless(false);

    driver = new ChromeDriver(options);
    // driver.manage().window().setSize(new Dimension(1600, 862));
    driver.manage().timeouts().implicitlyWait(timeout);
    // driver.manage().deleteAllCookies();

  }

  /**
   * Metodo para cerrar el driver.
   */
  private void tearDown() {
     driver.quit();
  }
}
