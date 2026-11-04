/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ministore;

import ministore.modelo.Categoria;
import ministore.modelo.Producto; 
import ministore.modelo.Cliente;
import ministore.modelo.DetalleVenta;
import ministore.modelo.VentaOld;
import ministore.modelo.Usuario;
import ministore.modelo.Rol;
import ministore.util.Seguridad;
import ministore.modelo.Tienda;
import ministore.modelo.Venta;
/**
 *
 * @author Mauricio
 */
public class MiniStore {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Categoria granos = new Categoria(1, "Granos");
        //System.out.println(granos.obtenerInformacion());
        
        //Categoria sinNombre = new Categoria(2,"");
        //System.out.println(sinNombre.obtenerInformacion());
        
       // Producto leche = new Producto("P002","Leche");
       // System.out.println(leche.obtenerInformacion());
       // System.out.println("¿Hay existencias?" + leche.hayExistencias());
        
        // Procuto vacio
       // Producto vacio = new Producto ("","");
       // System.out.println(vacio.obtenerInformacion());
       
        //Cliente ana = new Cliente("1234567", "Ana Pérez", "3001234567", "ana@correo.com");
        //System.out.println(ana.obtenerInformacion());           // 1234567 - Ana Pérez | 3001234567
        //System.out.println(ana.esConsumidorFinal());            // false

        //ana.setNombre("");                                      // Inválido: se rechaza.
        //System.out.println(ana.getNombre());                    // Ana Pérez

        //Cliente generico = new Cliente("222222222222", "Consumidor final", "", "");
        //System.out.println(generico.esConsumidorFinal());       // true

        //Cliente sinDocumento = new Cliente("", "Carlos", "3100000000", "");
        //System.out.println(sinDocumento.getDocumento());        // Sin documento
        //System.out.println(sinDocumento.isActivo());            // true
       //_______________________________________________________________________________
    /**
       Categoria granos = new Categoria(1, "Granos");
    Producto arroz = new Producto("P001", "Arroz 1 kg", granos, 3200.50, 4500.75, 20);

    DetalleVenta linea = new DetalleVenta(arroz, 2);         // 2 bultos de arroz.
    System.out.println(linea.obtenerInformacion());           // Arroz 1 kg x 2 = $9001.5
    System.out.println("Utilidad: $" + linea.calcularUtilidad());   // Utilidad: $2600.5

    arroz.setPrecioVenta(5000);                               // Mañana sube el arroz...
    System.out.println(linea.obtenerInformacion());           // ...y la línea NO cambia: $9001.5

    DetalleVenta invalida = new DetalleVenta(arroz, -3);     // Cantidad inválida.
    System.out.println(invalida.getCantidad());               // 1 (el valor por defecto)
   */
    /*
    Cliente ana = new Cliente("1234567", "Ana Pérez", "3001234567", "ana@correo.com");
    VentaOld venta = new VentaOld(ana);

    System.out.println(venta.getCliente().getNombre());   // Ana Pérez
    System.out.println(venta.getDetalles().size());       // 0  (carrito vacío)
    System.out.println(venta.getTotal());                 // 0.0
    System.out.println(venta.isConfirmada());             // false
    System.out.println(venta.getFecha());                 // La fecha y hora de este momento
    */
    /*_________________________________Pruebas de  venta */
    /*
            // ===== PRUEBA 1: crear la venta =====
        Cliente ana = new Cliente("1234567", "Ana Pérez", "3001234567", "ana@correo.com");
        VentaOld venta = new VentaOld(ana);
        System.out.println(venta.getCliente().getNombre());   // Ana Pérez
        System.out.println(venta.getDetalles().size());       // 0
        System.out.println(venta.getTotal());                 // 0.0
        System.out.println(venta.isConfirmada());             // false
 
        // ===== PRUEBA 2: agregar productos =====
        Categoria granos = new Categoria(1, "Granos");
        Categoria lacteos = new Categoria(2, "Lácteos");
        Producto arroz = new Producto("P001", "Arroz 1 kg", granos, 3200.50, 4500.75, 20);
        Producto leche = new Producto("P002", "Leche 1 L", lacteos, 2800, 3900, 30);
 
        System.out.println("¿Agregó 2 arroz? " + venta.agregarProducto(arroz, 2));     // true
        System.out.println("¿Agregó 1 leche? " + venta.agregarProducto(leche, 1));     // true
        System.out.println("¿Agregó 50 arroz? " + venta.agregarProducto(arroz, 50));   // false: solo hay 20
        System.out.println("¿Agregó 0 leche? " + venta.agregarProducto(leche, 0));     // false: cantidad inválida
        System.out.println("¿Agregó 3 arroz? " + venta.agregarProducto(arroz, 3));     // true: el arroz suma 5
        System.out.println("Líneas en el carrito: " + venta.getDetalles().size());     // 2 (no 3)
 
        // ===== PRUEBA 3: mostrar el carrito, el total y quitar =====
        for (DetalleVenta d : venta.getDetalles()) {
            System.out.println(d.obtenerInformacion());
        }
        System.out.println("Total: $" + venta.calcularTotal());          // 26403.75
        System.out.println("Utilidad: $" + venta.calcularUtilidad());    // 7601.25
        venta.quitarDetalle(1);                                          // Quita la leche.
        System.out.println("Total sin la leche: $" + venta.calcularTotal());   // 22503.75
 
        // ===== PRUEBA 4: confirmar =====
        System.out.println("Stock del arroz antes: " + arroz.getStock());     // 20
        System.out.println("¿Confirmó? " + venta.confirmar());                 // true
        System.out.println("Stock del arroz después: " + arroz.getStock());   // 15
        System.out.println("¿Confirmó otra vez? " + venta.confirmar());        // false
        System.out.println("¿Agregó después de confirmar? " + venta.agregarProducto(leche, 1));   // false
*/
    
    /* // Puebla clase Usuario, rol y seguridad
    Rol administrador = new Rol(1, "Administrador");
    Rol vendedor = new Rol(2, "Vendedor");
    System.out.println(administrador.getId() + " - " + administrador.getNombre());   // 1 - Administrador
    System.out.println(vendedor.getId() + " - " + vendedor.getNombre());             // 2 - Vendedor

    Usuario carlos = new Usuario("carlos", "carlos123", "Carlos Gómez", vendedor);
    Usuario ana = new Usuario("ana", "ana12345", "Ana Pérez", vendedor);
    System.out.println("¿Comparten el mismo objeto Rol? " + (carlos.getRol() == ana.getRol()));   // true
    
    System.out.println(Seguridad.hashSHA256("carlos123"));
    // ac9c2c34c9f7ad52528c3422af40a66e2e24aaf2a727831255413c9470158984
    System.out.println("¿Clave correcta? " + carlos.verificarContrasena("carlos123"));   // true
    System.out.println("¿Clave incorrecta? " + carlos.verificarContrasena("12345"));     // false
    System.out.println("¿Carlos es admin? " + carlos.esAdministrador());                // false
    System.out.println(carlos.obtenerInformacion());                                     // Carlos Gómez (Vendedor)
    */
    /*
    // Prueba Clase Tienda
    Tienda tienda = new Tienda("Minimercado Don José", "900.123.456-7", "Calle 5 # 12-30", "602 555 1234");
    System.out.print(tienda.obtenerEncabezado());

    tienda.setNombre("");                                    // Inválido: se rechaza.
    System.out.println("Nombre después de intentar dejarlo vacío: " + tienda.getNombre());

    Tienda sinDatos = new Tienda("", "", "Carrera 10", "3001112233");   // Sin nombre ni NIT.
    System.out.print(sinDatos.obtenerEncabezado());
    */
    
    // Prueba Clase venta nueva
    Rol vendedor = new Rol(2, "Vendedor");
    Usuario carlos = new Usuario("carlos", "carlos123", "Carlos Gómez", vendedor);
    Cliente ana = new Cliente("1234567", "Ana Pérez", "3001234567", "ana@correo.com");
    Categoria granos = new Categoria(1, "Granos");
    Categoria lacteos = new Categoria(2, "Lácteos");
    Producto arroz = new Producto("P001", "Arroz 1 kg", granos, 3200.50, 4500.75, 20);
    Producto leche = new Producto("P002", "Leche 1 L", lacteos, 2800, 3900, 30);
    Tienda tienda = new Tienda("Minimercado Don José", "900.123.456-7", "Calle 5 # 12-30", "602 555 1234");

    Venta venta = new Venta(ana, carlos);            // Constructor 2: con vendedor.
    venta.agregarProducto(arroz, 5);
    venta.agregarProducto(leche, 1);
    venta.setId(15);                                 // En la aplicación lo asigna el repositorio.
    System.out.println("¿Confirmó? " + venta.confirmar());
    System.out.println("Vendedor: " + venta.getVendedor().getNombreCompleto());
    System.out.print(venta.generarComprobante(tienda));

    Venta sinVendedor = new Venta(ana);              // Constructor 1: el de la sesión 8.
    sinVendedor.agregarProducto(leche, 2);
    System.out.print(sinVendedor.generarComprobante(tienda));
    

    
    
    }
    
}
