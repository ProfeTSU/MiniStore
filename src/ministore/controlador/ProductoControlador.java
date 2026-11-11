/*
 * =========================================================================
 *  CLASE: ProductoControlador             PAQUETE: ministore.controlador
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Es el INTERMEDIARIO entre la ventana de productos y la bodega.
 *    Recibe los TEXTOS que el usuario escribió en la ventana, los VALIDA
 *    (campos vacíos, números mal escritos, códigos repetidos) y, si todo
 *    está bien, usa el repositorio. Devuelve un MENSAJE para la ventana.
 *    Cubre el RF3 (CRUD de productos) y el RF6 (búsqueda).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:
 *      - ProductoRepositorio   → para guardar, buscar, actualizar y desactivar.
 *      - CategoriaRepositorio  → para la lista de categorías de cmbCategoria.
 *      - Producto y Categoria  → crea los objetos y los entrega al repositorio.
 *    La usa:
 *      - VentanaProductos      → la vista.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    Producto y Categoria (modelo), CategoriaRepositorio y
 *    ProductoRepositorio con su segundo constructor y buscarPorCodigo().
 *
 *  REGLA DE MVC: el controlador NO conoce botones ni campos de texto.
 *  Recibe datos (String, int) y devuelve datos (mensajes y listas).
 *  Por eso se puede probar con un main, sin abrir ninguna ventana.
 * =========================================================================
 */
package ministore.controlador;   // Paquete de la capa que COORDINA la vista y los datos.

import java.util.ArrayList;
import ministore.modelo.Categoria;
import ministore.modelo.Producto;
import ministore.repositorio.CategoriaRepositorio;
import ministore.repositorio.ProductoRepositorio;

/**
 * Controlador de productos.
 *
 * @author Mauricio
 */
public class ProductoControlador {

    // =====================================================================
    // ATRIBUTOS: los dos repositorios con los que trabaja
    // =====================================================================

    private ProductoRepositorio productos;      // La bodega de productos.
    private CategoriaRepositorio categorias;   // La bodega de categorías.

    // =====================================================================
    // CONSTRUCTOR
    // Los repositorios LLEGAN DE AFUERA: el controlador no hace new de ellos.
    // ¿Por qué? Si cada controlador creara su propio repositorio, cada ventana
    // tendría SU PROPIA lista de productos, y lo que se guarda en una ventana
    // no se vería en otra. Los repositorios se crean UNA sola vez.
    // =====================================================================

    public ProductoControlador(ProductoRepositorio productos, CategoriaRepositorio categorias) {
        this.productos = productos;
        this.categorias = categorias;
    }

    // =====================================================================
    // CONSULTAS: la ventana las usa para llenar la tabla y la lista
    // =====================================================================

    // Todos los productos activos: para cargar tblProductos.
    public ArrayList<Producto> listarProductos() {
        return productos.listar();
    }

    // Los productos cuyo nombre contiene el texto: para el botón Buscar (RF6).
    // trim(): quita los espacios del inicio y del final ("  arroz " → "arroz").
    public ArrayList<Producto> buscarProductos(String texto) {
        return productos.buscarPorNombre(texto.trim());
    }

    // Las categorías activas: para llenar cmbCategoria.
    public ArrayList<Categoria> listarCategorias() {
        return categorias.listar();
    }

    // =====================================================================
    // VALIDACIÓN (private: solo la usan guardarProducto y actualizarProducto)
    // Revisa los textos del formulario EN ORDEN. Retorna "" si todo está bien,
    // o el mensaje del PRIMER error que encuentre.
    // =====================================================================

    private String validar(String codigo, String nombre, String compraTxt, String ventaTxt,
                           String stockTxt, String minimoTxt) {

        // Regla 1: lo que identifica al producto no puede quedar vacío.
        // isEmpty(): responde true si el texto no tiene ningún carácter.
        if (codigo.trim().isEmpty() || nombre.trim().isEmpty()) {
            return "El código y el nombre son obligatorios";
        }

        // Regla 2: los precios y el stock deben ser números.
        // try/catch: "intenta convertir los textos en números; si alguno no es
        // un número (por ejemplo "abc" o "4500,75" con coma), en lugar de cerrar
        // la aplicación, devuelve un mensaje".
        try {
            double compra = Double.parseDouble(compraTxt);   // "3200.50" → 3200.50
            double venta = Double.parseDouble(ventaTxt);
            int stock = Integer.parseInt(stockTxt);          // "20" → 20
            int minimo = Integer.parseInt(minimoTxt);

            // Regla 3: las reglas del negocio, para dar un mensaje claro.
            // (Producto también las revisa en sus set, pero en silencio:
            // si el dato es inválido, simplemente no lo cambia).
            if (compra <= 0) {
                return "El precio de compra debe ser mayor que 0";
            }
            if (venta <= compra) {
                return "El precio de venta debe ser mayor que el de compra";
            }
            if (stock < 0 || minimo < 0) {
                return "El stock no puede ser negativo";
            }
        } catch (NumberFormatException e) {                  // El error que lanza parse... con un texto inválido.
            return "Los precios y el stock deben ser números (decimales con punto)";
        }

        return "";                                           // Pasó todas las reglas.
    }

    // =====================================================================
    // C: GUARDAR un producto nuevo  →  botón Guardar
    // Recibe los textos del formulario y la posición elegida en cmbCategoria.
    // =====================================================================

    public String guardarProducto(String codigo, String nombre, String descripcion, int indiceCategoria,
                                  String compraTxt, String ventaTxt, String stockTxt, String minimoTxt,
                                  String rutaImagen) {

        // Paso 1: validar. Si hay error, se devuelve el mensaje y el método termina.
        String error = validar(codigo, nombre, compraTxt, ventaTxt, stockTxt, minimoTxt);
        if (!error.isEmpty()) {
            return error;
        }

        // Paso 2: ¿el código ya existe? Se le pregunta al repositorio.
        if (productos.buscarPorCodigo(codigo.trim()) != null) {
            return "Ya existe un producto con el código " + codigo;
        }

        // Paso 3: todo está bien → se convierten los textos, se crea el objeto y se guarda.
        Producto p = new Producto(codigo.trim(), nombre.trim(), categoria(indiceCategoria),
                Double.parseDouble(compraTxt), Double.parseDouble(ventaTxt), Integer.parseInt(stockTxt));
        p.setDescripcion(descripcion.trim());
        p.setStockMinimo(Integer.parseInt(minimoTxt));
        p.setRutaImagen(rutaImagen);
        productos.guardar(p);                                // El repositorio le asigna el id.
        return "Producto guardado";
    }

    // =====================================================================
    // U: ACTUALIZAR el producto seleccionado  →  botón Actualizar
    // Recibe además el id del producto que se eligió en la tabla.
    // =====================================================================

    public String actualizarProducto(int id, String codigo, String nombre, String descripcion, int indiceCategoria,
                                     String compraTxt, String ventaTxt, String stockTxt, String minimoTxt,
                                     String rutaImagen) {

        // Paso 1: ¿se eligió un producto? (la ventana envía -1 si no hay ninguno).
        Producto p = productos.buscarPorId(id);
        if (p == null) {
            return "Seleccione un producto de la tabla";
        }

        // Paso 2: validar los datos nuevos.
        String error = validar(codigo, nombre, compraTxt, ventaTxt, stockTxt, minimoTxt);
        if (!error.isEmpty()) {
            return error;
        }

        // Paso 3: si cambió el código, que no sea el de OTRO producto.
        Producto otro = productos.buscarPorCodigo(codigo.trim());
        if (otro != null && otro.getId() != id) {
            return "Ya existe otro producto con el código " + codigo;
        }

        // Paso 4: se actualizan los datos con los set (que aplican las reglas del modelo).
        p.setCodigo(codigo.trim());
        p.setNombre(nombre.trim());
        p.setDescripcion(descripcion.trim());
        p.setCategoria(categoria(indiceCategoria));
        p.setPrecioCompra(Double.parseDouble(compraTxt));    // Primero la compra...
        p.setPrecioVenta(Double.parseDouble(ventaTxt));      // ...porque la venta se compara con ella.
        p.setStock(Integer.parseInt(stockTxt));
        p.setStockMinimo(Integer.parseInt(minimoTxt));
        p.setRutaImagen(rutaImagen);
        productos.actualizar(p);
        return "Producto actualizado";
    }

    // =====================================================================
    // D: ELIMINAR (desactivar) el producto seleccionado  →  botón Eliminar
    // =====================================================================

    public String desactivarProducto(int id) {
        if (productos.desactivar(id)) {                      // true si existía y se desactivó.
            return "Producto eliminado";
        }
        return "Seleccione un producto de la tabla";
    }

    // =====================================================================
    // AYUDANTE (private)
    // cmbCategoria muestra TEXTOS ("Granos", "Lácteos"...), pero el producto
    // necesita el OBJETO Categoria. La posición elegida en la lista sirve
    // para encontrarlo: la posición 0 es la primera categoría, y así.
    // =====================================================================

    private Categoria categoria(int indice) {
        ArrayList<Categoria> lista = categorias.listar();
        if (indice >= 0 && indice < lista.size()) {          // ¿La posición existe?
            return lista.get(indice);
        }
        return new Categoria(0, "Sin categoría");            // Nada elegido (-1) o posición inválida.
    }
}