package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.controlador.ProductoControlador;
import ministore.modelo.Producto;
import ministore.repositorio.CategoriaRepositorio;
import ministore.repositorio.ProductoRepositorio;

/**
 * Prueba de ProductoControlador con MySQL, SIN ventana. Se ejecuta con Shift + F6.
 * El controlador NO cambió: solo cambió el repositorio que usa por dentro.
 *
 * IMPORTANTE: esta prueba CAMBIA los datos (guarda el café y desactiva la leche).
 * Antes de ejecutarla, correr el script 02_datos_prueba_ministore.sql en Workbench.
 *
 * @author Mauricio
 */
public class PruebaProductoControlador {

    public static void main(String[] args) {

        CategoriaRepositorio categorias = new CategoriaRepositorio();
        ProductoRepositorio productos = new ProductoRepositorio(categorias);
        ProductoControlador controlador = new ProductoControlador(productos, categorias);

        // 1. Validaciones de guardar: cada línea falla por una razón distinta.
        System.out.println(controlador.guardarProducto("", "Café", "", 0, "7000", "9800", "10", "5", ""));           // código vacío
        System.out.println(controlador.guardarProducto("P020", "Café 500 g", "", 0, "abc", "9800", "10", "5", "")); // "abc" no es número
        System.out.println(controlador.guardarProducto("P020", "Café 500 g", "", 0, "7000", "6000", "10", "5", "")); // venta < compra
        System.out.println(controlador.guardarProducto("P001", "Café 500 g", "", 0, "7000", "9800", "10", "5", "")); // código repetido

        // 2. Guardar con datos correctos: queda guardado EN MYSQL.
        System.out.println(controlador.guardarProducto("P020", "Café 500 g", "", 0, "7000", "9800", "10", "5", ""));

        // 3. El café recibió el id que asignó MySQL.
        Producto cafe = productos.buscarPorCodigo("P020");
        System.out.println("Id del café en MySQL: " + cafe.getId());                // 18

        // 4. Actualizar el café: nuevo precio de venta.
        System.out.println(controlador.actualizarProducto(cafe.getId(), "P020", "Café 500 g", "", 0,
                "7000", "10500", "10", "5", ""));
        System.out.println("Nuevo precio: " + productos.buscarPorId(cafe.getId()).getPrecioVenta());   // 10500.0

        // 5. Eliminar la leche (id 2) y un id que no existe.
        System.out.println(controlador.desactivarProducto(2));
        System.out.println(controlador.desactivarProducto(99));
        System.out.println("Productos activos: " + controlador.listarProductos().size());   // 16
    }
}