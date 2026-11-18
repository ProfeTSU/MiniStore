package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Producto;
import ministore.repositorio.CategoriaRepositorio;
import ministore.repositorio.ProductoRepositorio;

/**
 * Prueba de ProductoRepositorio (versión MySQL). Se ejecuta con Shift + F6.
 *
 * IMPORTANTE: esta prueba CAMBIA los datos de MySQL (desactiva la leche).
 * Antes de ejecutarla, correr el script 02_datos_prueba_ministore.sql en
 * Workbench, para empezar siempre con los mismos datos.
 *
 * @author Mauricio
 */
public class PruebaProductoRepositorio {

    public static void main(String[] args) {

        CategoriaRepositorio categorias = new CategoriaRepositorio();
        ProductoRepositorio productos = new ProductoRepositorio(categorias);

        // 1. Listar: los 16 productos activos del script 02 (la avena está desactivada).
        for (Producto p : productos.listar()) {
            System.out.println(p.getId() + ". " + p.obtenerInformacion());
        }

        // 2. Buscar por código, sin importar mayúsculas.
        System.out.println("¿Existe P001? " + (productos.buscarPorCodigo("P001") != null));   // true
        System.out.println("¿Existe p002? " + (productos.buscarPorCodigo("p002") != null));   // true
        System.out.println("¿Existe P099? " + (productos.buscarPorCodigo("P099") != null));   // false

        // 3. Buscar por nombre (RF6).
        System.out.println("Con 'ar': " + productos.buscarPorNombre("ar").size() + " productos");   // 2 (el arroz y el jabón en barra)

        // 4. La categoría llega con el JOIN.
        //    En MySQL se compara por id: cada consulta crea objetos NUEVOS.
        Producto arroz = productos.buscarPorCodigo("P001");
        System.out.println("Categoría del arroz: " + arroz.getCategoria().getNombre());   // Granos
        System.out.println("¿Misma categoría? "
                + (arroz.getCategoria().getId() == categorias.buscarPorNombre("Granos").getId()));   // true

        // 5. Desactivar la leche: deja de aparecer en listar(), pero SIGUE en MySQL.
        productos.desactivar(2);
        System.out.println("Productos activos: " + productos.listar().size());     // 15
        System.out.println("¿La leche sigue en MySQL? " + (productos.buscarPorId(2) != null));   // true
    }
}