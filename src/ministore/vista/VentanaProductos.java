/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ministore.vista;
import java.util.ArrayList;                          // Para la lista de productos de la tabla.
import javax.swing.JOptionPane;                      // Para los mensajes (se usará con los botones).
import javax.swing.table.DefaultTableModel;          // El "contenido" de la tabla.
import ministore.controlador.ProductoControlador;    // El controlador que recibe la ventana.
import ministore.modelo.Categoria;
import ministore.modelo.Producto;
import ministore.repositorio.CategoriaRepositorio;   // Los usa el main para probar la ventana.
import ministore.repositorio.ProductoRepositorio;
/**
 *
 * @author Mauricio
 */
public class VentanaProductos extends javax.swing.JFrame {
    
    // =====================================================================
    // ATRIBUTOS QUE NO GENERA EL DISEÑADOR (se escriben a mano)
    // =====================================================================
    private ProductoControlador controlador;     // Quien valida y guarda: la ventana solo lo usa.
    private DefaultTableModel modelo;            // El contenido de la tabla: filas y columnas.
    private ArrayList<Producto> productosEnTabla = new ArrayList<>();   // Qué producto hay en cada fila.
    private int idSeleccionado = -1;             // El producto elegido en la tabla (-1 = ninguno).
    private String rutaImagen = "";              // La imagen del producto (por ahora, vacía).
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaProductos.class.getName());

    /**
     * Creates new form VentanaProductos
     */
    //public VentanaProductos() { // Las siguientes 3 Lineas se  borran o se comentan , porque se reemplzan por el constructor
    //    initComponents();
    //}
    // =====================================================================
    // CONSTRUCTOR: ahora RECIBE el controlador.
    // La ventana no crea su propio controlador: usa el que le entregan,
    // para que todas las ventanas compartan los mismos datos.
    // =====================================================================
    public VentanaProductos(ProductoControlador controlador) {
        initComponents();                        // PRIMERO: crea los componentes del diseñador.
                                                 // Antes de esta línea, tblProductos no existe.
        this.controlador = controlador;          // Guarda el controlador recibido.

        // La tabla con sus columnas reales (reemplaza "Title 1... Title 4").
        // El 0 significa: empieza sin filas.
        modelo = new DefaultTableModel(new String[]{"Código", "Nombre", "Categoría", "Precio", "Stock"}, 0);
        tblProductos.setModel(modelo);
        tblProductos.setDefaultEditor(Object.class, null);   // Las celdas no se editan con doble clic.

        cargarCategorias();                           // Llena cmbCategoria.
        cargarTabla(controlador.listarProductos());   // Muestra los productos.
        setLocationRelativeTo(null);                  // Abre la ventana en el centro de la pantalla.
    }

    // =====================================================================
    // MÉTODOS AYUDANTES: muestran los datos en la ventana
    // =====================================================================

    // Llena la lista desplegable con los NOMBRES de las categorías.
    private void cargarCategorias() {
        cmbCategoria.removeAllItems();           // Quita "Item 1, Item 2..." que puso el diseñador.
        for (Categoria c : controlador.listarCategorias()) {
            cmbCategoria.addItem(c.getNombre());
        }
    }

    // Llena la tabla: una fila por cada producto de la lista que recibe.
    private void cargarTabla(ArrayList<Producto> lista) {
        productosEnTabla = lista;                // Recuerda los productos: la fila 2 es productosEnTabla.get(2).
        modelo.setRowCount(0);                   // Borra las filas anteriores (si no, se duplicarían).
        for (Producto p : lista) {
            modelo.addRow(new Object[]{          // Una fila: un valor por cada columna, en orden.
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria().getNombre(),
                p.getPrecioVenta(),
                p.getStock()
            });  // ← cierra el addRow
        } //  ← cierra el for
    }   // ← cierra el método cargarTabla

    // Muestra el mensaje que devolvió el controlador.
    // Si la operación fue exitosa ("Producto guardado", "Producto actualizado"
    // o "Producto eliminado"), limpia el formulario y recarga la tabla.
    // La usan los botones Guardar, Actualizar y Eliminar: se escribe UNA vez.
    private void mostrarResultado(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);   // Ventanita con el mensaje.
        if (mensaje.startsWith("Producto ")) {          // startsWith: ¿el texto empieza así?
            limpiar();                                  // Si salió bien: formulario vacío...
            cargarTabla(controlador.listarProductos()); // ...y la tabla con los datos nuevos.
        }
    }

    // Deja el formulario vacío y sin producto seleccionado.
    // La usan mostrarResultado y el botón Limpiar.
    private void limpiar() {
        idSeleccionado = -1;                            // -1 = ningún producto elegido.
        rutaImagen = "";
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
        txtStock.setText("");
        txtStockMinimo.setText("5");                    // El valor por defecto.
        tblProductos.clearSelection();                  // Quita la selección de la tabla.
    }
    
    
    // Pasa los datos de la fila seleccionada al formulario.
    // Guarda el id del producto: lo necesitan Actualizar y Eliminar.
    private void mostrarSeleccionado() {
        int fila = tblProductos.getSelectedRow();       // La fila elegida (-1 si no hay ninguna).
        if (fila == -1) {
            return;                                     // Nada seleccionado: no hace nada.
        }
        Producto p = productosEnTabla.get(fila);        // El producto de esa fila.
        idSeleccionado = p.getId();                     // Lo recuerda para Actualizar y Eliminar.

        // setText necesita TEXTO: String.valueOf convierte los números en texto.
        txtCodigo.setText(p.getCodigo());
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        cmbCategoria.setSelectedItem(p.getCategoria().getNombre());   // Elige su categoría en la lista.
        txtPrecioCompra.setText(String.valueOf(p.getPrecioCompra()));
        txtPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
        txtStock.setText(String.valueOf(p.getStock()));
        txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
        rutaImagen = p.getRutaImagen();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtCodigo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        cmbCategoria = new javax.swing.JComboBox<>();
        txtPrecioCompra = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        txtStockMinimo = new javax.swing.JTextField();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MiniStore - Productos");

        cmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtStockMinimo.setText("5");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProductos);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jLabel1.setText("Código:");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Descripción:");

        jLabel4.setText("Categoría:");

        jLabel5.setText("Precio compra:");

        jLabel6.setText("Precio venta:");

        jLabel7.setText("Stock:");

        jLabel8.setText("Stock mínimo:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCodigo)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbCategoria, 0, 118, Short.MAX_VALUE)
                                    .addComponent(txtPrecioCompra)
                                    .addComponent(txtPrecioVenta)
                                    .addComponent(txtStock)
                                    .addComponent(txtStockMinimo))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnGuardar)
                        .addGap(46, 46, 46)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnBuscar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(btnActualizar)
                                .addGap(42, 42, 42)
                                .addComponent(btnEliminar)
                                .addGap(80, 80, 80)
                                .addComponent(btnLimpiar))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnActualizar)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar))
                .addContainerGap(112, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
                // La vista solo LEE lo que escribió el usuario (getText),
        // se lo PASA al controlador y MUESTRA la respuesta.
        // No hay ni un if de validación aquí: eso lo hace el controlador.
        String mensaje = controlador.guardarProducto(
                txtCodigo.getText(),
                txtNombre.getText(),
                txtDescripcion.getText(),
                cmbCategoria.getSelectedIndex(),        // La POSICIÓN elegida en la lista (0 = Granos).
                txtPrecioCompra.getText(),
                txtPrecioVenta.getText(),
                txtStock.getText(),
                txtStockMinimo.getText(),
                rutaImagen);
        mostrarResultado(mensaje);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tblProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosMouseClicked
        // TODO add your handling code here:   -- Reemplazar por el metodo mostrar seleccionado
       mostrarSeleccionado();                          // Al hacer clic en una fila, sus datos pasan al formulario.
    }//GEN-LAST:event_tblProductosMouseClicked

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:  /// reemplazar por el  objeto mensaje que es un atributo tipo objeto
                // Igual que Guardar, pero además envía el id del producto elegido.
        // Si no hay ninguno, idSeleccionado vale -1 y el controlador responde
        // "Seleccione un producto de la tabla".
        String mensaje = controlador.actualizarProducto(
                idSeleccionado,                         // A QUÉ producto se le aplican los cambios.
                txtCodigo.getText(),
                txtNombre.getText(),
                txtDescripcion.getText(),
                cmbCategoria.getSelectedIndex(),
                txtPrecioCompra.getText(),
                txtPrecioVenta.getText(),
                txtStock.getText(),
                txtStockMinimo.getText(),
                rutaImagen);
        mostrarResultado(mensaje);
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:  /// 
                // 1. ¿Hay un producto elegido? Si no, se avisa y el método termina.
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla");
            return;
        }

        // 2. Se pide confirmación: showConfirmDialog muestra los botones Sí y No,
        //    y devuelve cuál se eligió (YES_OPTION o NO_OPTION).
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el producto seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);

        // 3. Solo si dijo que Sí, se le pide al controlador que lo desactive.
        //    (Eliminación lógica: el producto no se borra, deja de aparecer en la tabla).
        if (respuesta == JOptionPane.YES_OPTION) {
            mostrarResultado(controlador.desactivarProducto(idSeleccionado));
        }
        
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        // Le pide al controlador los productos que contienen el texto (RF6)
        // y muestra en la tabla solo esos. Con el campo vacío, aparecen todos.
        cargarTabla(controlador.buscarProductos(txtBuscar.getText()));
        
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        // Deja el formulario vacío y sin producto seleccionado.
        // También recarga la tabla completa, por si se había hecho una búsqueda.
        limpiar();
        cargarTabla(controlador.listarProductos());
        
    }//GEN-LAST:event_btnLimpiarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        // java.awt.EventQueue.invokeLater(() -> new VentanaProductos().setVisible(true)); -- Se borra
        // Para probar esta ventana sola: se crean los repositorios y el controlador,
        // y se le entregan a la ventana. (Más adelante lo hará el main del proyecto,
        // para que TODAS las ventanas compartan los mismos repositorios).
        java.awt.EventQueue.invokeLater(() -> {
            CategoriaRepositorio categorias = new CategoriaRepositorio();                      // 1. Las categorías.
            ProductoRepositorio productos = new ProductoRepositorio(categorias);               // 2. Los productos, con esas categorías.
            ProductoControlador controlador = new ProductoControlador(productos, categorias);  // 3. El controlador, con los dos.
            new VentanaProductos(controlador).setVisible(true);                                // 4. La ventana, con el controlador.
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecioCompra;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtStockMinimo;
    // End of variables declaration//GEN-END:variables
}
