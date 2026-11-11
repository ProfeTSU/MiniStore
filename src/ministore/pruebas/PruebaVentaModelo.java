package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Categoria;
import ministore.modelo.Cliente;
import ministore.modelo.DetalleVenta;
import ministore.modelo.Producto;
import ministore.modelo.Venta;

/**
 * Prueba de la clase Venta (capa modelo). Se ejecuta con Shift + F6.
 * Recorre una compra completa: carrito vacío, agregar, repetir, rechazar,
 * quitar, totalizar y confirmar.
 *
 * @author Mauricio
 */
public class PruebaVentaModelo {

    public static void main(String[] args) {

        // Datos de partida: categorías, productos y un cliente.
        Categoria granos = new Categoria(1, "Granos");
        Categoria lacteos = new Categoria(2, "Lácteos");
        Producto arroz = new Producto("P001", "Arroz 1 kg", granos, 3200.50, 4500.75, 20);
        Producto leche = new Producto("P002", "Leche 1 L", lacteos, 2800, 3900, 30);
        Producto queso = new Producto("P004", "Queso 250 g", lacteos, 6500, 8900, 4);
        Cliente ana = new Cliente("1234567", "Ana Pérez", "3001234567", "ana@correo.com");

        System.out.println("--- Prueba 1: la venta nace con el carrito vacío ---");
        Venta venta = new Venta(ana);
        System.out.println("Líneas: " + venta.getDetalles().size());     // 0
        System.out.println("Total: " + venta.calcularTotal());            // 0.0
        System.out.println("¿Confirmada? " + venta.isConfirmada());       // false
        System.out.println("¿Confirmó vacía? " + venta.confirmar());      // false: no hay qué vender

        System.out.println("--- Prueba 2: agregar productos ---");
        System.out.println("¿Agregó arroz? " + venta.agregarProducto(arroz, 2));   // true
        System.out.println("¿Agregó leche? " + venta.agregarProducto(leche, 1));   // true
        for (DetalleVenta d : venta.getDetalles()) {
            System.out.println("  " + d.obtenerInformacion());
        }
        //   Arroz 1 kg x 2 = $9001.5
        //   Leche 1 L x 1 = $3900.0

        System.out.println("--- Prueba 3: repetir un producto suma la cantidad ---");
        System.out.println("¿Agregó 3 arroces más? " + venta.agregarProducto(arroz, 3));   // true
        System.out.println("Líneas: " + venta.getDetalles().size());      // 2: no se creó otra línea
        System.out.println("  " + venta.getDetalles().get(0).obtenerInformacion());   // Arroz 1 kg x 5 = $22503.75

        System.out.println("--- Prueba 4: lo que el carrito rechaza ---");
        System.out.println("¿Leche x 0? " + venta.agregarProducto(leche, 0));      // false: cantidad inválida
        System.out.println("¿Leche x -2? " + venta.agregarProducto(leche, -2));    // false: cantidad inválida
        System.out.println("¿Queso x 5? " + venta.agregarProducto(queso, 5));      // false: solo hay 4
        System.out.println("¿Arroz x 16? " + venta.agregarProducto(arroz, 16));    // false: 5 + 16 = 21 y hay 20
        System.out.println("Líneas: " + venta.getDetalles().size());               // 2: nada cambió

        System.out.println("--- Prueba 5: quitar una línea ---");
        venta.agregarProducto(queso, 1);                                  // Ahora hay 3 líneas.
        System.out.println("Líneas: " + venta.getDetalles().size());      // 3
        System.out.println("¿Quitó la línea 2? " + venta.quitarDetalle(2));   // true: el queso (posición 2)
        System.out.println("¿Quitó la línea 5? " + venta.quitarDetalle(5));   // false: no existe
        System.out.println("Líneas: " + venta.getDetalles().size());      // 2

        System.out.println("--- Prueba 6: total y utilidad ---");
        System.out.println("Total: " + venta.calcularTotal());            // 26403.75 (22503.75 + 3900)
        System.out.println("Utilidad: " + venta.calcularUtilidad());      // 7601.25 (6501.25 + 1100)
        System.out.println("Stock arroz: " + arroz.getStock());           // 20: aún no se descuenta
        System.out.println("Stock leche: " + leche.getStock());           // 30

        System.out.println("--- Prueba 7: confirmar la venta ---");
        System.out.println("¿Confirmó? " + venta.confirmar());            // true
        System.out.println("¿Confirmada? " + venta.isConfirmada());       // true
        System.out.println("Stock arroz: " + arroz.getStock());           // 15: ahora sí se descontó
        System.out.println("Stock leche: " + leche.getStock());           // 29

        System.out.println("--- Prueba 8: una venta confirmada ya no cambia ---");
        System.out.println("¿Confirmó otra vez? " + venta.confirmar());   // false: no descuenta dos veces
        System.out.println("Stock arroz: " + arroz.getStock());           // 15: sigue igual
        System.out.println("¿Agregó arroz? " + venta.agregarProducto(arroz, 1));   // false
        System.out.println("¿Quitó la línea 0? " + venta.quitarDetalle(0));        // false

        System.out.println("--- Prueba 9: ¿por qué confirmar usa DOS recorridos? ---");
        Venta otra = new Venta(ana);
        otra.agregarProducto(leche, 1);                   // Hay 29: se acepta.
        otra.agregarProducto(queso, 4);                   // Hay 4: se acepta.
        queso.descontarStock(2);                          // Mientras tanto, otra caja vende 2 quesos.
        System.out.println("Stock queso: " + queso.getStock());           // 2
        System.out.println("¿Confirmó? " + otra.confirmar());             // false: pide 4 y quedan 2
        System.out.println("Stock leche: " + leche.getStock());           // 29: la leche NO se descontó
        System.out.println("¿Confirmada? " + otra.isConfirmada());        // false
    }
}