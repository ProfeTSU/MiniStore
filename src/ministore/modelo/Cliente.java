package ministore.modelo;   // Paquete donde vive la clase.

/**
 * PREGUNTA 1: ¿Qué representa en la tienda?
 * A quién se le vende. Toda Venta tiene un Cliente.
 *
 * @author Mauricio
 */
public class Cliente {

    // =====================================================================
    // PREGUNTA 2: ¿Qué necesita saber?  →  ATRIBUTOS
    // Todos son "private" (encapsulamiento).
    // =====================================================================

    private int id;            // Identifica al cliente. En MySQL será la llave primaria.
    private String documento;  // String y no int: no se hacen cuentas con él y puede empezar en 0.
    private String nombre;     // A quién se le vende.
    private String telefono;   // String y no int: un celular (3001234567) no cabe en un int.
    private String correo;     // Contacto del cliente. Puede quedar vacío.
    private boolean activo;    // true = activo; false = "eliminado" (eliminación lógica).
                               // Un cliente con compras no se borra: se desactiva.

    // =====================================================================
    // PREGUNTA 5: ¿Cómo nace un objeto?  →  CONSTRUCTOR
    // Usa los set para que la validación también se aplique al crear el objeto.
    // =====================================================================

    public Cliente(String documento, String nombre, String telefono, String correo) {
        this.documento = "Sin documento";   // Primero, un valor por defecto...
        setDocumento(documento);            // ...luego intenta asignar el recibido (lo valida).
        this.nombre = "Sin nombre";         // Valor por defecto.
        setNombre(nombre);                  // Solo lo cambia si no está vacío.
        this.telefono = telefono;           // Sin validación: no todos los clientes lo dan.
        this.correo = correo;               // Sin validación: puede quedar vacío.
        this.activo = true;                 // Todo cliente nuevo empieza activo.
    }

    // =====================================================================
    // PREGUNTA 3: ¿Qué reglas debe cumplir?  →  SET CON VALIDACIÓN
    // Lo que identifica al cliente (documento y nombre) no puede quedar vacío.
    // =====================================================================

    public void setDocumento(String documento) {
        if (!documento.equals("")) {        // Si el documento NO está vacío...
            this.documento = documento;     // ...lo acepta.
        }
    }

    public void setNombre(String nombre) {
        if (!nombre.equals("")) {           // Si el nombre NO está vacío...
            this.nombre = nombre;           // ...lo acepta.
        }
    }

    // =====================================================================
    // PREGUNTA 4: ¿Qué necesita hacer o responder?  →  MÉTODOS
    // =====================================================================

    // Devuelve los datos principales como texto. NO imprime (regla de MVC).
    public String obtenerInformacion() {
        return documento + " - " + nombre + " | " + telefono;   // "1234567 - Ana Pérez | 3001234567"
    }

    // Responde si es el cliente genérico que usa la DIAN para ventas sin identificar.
    public boolean esConsumidorFinal() {
        //return "222222222222" == documento;    // INCORRECTO: == compara objetos, no el texto.
        return documento.equals("222222222222"); // CORRECTO: equals compara el contenido.
    }

    // =====================================================================
    // GET Y SET sin reglas (generados con Insert Code)
    // =====================================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {             // Lo usa ClienteRepositorio para asignar el id.
        this.id = id;
    }

    public String getDocumento() {          // Lo usa ClienteRepositorio para buscarPorDocumento.
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isActivo() {             // Lo usa ClienteRepositorio para listar solo los activos.
        return activo;
    }

    public void setActivo(boolean activo) { // Lo usa ClienteRepositorio para desactivar.
        this.activo = activo;
    }
}