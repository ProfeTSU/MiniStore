package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Categoria;
import ministore.repositorio.CategoriaRepositorio;

/**
 * Prueba de CategoriaRepositorio. Se ejecuta con Shift + F6.
 * Comprueba los datos de prueba, el listado y la búsqueda por nombre.
 *
 * @author Mauricio
 */
public class PruebaCategoriaRepositorio {

    public static void main(String[] args) {

        CategoriaRepositorio categorias = new CategoriaRepositorio();   // Arranca con 4 categorías.

        // 1. Listar: deben aparecer las 4, con sus ids 1, 2, 3 y 4.
        for (Categoria c : categorias.listar()) {
            System.out.println(c.obtenerInformacion());
        }

        // 2. Buscar una que existe y una que no.
        System.out.println("¿Existe Lácteos? " + (categorias.buscarPorNombre("Lácteos") != null));   // true
        System.out.println("¿Existe Carnes? " + (categorias.buscarPorNombre("Carnes") != null));     // false

        // 3. Guardar una nueva: debe recibir el id 5.
        categorias.guardar(new Categoria(0, "Carnes"));
        System.out.println("Categorías: " + categorias.listar().size());                           // 5
    }
}