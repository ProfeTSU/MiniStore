package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Producto;
import ministore.repositorio.CategoriaRepositorio;
import ministore.repositorio.ProductoRepositorio;

/**
 * Prueba de ProductoRepositorio. Se ejecuta con Shift + F6.
 * Comprueba el CRUD, la búsqueda por código y que las categorías sean compartidas.
 *
 * @author Mauricio
 */
public class PruebaProductoRepositorio {

    public static void main(String[] args) {

        CategoriaRepositorio categorias = new CategoriaRepositorio();            // 4 categorías.
        ProductoRepositorio productos = new ProductoRepositorio(categorias);     // Constructor 2.

        // 1. Listar: los 3 productos de prueba.
        for (Producto p : productos.listar()) {
            System.out.println(p.getId() + ". " + p.obtenerInformacion());
        }

        // 2. Buscar por código (NUEVO): sin importar mayúsculas.
        System.out.println("¿Existe P001? " + (productos.buscarPorCodigo("P001") != null));   // true
        System.out.println("¿Existe p002? " + (productos.buscarPorCodigo("p002") != null));   // true
        System.out.println("¿Existe P009? " + (productos.buscarPorCodigo("P009") != null));   // false

        // 3. Categorías COMPARTIDAS: el arroz tiene el MISMO objeto "Granos" del repositorio.
        Producto arroz = productos.buscarPorCodigo("P001");
        System.out.println("¿Misma categoría? " + (arroz.getCategoria() == categorias.buscarPorNombre("Granos")));   // true

        // 4. Desactivar la leche: deja de aparecer en listar().
        productos.desactivar(2);
        System.out.println("Productos activos: " + productos.listar().size());   // 2

        // 5. El constructor 1 (el de la sesión 8) sigue funcionando.
        ProductoRepositorio otro = new ProductoRepositorio();
        System.out.println("Con el constructor 1: " + otro.listar().size() + " productos");   // 3
    }
}