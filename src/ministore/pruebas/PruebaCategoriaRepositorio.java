package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Categoria;
import ministore.repositorio.CategoriaRepositorio;

/**
 * Prueba de CategoriaRepositorio (versión MySQL). Se ejecuta con Shift + F6.
 * Comprueba el listado, la búsqueda por nombre y el guardado.
 *
 * IMPORTANTE: esta prueba CAMBIA los datos (guarda la categoría Carnes).
 * Antes de ejecutarla, correr el script 02_datos_prueba_ministore.sql en Workbench.
 * Si se ejecuta dos veces seguidas, la segunda falla con "Duplicate entry 'Carnes'":
 * es la prueba de que la categoría quedó guardada de verdad en MySQL.
 *
 * @author Mauricio
 */
public class PruebaCategoriaRepositorio {

    public static void main(String[] args) {

        CategoriaRepositorio categorias = new CategoriaRepositorio();   // Los datos están en MySQL.

        // 1. Listar: las 10 categorías del script 02, con sus ids del 1 al 10.
        for (Categoria c : categorias.listar()) {
            System.out.println(c.obtenerInformacion());
        }

        // 2. Buscar una que existe y una que no.
        System.out.println("¿Existe Lácteos? " + (categorias.buscarPorNombre("Lácteos") != null));   // true
        System.out.println("¿Existe Carnes? " + (categorias.buscarPorNombre("Carnes") != null));     // false

        // 3. Guardar una nueva: MySQL le asigna el id 11 (AUTO_INCREMENT).
        Categoria carnes = new Categoria(0, "Carnes");
        categorias.guardar(carnes);
        System.out.println("Id de Carnes en MySQL: " + carnes.getId());                              // 11
        System.out.println("Categorías: " + categorias.listar().size());                             // 11
    }
}