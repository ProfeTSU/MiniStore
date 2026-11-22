package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Tienda;
import ministore.repositorio.TiendaRepositorio;

/**
 * Prueba de TiendaRepositorio (versión MySQL). Se ejecuta con Shift + F6.
 *
 * Esta prueba CAMBIA el teléfono de la tienda y al final lo deja como estaba.
 * Si se interrumpe a mitad de camino, ejecutar otra vez el script 02.
 *
 * @author Mauricio
 */
public class PruebaTiendaRepositorio {

    public static void main(String[] args) {

        TiendaRepositorio tiendas = new TiendaRepositorio();

        // 1. Leer la tienda que insertó el script 02.
        System.out.println("--- Prueba 1: obtener ---");
        Tienda tienda = tiendas.obtener();
        System.out.println("Id: " + tienda.getId());                            // 1
        System.out.print(tienda.obtenerEncabezado());                           // El encabezado del comprobante

        // 2. Cambiar el teléfono y comprobar que MySQL lo guardó.
        //    Se vuelve a llamar obtener(): así se lee de MySQL y no del objeto.
        System.out.println("--- Prueba 2: actualizar ---");
        String telefonoOriginal = tienda.getTelefono();                         // Se guarda para restaurarlo
        tienda.setTelefono("602 999 0000");
        System.out.println("¿Se actualizó? " + tiendas.actualizar(tienda));     // true
        System.out.println("Teléfono en MySQL: " + tiendas.obtener().getTelefono());   // 602 999 0000

        // 3. Dejar el teléfono como estaba, para no cambiar los datos de prueba.
        tienda.setTelefono(telefonoOriginal);
        tiendas.actualizar(tienda);
        System.out.println("Teléfono restaurado: " + tiendas.obtener().getTelefono()); // 602 555 1234
    }
}