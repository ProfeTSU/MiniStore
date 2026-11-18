/*
 * =========================================================================
 *  CLASE: ClienteControlador              PAQUETE: ministore.controlador
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es el intermediario entre VentanaClientes y ClienteRepositorio.
 *    Recibe los TEXTOS que el usuario escribió, los VALIDA (campos vacíos,
 *    documentos repetidos, el Consumidor final) y, si todo está bien, usa
 *    el repositorio. Devuelve un MENSAJE para la ventana. Cubre el RF4.
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:      ClienteRepositorio y Cliente.
 *    La usan:  VentanaClientes (y más adelante la venta, para buscar al cliente).
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    Cliente (modelo) y ClienteRepositorio (versión MySQL).
 *
 *  REGLA DE MVC: no conoce botones ni campos de texto. Recibe y devuelve datos.
 * =========================================================================
 */
package ministore.controlador;

import java.util.ArrayList;
import ministore.modelo.Cliente;
import ministore.repositorio.ClienteRepositorio;

/**
 * Controlador de clientes.
 *
 * @author Mauricio
 */
public class ClienteControlador {

    private ClienteRepositorio clientes;     // Llega de afuera: se crea UNA sola vez.

    public ClienteControlador(ClienteRepositorio clientes) {
        this.clientes = clientes;
    }

    // ===== Consultas =====

    // Todos los clientes activos: para cargar tblClientes.
    public ArrayList<Cliente> listarClientes() {
        return clientes.listar();
    }

    // Busca un cliente ACTIVO por documento: para el botón Buscar (y para la venta).
    // Devuelve null si no existe o si está desactivado.
    public Cliente buscarCliente(String documento) {
        Cliente c = clientes.buscarPorDocumento(documento.trim());
        if (c != null && c.isActivo()) {
            return c;
        }
        return null;
    }

    // ===== C: guardar =====
    public String guardarCliente(String documento, String nombre, String telefono, String correo) {
        if (documento.trim().isEmpty() || nombre.trim().isEmpty()) {
            return "El documento y el nombre son obligatorios";              // 1. Datos obligatorios
        }
        if (clientes.buscarPorDocumento(documento.trim()) != null) {
            return "Ya existe un cliente con ese documento";                  // 2. Documento repetido
        }
        clientes.guardar(new Cliente(documento.trim(), nombre.trim(), telefono.trim(), correo.trim()));
        return "Cliente guardado";                                            // 3. Todo bien
    }

    // ===== U: actualizar el cliente seleccionado =====
    public String actualizarCliente(int id, String documento, String nombre, String telefono, String correo) {
        Cliente c = clientes.buscarPorId(id);
        if (c == null) {
            return "Seleccione un cliente de la tabla";                       // La ventana envía -1
        }
        if (documento.trim().isEmpty() || nombre.trim().isEmpty()) {
            return "El documento y el nombre son obligatorios";
        }
        Cliente otro = clientes.buscarPorDocumento(documento.trim());
        if (otro != null && otro.getId() != id) {                             // ¿El documento es de OTRO cliente?
            return "Ya existe otro cliente con ese documento";
        }
        c.setDocumento(documento.trim());
        c.setNombre(nombre.trim());
        c.setTelefono(telefono.trim());
        c.setCorreo(correo.trim());
        clientes.actualizar(c);
        return "Cliente actualizado";
    }

    // ===== D: eliminar (desactivar) =====
    public String desactivarCliente(int id) {
        Cliente c = clientes.buscarPorId(id);
        if (c == null) {
            return "Seleccione un cliente de la tabla";
        }
        if (c.esConsumidorFinal()) {                                          // Regla del negocio
            return "El Consumidor final no se puede eliminar";
        }
        clientes.desactivar(id);
        return "Cliente eliminado";
    }
}