/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ministore.modelo;

/**
 *
 * @author Mauricio
 */
public class Producto {
    
    // ===== ATRIBUTOS (todos privados) =====
    private int id;                           // Identificador (llave primaria en MySQL).
    private String codigo;                    // Código del producto, por ejemplo "P001".
    private String nombre;                    // Nombre, por ejemplo "Arroz 1 kg".
    private String descripcion;               // Texto opcional con más detalles.
    private Categoria categoria;              // Atributo de tipo objeto: la categoría a la que pertenece.
    private double precioCompra;              // Lo que le cuesta el producto a la tienda (con centavos).
    private double precioVenta;               // Lo que paga el cliente (con centavos).
    private int stock;                        // Unidades disponibles.
    private int stockMinimo;                  // Si el stock baja de aquí, hay que reponer.
    private String rutaImagen;                // Ruta del archivo de imagen (se usará en la GUI).
    private boolean activo;                   // true = disponible; false = "eliminado".
    
    
    
    // Constructor 1: solo código y nombre
    
    // Útil cuando se registra un producto rápido y el resto de datos se completa después.
    public Producto(String codigo, String nombre) {
        this.codigo = "Sin código";      // 1. Pone el valor por defecto
        setCodigo(codigo);               // 2. Valida: como llega "", lo rechaza → sigue "Sin código"
        this.nombre = "Sin nombre";      // 3. Pone el valor por defecto
        setNombre(nombre);               // 4. Valida: rechaza "" → sigue "Sin nombre"    
        //this.codigo = codigo;                             
        //this.nombre = nombre;                             // Guarda el nombre recibido.
        this.descripcion = "";                            // Sin descripción por ahora.
        this.categoria = new Categoria(0, "Sin categoría"); // Categoría por defecto (evita que quede vacía).
        this.precioCompra = 0;                            // Precios en cero hasta que se definan.
        this.precioVenta = 0;
        this.stock = 0;                                   // Sin unidades al crearse.
        this.stockMinimo = 5;                             // Valor por defecto para la alerta.
        this.rutaImagen = "";                             // Sin imagen por ahora.
        this.activo = true;                               // Todo producto nuevo empieza activo.
    }    
    
    // constructor 2; datos completos (sobrecarga)
    // Mismo nombre que el anterior, pero con más parámetros: Java sabe cuál usar por los datos que recibe.
    public Producto(String codigo, String nombre, Categoria categoria,
                    double precioCompra, double precioVenta, int stock) {
        this.codigo = "Sin código";      // Valor por defecto.
        setCodigo(codigo);               // Solo lo cambia si no está vacío.
        this.nombre = "Sin nombre";      // Valor por defecto.
        setNombre(nombre);               // Solo lo cambia si no está vacío.
        this.descripcion = "";                    // Sin descripción por ahora.
        this.categoria = categoria;               // Guarda el objeto Categoria recibido.
        setPrecioCompra(precioCompra);            // Usa el set para validar el precio de compra.
        setPrecioVenta(precioVenta);              // Usa el set para validar el precio de venta (va después de la compra).
        setStock(stock);                          // Usa el set para validar el stock.
        this.stockMinimo = 5;                     // Valor por defecto para la alerta.
        this.rutaImagen = "";                     // Sin imagen por ahora.
        this.activo = true;                       // Empieza activo.
    }  
    
    // Sets con validacion 
    
    public void setPrecioCompra(double precioCompra){
        if (precioCompra > 0){
            this.precioCompra = precioCompra;
        }
    }
    
    public void setPrecioVenta(double precioVenta){
        if (precioVenta > precioCompra){              /// vender por encima de lo que costo
            this.precioVenta = precioVenta;
        }
    }

    public void setStock(int stock) {                    // Recibe la nueva cantidad.
        if (stock >= 0) {                                // El stock nunca puede ser negativo.
            this.stock = stock;                          // Si es válido, lo guarda.
        }
    }
    
    // Metodos de negocio
    // Resta unidades cuando se vende. Retorna true si pudo hacerlo y false si no.
    public boolean descontarStock(int cantidad) {
        if (cantidad > 0 && cantidad <= stock) {         // La cantidad debe ser positiva y no superar lo disponible.
            stock = stock - cantidad;                    // Resta las unidades vendidas.
            return true;                                 // Avisa que la operación se hizo.
        }
        return false;                                    // Si no se cumplió la condición, no cambia nada.
    }

    // Suma unidades cuando llega mercancía. Retorna true si pudo hacerlo.
    public boolean aumentarStock(int cantidad) {
        if (cantidad > 0) {                              // Solo acepta cantidades positivas.
            stock = stock + cantidad;                    // Suma las unidades.
            return true;
        }
        return false;
    }

    // Responde si hay al menos una unidad disponible.
    public boolean hayExistencias() {
        return stock > 0;                                // La comparación ya da true o false.
    }

    // Responde si el stock llegó al mínimo y hay que pedir más.
    public boolean necesitaReposicion() {
        return stock <= stockMinimo;                     // true si quedan pocas unidades.
    }

    // Calcula cuánto gana la tienda por cada unidad vendida.
    public double calcularUtilidad() {
        return precioVenta - precioCompra;               // Diferencia entre venta y compra.
    }

    // Devuelve los datos principales como texto (no imprime).
    public String obtenerInformacion() {
        return codigo + " - " + nombre                   // Código y nombre...
             + " | " + categoria.getNombre()             // ...nombre de la categoría (usa el get de Categoria)...
             + " | $" + precioVenta                      // ...precio de venta...
             + " | Stock: " + stock;                     // ...y unidades disponibles.
    }     
    
    public void setCodigo(String codigo){
        if(!codigo.equals("")){
            this.codigo = codigo;
    }
    }
    
    public void setNombre(String nombre) {
               if (!nombre.equals("")) {        // El nombre no puede quedar vacío.
        this.nombre = nombre;
        }
    }   
    
  //=================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public int getStock() {
        return stock;
    }
    
    
    
}
