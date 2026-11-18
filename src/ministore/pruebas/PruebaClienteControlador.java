package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.controlador.ClienteControlador;
import ministore.modelo.Cliente;
import ministore.repositorio.ClienteRepositorio;

/**
 * Prueba de ClienteControlador con MySQL, SIN ventana. Se ejecuta con Shift + F6.
 * IMPORTANTE: CAMBIA los datos. Antes de ejecutarla, correr el script 02.
 *
 * @author Mauricio
 */
public class PruebaClienteControlador {

    public static void main(String[] args) {

        ClienteRepositorio clientes = new ClienteRepositorio();
        ClienteControlador controlador = new ClienteControlador(clientes);

        // 1. Validaciones de guardar.
        System.out.println(controlador.guardarCliente("", "Laura Díaz", "", ""));          // documento vacío
        System.out.println(controlador.guardarCliente("1234567", "Laura Díaz", "", ""));   // documento de Ana Pérez

        // 2. Guardar con datos correctos.
        System.out.println(controlador.guardarCliente("9876543", "Laura Díaz", "3001112233", ""));

        // 3. Buscar: el que existe y uno desactivado (Paola Mejía, 7788990).
        //    Si el cliente no existe (o está desactivado), buscarCliente devuelve null:
        //    hay que revisarlo ANTES de usarlo, o Java lanza NullPointerException.
        Cliente laura = controlador.buscarCliente("9876543");
        System.out.println("Buscar 9876543: " + (laura != null ? laura.getNombre() : "no encontrado"));   // Laura Díaz
        System.out.println("Buscar 7788990: " + controlador.buscarCliente("7788990"));                    // null

        // 4. Actualizar a Ana Pérez (id 2): primero con el documento de otro cliente, luego bien.
        System.out.println(controlador.actualizarCliente(2, "7654321", "Ana Pérez", "", ""));
        System.out.println(controlador.actualizarCliente(2, "1234567", "Ana Pérez", "3001234567", "ana@correo.com"));

        // 5. Eliminar: el Consumidor final (id 1) no se puede; Carlos Ruiz (id 3) sí.
        System.out.println(controlador.desactivarCliente(1));
        System.out.println(controlador.desactivarCliente(3));
        System.out.println(controlador.desactivarCliente(99));
        System.out.println("Clientes activos: " + controlador.listarClientes().size());   // 9
    }
}