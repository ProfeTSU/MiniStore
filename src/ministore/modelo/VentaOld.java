package ministore.modelo;   // Paquete donde vive la clase.

// NOTA PARA LA CLASE: escribir primero el código SIN estos import, dejar que aparezca
// el error en rojo, y luego agregarlos con Ctrl + Shift + I para que los estudiantes
// entiendan para qué sirve un import.
import java.time.LocalDateTime;   // Clase de Java para guardar una fecha con su hora.
import java.util.ArrayList;       // Clase de Java para listas que crecen solas.

/**
 * PREGUNTA 1: ¿Qué representa en la tienda?
 * Una compra completa: a quién se le vende, cuándo, y el carrito con sus productos.
 * VERSIÓN DE LA SESIÓN 8: todavía sin vendedor (llega con Usuario, sesión 10)
 * y sin comprobante (llega con Tienda).
 *
 * @author Mauricio
 */
public class VentaOld {

    // =====================================================================
    // PREGUNTA 2: ¿Qué necesita saber?  →  ATRIBUTOS (todos privados)
    // =====================================================================

    private int id;                  // Número de la venta. Será el número del comprobante
                                     // y la llave primaria en MySQL. Lo asigna el repositorio.
    private LocalDateTime fecha;     // Cuándo se hizo la venta. Sirve para los reportes
                                     // semanales y mensuales (RF7).
    private double total;            // Lo que paga el cliente. NO se escribe: se CALCULA
                                     // sumando los subtotales del carrito.
    private boolean confirmada;      // true cuando la venta ya descontó el stock.
                                     // Evita que se confirme (y se descuente) dos veces.

    // Atributos de tipo objeto
    private Cliente cliente;                     // A quién se le vende.
    private ArrayList<DetalleVenta> detalles;    // EL CARRITO: una lista de líneas.
                                                 // Cada línea es un DetalleVenta (producto + cantidad).

    // =====================================================================
    // PREGUNTA 5: ¿Cómo nace un objeto?  →  CONSTRUCTOR
    // Recibe solo el cliente: es lo único que se sabe cuando empieza una venta.
    // Los productos se agregan después, uno por uno, con agregarProducto.
    // =====================================================================

    public VentaOld(Cliente cliente) {
        this.cliente = cliente;              // Guarda el cliente que llegó como parámetro.
        this.fecha = LocalDateTime.now();    // La fecha y la hora de ESTE momento.
        this.detalles = new ArrayList<>();   // CREA el carrito vacío. Sin esta línea la lista
                                             // no existiría (sería null) y el primer add fallaría.
        this.total = 0;                      // Todavía no hay nada que pagar.
        this.confirmada = false;             // Todavía no se ha confirmado.
    }

    /* ===== HASTA AQUÍ: PRUEBA 1 EN EL MAIN (crear la venta y ver sus datos) ===== */

    // =====================================================================
    // PREGUNTA 4: ¿Qué necesita hacer?  →  MÉTODOS DEL NEGOCIO
    // =====================================================================

    // Agrega un producto al carrito. Retorna true si pudo y false si no.
    // PREGUNTA 3 (reglas): no se modifica una venta confirmada, la cantidad debe ser
    // positiva y no se puede vender más de lo que hay en stock.
    // MEJORA: si el producto YA está en el carrito, suma la cantidad a esa línea
    // en lugar de crear una línea repetida.
    public boolean agregarProducto(Producto producto, int cantidad) {
        if (confirmada || cantidad <= 0) {                   // Regla 1: ¿venta confirmada O cantidad inválida?
            return false;                                    //   → no se agrega.
        }

        // Paso 1: buscar si el producto ya está en el carrito.
        for (DetalleVenta d : detalles) {                    // "Para cada línea d del carrito".
            if (d.getProducto().getCodigo().equals(producto.getCodigo())) {   // ¿Es el mismo producto?
                int nuevaCantidad = d.getCantidad() + cantidad;              // Lo que ya tenía + lo nuevo.
                if (nuevaCantidad > producto.getStock()) {   // Regla 2: ¿alcanza el stock para el total?
                    return false;                            //   → no se agrega.
                }
                d.setCantidad(nuevaCantidad);                // Ya estaba: solo aumenta la cantidad.
                return true;
            }
        }

        // Paso 2: si llegó aquí, el producto NO estaba en el carrito.
        if (cantidad > producto.getStock()) {                // Regla 2: ¿se pide más de lo que hay?
            return false;                                    //   → no se agrega.
        }
        detalles.add(new DetalleVenta(producto, cantidad));  // Crea la línea Y la guarda en el carrito.
        return true;                                         //   → avisa que sí se agregó.
    }

    /* ===== HASTA AQUÍ: PRUEBA 2 EN EL MAIN (agregar productos al carrito) ===== */

    // Quita una línea del carrito según su posición. Retorna true si pudo.
    public boolean quitarDetalle(int indice) {
        if (!confirmada && indice >= 0 && indice < detalles.size()) {   // ¿Sin confirmar y la posición existe?
            detalles.remove(indice);                         // La quita. Las líneas de la derecha se corren.
            return true;
        }
        return false;                                        // VentaOld confirmada o posición inválida.
    }

    // Suma los subtotales de todas las líneas: lo que paga el cliente.
    public double calcularTotal() {
        double suma = 0;                                     // El acumulador empieza en 0.
        for (DetalleVenta d : detalles) {                    // Para cada línea del carrito...
            suma = suma + d.calcularSubtotal();              // ...suma su subtotal (precio x cantidad).
        }
        total = suma;                                        // Guarda el resultado en el atributo.
        return total;
    }

    // Suma la utilidad de todas las líneas: lo que gana la tienda con esta venta (RF11).
    public double calcularUtilidad() {
        double suma = 0;
        for (DetalleVenta d : detalles) {
            suma = suma + d.calcularUtilidad();              // (precio - costo) x cantidad de cada línea.
        }
        return suma;
    }

    /* ===== HASTA AQUÍ: PRUEBA 3 EN EL MAIN (mostrar el carrito, el total y quitar) ===== */

    // Confirma la venta: descuenta del stock cada producto del carrito.
    // Primero VERIFICA todo y después DESCUENTA, para no dejar una venta a medias.
    public boolean confirmar() {
        if (confirmada || detalles.isEmpty()) {              // ¿Ya confirmada o carrito vacío?
            return false;                                    //   → no se puede confirmar.
        }
        for (DetalleVenta d : detalles) {                    // Recorrido 1: ¿todavía hay stock para todo?
            if (d.getCantidad() > d.getProducto().getStock()) {
                return false;                                // Si falta uno, NO se descuenta nada.
            }
        }
        for (DetalleVenta d : detalles) {                    // Recorrido 2: ahora sí descuenta.
            d.getProducto().descontarStock(d.getCantidad());
        }
        calcularTotal();                                     // Deja el total actualizado.
        confirmada = true;                                   // Ya no se puede modificar ni confirmar otra vez.
        return true;
    }

    /* ===== HASTA AQUÍ: PRUEBA 4 EN EL MAIN (confirmar y ver el stock) ===== */

    // =====================================================================
    // GET: consultar los datos (generados con Insert Code → Getter)
    // =====================================================================

    public int getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }

    public boolean isConfirmada() {       // En los boolean se usa "is" en lugar de "get".
        return confirmada;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public ArrayList<DetalleVenta> getDetalles() {   // Permite recorrer el carrito desde afuera,
        return detalles;                             // por ejemplo para mostrarlo en pantalla.
    }

    // =====================================================================
    // SET
    // NetBeans generó un set por cada atributo, pero en VentaOld la mayoría
    // NO deben usarse: romperían las reglas del negocio. Por eso están
    // comentados (Java los ignora). Solo setId queda activo.
    // =====================================================================

    // ✓ ACTIVO: el repositorio asigna el número de la venta.
    public void setId(int id) {
        this.id = id;
    }

    // ✗ DESACTIVADO: la fecha es la del momento en que se hizo la venta.
    //   Si se pudiera cambiar, una venta de hoy podría aparecer en los
    //   reportes del mes pasado (RF7).
    /*
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    */

    // ✗ DESACTIVADO: el total NO se escribe, se CALCULA sumando el carrito.
    //   Con este set, cualquiera podría hacer venta.setTotal(100) en una
    //   compra de $22.503 y la venta quedaría con un total falso.
    /*
    public void setTotal(double total) {
        this.total = total;
    }
    */

    // ✗ DESACTIVADO: solo el método confirmar() debe decidir cuándo se
    //   confirma la venta. Con setConfirmada(false), una venta ya confirmada
    //   podría confirmarse otra vez y descontar el stock dos veces.
    /*
    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }
    */

    // ✗ DESACTIVADO: el cliente se define al crear la venta, en el constructor.
    //   Cambiarlo después haría que una compra ya registrada quede a nombre
    //   de otra persona.
    /*
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    */

    // ✗ DESACTIVADO: permitiría reemplazar el carrito completo por otra lista,
    //   metiendo productos SIN pasar por agregarProducto y sus validaciones
    //   de stock. Los productos solo deben entrar por agregarProducto.
    /*
    public void setDetalles(ArrayList<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
    */
}