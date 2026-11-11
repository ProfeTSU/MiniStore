package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Categoria;
import ministore.modelo.Cliente;
import ministore.modelo.Producto;
import ministore.modelo.Rol;
import ministore.modelo.Tienda;
import ministore.modelo.Usuario;
import ministore.modelo.Venta;

/**
 * Prueba de Tienda (capa modelo) y del comprobante de venta (RF8),
 * que reúne casi todas las clases del modelo. Se ejecuta con Shift + F6.
 *
 * @author Mauricio
 */
public class PruebaTiendaModelo {

    public static void main(String[] args) {

        System.out.println("--- Prueba 1: crear la tienda ---");
        Tienda tienda = new Tienda("Minimercado Don José", "900.123.456-7", "Calle 5 # 12-30", "602 555 1234");
        // print y no println: el encabezado ya termina en salto de línea.
        System.out.print(tienda.obtenerEncabezado());
        // MINIMERCADO DON JOSÉ
        // NIT 900.123.456-7
        // Calle 5 # 12-30 - Tel. 602 555 1234

        System.out.println("--- Prueba 2: validaciones ---");
        tienda.setNombre("");                                // Vacío: se rechaza.
        tienda.setNit("");                                   // Vacío: se rechaza.
        System.out.println("Nombre: " + tienda.getNombre()); // Minimercado Don José
        System.out.println("NIT: " + tienda.getNit());       // 900.123.456-7
        Tienda sinDatos = new Tienda("", "", "", "");        // El constructor también valida.
        System.out.print(sinDatos.obtenerEncabezado());
        // MI TIENDA
        // NIT Sin NIT
        //  - Tel.

        System.out.println("--- Prueba 3: la tienda se muda ---");
        tienda.setDireccion("Carrera 10 # 8-15");            // Dirección y teléfono no se validan.
        System.out.print(tienda.obtenerEncabezado());
        // MINIMERCADO DON JOSÉ
        // NIT 900.123.456-7
        // Carrera 10 # 8-15 - Tel. 602 555 1234

        // ----- Datos para las ventas -----
        Categoria granos = new Categoria(1, "Granos");
        Categoria lacteos = new Categoria(2, "Lácteos");
        Producto arroz = new Producto("P001", "Arroz 1 kg", granos, 3200.50, 4500.75, 20);
        Producto leche = new Producto("P002", "Leche 1 L", lacteos, 2800, 3900, 30);
        Producto queso = new Producto("P004", "Queso 250 g", lacteos, 6500, 8900, 4);
        Cliente ana = new Cliente("1234567", "Ana Pérez", "3001234567", "ana@correo.com");
        Cliente generico = new Cliente("222222222222", "Consumidor final", "", "");
        Usuario carlos = new Usuario("carlos", "carlos123", "Carlos Gómez", new Rol(2, "Vendedor"));

        System.out.println("--- Prueba 4: comprobante con vendedor ---");
        Venta venta = new Venta(ana, carlos);    // Constructor completo: cliente y vendedor.
        venta.setId(1);                          // En la aplicación lo asigna el repositorio.
        venta.agregarProducto(arroz, 2);
        venta.agregarProducto(leche, 1);
        venta.confirmar();
        System.out.print(venta.generarComprobante(tienda));   // La venta USA la tienda: la recibe como parámetro.

        System.out.println("--- Prueba 5: comprobante sin vendedor ---");
        Venta rapida = new Venta(generico);      // Constructor de la sesión 8: sin vendedor.
        rapida.setId(2);
        rapida.agregarProducto(queso, 1);
        rapida.confirmar();
        System.out.print(rapida.generarComprobante(tienda));  // Sin la línea "Atendió".
    }
}