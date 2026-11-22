package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import java.util.ArrayList;
import ministore.controlador.VentaControlador;
import ministore.modelo.DetalleVenta;
import ministore.modelo.Producto;
import ministore.modelo.Usuario;
import ministore.repositorio.ClienteRepositorio;
import ministore.repositorio.ProductoRepositorio;
import ministore.repositorio.TiendaRepositorio;
import ministore.repositorio.UsuarioRepositorio;
import ministore.repositorio.VentaRepositorio;

/**
 * Prueba de VentaControlador con MySQL, SIN ventana. Se ejecuta con Shift + F6.
 * Hace lo mismo que hará VentanaVenta, pero escribiendo los datos a mano.
 *
 * ¡OJO! Esta prueba CAMBIA los datos: guarda una venta y descuenta stock.
 * Antes de ejecutarla, correr el script 02 en Workbench.
 *
 * @author Mauricio
 */
public class PruebaVentaControlador {

    public static void main(String[] args) {

        // Lo mismo que hará Aplicacion: crear los repositorios y el vendedor.
        Usuario carlos = new UsuarioRepositorio().buscarPorNombreUsuario("carlos");
        VentaControlador controlador = new VentaControlador(new VentaRepositorio(),
                new ProductoRepositorio(), new ClienteRepositorio(), new TiendaRepositorio(), carlos);

        // Los productos de cmbProducto: posición 0 = Arroz, 1 = Leche.
        ArrayList<Producto> lista = controlador.listarProductos();
        Producto arroz = lista.get(0);
        Producto leche = lista.get(1);

        // 1. Al empezar: carrito vacío a nombre del Consumidor final.
        System.out.println("--- 1. Venta nueva ---");
        System.out.println("Cliente: " + controlador.getVenta().getCliente().getNombre());
        System.out.println(controlador.confirmarVenta());                  // carrito vacío

        // 2. Validaciones de agregar (lo que escribe el usuario en txtCantidad).
        System.out.println("--- 2. Validaciones de agregar ---");
        System.out.println(controlador.agregarProducto(null, "2"));        // nada elegido
        System.out.println(controlador.agregarProducto(arroz, "abc"));     // no es un número
        System.out.println(controlador.agregarProducto(arroz, "0"));       // cero
        System.out.println(controlador.agregarProducto(arroz, "100"));     // más que el stock (20)

        // 3. Agregar bien. El arroz se agrega dos veces: se SUMA en la misma línea.
        System.out.println("--- 3. Agregar al carrito ---");
        System.out.println(controlador.agregarProducto(arroz, "2"));
        System.out.println(controlador.agregarProducto(leche, "1"));
        System.out.println(controlador.agregarProducto(arroz, " 1 "));     // con espacios: trim
        mostrarCarrito(controlador);                                        // Arroz x3, Leche x1

        // 4. Quitar: sin fila seleccionada (-1) y luego la leche (fila 1).
        System.out.println("--- 4. Quitar del carrito ---");
        System.out.println(controlador.quitarProducto(-1));
        System.out.println(controlador.quitarProducto(1));
        mostrarCarrito(controlador);                                        // solo Arroz x3

        // 5. El cliente: vacío, uno que no existe, y Ana Pérez.
        //    El carrito NO se pierde al cambiar de cliente.
        System.out.println("--- 5. Buscar el cliente ---");
        System.out.println(controlador.buscarCliente(""));
        System.out.println(controlador.buscarCliente("000"));
        System.out.println(controlador.buscarCliente("1234567"));
        mostrarCarrito(controlador);                                        // sigue Arroz x3

        // 6. Confirmar y ver el comprobante.
        System.out.println("--- 6. Confirmar ---");
        System.out.println(controlador.confirmarVenta());                  // Venta N° 14 guardada
        System.out.println(controlador.generarComprobante());

        // 7. Lo que hace la ventana después de confirmar: empezar otra venta.
        System.out.println("--- 7. Siguiente venta ---");
        controlador.nuevaVenta();
        System.out.println("Cliente: " + controlador.getVenta().getCliente().getNombre());
        System.out.println("Líneas en el carrito: " + controlador.getVenta().getDetalles().size());
        System.out.println("Stock del arroz en MySQL: " + controlador.listarProductos().get(0).getStock());   // 17
    }

    // Muestra el carrito como lo mostrará tblCarrito, y el total como lblTotal.
    private static void mostrarCarrito(VentaControlador controlador) {
        for (DetalleVenta d : controlador.getVenta().getDetalles()) {
            System.out.println("  " + d.getProducto().getNombre() + " x" + d.getCantidad()
                    + " = " + d.calcularSubtotal());
        }
        System.out.println("  TOTAL: " + controlador.getVenta().calcularTotal());
    }
}
