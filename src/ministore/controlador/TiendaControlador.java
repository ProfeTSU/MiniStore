/*
 * =========================================================================
 *  CLASE: TiendaControlador               PAQUETE: ministore.controlador
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es el intermediario entre VentanaConfiguracion y TiendaRepositorio.
 *    Entrega los datos actuales de la tienda para mostrarlos en la ventana
 *    y, cuando el administrador los cambia, recibe los TEXTOS que escribió,
 *    los VALIDA y, si todo está bien, los guarda. Devuelve un MENSAJE.
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:      TiendaRepositorio y Tienda.
 *    La usan:  VentanaConfiguracion.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    Tienda (modelo) y TiendaRepositorio.
 *
 *  LAS REGLAS QUE VALIDA:
 *    - El nombre y el NIT son obligatorios: los dos aparecen en el
 *      comprobante y sin ellos la factura no identifica a la tienda.
 *    - Debe existir la fila de la tienda en MySQL (la crea el script 02).
 *
 *  REGLA DE MVC: no conoce botones ni campos de texto. Recibe y devuelve datos.
 * =========================================================================
 */
package ministore.controlador;

import ministore.modelo.Tienda;
import ministore.repositorio.TiendaRepositorio;

/**
 * Controlador de los datos de la tienda.
 *
 * @author Mauricio
 */
public class TiendaControlador {

    private TiendaRepositorio tiendas;       // Llega de afuera: se crea UNA sola vez.

    public TiendaControlador(TiendaRepositorio tiendas) {
        this.tiendas = tiendas;
    }

    // =====================================================================
    // CONSULTA
    // Los datos actuales de la tienda: la ventana los muestra al abrir.
    // Devuelve null si la tabla está vacía (no se ejecutó el script 02).
    // =====================================================================

    public Tienda obtenerTienda() {
        return tiendas.obtener();
    }

    // =====================================================================
    // ACTUALIZAR
    // Recibe los textos de los campos de la ventana. Cada if termina con
    // return: al primer problema se devuelve el mensaje y no se guarda nada.
    // Si todo está bien, el mensaje empieza por "Datos de la tienda".
    // =====================================================================

    public String actualizarTienda(String nombre, String nit, String direccion, String telefono) {
        // 1. Datos obligatorios: el nombre y el NIT van en el comprobante.
        //    trim() quita los espacios: "   " cuenta como vacío.
        if (nombre.trim().isEmpty() || nit.trim().isEmpty()) {
            return "El nombre y el NIT son obligatorios";
        }

        // 2. La tienda debe existir en MySQL: no se crean tiendas nuevas.
        Tienda t = tiendas.obtener();
        if (t == null) {
            return "No hay datos de la tienda. Ejecute el script 02";
        }

        // 3. Todo bien: se cambian los datos del objeto y se guardan en MySQL.
        t.setNombre(nombre.trim());
        t.setNit(nit.trim());
        t.setDireccion(direccion.trim());
        t.setTelefono(telefono.trim());
        tiendas.actualizar(t);
        return "Datos de la tienda guardados";
    }
}