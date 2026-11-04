/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package ministore.modelo;
 
/*
   PREGUNTA 1: ¿Qué representa en la tienda?
* Un grupo de productos, por ejemplo "Granos" o "Lácteos".
* Existe porque el Proyecto Integrador pide buscar y filtrar productos por categoría (RF6).
*
* @author Mauricio
*/
public class Categoria {
     // PREGUNTA 2: ¿Qué necesita saber?  →  ATRIBUTOS
    // Todos son "private": solo esta clase puede leerlos o cambiarlos
    // directamente. Desde afuera hay que usar los get y set (encapsulamiento).
    private int id;
    private String nombre;
    private boolean activo;
    // PREGUNTA 5: ¿Cómo nace un objeto?  →  CONSTRUCTOR
    // Se ejecuta cada vez que se escribe "new Categoria(...)".
    // Garantiza que toda categoría nazca con nombre y activa.
    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre="sin nombre";
        setNombre(nombre);
        this.activo=true;
    }
    // GET: consultar el valor de un atributo sin modificarlo.   
    public int getId(){
        return id;
    }
    public String getNombre(){
        return nombre;
    }  
    public boolean isActivo(){
        return activo;
    }
    // SET: modificar el valor de un atributo. "void" = no retorna nada.
    public void setId(int id){
        this.id =id;
    }
    // PREGUNTA 3: ¿Qué reglas debe cumplir?  →  SET CON VALIDACIÓN
    // Regla del negocio: una categoría no puede quedar sin nombre,
    // porque no se podría mostrar ni filtrar. Por eso este set se escribe a mano.    
    // PREGUNTA 3: ¿Qué reglas debe cumplir?  →  SET CON VALIDACIÓN
    // Regla del negocio: una categoría no puede quedar sin nombre,
    // porque no se podría mostrar ni filtrar. Por eso este set se escribe a mano.
    public void setNombre(String nombre){
        if(!nombre.equals("")){
            this.nombre = nombre;
        }
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    // PREGUNTA 4: ¿Qué necesita hacer o responder?  →  MÉTODOS
    // Devuelve los datos como texto. NO imprime: el modelo no sabe si existe
    // una consola o una ventana. Quien lo llame decide dónde mostrarlo (regla de MVC).
    public String obtenerInformacion(){
        return id +" - " + nombre; 
    }
    
}