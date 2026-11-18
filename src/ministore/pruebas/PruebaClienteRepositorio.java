package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Cliente;
import ministore.repositorio.ClienteRepositorio;

/**
 * Prueba de ClienteRepositorio (versión MySQL). Se ejecuta con Shift + F6.
 * IMPORTANTE: CAMBIA los datos. Antes de ejecutarla, correr el script 02.
 *
 * @author Mauricio
 */
public class PruebaClienteRepositorio {

    public static void main(String[] args) {

        ClienteRepositorio clientes = new ClienteRepositorio();

        // 1. Listar: los 9 clientes activos del script 02 (Paola Mejía está desactivada).
        for (Cliente c : clientes.listar()) {
            System.out.println(c.getId() + ". " + c.obtenerInformacion());
        }

        // 2. Buscar por documento.
        System.out.println("¿Existe 1234567? " + (clientes.buscarPorDocumento("1234567") != null));   // true (Ana Pérez)
        System.out.println("¿Existe 9999999? " + (clientes.buscarPorDocumento("9999999") != null));   // false

        // 3. Guardar uno nuevo: MySQL le asigna el id 11.
        Cliente laura = new Cliente("9876543", "Laura Díaz", "3001112233", "laura@correo.com");
        clientes.guardar(laura);
        System.out.println("Id de Laura en MySQL: " + laura.getId());                              // 11

        // 4. Actualizar su teléfono.
        laura.setTelefono("3159998877");
        clientes.actualizar(laura);
        System.out.println("Nuevo teléfono: " + clientes.buscarPorId(laura.getId()).getTelefono()); // 3159998877

        // 5. Desactivarla: deja de aparecer en listar(), pero SIGUE en MySQL.
        clientes.desactivar(laura.getId());
        System.out.println("Clientes activos: " + clientes.listar().size());                        // 9
        System.out.println("¿Laura sigue en MySQL? " + (clientes.buscarPorId(laura.getId()) != null)); // true
    }
}