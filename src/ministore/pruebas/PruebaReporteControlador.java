package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.controlador.ReporteControlador;
import ministore.modelo.Producto;
import ministore.repositorio.ProductoRepositorio;
import ministore.repositorio.ReporteRepositorio;

/**
 * Prueba de ReporteControlador con MySQL, SIN ventana. Se ejecuta con Shift + F6.
 * Solo CONSULTA. Los números son los del script 02 recién ejecutado.
 *
 * @author Mauricio
 */
public class PruebaReporteControlador {

    public static void main(String[] args) {

        ReporteControlador controlador = new ReporteControlador(new ReporteRepositorio(), new ProductoRepositorio());

        // 1. Los textos de cmbPeriodo y la validación de la posición.
        System.out.println("--- 1. Los periodos ---");
        String[] periodos = controlador.getPeriodos();
        for (int i = 0; i < periodos.length; i++) {
            System.out.println("Posición " + i + ": " + periodos[i]);
        }
        System.out.println("¿Posición -1 válida? " + controlador.periodoValido(-1));   // false

        // 2. Lo que hará el botón Generar con la posición 1 (Mensual).
        System.out.println("--- 2. Reporte mensual (posición 1) ---");
        System.out.println("Ventas: " + controlador.ventasPorPeriodo(1).size());                  // 11
        System.out.println(String.format("Total vendido: %.2f", controlador.totalVendido(1)));
        System.out.println(String.format("Utilidad: %.2f", controlador.utilidad(1)));
        System.out.println("Más vendido: " + controlador.productosMasVendidos(1).get(0)[0]);     // Leche 1 L

        // 3. Productos para reponer: stock <= stock mínimo.
        System.out.println("--- 3. Productos para reponer ---");
        for (Producto p : controlador.productosParaReponer()) {
            System.out.println(p.getCodigo() + " - " + p.getNombre()
                    + ": stock " + p.getStock() + " (mínimo " + p.getStockMinimo() + ")");
        }
    }
}