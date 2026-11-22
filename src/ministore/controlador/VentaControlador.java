/*
 * =========================================================================
 *  CLASE: VentaControlador                PAQUETE: ministore.controlador
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es el intermediario entre VentanaVenta y los repositorios. Maneja LA
 *    VENTA EN CURSO (el carrito): elegir el cliente, agregar y quitar
 *    productos, confirmar y generar el comprobante. Recibe los TEXTOS que el
 *    usuario escribió, los VALIDA y devuelve un MENSAJE para la ventana.
 *    Cubre los RF5 (inventario), RF8 (comprobante) y RF10 (vendedor).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - VentaRepositorio    → guarda la venta confirmada (con su transacción).
 *      - ProductoRepositorio → la lista de productos para cmbProducto.
 *      - ClienteRepositorio  → buscar el cliente por documento.
 *      - TiendaRepositorio   → los datos de la tienda para el comprobante.
 *      - Venta, Cliente, Producto y Usuario (modelo).
 *    La usan:
 *      - VentanaVenta (y la crea Aplicacion, en abrirVenta).
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - Los cuatro repositorios en su versión MySQL (VentaRepositorio probado).
 *    - El Consumidor final en la tabla clientes (script 02).
 *
 *  MÉTODOS:
 *    nuevaVenta, getVenta, listarProductos, buscarCliente,
 *    usarConsumidorFinal, agregarProducto, quitarProducto,
 *    confirmarVenta, generarComprobante.
 *
 *  REGLA DE MVC: no conoce botones ni tablas. Recibe y devuelve datos.
 * =========================================================================
 */
package ministore.controlador;

import java.util.ArrayList;
import ministore.modelo.Cliente;
import ministore.modelo.DetalleVenta;
import ministore.modelo.Producto;
import ministore.modelo.Usuario;
import ministore.modelo.Venta;
import ministore.repositorio.ClienteRepositorio;
import ministore.repositorio.ProductoRepositorio;
import ministore.repositorio.TiendaRepositorio;
import ministore.repositorio.VentaRepositorio;

/**
 * Controlador de la venta en curso.
 *
 * @author Mauricio
 */
public class VentaControlador {

    // =====================================================================
    // ATRIBUTOS
    // Los repositorios llegan de afuera (los crea Aplicacion UNA sola vez).
    // =====================================================================

    // Documento del cliente genérico de la DIAN, el mismo que usa Cliente.esConsumidorFinal().
    private static final String DOCUMENTO_CONSUMIDOR_FINAL = "222222222222";

    private VentaRepositorio ventas;
    private ProductoRepositorio productos;
    private ClienteRepositorio clientes;
    private TiendaRepositorio tiendas;
    private Usuario vendedor;    // El usuario que inició sesión: queda como "Atendió:" (RF10).
    private Venta venta;         // LA VENTA EN CURSO: el carrito que se ve en la ventana.

    // =====================================================================
    // CONSTRUCTOR
    // Recibe los repositorios y el vendedor, y deja lista una venta vacía.
    // =====================================================================

    public VentaControlador(VentaRepositorio ventas, ProductoRepositorio productos,
                            ClienteRepositorio clientes, TiendaRepositorio tiendas, Usuario vendedor) {
        this.ventas = ventas;
        this.productos = productos;
        this.clientes = clientes;
        this.tiendas = tiendas;
        this.vendedor = vendedor;
        nuevaVenta();                // Al abrir la ventana ya hay un carrito vacío.
    }

    // =====================================================================
    // LA VENTA EN CURSO
    // =====================================================================

    // Empieza una venta vacía a nombre del Consumidor final.
    // La usa el constructor y la ventana después de cada venta confirmada.
    public void nuevaVenta() {
        Cliente consumidor = clientes.buscarPorDocumento(DOCUMENTO_CONSUMIDOR_FINAL);
        venta = new Venta(consumidor, vendedor);
    }

    // La ventana la usa para llenar tblCarrito, lblCliente y lblTotal.
    public Venta getVenta() {
        return venta;
    }

    // Los productos activos para cmbProducto. Se vuelve a leer de MySQL
    // después de cada venta, para que el stock esté al día.
    public ArrayList<Producto> listarProductos() {
        return productos.listar();
    }

    // =====================================================================
    // EL CLIENTE  →  botones Buscar y Consumidor final
    // =====================================================================

    // Busca un cliente ACTIVO por documento y lo pone en la venta.
    public String buscarCliente(String documento) {
        if (documento.trim().isEmpty()) {
            return "Escriba el documento del cliente";                         // 1. Campo vacío
        }
        Cliente c = clientes.buscarPorDocumento(documento.trim());
        if (c == null || !c.isActivo()) {
            return "No existe un cliente activo con ese documento";            // 2. No existe
        }
        cambiarCliente(c);
        return "Cliente: " + c.getNombre();                                    // 3. Todo bien
    }

    // Vuelve a poner al Consumidor final (venta sin identificar).
    public String usarConsumidorFinal() {
        cambiarCliente(clientes.buscarPorDocumento(DOCUMENTO_CONSUMIDOR_FINAL));
        return "Cliente: Consumidor final";
    }

    // AYUDANTE (private). En Venta, setCliente está DESACTIVADO a propósito:
    // el cliente solo se define en el constructor. Por eso, para cambiarlo,
    // se crea una venta NUEVA con el otro cliente y se le pasan los productos
    // del carrito anterior. Así no se cambia la clase Venta.
    private void cambiarCliente(Cliente cliente) {
        Venta anterior = venta;
        venta = new Venta(cliente, vendedor);
        for (DetalleVenta d : anterior.getDetalles()) {              // Copia cada línea del carrito.
            venta.agregarProducto(d.getProducto(), d.getCantidad());
        }
    }

    // =====================================================================
    // EL CARRITO  →  botones Agregar y Quitar
    // =====================================================================

    // Recibe el producto elegido en cmbProducto y el texto de txtCantidad.
    public String agregarProducto(Producto producto, String cantidadTxt) {
        if (producto == null) {
            return "Seleccione un producto";                                   // 1. Nada elegido
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTxt.trim());                   // "2" → 2
        } catch (NumberFormatException e) {                                    // "abc", "1.5" o vacío
            return "La cantidad debe ser un número entero";                    // 2. No es un número
        }
        if (cantidad <= 0) {
            return "La cantidad debe ser mayor que 0";                         // 3. Cero o negativa
        }
        if (!venta.agregarProducto(producto, cantidad)) {                      // Venta revisa el stock
            return "No hay stock suficiente. Disponible: " + producto.getStock();   // 4. No alcanza
        }
        return "Producto agregado";                                            // 5. Todo bien
    }

    // Recibe la fila seleccionada en tblCarrito (-1 si no hay ninguna).
    // La fila de la tabla es la misma posición de la línea en el carrito.
    public String quitarProducto(int indice) {
        if (!venta.quitarDetalle(indice)) {
            return "Seleccione un producto del carrito";
        }
        return "Producto quitado";
    }

    // =====================================================================
    // CONFIRMAR  →  botón Confirmar venta
    // =====================================================================

    public String confirmarVenta() {
        if (venta.getDetalles().isEmpty()) {
            return "El carrito está vacío";                                    // 1. Nada que vender
        }
        // 2. PRIMERO MySQL: guardar dentro de la transacción (todo o nada).
        //    MySQL tiene el stock REAL: si otro vendedor ya vendió, devuelve false.
        if (!ventas.guardar(venta)) {
            return "No hay stock suficiente en la tienda. Revise el carrito";
        }
        // 3. DESPUÉS el objeto: confirmar() descuenta el stock en memoria y
        //    marca la venta como confirmada (ya no se puede modificar).
        venta.confirmar();
        return "Venta N° " + venta.getId() + " guardada";                      // 4. Todo bien
    }

    // El texto del comprobante de la venta recién confirmada (RF8).
    // Los datos de la tienda salen de MySQL (los que se editan en Configuración).
    public String generarComprobante() {
        return venta.generarComprobante(tiendas.obtener());
    }
}