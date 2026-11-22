package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Cliente;
import ministore.modelo.Producto;
import ministore.modelo.Usuario;
import ministore.modelo.Venta;
import ministore.repositorio.ClienteRepositorio;
import ministore.repositorio.ProductoRepositorio;
import ministore.repositorio.UsuarioRepositorio;
import ministore.repositorio.VentaRepositorio;

/**
 * Prueba de VentaRepositorio (versión MySQL). Se ejecuta con Shift + F6.
 *
 * ¡OJO! Esta prueba CAMBIA los datos: guarda una venta y descuenta stock.
 * Antes de ejecutarla, correr el script 02 en Workbench.
 *
 * @author Mauricio
 */
public class PruebaVentaRepositorio {

    public static void main(String[] args) {

        ProductoRepositorio productos = new ProductoRepositorio();
        ClienteRepositorio clientes = new ClienteRepositorio();
        UsuarioRepositorio usuarios = new UsuarioRepositorio();
        VentaRepositorio ventas = new VentaRepositorio();

        // Los datos de la venta salen de MySQL (script 02).
        Cliente consumidor = clientes.buscarPorDocumento("222222222222");   // Consumidor final
        Usuario carlos = usuarios.buscarPorNombreUsuario("carlos");        // El vendedor
        Producto arroz = productos.buscarPorCodigo("P001");                // Stock 20
        Producto leche = productos.buscarPorCodigo("P002");                // Stock 30

        // 1. Una venta normal: 2 arroces y 1 leche.
        System.out.println("--- Prueba 1: guardar una venta ---");
        System.out.println("Ventas antes: " + ventas.contarVentas());                    // 13
        Venta venta = new Venta(consumidor, carlos);
        venta.agregarProducto(arroz, 2);
        venta.agregarProducto(leche, 1);
        System.out.println("¿Se guardó? " + ventas.guardar(venta));                       // true
        System.out.println("Número de la venta: " + venta.getId());                       // 14
        System.out.println("Ventas después: " + ventas.contarVentas());                  // 14
        System.out.println("Stock del arroz en MySQL: " + productos.buscarPorCodigo("P001").getStock());   // 18
        System.out.println("Stock de la leche en MySQL: " + productos.buscarPorCodigo("P002").getStock()); // 29

        // 2. La transacción: una venta que NO se puede guardar.
        //    Se simula que otro vendedor ya vendió el fríjol: en el objeto el
        //    stock dice 100, pero en MySQL solo hay 15. La línea del arroz SÍ
        //    alcanza; la del fríjol NO. ¿Queda el arroz descontado? No: rollback.
        System.out.println("--- Prueba 2: la transacción (todo o nada) ---");
        Producto arrozOtraVez = productos.buscarPorCodigo("P001");                        // Stock 18
        Producto frijol = productos.buscarPorCodigo("P003");                              // Stock 15 en MySQL
        frijol.setStock(100);                                                            // Solo en el objeto
        Venta imposible = new Venta(consumidor, carlos);
        imposible.agregarProducto(arrozOtraVez, 1);                                       // Esta sí alcanza
        imposible.agregarProducto(frijol, 50);                                            // Esta no
        System.out.println("¿Se guardó? " + ventas.guardar(imposible));                   // false
        System.out.println("Ventas: " + ventas.contarVentas());                          // 14: no quedó a medias
        System.out.println("Stock del arroz en MySQL: " + productos.buscarPorCodigo("P001").getStock());   // 18: no se descontó
        System.out.println("Stock del fríjol en MySQL: " + productos.buscarPorCodigo("P003").getStock());  // 15
    }
}