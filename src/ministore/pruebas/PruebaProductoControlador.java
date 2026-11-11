package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.controlador.ProductoControlador;
import ministore.modelo.Producto;
import ministore.repositorio.CategoriaRepositorio;
import ministore.repositorio.ProductoRepositorio;

/**
 * Prueba de ProductoControlador SIN ventana. Se ejecuta con Shift + F6.
 * Cada llamada simula lo que escribiría un usuario en el formulario.
 * Parámetros de guardarProducto: código, nombre, descripción, posición de la
 * categoría, precio de compra, precio de venta, stock, stock mínimo e imagen.
 *
 * @author Mauricio
 */
public class PruebaProductoControlador {

    public static void main(String[] args) {

        // Los repositorios se crean UNA vez y se le entregan al controlador.
        CategoriaRepositorio categorias = new CategoriaRepositorio();
        ProductoRepositorio productos = new ProductoRepositorio(categorias);
        ProductoControlador controlador = new ProductoControlador(productos, categorias);

        // 1. Validaciones de guardar: cada línea falla por una razón distinta.
        System.out.println(controlador.guardarProducto("", "Café", "", 0, "7000", "9800", "10", "5", ""));           // código vacío
        System.out.println(controlador.guardarProducto("P010", "Café 500 g", "", 0, "abc", "9800", "10", "5", "")); // "abc" no es número
        System.out.println(controlador.guardarProducto("P010", "Café 500 g", "", 0, "7000", "6000", "10", "5", "")); // venta < compra
        System.out.println(controlador.guardarProducto("P001", "Café 500 g", "", 0, "7000", "9800", "10", "5", "")); // código repetido

        // 2. Guardar con datos correctos.
        System.out.println(controlador.guardarProducto("P010", "Café 500 g", "", 0, "7000", "9800", "10", "5", ""));

        // 3. Listar: ahora son 4 productos.
        for (Producto p : controlador.listarProductos()) {
            System.out.println(p.getId() + ". " + p.obtenerInformacion());
        }

        // 4. Actualizar el café (id 4): nuevo precio de venta.
        System.out.println(controlador.actualizarProducto(4, "P010", "Café 500 g", "", 0, "7000", "10500", "10", "5", ""));

        // 5. Eliminar la leche (id 2) y un id que no existe.
        System.out.println(controlador.desactivarProducto(2));
        System.out.println(controlador.desactivarProducto(99));
        System.out.println("Productos activos: " + controlador.listarProductos().size());
    }
}