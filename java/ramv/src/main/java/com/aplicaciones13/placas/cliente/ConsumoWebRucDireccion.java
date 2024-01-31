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
import com.aplicaciones13.placas.cliente.utilidades.MainFiles;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;

import java.util.Scanner;

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
public class ConsumoWebRucDireccion {
  private WebDriver driver;
  private Duration timeout;
  private String respuesta;
  private String ruc = "0102581709001";
  private String urlSRI = "https://srienlinea.sri.gob.ec/facturacion-internet/consultas/publico/ruc-datos2.jspa?ruc=";
  private String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.90 Safari/537.36";
  private String chromeDriver = "/home/colaborador/Descargas/chromedriver_linux64/chromedriver-114.0.5735.90";

  public static void main(String[] args) {
    ConsumoWebRucDireccion consumoWebClienteDireccion = new ConsumoWebRucDireccion();
    consumoWebClienteDireccion.ejecutarTest();
  }

  /**
   * Metodo para crear el objeto.
   * 
   * Para ejecuar el proceso String[] args
   */
  private void ejecutarTest() {
    ConsumoWebRucDireccion consumoWebClienteDireccion = new ConsumoWebRucDireccion();
    consumoWebClienteDireccion.setTimeout(Duration.ofSeconds(5));
    boolean estado = consumoWebClienteDireccion.ejecutar();
    log.info("Estado: {}", estado);
    log.info("Respuesta: {}", consumoWebClienteDireccion.getRespuesta());
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

    List<String> listaRuc = leerArchivo("/home/colaborador/RommelIngreso.csv");

    List<String> provincias = crearProvincias();

    if (configurarDriver()) {
      for (String ruc : listaRuc) {
        this.ruc = ruc;
        log.info("Ruc: {}", ruc);
        if (isLink()) {
          estado = analizarRespuesta();

          if (estado) {
            int indiceProvincia = obtenerIndiceProvincia(provincias, respuesta);
            respuesta = respuesta.substring(indiceProvincia);
            respuesta = borrarUltimaPalabra(respuesta);
            MainFiles.escribirLogDefault(ruc, respuesta.trim());
          }

          // cada 50 registros esperar 1 minuto
          if (listaRuc.indexOf(ruc) % 100 == 0) {
            Generador.generarEsperaAleatoria(30000, 60000);
          }
        }
      }
    }

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
  private boolean configurarDriver() {
    try {
      System.setProperty("webdriver.chrome.driver", chromeDriver);
      timeout = Duration.ofMillis(3000);

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
      options.setHeadless(true);

      driver = new ChromeDriver(options);
      driver.manage().window().setSize(new Dimension(1600, 862));
      driver.manage().timeouts().implicitlyWait(timeout);
      driver.manage().deleteAllCookies();
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
  private boolean isLink() {
    try {
      driver.get(urlSRI + ruc);
      Generador.generarEsperaAleatoria(200, 500);
    
      driver.findElement(By.cssSelector(".listaLinks > a")).click();
      return true;
    } catch (Exception e) {
      respuesta = String.format("No se puede ejecutar el link %s", e.toString());
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
    Generador.generarEsperaAleatoria(200, 500);
    try {
      WebElement tablaDireccion = wait
          .until(ExpectedConditions.presenceOfElementLocated(
              By.xpath("/html/body/table/tbody/tr/td/div[3]/div/div[3]/form/table[2]/tbody/tr[2]")));
      respuesta = tablaDireccion.getText();
      log.warn("Respuesta a analizar: {}", respuesta);
      if (respuesta.indexOf("00") >= 0) {
        return true;
      } else {
        respuesta = "No se encontro la direcciones de establecimiento";
      }
    } catch (Exception e2) {
      respuesta = String.format("No se encontro direcciones de establecimiento %s", e2.toString());
    }
    return false;
  }

  /**
   * Metodo para cerrar el driver.
   */
  private void tearDown() {
    driver.quit();
  }

  // lista de provincias del ecuador en List<String>

  private List<String> crearProvincias() {
    List<String> provincias = new ArrayList<String>();
    provincias.add("AZUAY");
    provincias.add("BOLIVAR");
    provincias.add("CAÑAR");
    provincias.add("CARCHI");
    provincias.add("COTOPAXI");
    provincias.add("CHIMBORAZO");
    provincias.add("EL ORO");
    provincias.add("ESMERALDAS");
    provincias.add("GUAYAS");
    provincias.add("IMBABURA");
    provincias.add("LOJA");
    provincias.add("LOS RIOS");
    provincias.add("MANABI");
    provincias.add("MORONA SANTIAGO");
    provincias.add("NAPO");
    provincias.add("PASTAZA");
    provincias.add("PICHINCHA");
    provincias.add("TUNGURAHUA");
    provincias.add("ZAMORA CHINCHIPE");
    provincias.add("GALAPAGOS");
    provincias.add("SUCUMBIOS");
    provincias.add("ORELLANA");
    provincias.add("SANTO DOMINGO DE LOS TSACHILAS");
    provincias.add("SANTA ELENA");
    provincias.add("FRANCISCO DE ORELLANA");
    provincias.add("TUNGURAHUA");
    provincias.add("ZAMORA CHINCHIPE");

    return provincias;
  }

  /**
   * Metodo para leer un archivo de texto y salcar en una lista de String cada
   * linea del archivo con las clases mas old school
   */
  private List<String> leerArchivo(String path) {
    MainFiles mainFiles = new MainFiles(path);
    String texto = mainFiles.getText();
    List<String> lista = new ArrayList<String>();
    Scanner scanner = new Scanner(texto);
    while (scanner.hasNextLine()) {
      String linea = scanner.nextLine();
      lista.add(linea);
    }
    scanner.close();
    return lista;
  }

  /**
   * Metodo que tiene un texto 003 GERARDO ORTIZ E HIJOS GUAYAS / GUAYAQUIL / KM
   * 1.5 AV. CARLOS JULIO AROSEMENA S/N Abierto
   * donde GUAYAS es la provicia y tengo que buscar de mi lista de provincias para
   * obtener el indice
   *
   **/
  private int obtenerIndiceProvincia(List<String> provincias, String texto) {
    for (String provincia : provincias) {
      log.info(texto+" "+provincia);
      if (texto.indexOf(provincia+" /") >= 0) {
        log.info(texto+" "+provincia);

        return texto.indexOf(provincia)-1;
      }
    }
    return 0;
  }

  /**
   * MEtodo para borrar la ultima palabra de 003 GERARDO ORTIZ E HIJOS GUAYAS /
   * GUAYAQUIL / KM 1.5 AV. CARLOS JULIO AROSEMENA S/N Abierto
   * 
   */
  private String borrarUltimaPalabra(String texto) {
    String[] palabras = texto.split(" ");
    String textoNuevo = "";
    for (int i = 0; i < palabras.length - 1; i++) {
      textoNuevo += palabras[i] + " ";
    }

    return textoNuevo;
  }
}
