package ministore.repositorio;     // Paquete de la CAPA DE DATOS: dónde se guardan los objetos.

import java.util.ArrayList;        // La lista que crece sola (sesión 8).
import ministore.modelo.Categoria; // Las clases del modelo están en OTRO paquete del proyecto,
import ministore.modelo.Producto;  // por eso hay que importarlas, igual que en el main.

/**
 * PREGUNTA 1: ¿Qué representa en la tienda?
 * LA BODEGA: guarda TODOS los productos de la tienda y permite el CRUD
 * (crear, leer, actualizar y eliminar).
 *
 * Diferencia con el carrito:
 *   - El carrito (Venta) guarda los productos de UNA venta.
 *   - La bodega (ProductoRepositorio) guarda TODOS los productos.
 *
 * Hoy guarda en un ArrayList (en memoria: los datos se pierden al cerrar).
 * En la sesión 12 se reemplaza por una versión con MySQL que tendrá
 * EXACTAMENTE los mismos métodos, así que las ventanas no tendrán que cambiar.
 *
 * El repositorio NO valida los datos de cada producto: eso ya lo hace Producto
 * en sus set. El repositorio solo administra la COLECCIÓN.
 *
 * @author Mauricio
 */
public class ProductoRepositorio {

    // =====================================================================
    // PREGUNTA 2: ¿Qué necesita saber?  →  ATRIBUTOS
    // =====================================================================

    private ArrayList<Producto> productos;   // La lista donde viven todos los productos,
                                             // incluidos los desactivados.
    private int siguienteId;                 // El número que recibirá el PRÓXIMO producto
                                             // que se guarde: 1, 2, 3... (en MySQL lo hará
                                             // automáticamente el AUTO_INCREMENT).

    // =====================================================================
    // PREGUNTA 5: ¿Cómo nace?  →  CONSTRUCTOR
    // No recibe parámetros: la bodega siempre arranca igual.
    // =====================================================================

    public ProductoRepositorio() {
        productos = new ArrayList<>();       // CREA la lista vacía. Sin esta línea, productos
                                             // valdría null y el primer add fallaría.
        siguienteId = 1;                     // El primer producto recibirá el id 1.
        cargarDatosDePrueba();               // Llena la bodega con productos de ejemplo,
                                             // para que la aplicación no arranque vacía.
    }

    // =====================================================================
    // C: CREATE (crear)  →  guardar
    // =====================================================================

    // Guarda un producto nuevo en la bodega.
    // Es void porque no necesita responder nada: siempre se puede guardar.
    public void guardar(Producto producto) {
        producto.setId(siguienteId);         // 1. Le asigna su número (por eso Producto necesita setId).
        siguienteId++;                       // 2. Prepara el número del siguiente: 1 → 2 → 3...
        productos.add(producto);             // 3. Lo agrega al final de la lista.
    }

    // =====================================================================
    // R: READ (leer)  →  listar, buscarPorId, buscarPorNombre
    // =====================================================================

    // Devuelve SOLO los productos activos.
    // No devuelve la lista original "productos", porque esa incluye los desactivados
    // ("eliminados"), que no se deben mostrar en pantalla.
    public ArrayList<Producto> listar() {
        ArrayList<Producto> activos = new ArrayList<>();   // Una lista nueva, vacía.
        for (Producto p : productos) {       // for-each: "para cada producto p en productos".
            if (p.isActivo()) {              // Si el producto está activo...
                activos.add(p);              // ...lo pasa a la lista nueva.
            }
        }
        return activos;                      // Devuelve solo los activos.
    }

    // Busca un producto por su número (id).
    // Retorna el producto si lo encuentra, o null si no existe.
    // null significa "no hay ningún objeto": quien llame este método debe revisarlo.
    public Producto buscarPorId(int id) {
        for (Producto p : productos) {       // Revisa los productos uno por uno.
            if (p.getId() == id) {           // Los int se comparan con ==. (Los String, con equals.)
                return p;                    // Lo encontró: return termina el método aquí mismo.
            }
        }
        return null;                         // Recorrió toda la lista y no lo encontró.
    }

    // Busca los productos activos cuyo nombre CONTENGA el texto escrito (RF6).
    // No importan las mayúsculas: "arr", "ARR" y "Arr" encuentran "Arroz 1 kg".
    public ArrayList<Producto> buscarPorNombre(String texto) {
        ArrayList<Producto> encontrados = new ArrayList<>();
        for (Producto p : listar()) {        // Recorre solo los activos (reutiliza listar).
            // toLowerCase(): pasa el texto a minúsculas, para comparar sin importar mayúsculas.
            // contains():   responde true si un texto contiene a otro ("arroz 1 kg" contiene "arr").
            if (p.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                encontrados.add(p);
            }
        }
        return encontrados;                  // Puede estar vacía si no encontró nada.
    }

    // =====================================================================
    // U: UPDATE (actualizar)  →  actualizar
    // =====================================================================

    // Reemplaza el producto que tenga el mismo id que productoNuevo.
    // Retorna true si lo encontró y lo reemplazó, false si ese id no existe.
    // Aquí se usa un for CON POSICIÓN (i), y no un for-each, porque set
    // necesita saber en qué posición poner el producto nuevo.
    public boolean actualizar(Producto productoNuevo) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == productoNuevo.getId()) {   // ¿Es el mismo id?
                productos.set(i, productoNuevo);   // Pone el nuevo en la posición del viejo.
                return true;
            }
        }
        return false;                        // Ningún producto tenía ese id.
    }

    // =====================================================================
    // D: DELETE (eliminar)  →  desactivar  (ELIMINACIÓN LÓGICA)
    // =====================================================================

    // "Elimina" un producto SIN borrarlo de la lista: lo desactiva.
    // ¿Por qué no se usa remove? Porque un producto que ya se vendió aparece en
    // ventas anteriores; si se borrara, se perdería ese historial.
    // Después de desactivarlo, listar() ya no lo muestra.
    public boolean desactivar(int id) {
        Producto p = buscarPorId(id);        // Reutiliza la búsqueda: no repite el ciclo.
        if (p != null) {                     // != null significa "sí lo encontró".
            p.setActivo(false);              // Lo desactiva.
            return true;
        }
        return false;                        // No existe un producto con ese id.
    }

    // =====================================================================
    // DATOS DE PRUEBA
    // Es private porque solo lo usa el constructor: nadie de afuera debe llamarlo.
    // Usa guardar(), así cada producto de prueba también recibe su id (1, 2, 3).
    // =====================================================================

    private void cargarDatosDePrueba() {
        // Simplificación temporal: las categorías se crean aquí. Cuando exista
        // CategoriaRepositorio, se tomarán de allá para que todos compartan los mismos objetos.
        Categoria granos = new Categoria(1, "Granos");
        Categoria lacteos = new Categoria(2, "Lácteos");

        guardar(new Producto("P001", "Arroz 1 kg", granos, 3200.50, 4500.75, 20));   // id 1
        guardar(new Producto("P002", "Leche 1 L", lacteos, 2800, 3900, 30));         // id 2
        guardar(new Producto("P003", "Fríjol 500 g", granos, 4100, 5800, 15));       // id 3
    }
}