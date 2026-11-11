/*
 * =========================================================================
 *  CLASE: ProductoRepositorio             PAQUETE: ministore.repositorio
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es LA BODEGA: guarda TODOS los productos de la tienda y permite el
 *    CRUD (crear, leer, actualizar y eliminar). Cubre el RF3 (gestión de
 *    productos), el RF5 (inventario) y el RF6 (búsqueda).
 *
 *    Diferencia con el carrito:
 *      - El carrito (Venta) guarda los productos de UNA venta.
 *      - La bodega (ProductoRepositorio) guarda TODOS los productos.
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - Producto              → los guarda en una lista (ArrayList).
 *      - CategoriaRepositorio  → toma de allí las categorías de los datos de prueba.
 *    La usan:
 *      - ProductoControlador   → y, a través de él, VentanaProductos.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    Las clases Producto y Categoria (modelo) y CategoriaRepositorio.
 *
 *  NUEVO EN ESTA VERSIÓN:
 *    - Un segundo constructor (sobrecarga) que recibe el CategoriaRepositorio.
 *    - El método buscarPorCodigo(), para no repetir códigos.
 *
 *  Hoy guarda en un ArrayList (en memoria: los datos se pierden al cerrar).
 *  En la sesión 12 se reemplaza por una versión con MySQL que tendrá
 *  EXACTAMENTE los mismos métodos, así que las ventanas no tendrán que cambiar.
 *  El repositorio NO valida los datos de cada producto: eso lo hacen Producto
 *  (en sus set) y el controlador. El repositorio solo administra la COLECCIÓN.
 * =========================================================================
 */
package ministore.repositorio;     // Paquete de la CAPA DE DATOS: dónde se guardan los objetos.

import java.util.ArrayList;        // La lista que crece sola.
import ministore.modelo.Categoria; // Las clases del modelo están en OTRO paquete del proyecto,
import ministore.modelo.Producto;  // por eso hay que importarlas.

/**
 * La bodega de productos.
 *
 * @author Mauricio
 */
public class ProductoRepositorio {

    // =====================================================================
    // ¿Qué necesita saber?  →  ATRIBUTOS
    // =====================================================================

    private ArrayList<Producto> productos;   // La lista donde viven todos los productos,
                                             // incluidos los desactivados.
    private int siguienteId;                 // El número que recibirá el PRÓXIMO producto
                                             // que se guarde: 1, 2, 3... (en MySQL lo hará
                                             // automáticamente el AUTO_INCREMENT).

    // =====================================================================
    // ¿Cómo nace?  →  CONSTRUCTORES (SOBRECARGA)
    // Dos constructores con el mismo nombre y distintos parámetros,
    // igual que en Producto y en Venta.
    // =====================================================================

    // Constructor 1 (el de la sesión 8): crea sus PROPIAS categorías.
    // Se conserva para que las pruebas anteriores sigan funcionando.
    // this(...) llama al constructor 2, para no repetir las mismas líneas.
    public ProductoRepositorio() {
        this(new CategoriaRepositorio());
    }

    // Constructor 2 (NUEVO): recibe las categorías COMPARTIDAS de la aplicación.
    // ¿Por qué? Para que los productos y la lista desplegable cmbCategoria
    // usen LOS MISMOS objetos Categoria, y no dos copias distintas de "Granos".
    public ProductoRepositorio(CategoriaRepositorio categorias) {
        productos = new ArrayList<>();       // CREA la lista vacía. Sin esta línea, productos
                                             // valdría null y el primer add fallaría.
        siguienteId = 1;                     // El primer producto recibirá el id 1.
        cargarDatosDePrueba(categorias);     // Llena la bodega con productos de ejemplo,
                                             // usando las categorías recibidas.
    }

    // =====================================================================
    // C: CREATE (crear)  →  guardar
    // =====================================================================

    // Guarda un producto nuevo. Es void porque siempre se puede guardar.
    public void guardar(Producto producto) {
        producto.setId(siguienteId);         // 1. Le asigna su número (por eso Producto necesita setId).
        siguienteId++;                       // 2. Prepara el número del siguiente: 1 → 2 → 3...
        productos.add(producto);             // 3. Lo agrega al final de la lista.
    }

    // =====================================================================
    // R: READ (leer)  →  listar, buscarPorId, buscarPorCodigo, buscarPorNombre
    // =====================================================================

    // Devuelve SOLO los productos activos.
    // No devuelve la lista original "productos", porque esa incluye los
    // desactivados ("eliminados"), que no se deben mostrar en pantalla.
    public ArrayList<Producto> listar() {
        ArrayList<Producto> activos = new ArrayList<>();   // Una lista nueva, vacía.
        for (Producto p : productos) {       // for-each: "para cada producto p en productos".
            if (p.isActivo()) {              // Si el producto está activo...
                activos.add(p);              // ...lo pasa a la lista nueva.
            }
        }
        return activos;
    }

    // Busca un producto por su número (id).
    // Retorna el producto si lo encuentra, o null si no existe.
    // null significa "no hay ningún objeto": quien llame este método debe revisarlo.
    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {           // Los int se comparan con ==. (Los String, con equals.)
                return p;                    // Lo encontró: return termina el método aquí mismo.
            }
        }
        return null;                         // Recorrió toda la lista y no lo encontró.
    }

    // NUEVO: busca un producto por su código ("P001").
    // ¿Para qué? El controlador lo usa antes de guardar, para no permitir
    // dos productos con el mismo código.
    // equalsIgnoreCase: como equals, pero sin importar mayúsculas ("p001" = "P001").
    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    // Busca los productos activos cuyo nombre CONTENGA el texto escrito (RF6).
    // No importan las mayúsculas: "arr", "ARR" y "Arr" encuentran "Arroz 1 kg".
    public ArrayList<Producto> buscarPorNombre(String texto) {
        ArrayList<Producto> encontrados = new ArrayList<>();
        for (Producto p : listar()) {        // Recorre solo los activos (reutiliza listar).
            // toLowerCase(): pasa el texto a minúsculas, para comparar sin importar mayúsculas.
            // contains():   responde true si un texto contiene a otro.
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
    // private: solo lo usa el constructor; nadie de afuera debe llamarlo.
    // Usa guardar(), así cada producto de prueba también recibe su id (1, 2, 3).
    // CAMBIO: ahora recibe el CategoriaRepositorio y toma de allí las categorías,
    // en lugar de crear unas nuevas.
    // =====================================================================

    private void cargarDatosDePrueba(CategoriaRepositorio categorias) {
        Categoria granos = categorias.buscarPorNombre("Granos");     // El MISMO objeto que
        Categoria lacteos = categorias.buscarPorNombre("Lácteos");   // verá cmbCategoria.

        guardar(new Producto("P001", "Arroz 1 kg", granos, 3200.50, 4500.75, 20));   // id 1
        guardar(new Producto("P002", "Leche 1 L", lacteos, 2800, 3900, 30));         // id 2
        guardar(new Producto("P003", "Fríjol 500 g", granos, 4100, 5800, 15));       // id 3
    }
}