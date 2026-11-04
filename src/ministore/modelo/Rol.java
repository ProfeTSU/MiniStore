/*
 * =========================================================================
 *  CLASE: Rol                             PAQUETE: ministore.modelo
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Representa lo que puede hacer un usuario dentro del MiniStore.
 *    Solo existen dos roles: "Administrador" (ve y hace todo) y
 *    "Vendedor" (productos, clientes y ventas). Cubre el RF9.
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa: ninguna clase. Es la más sencilla del modelo.
 *    La usan:
 *      - Usuario              → en el atributo "rol" (ASOCIACIÓN, * a 1:
 *                               muchos usuarios comparten un mismo rol).
 *      - UsuarioRepositorio   → crea los dos roles y los entrega a quien los pida.
 *      - VentanaUsuarios      → muestra los roles en la lista desplegable cmbRol.
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    Solo el paquete ministore.modelo. Por eso se construye ANTES que Usuario.
 * =========================================================================
 */
package ministore.modelo;   // Paquete del modelo: los datos y las reglas del negocio.

/**
 * PREGUNTA 1: ¿Qué representa en la tienda?
 * El tipo de usuario: Administrador o Vendedor.
 *
 * ¿Por qué una clase y no un simple texto dentro de Usuario?
 * Con texto, un usuario podría quedar como "admin", otro como "Admin" y otro
 * como "administrador", y la comparación del menú fallaría por un error de
 * escritura. Con una clase, todos los administradores comparten EL MISMO objeto
 * Rol, y en la base de datos el rol tendrá su propia tabla.
 *
 * @author Mauricio
 */
public class Rol {

    // =====================================================================
    // PREGUNTA 2: ¿Qué necesita saber?  →  ATRIBUTOS (todos privados)
    // =====================================================================

    private int id;            // Identificador: 1 = Administrador, 2 = Vendedor.
                               // Será la llave primaria de la tabla "rol" en MySQL.

    private String nombre;     // "Administrador" o "Vendedor".
                               // Es lo que compara Usuario.esAdministrador() con equals.

    // =====================================================================
    // PREGUNTA 5: ¿Cómo nace un objeto?  →  CONSTRUCTOR
    // Recibe sus dos datos. Los roles se crean una sola vez, en el
    // repositorio, y después todos los usuarios usan esos mismos objetos.
    // =====================================================================

    public Rol(int id, String nombre) {
        this.id = id;           // Asigna el número del rol.
        this.nombre = nombre;   // Asigna el nombre del rol.
    }

    // =====================================================================
    // PREGUNTA 3: ¿Qué reglas debe cumplir?
    // Ninguna validación: los roles no los escribe el usuario, los crea el
    // repositorio con valores fijos. La regla de esta clase es otra:
    // UNA VEZ CREADO, UN ROL NO CAMBIA (por eso no tiene set).
    //
    // PREGUNTA 4: ¿Qué necesita hacer o responder?
    // Solo responder sus datos. La pregunta "¿es administrador?" la responde
    // Usuario, que es quien tiene el rol.
    // =====================================================================

    // =====================================================================
    // GET (generados con Insert Code → Getter)
    // =====================================================================

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // =====================================================================
    // LO QUE NO EXISTE A PROPÓSITO (estos métodos no se generan con Insert Code):
    //  - setId() y setNombre(): si alguien pudiera renombrar el rol "Vendedor"
    //    como "Administrador", TODOS los vendedores pasarían a ser
    //    administradores al mismo tiempo, porque comparten el mismo objeto.
    // =====================================================================
}