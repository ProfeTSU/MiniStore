/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ministore.pruebas;
import ministore.modelo.Categoria;

/**
 *
 * @author Mauricio
 */
public class PruebaCategoriaModelo {
    public static void main(String[] args) {
                // TODO code application logic here
        Categoria granos = new Categoria(1, "Granos");
        System.out.println(granos.obtenerInformacion());
        
        Categoria sinNombre = new Categoria(2,"");
        System.out.println(sinNombre.obtenerInformacion());

    }
    
}
