package ministore.pruebas;   // Las pruebas viven aparte: NO van en el diagrama de clases.

import ministore.modelo.Cliente;

/**
 * Prueba de la clase Cliente (capa modelo). Se ejecuta con Shift + F6.
 * Comprueba el constructor, las validaciones, el consumidor final
 * y la eliminación lógica.
 *
 * @author Mauricio
 */
public class PruebaClienteModelo {

    public static void main(String[] args) {

        System.out.println("--- Prueba 1: crear un cliente válido ---");
        Cliente ana = new Cliente("1234567", "Ana Pérez", "3001234567", "ana@correo.com");
        System.out.println(ana.obtenerInformacion());                         // 1234567 - Ana Pérez | 3001234567
        System.out.println("¿Consumidor final? " + ana.esConsumidorFinal());  // false

        System.out.println("--- Prueba 2: los set rechazan datos vacíos ---");
        ana.setNombre("");                                // Inválido: se rechaza.
        System.out.println("Nombre: " + ana.getNombre());              // Ana Pérez
        ana.setDocumento("");                             // Inválido: se rechaza.
        System.out.println("Documento: " + ana.getDocumento());        // 1234567

        System.out.println("--- Prueba 3: el consumidor final ---");
        // Cliente genérico de la DIAN para ventas a quien no se identifica.
        Cliente generico = new Cliente("222222222222", "Consumidor final", "", "");
        System.out.println("Nombre: " + generico.getNombre());                   // Consumidor final
        System.out.println("¿Consumidor final? " + generico.esConsumidorFinal()); // true

        System.out.println("--- Prueba 4: el constructor también valida ---");
        Cliente sinDocumento = new Cliente("", "Carlos", "3100000000", "");
        System.out.println("Documento: " + sinDocumento.getDocumento());   // Sin documento
        System.out.println("¿Activo? " + sinDocumento.isActivo());         // true: todo cliente nace activo

        System.out.println("--- Prueba 5: ¿por qué equals y no ==? ---");
        // "new String" crea un objeto distinto con el MISMO texto,
        // como ocurre con lo que el usuario escribe en un campo de la ventana.
        String documentoDigitado = new String("222222222222");
        System.out.println("Con == : " + (documentoDigitado == "222222222222"));      // false: compara objetos
        System.out.println("Con equals: " + documentoDigitado.equals("222222222222")); // true: compara el texto

        System.out.println("--- Prueba 6: teléfono y correo no se validan ---");
        ana.setTelefono("3109998877");                    // Se acepta: cambió de número.
        System.out.println(ana.obtenerInformacion());     // 1234567 - Ana Pérez | 3109998877
        ana.setCorreo("");                                // También se acepta: el correo es opcional.
        System.out.println("Correo: [" + ana.getCorreo() + "]");      // Correo: []

        System.out.println("--- Prueba 7: eliminación lógica ---");
        ana.setActivo(false);                             // "Eliminar": no se borra, se desactiva.
        System.out.println("¿Activo? " + ana.isActivo()); // false
        System.out.println(ana.obtenerInformacion());     // Los datos siguen: 1234567 - Ana Pérez | 3109998877
    }
}