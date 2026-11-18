package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Categoria;

/**
 * Prueba de la clase Categoria (capa modelo). Se ejecuta con Shift + F6.
 * Comprueba el constructor y la validación del nombre.
 *
 * @author Mauricio
 */
public class PruebaCategoriaModelo {

    public static void main(String[] args) {

        System.out.println("--- Prueba 1: crear una categoría válida ---");
        Categoria granos = new Categoria(1, "Granos");
        System.out.println(granos.obtenerInformacion());      // 1 - Granos

        System.out.println("--- Prueba 2: el constructor valida el nombre ---");
        Categoria sinNombre = new Categoria(2, "");           // Vacío: se rechaza.
        System.out.println(sinNombre.obtenerInformacion());   // 2 - Sin nombre
    }
}