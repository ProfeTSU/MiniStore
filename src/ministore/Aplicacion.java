/*
 * =========================================================================
 *  CLASE: Aplicacion                      PAQUETE: ministore
 * =========================================================================
 *  ¿PARA QUÉ SIRVE?
 *    ARMA el programa: crea los repositorios UNA sola vez (para que todas
 *    las ventanas compartan los mismos) y sabe cómo abrir cada ventana con
 *    su controlador. No es modelo, ni controlador, ni vista: es el
 *    "pegamento" que une las capas.
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    La usan: MiniStore (la arranca), VentanaLogin y VentanaPrincipal
 *             (le piden que abra otras ventanas).
 *
 *  CADA VENTANA NUEVA = UN MÉTODO NUEVO AQUÍ (abrirVenta, abrirUsuarios...).
 * =========================================================================
 */
package ministore;

import ministore.controlador.ClienteControlador;
import ministore.controlador.LoginControlador;
import ministore.controlador.ProductoControlador;
import ministore.modelo.Usuario;
import ministore.repositorio.CategoriaRepositorio;
import ministore.repositorio.ClienteRepositorio;
import ministore.repositorio.ProductoRepositorio;
import ministore.repositorio.UsuarioRepositorio;
import ministore.vista.VentanaClientes;
import ministore.vista.VentanaLogin;
import ministore.vista.VentanaPrincipal;
import ministore.vista.VentanaProductos;
import ministore.controlador.TiendaControlador;
import ministore.repositorio.TiendaRepositorio;
import ministore.vista.VentanaConfiguracion;
/**
 * Arma la aplicación y abre cada ventana.
 *
 * @author Mauricio
 */
public class Aplicacion {

    // Los repositorios: se crean UNA vez y se comparten.
    private CategoriaRepositorio categorias = new CategoriaRepositorio();
    private ProductoRepositorio productos = new ProductoRepositorio(categorias);
    private ClienteRepositorio clientes = new ClienteRepositorio();
    private UsuarioRepositorio usuarios = new UsuarioRepositorio();
    private TiendaRepositorio tiendas = new TiendaRepositorio();     // La usarán Configuración y Venta.

    // Arranca el programa: abre el login.
    public void iniciar() {
        new VentanaLogin(new LoginControlador(usuarios), this).setVisible(true);
    }

    // Abre el menú principal para el usuario que inició sesión.
    public void abrirPrincipal(Usuario usuario) {
        new VentanaPrincipal(this, usuario).setVisible(true);
    }

    // Abre Productos con su controlador.
    public void abrirProductos() {
        new VentanaProductos(new ProductoControlador(productos, categorias)).setVisible(true);
    }

    // Abre Clientes con su controlador.
    public void abrirClientes() {
        new VentanaClientes(new ClienteControlador(clientes)).setVisible(true);
    }
    // Abre Configuración (solo el administrador) con su controlador.
    public void abrirConfiguracion() {
        new VentanaConfiguracion(new TiendaControlador(tiendas)).setVisible(true);
    }
}