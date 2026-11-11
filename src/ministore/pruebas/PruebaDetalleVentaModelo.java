package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Categoria;
import ministore.modelo.DetalleVenta;
import ministore.modelo.Producto;

/**
 * Prueba de la clase DetalleVenta (capa modelo). Se ejecuta con Shift + F6.
 * Comprueba los cálculos de cada línea del carrito y, sobre todo,
 * que el detalle COPIA el precio y el costo del momento de la venta.
 *
 * @author Mauricio
 */
public class PruebaDetalleVentaModelo {

    public static void main(String[] args) {

        // Un detalle necesita un producto, y un producto necesita una categoría.
        Categoria granos = new Categoria(1, "Granos");
        Producto arroz = new Producto("P001", "Arroz 1 kg", granos, 3200.50, 4500.75, 20);

        System.out.println("--- Prueba 1: crear una línea del carrito ---");
        DetalleVenta detalle = new DetalleVenta(arroz, 2);
        System.out.println(detalle.obtenerInformacion());                     // Arroz 1 kg x 2 = $9001.5
        System.out.println("Precio unitario: " + detalle.getPrecioUnitario()); // 4500.75 (copiado del producto)
        System.out.println("Costo unitario: " + detalle.getCostoUnitario());   // 3200.5 (copiado del producto)

        System.out.println("--- Prueba 2: subtotal y utilidad ---");
        System.out.println("Subtotal: " + detalle.calcularSubtotal());   // 9001.5 (4500.75 x 2)
        System.out.println("Utilidad: " + detalle.calcularUtilidad());   // 2600.5 ((4500.75 - 3200.5) x 2)

        System.out.println("--- Prueba 3: cantidades inválidas ---");
        DetalleVenta unidad = new DetalleVenta(arroz, 0);   // Cero: el constructor deja 1 por defecto.
        System.out.println(unidad.obtenerInformacion());     // Arroz 1 kg x 1 = $4500.75
        unidad.setCantidad(-3);                              // Negativa: se rechaza.
        System.out.println("Cantidad: " + unidad.getCantidad());   // 1

        System.out.println("--- Prueba 4: cambiar la cantidad ---");
        unidad.setCantidad(3);                               // Válida: se acepta.
        System.out.println(unidad.obtenerInformacion());     // Arroz 1 kg x 3 = $13502.25

        System.out.println("--- Prueba 5: el detalle NO toca el stock ---");
        DetalleVenta grande = new DetalleVenta(arroz, 100);  // Más de lo que hay (20)...
        System.out.println(grande.obtenerInformacion());     // ...y se acepta: Arroz 1 kg x 100 = $450075.0
        System.out.println("Stock del arroz: " + arroz.getStock());   // 20: no se descontó nada

        System.out.println("--- Prueba 6: suben los precios del arroz ---");
        arroz.setPrecioCompra(3500);       // Primero la compra...
        arroz.setPrecioVenta(5000);        // ...porque la venta debe ser mayor que la compra.
        System.out.println("Precio del producto hoy: " + arroz.getPrecioVenta());   // 5000.0
        // El detalle de la prueba 1 conserva los precios del momento en que se creó:
        System.out.println("Precio en el detalle: " + detalle.getPrecioUnitario()); // 4500.75
        System.out.println("Costo en el detalle: " + detalle.getCostoUnitario());   // 3200.5
        System.out.println(detalle.obtenerInformacion());                           // Arroz 1 kg x 2 = $9001.5
        System.out.println("Utilidad: " + detalle.calcularUtilidad());              // 2600.5: no cambió

        System.out.println("--- Prueba 7: una venta nueva usa el precio nuevo ---");
        DetalleVenta nuevo = new DetalleVenta(arroz, 2);
        System.out.println(nuevo.obtenerInformacion());                  // Arroz 1 kg x 2 = $10000.0
        System.out.println("Utilidad: " + nuevo.calcularUtilidad());     // 3000.0 ((5000 - 3500) x 2)
    }
}