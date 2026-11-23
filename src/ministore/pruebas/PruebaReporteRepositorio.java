package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.repositorio.ReporteRepositorio;

/**
 * Prueba de ReporteRepositorio (versión MySQL). Se ejecuta con Shift + F6.
 * Solo CONSULTA. Los números de la diapositiva son los del script 02 recién
 * ejecutado: si se hicieron ventas después, cambian (y está bien).
 *
 * @author Mauricio
 */
public class PruebaReporteRepositorio {

    public static void main(String[] args) {

        ReporteRepositorio reportes = new ReporteRepositorio();

        // 1. Semanal (7 días) y mensual (30 días): cuántas ventas, total y utilidad.
        //    %.2f = dos decimales (con coma en un Windows en español).
        for (int dias : new int[]{7, 30}) {
            System.out.println("--- Últimos " + dias + " días ---");
            System.out.println("Ventas: " + reportes.ventasPorPeriodo(dias).size());
            System.out.println(String.format("Total vendido: %.2f", reportes.totalVendido(dias)));
            System.out.println(String.format("Utilidad: %.2f", reportes.utilidad(dias)));
        }

        // 2. Las filas de la tabla de ventas de la semana: { número, fecha, cliente, vendedor, total }.
        System.out.println("--- Ventas de la semana ---");
        for (Object[] f : reportes.ventasPorPeriodo(7)) {
            System.out.println(f[0] + " | " + f[2] + " | " + f[3] + " | " + f[4]);
        }

        // 3. Los 5 más vendidos del mes: { producto, unidades }.
        System.out.println("--- Más vendidos del mes ---");
        for (Object[] f : reportes.productosMasVendidos(30)) {
            System.out.println(f[0] + ": " + f[1]);
        }
    }
}