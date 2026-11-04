/*
 * =========================================================================
 *  CLASE: Tienda                          PAQUETE: ministore.modelo
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Representa el negocio que usa el MiniStore (por ejemplo, "Minimercado
 *    Don José"). Sus datos aparecen en el encabezado del comprobante de
 *    venta (RF8) y cubren el RF9: "nombre, dirección y contacto".
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa: ninguna clase.
 *    La usan:
 *      - Venta                 → DEPENDENCIA (flecha punteada «usa»): la recibe
 *                                como parámetro en generarComprobante(tienda),
 *                                pero NO la guarda como atributo.
 *      - TiendaRepositorio     → guarda el único objeto Tienda del sistema.
 *      - TiendaControlador     → valida y actualiza sus datos.
 *      - VentanaConfiguracion  → donde el administrador edita los datos.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    Solo el paquete ministore.modelo. No depende de ninguna otra clase.
 *
 *  PARTICULARIDAD: de esta clase existe UN SOLO objeto en todo el sistema,
 *  porque la aplicación pertenece a un solo negocio.
 * =========================================================================
 */
package ministore.modelo;   // Paquete del modelo: los datos y las reglas del negocio.

/**
 * PREGUNTA 1: ¿Qué representa en la tienda?
 * El propio negocio: sus datos de identificación y de contacto.
 *
 * @author Mauricio
 */
public class Tienda {

    // =====================================================================
    // PREGUNTA 2: ¿Qué necesita saber?  →  ATRIBUTOS (todos privados)
    // =====================================================================

    private int id;             // Identificador. En MySQL la tabla "tienda" tendrá una sola fila.

    private String nombre;      // "Minimercado Don José": lo primero que se lee en el comprobante.

    private String nit;         // Número de identificación tributaria: "900.123.456-7".
                                // Es String y no int porque lleva puntos y guion,
                                // y no se hacen cuentas con él.

    private String direccion;   // "Calle 5 # 12-30": para el contacto con el cliente.

    private String telefono;    // String y no int: un número de teléfono no se suma ni se
                                // multiplica, y puede tener espacios o empezar en 0.

    // =====================================================================
    // PREGUNTA 5: ¿Cómo nace un objeto?  →  CONSTRUCTOR
    // Usa los set para que la validación también se aplique al crear el objeto.
    // =====================================================================

    public Tienda(String nombre, String nit, String direccion, String telefono) {
        this.nombre = "Mi tienda";      // Valor por defecto...
        setNombre(nombre);              // ...y luego el set, que valida.
        this.nit = "Sin NIT";           // Valor por defecto...
        setNit(nit);                    // ...y luego el set, que valida.
        this.direccion = direccion;     // Sin validación: se acepta como llegue.
        this.telefono = telefono;       // Sin validación: se acepta como llegue.
    }

    // =====================================================================
    // PREGUNTA 3: ¿Qué reglas debe cumplir?  →  SET CON VALIDACIÓN (a mano)
    // Lo que identifica al negocio en el comprobante no puede quedar vacío.
    // =====================================================================

    public void setNombre(String nombre) {
        if (!nombre.equals("")) {       // Si NO está vacío...
            this.nombre = nombre;       // ...lo acepta.
        }
    }

    public void setNit(String nit) {
        if (!nit.equals("")) {          // Si NO está vacío...
            this.nit = nit;             // ...lo acepta.
        }
    }

    // =====================================================================
    // PREGUNTA 4: ¿Qué necesita hacer o responder?  →  MÉTODOS
    // =====================================================================

    // Arma las tres primeras líneas del comprobante. Por ejemplo:
    //   MINIMERCADO DON JOSÉ
    //   NIT 900.123.456-7
    //   Calle 5 # 12-30 - Tel. 602 555 1234
    // Devuelve el texto y NO imprime (regla de MVC): Venta lo usa para armar
    // el comprobante completo, y la ventana decide cómo mostrarlo.
    public String obtenerEncabezado() {
        return nombre.toUpperCase() + "\n"                  // toUpperCase(): el nombre en mayúsculas.
             + "NIT " + nit + "\n"                          // "\n" = salto de línea.
             + direccion + " - Tel. " + telefono + "\n";
    }

    // =====================================================================
    // GET Y SET SIN REGLAS (generados con Insert Code)
    // =====================================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {                         // Lo usa el repositorio.
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNit() {
        return nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {        // La tienda puede mudarse.
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {          // Puede cambiar de número.
        this.telefono = telefono;
    }
}