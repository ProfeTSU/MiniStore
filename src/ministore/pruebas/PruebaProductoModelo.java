package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Categoria;
import ministore.modelo.Producto;

/**
 * Prueba de la clase Producto (capa modelo). Se ejecuta con Shift + F6.
 * Comprueba los dos constructores, las reglas de precios y de stock,
 * y los métodos del negocio.
 *
 * @author Mauricio
 */
public class PruebaProductoModelo {

    public static void main(String[] args) {

        // Todo producto necesita una categoría: se crea primero.
        Categoria granos = new Categoria(1, "Granos");

        System.out.println("--- Prueba 1: constructor con datos completos ---");
        Producto arroz = new Producto("P001", "Arroz 1 kg", granos, 3200.50, 4500.75, 20);
        System.out.println(arroz.obtenerInformacion());   // P001 - Arroz 1 kg | Granos | $4500.75 | Stock: 20

        System.out.println("--- Prueba 2: constructor rápido (sobrecarga) ---");
        Producto leche = new Producto("P002", "Leche");   // Solo código y nombre.
        System.out.println(leche.obtenerInformacion());   // P002 - Leche | Sin categoría | $0.0 | Stock: 0
        System.out.println("¿Hay existencias? " + leche.hayExistencias());   // false

        System.out.println("--- Prueba 3: constructor con datos vacíos ---");
        Producto vacio = new Producto("", "");
        System.out.println(vacio.obtenerInformacion());   // Sin código - Sin nombre | Sin categoría | $0.0 | Stock: 0

        System.out.println("--- Prueba 4: reglas de los precios ---");
        arroz.setPrecioCompra(-100);                      // Negativo: se rechaza.
        System.out.println("Compra: " + arroz.getPrecioCompra());   // 3200.5
        arroz.setPrecioVenta(3000);                       // Menor que la compra (venta a pérdida): se rechaza.
        System.out.println("Venta: " + arroz.getPrecioVenta());     // 4500.75
        arroz.setPrecioVenta(4800);                       // Válido.
        System.out.println("Venta: " + arroz.getPrecioVenta());     // 4800.0

        System.out.println("--- Prueba 5: reglas del stock ---");
        arroz.setStock(-5);                               // Negativo: se rechaza.
        System.out.println("Stock: " + arroz.getStock());                        // 20
        System.out.println("¿Descontó 5? " + arroz.descontarStock(5));           // true
        System.out.println("Stock: " + arroz.getStock());                        // 15
        System.out.println("¿Descontó 50? " + arroz.descontarStock(50));         // false: no hay tantos
        System.out.println("¿Descontó 0? " + arroz.descontarStock(0));           // false: cantidad inválida
        System.out.println("¿Aumentó 10? " + arroz.aumentarStock(10));           // true: llegó mercancía
        System.out.println("Stock: " + arroz.getStock());                        // 25

        System.out.println("--- Prueba 6: reposición (stock mínimo = 5) ---");
        System.out.println("¿Reponer? " + arroz.necesitaReposicion());           // false: hay 25
        arroz.descontarStock(21);                         // Quedan 4.
        System.out.println("¿Reponer? " + arroz.necesitaReposicion());           // true: 4 <= 5
        System.out.println("¿Hay existencias? " + arroz.hayExistencias());       // true: aún quedan 4

        System.out.println("--- Prueba 7: utilidad por unidad ---");
        System.out.println("Utilidad: " + arroz.calcularUtilidad());             // 1599.5 (4800 - 3200.5)
    }
}