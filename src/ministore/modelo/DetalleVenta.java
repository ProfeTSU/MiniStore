package ministore.modelo;   // Paquete donde vive la clase.

/**
 * PREGUNTA 1: ¿Qué representa en la tienda?
 * Una línea del carrito de compras: qué producto, cuántas unidades y a qué precio.
 * Una Venta tiene uno o más DetalleVenta (composición).
 *
 * @author Mauricio
 */
public class DetalleVenta {

    // =====================================================================
    // PREGUNTA 2: ¿Qué necesita saber?  →  ATRIBUTOS (todos privados)
    // =====================================================================

    private int id;                  // Identificador (llave primaria en MySQL, RF14).
    private Producto producto;       // Atributo de tipo objeto: el producto que se vende.
    private int cantidad;            // Cuántas unidades. Es int porque se hacen cuentas con ella.
    private double precioUnitario;   // Lo que PAGA EL CLIENTE por unidad, en el momento de la venta.
    private double costoUnitario;    // Lo que le CUESTA A LA TIENDA por unidad, en ese momento (RF11).

    // =====================================================================
    // PREGUNTA 5: ¿Cómo nace un objeto?  →  CONSTRUCTOR
    // =====================================================================

    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;                         // Guarda el producto.
        this.cantidad = 1;                                // Valor por defecto: al menos una unidad.
        setCantidad(cantidad);                            // Solo la cambia si es válida.
        this.precioUnitario = producto.getPrecioVenta();  // COPIA el precio de hoy: si mañana sube,
        this.costoUnitario = producto.getPrecioCompra();  // esta venta no cambia su total ni su utilidad.
    }

    // =====================================================================
    // PREGUNTA 3: ¿Qué reglas debe cumplir?  →  SET CON VALIDACIÓN
    // =====================================================================

    // No se venden cero ni menos unidades.
    public void setCantidad(int cantidad) {
        if (cantidad > 0) {
            this.cantidad = cantidad;
        }
    }

    // =====================================================================
    // PREGUNTA 4: ¿Qué necesita hacer o responder?  →  MÉTODOS
    // =====================================================================

    // Lo que paga el cliente por esta línea: precio x cantidad.
    public double calcularSubtotal() {
        return precioUnitario * cantidad;
    }

    // Lo que gana la tienda con esta línea: (precio - costo) x cantidad.
    public double calcularUtilidad() {
        return (precioUnitario - costoUnitario) * cantidad;
    }

    // La línea del carrito como texto. NO imprime (regla de MVC).
    public String obtenerInformacion() {
        return producto.getNombre() + " x " + cantidad + " = $" + calcularSubtotal();   // "Arroz 1 kg x 2 = $9001.5"
    }

    // =====================================================================
    // GET (generados con Insert Code → Getter)
    // precioUnitario y costoUnitario NO tienen set: hecha la venta, no se cambian.
    // =====================================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }
}