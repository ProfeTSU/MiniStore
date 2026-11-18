/*
 * =========================================================================
 *  CLASE: ConexionBD                      PAQUETE: ministore.util
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Abre la conexión entre el programa Java y la base de datos MySQL.
 *    Es el único lugar del proyecto donde están los datos de conexión
 *    (dónde está el servidor, qué base de datos, usuario y contraseña).
 *    Si mañana cambia la contraseña, solo se cambia aquí.
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa:      DriverManager y Connection (de java.sql) y, por dentro,
 *              el conector mysql-connector-j (el .jar de Libraries).
 *    La usan:  los repositorios de MySQL (a partir de la sesión 12).
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    - MySQL Server instalado y en ejecución (servicio MySQL80).
 *    - La base de datos ministore, creada con los scripts 01 y 02.
 *    - El .jar de Connector/J agregado a Libraries.
 * =========================================================================
 */
package ministore.util;

import java.sql.Connection;      // Representa una conexión abierta con la base de datos.
import java.sql.DriverManager;   // El "conmutador" que abre conexiones usando el conector.
import java.sql.SQLException;    // El error que lanza Java cuando algo falla con la base de datos.

/**
 * Abre conexiones con la base de datos ministore.
 *
 * @author Mauricio
 */
public class ConexionBD {

    // =====================================================================
    // DATOS DE LA CONEXIÓN (constantes)
    // static final: pertenecen a la clase y NO cambian mientras el programa
    // se ejecuta. Por convención, las constantes se escriben en MAYÚSCULAS.
    // =====================================================================

    // La URL dice DÓNDE está la base de datos:
    //   jdbc:mysql://  → el protocolo: Java (jdbc) hablando con MySQL
    //   localhost      → el servidor está en ESTE computador
    //   3306           → el puerto que se configuró al instalar MySQL
    //   ministore      → el nombre de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/ministore";

    private static final String USUARIO = "root";          // El usuario de MySQL.
    private static final String CLAVE = "programacion1";    // La contraseña de root que se anotó al instalar.

    // =====================================================================
    // CONSTRUCTOR PRIVADO
    // Nadie necesita crear objetos ConexionBD: se usa directamente
    // ConexionBD.obtenerConexion(), igual que Seguridad.hashSHA256().
    // =====================================================================
    private ConexionBD() {
    }

    // =====================================================================
    // obtenerConexion: abre y devuelve una conexión NUEVA.
    // static: se llama con el nombre de la clase, sin hacer new.
    // throws SQLException: si el servidor está apagado, la contraseña es
    // incorrecta o la base de datos no existe, Java lanza un SQLException.
    // Este método no lo atrapa: lo deja pasar ("throws") para que quien
    // lo llame decida qué mensaje mostrar.
    // =====================================================================
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}