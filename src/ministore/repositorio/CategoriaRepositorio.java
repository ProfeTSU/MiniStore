/*
 * =========================================================================
 *  CLASE: CategoriaRepositorio            PAQUETE: ministore.repositorio
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es la "bodega" de las categorías (Granos, Lácteos, Aseo, Bebidas).
 *    Guarda TODAS las categorías de la tienda y permite consultarlas.
 *    Cubre el RF6: buscar y filtrar productos por categoría.
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - Categoria            → las guarda en una lista (ArrayList).
 *    La usan:
 *      - ProductoRepositorio  → toma de aquí las categorías de sus datos de prueba.
 *      - ProductoControlador  → le pide la lista para llenar cmbCategoria.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    La clase Categoria (paquete ministore.modelo), con setId() e isActivo().
 *
 *  ¿POR QUÉ EXISTE, SI ANTES NO HACÍA FALTA?
 *    Antes, ProductoRepositorio creaba sus propias categorías. Pero la ventana
 *    necesita mostrarlas en una lista desplegable: si los productos tuvieran
 *    sus categorías y la lista las suyas, existirían DOS objetos "Granos"
 *    distintos. Con este repositorio, todos comparten LOS MISMOS objetos.
 * =========================================================================
 */
package ministore.repositorio;   // Paquete de la CAPA DE DATOS: dónde se guardan los objetos.

import java.util.ArrayList;          // La lista que crece sola.
import ministore.modelo.Categoria;   // Categoria está en OTRO paquete: hay que importarla.

/**
 * La bodega de categorías. Hoy guarda en un ArrayList (en memoria);
 * en la sesión 12 se reemplaza por MySQL con los mismos métodos.
 *
 * @author Mauricio
 */
public class CategoriaRepositorio {

    // =====================================================================
    // ¿Qué necesita saber?  →  ATRIBUTOS
    // =====================================================================

    private ArrayList<Categoria> categorias;   // Aquí viven todas las categorías,
                                               // incluidas las desactivadas.

    private int siguienteId;                   // El número que recibirá la PRÓXIMA categoría:
                                               // 1, 2, 3... (en MySQL lo hará el AUTO_INCREMENT).

    // =====================================================================
    // ¿Cómo nace?  →  CONSTRUCTOR
    // No recibe parámetros: la bodega siempre arranca igual.
    // =====================================================================

    public CategoriaRepositorio() {
        categorias = new ArrayList<>();        // CREA la lista vacía. Sin esta línea valdría null
                                               // y el primer add haría fallar el programa.
        siguienteId = 1;                       // La primera categoría recibirá el id 1.
        cargarDatosDePrueba();                 // Llena la bodega para que no arranque vacía.
    }

    // =====================================================================
    // C: CREATE (crear)  →  guardar
    // =====================================================================

    // Guarda una categoría nueva. Es void porque siempre se puede guardar.
    public void guardar(Categoria categoria) {
        categoria.setId(siguienteId);          // 1. Le asigna su número.
        siguienteId++;                         // 2. Prepara el número de la siguiente.
        categorias.add(categoria);             // 3. La agrega al final de la lista.
    }

    // =====================================================================
    // R: READ (leer)  →  listar y buscarPorNombre
    // =====================================================================

    // Devuelve SOLO las categorías activas: son las que se muestran en cmbCategoria.
    // No devuelve la lista original, porque esa incluye las desactivadas.
    public ArrayList<Categoria> listar() {
        ArrayList<Categoria> activas = new ArrayList<>();   // Una lista nueva, vacía.
        for (Categoria c : categorias) {                    // "Para cada categoría c".
            if (c.isActivo()) {                             // Si está activa...
                activas.add(c);                             // ...pasa a la lista nueva.
            }
        }
        return activas;
    }

    // Busca una categoría por su nombre. Devuelve la categoría o null si no existe.
    // La usa ProductoRepositorio para asignarle a cada producto de prueba
    // el MISMO objeto que verá la lista desplegable.
    public Categoria buscarPorNombre(String nombre) {
        for (Categoria c : categorias) {
            if (c.getNombre().equals(nombre)) {   // Los textos se comparan con equals, nunca con ==.
                return c;                         // La encontró: return termina el método aquí.
            }
        }
        return null;                              // Recorrió toda la lista y no la encontró.
    }

    // =====================================================================
    // DATOS DE PRUEBA
    // private: solo lo usa el constructor; nadie de afuera debe llamarlo.
    // Usa guardar(), así cada categoría también recibe su id (1, 2, 3, 4).
    // =====================================================================

    private void cargarDatosDePrueba() {
        guardar(new Categoria(0, "Granos"));      // El id 0 es provisional:
        guardar(new Categoria(0, "Lácteos"));     // guardar() lo reemplaza
        guardar(new Categoria(0, "Aseo"));        // por 1, 2, 3 y 4.
        guardar(new Categoria(0, "Bebidas"));
    }
}