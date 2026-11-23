/*
 * =========================================================================
 *  CLASE: ReporteControlador              PAQUETE: ministore.controlador
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es el intermediario entre VentanaReportes y los repositorios.
 *    Convierte el periodo elegido en cmbPeriodo (una POSICIÓN) en días,
 *    pide los datos del reporte y arma la lista de productos para reponer.
 *    Cubre el RF7 (reportes), el RF11 (utilidad), el RF12 (estadísticas)
 *    y el RF5 (inventario: productos por reponer).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ReporteRepositorio  → las consultas del reporte (SUM, GROUP BY).
 *      - ProductoRepositorio → los productos, para saber cuáles reponer.
 *      - Producto (modelo)   → su método necesitaReposicion().
 *    La usan:
 *      - VentanaReportes (y la crea Aplicacion, en abrirReportes).
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - ReporteRepositorio (probado) y ProductoRepositorio (versión MySQL).
 *
 *  REGLA DE MVC: no conoce botones ni tablas. Recibe y devuelve datos.
 * =========================================================================
 */
package ministore.controlador;

import java.util.ArrayList;
import ministore.modelo.Producto;
import ministore.repositorio.ProductoRepositorio;
import ministore.repositorio.ReporteRepositorio;

/**
 * Controlador de los reportes.
 *
 * @author Mauricio
 */
public class ReporteControlador {

    // =====================================================================
    // LOS PERIODOS
    // Dos arreglos en el MISMO orden: la posición 0 es "Semanal" y vale 7
    // días; la posición 1 es "Mensual" y vale 30 días. La ventana muestra
    // los textos y el controlador usa los días.
    // =====================================================================

    private static final String[] PERIODOS = {"Semanal (últimos 7 días)", "Mensual (últimos 30 días)"};
    private static final int[] DIAS = {7, 30};

    private ReporteRepositorio reportes;     // Llegan de afuera: los crea Aplicacion UNA vez.
    private ProductoRepositorio productos;

    public ReporteControlador(ReporteRepositorio reportes, ProductoRepositorio productos) {
        this.reportes = reportes;
        this.productos = productos;
    }

    // =====================================================================
    // EL PERIODO  →  cmbPeriodo
    // =====================================================================

    // Los textos para llenar cmbPeriodo.
    public String[] getPeriodos() {
        return PERIODOS;
    }

    // ¿La posición elegida existe? (-1 = nada elegido).
    public boolean periodoValido(int indice) {
        return indice >= 0 && indice < DIAS.length;
    }

    // =====================================================================
    // EL REPORTE  →  botón Generar
    // Cada método recibe la POSICIÓN del combo y la convierte en días.
    // La ventana debe revisar antes periodoValido().
    // =====================================================================

    public ArrayList<Object[]> ventasPorPeriodo(int indice) {
        return reportes.ventasPorPeriodo(DIAS[indice]);
    }

    public double totalVendido(int indice) {
        return reportes.totalVendido(DIAS[indice]);
    }

    public double utilidad(int indice) {
        return reportes.utilidad(DIAS[indice]);
    }

    public ArrayList<Object[]> productosMasVendidos(int indice) {
        return reportes.productosMasVendidos(DIAS[indice]);
    }

    // =====================================================================
    // PRODUCTOS PARA REPONER  →  botón Productos para reponer      (RF5)
    // Aquí SÍ hay un ciclo en Java: se recorren los productos activos y se
    // le pregunta a cada uno necesitaReposicion() (stock <= stock mínimo).
    // La regla ya existe en Producto desde las primeras semanas: no se repite.
    // =====================================================================

    public ArrayList<Producto> productosParaReponer() {
        ArrayList<Producto> lista = new ArrayList<>();
        for (Producto p : productos.listar()) {         // "Para cada producto p"
            if (p.necesitaReposicion()) {               // ¿Le queda poco?
                lista.add(p);
            }
        }
        return lista;
    }
}