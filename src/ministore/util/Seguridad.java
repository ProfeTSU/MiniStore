/*
 * =========================================================================
 *  CLASE: Seguridad                       PAQUETE: ministore.util
 * =========================================================================
 *
 *  ¿PARA QUÉ SIRVE?
 *    Convierte las contraseñas en un HASH: una "huella" de 64 caracteres
 *    de la que no se puede recuperar el texto original. Así, el MiniStore
 *    nunca guarda las contraseñas tal cual (RF2: acceso seguro).
 *
 *  ¿CON QUIÉN SE CONECTA?
 *    Usa: clases de Java (MessageDigest), no clases del proyecto.
 *    La usa:
 *      - Usuario → DEPENDENCIA (flecha punteada «usa»): llama a hashSHA256()
 *                  en el constructor y en verificarContrasena().
 *
 *  ¿QUÉ DEBE EXISTIR ANTES DE CREAR ESTA CLASE?
 *    Solo el paquete ministore.util.
 *    ¿Por qué en "util" y no en "modelo"? Porque no es un dato del negocio
 *    (como un producto o un cliente): es una HERRAMIENTA que usa el modelo.
 * =========================================================================
 */
package ministore.util;

import java.nio.charset.StandardCharsets;     // Para convertir el texto en bytes.
import java.security.MessageDigest;           // La clase de Java que calcula el hash.
import java.security.NoSuchAlgorithmException; // El error que ocurre si el algoritmo no existe.

/**
 * Herramienta para el hash de las contraseñas.
 * Se usa sin necesidad de entender su interior, igual que Scanner.
 *
 * @author Mauricio
 */
public class Seguridad {

    // =====================================================================
    // ¿Por qué "static"?
    // Porque no hace falta crear un objeto Seguridad para usarlo: se llama
    // con el nombre de la clase, sin new:   Seguridad.hashSHA256("clave")
    // (En el diagrama de clases, los métodos static van subrayados).
    //
    // ¿Qué hace?
    // Recibe un texto y devuelve su huella de 64 caracteres.
    //   - La misma contraseña SIEMPRE produce el mismo hash: por eso se puede verificar.
    //   - A partir del hash NO se puede saber la contraseña: por eso es seguro.
    // =====================================================================
    public static String hashSHA256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");          // Pide el algoritmo SHA-256.
            byte[] bytes = md.digest(texto.getBytes(StandardCharsets.UTF_8)); // Calcula la huella (en bytes).
            StringBuilder hex = new StringBuilder();                          // Arma el texto del resultado.
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));                         // Cada byte en hexadecimal (2 caracteres).
            }
            return hex.toString();                                            // 32 bytes x 2 = 64 caracteres.
        } catch (NoSuchAlgorithmException e) {
            // try/catch: "intenta esto; si ocurre este error, haz esto otro".
            // Java obliga a prever que el algoritmo no exista, aunque SHA-256
            // viene incluido en todas las versiones de Java.
            throw new RuntimeException("SHA-256 no está disponible", e);
        }
    }
}