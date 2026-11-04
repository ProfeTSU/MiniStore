/*
 * =========================================================================
 *  CLASE: Usuario                         PAQUETE: ministore.modelo
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Representa a una persona que usa el MiniStore: el administrador o un
 *    vendedor. Permite registrar usuarios (RF1), iniciar sesión (RF2),
 *    saber qué opciones del menú mostrar según el rol (RF9) y saber qué
 *    vendedor hizo cada venta (RF10).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa (en el diagrama de clases):
 *      - Rol        → ASOCIACIÓN (flecha abierta, * a 1): lo guarda en el atributo "rol".
 *      - Seguridad  → DEPENDENCIA (flecha punteada «usa»): solo la llama para
 *                     calcular el hash; no la guarda como atributo.
 *    La usan:
 *      - Venta                → en el atributo "vendedor".
 *      - UsuarioRepositorio   → la guarda y la busca (capa de datos).
 *      - LoginControlador     → llama a verificarContrasena() al iniciar sesión.
 *      - UsuarioControlador   → crea usuarios nuevos.
 *      - VentanaPrincipal     → llama a esAdministrador() y obtenerInformacion().
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    1. La clase Rol, en el paquete ministore.modelo.
 *    2. El paquete ministore.util con la clase Seguridad (método hashSHA256).
 *    Orden de construcción: Rol → Seguridad → Usuario.
 *    Si se crea Usuario primero, NetBeans marca en rojo "Rol" y "Seguridad".
 * =========================================================================
 */
package ministore.modelo;   // Paquete del modelo: los datos y las reglas del negocio.

import ministore.util.Seguridad;   // Seguridad está en OTRO paquete (util): hay que importarla.

/**
 * PREGUNTA 1: ¿Qué representa en la tienda?
 * Una persona que usa el sistema: el administrador o un vendedor.
 *
 * REGLA DE ORO DE ESTA CLASE: la contraseña NUNCA se guarda tal cual.
 * Se guarda su HASH: una "huella" de la contraseña de la que no se puede
 * recuperar el texto original. Si alguien roba los datos, no ve las contraseñas.
 *
 * @author Mauricio
 */
public class Usuario {

    // =====================================================================
    // PREGUNTA 2: ¿Qué necesita saber?  →  ATRIBUTOS (todos privados)
    // =====================================================================

    private int id;                  // Identificador. Será la llave primaria en MySQL.
                                     // No lo escribe el usuario: lo asigna el repositorio.

    private String nombreUsuario;    // El nombre con el que inicia sesión: "carlos".
                                     // Es corto y sin espacios; no es su nombre real.

    private String contrasenaHash;   // El HASH de la contraseña, NO la contraseña.
                                     // Por eso se llama "contrasenaHash" y no "contrasena":
                                     // el nombre del atributo recuerda lo que guarda.

    private String nombreCompleto;   // Su nombre real: "Carlos Gómez".
                                     // ¿Para qué? Para el saludo del menú y para el
                                     // "Atendió:" del comprobante.

    private Rol rol;                 // Atributo de tipo objeto (ASOCIACIÓN con Rol).
                                     // ¿Por qué un objeto y no un texto como "admin"?
                                     // Porque con texto alguien podría escribir "Admin" o
                                     // "administrador" y la comparación fallaría. Con un
                                     // objeto, todos los administradores comparten el mismo Rol.

    private boolean activo;          // true = puede entrar; false = desactivado.
                                     // ¿Por qué no se borra? Porque un vendedor que se retira
                                     // tiene ventas registradas: borrarlo dañaría el historial.

    // =====================================================================
    // PREGUNTA 5: ¿Cómo nace un objeto?  →  CONSTRUCTOR
    // Recibe la contraseña NORMAL y la convierte en hash de inmediato:
    // es el ÚNICO momento en que la contraseña existe en texto claro.
    // =====================================================================

    public Usuario(String nombreUsuario, String contrasena, String nombreCompleto, Rol rol) {
        this.nombreUsuario = "sinusuario";                         // Valor por defecto...
        setNombreUsuario(nombreUsuario);                           // ...y luego el set, que valida.

        this.contrasenaHash = Seguridad.hashSHA256(contrasena);   // Se guarda la HUELLA, no la clave.
                                                                   // DEPENDENCIA: usa Seguridad, pero no
                                                                   // la guarda como atributo.

        this.nombreCompleto = "Sin nombre";                        // Valor por defecto...
        setNombreCompleto(nombreCompleto);                         // ...y luego el set, que valida.

        this.rol = rol;                                            // Administrador o Vendedor.
        this.activo = true;                                        // Todo usuario nuevo puede entrar.
                                                                   // (Un boolean sin asignar vale false).
    }

    // =====================================================================
    // PREGUNTA 3: ¿Qué reglas debe cumplir?  →  SET CON VALIDACIÓN (a mano)
    // Lo que identifica al usuario no puede quedar vacío.
    // La regla de la contraseña (mínimo 6 caracteres) la revisa el CONTROLADOR,
    // porque aquí la contraseña original solo existe dentro del constructor.
    // =====================================================================

    public void setNombreUsuario(String nombreUsuario) {
        if (!nombreUsuario.equals("")) {        // Si NO está vacío...
            this.nombreUsuario = nombreUsuario;  // ...lo acepta.
        }
    }

    public void setNombreCompleto(String nombreCompleto) {
        if (!nombreCompleto.equals("")) {         // Si NO está vacío...
            this.nombreCompleto = nombreCompleto; // ...lo acepta.
        }
    }

    // =====================================================================
    // PREGUNTA 4: ¿Qué necesita hacer o responder?  →  MÉTODOS
    // =====================================================================

    // ¿La contraseña que escribió es la correcta?  →  El corazón del login (RF2).
    // ¿Cómo, si la contraseña no se guarda? Se calcula el hash de lo que escribió
    // y se compara con el hash guardado: la misma contraseña siempre da el mismo hash.
    public boolean verificarContrasena(String contrasena) {
        String hash = Seguridad.hashSHA256(contrasena);   // Huella de lo que escribió.
        return hash.equals(contrasenaHash);               // Se comparan textos: con equals, nunca con ==.
    }

    // ¿Es administrador?  →  Con esto el menú decide qué botones mostrar (RF9).
    public boolean esAdministrador() {
        return rol.getNombre().equals("Administrador");   // Le pregunta al objeto Rol su nombre.
    }

    // Los datos como texto, por ejemplo: "Carlos Gómez (Vendedor)".
    // Devuelve el texto y NO imprime (regla de MVC): quien lo pida decide dónde mostrarlo.
    public String obtenerInformacion() {
        return nombreCompleto + " (" + rol.getNombre() + ")";
    }

    // =====================================================================
    // GET Y SET SIN REGLAS (generados con Insert Code)
    // =====================================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {                 // Lo usa el repositorio para asignar el número.
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean isActivo() {                 // En los boolean se usa "is" en lugar de "get".
        return activo;
    }

    public void setActivo(boolean activo) {     // Lo usa el repositorio para desactivar.
        this.activo = activo;
    }

    public String getContrasenaHash() {         // Solo devuelve la HUELLA, nunca la contraseña.
        return contrasenaHash;                  // Lo usará el repositorio MySQL (sesión 12) para guardarla.
    }

    // =====================================================================
    // LO QUE NO EXISTE A PROPÓSITO (estos métodos no se generan con Insert Code):
    //  - getContrasena():     la contraseña original no se guarda: no hay qué devolver.
    //  - setContrasenaHash(): permitiría reemplazar la huella por cualquier texto
    //                         y entrar sin conocer la contraseña.
    //  - setRol():            el rol se define al crear el usuario. Un set público
    //                         permitiría darle permisos de administrador a cualquiera.
    // =====================================================================
}