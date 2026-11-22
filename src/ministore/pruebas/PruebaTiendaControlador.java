package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.controlador.TiendaControlador;
import ministore.modelo.Tienda;
import ministore.repositorio.TiendaRepositorio;

/**
 * Prueba de TiendaControlador con MySQL, SIN ventana. Se ejecuta con Shift + F6.
 *
 * Esta prueba CAMBIA la dirección de la tienda y al final la deja como estaba.
 * Si se interrumpe a mitad de camino, ejecutar otra vez el script 02.
 *
 * @author Mauricio
 */
public class PruebaTiendaControlador {

    public static void main(String[] args) {

        TiendaRepositorio tiendas = new TiendaRepositorio();
        TiendaControlador controlador = new TiendaControlador(tiendas);

        // 1. Los datos actuales, como los mostrará la ventana al abrir.
        System.out.println("--- Prueba 1: obtener ---");
        Tienda t = controlador.obtenerTienda();
        System.out.println(t.getNombre() + " | " + t.getNit());

        // 2. Validaciones: nombre vacío y NIT con solo espacios.
        System.out.println("--- Prueba 2: validaciones ---");
        System.out.println(controlador.actualizarTienda("", t.getNit(), t.getDireccion(), t.getTelefono()));
        System.out.println(controlador.actualizarTienda(t.getNombre(), "   ", t.getDireccion(), t.getTelefono()));

        // 3. Datos correctos: se cambia la dirección y se comprueba en MySQL.
        System.out.println("--- Prueba 3: actualizar ---");
        String direccionOriginal = t.getDireccion();                // Se guarda para restaurarla
        System.out.println(controlador.actualizarTienda(t.getNombre(), t.getNit(),
                "Carrera 10 # 20-30", t.getTelefono()));
        System.out.println("Dirección en MySQL: " + controlador.obtenerTienda().getDireccion());

        // 4. Dejar la dirección como estaba, para no cambiar los datos de prueba.
        controlador.actualizarTienda(t.getNombre(), t.getNit(), direccionOriginal, t.getTelefono());
        System.out.println("Dirección restaurada: " + controlador.obtenerTienda().getDireccion());
    }
}