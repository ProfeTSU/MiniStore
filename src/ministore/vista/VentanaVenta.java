/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ministore.vista;
// PARTE A: los import
import java.util.ArrayList;                          // Para la lista de productos del combo.
import javax.swing.JOptionPane;                      // Para los mensajes.
import javax.swing.table.DefaultTableModel;          // El "contenido" de la tabla.
import ministore.controlador.VentaControlador;       // El controlador que recibe la ventana.
import ministore.modelo.DetalleVenta;                // Cada línea del carrito.
import ministore.modelo.Producto;                    // Los productos del combo.
import ministore.modelo.Usuario;                     // Lo usa el main: el vendedor de prueba.
import ministore.modelo.Venta;                       // La venta en curso.
import ministore.repositorio.ClienteRepositorio;     // Los repositorios los usa el main
import ministore.repositorio.ProductoRepositorio;    // para probar la ventana sola.
import ministore.repositorio.TiendaRepositorio;
import ministore.repositorio.UsuarioRepositorio;
import ministore.repositorio.VentaRepositorio;
/**
 *
 * @author Mauricio
 */
public class VentanaVenta extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaVenta.class.getName());
    // =====================================================================
    // PARTE B: ATRIBUTOS QUE NO GENERA EL DISEÑADOR (se escriben a mano)
    // =====================================================================
    private VentaControlador controlador;        // Quien valida y guarda: la ventana solo lo usa.
    private DefaultTableModel modelo;            // El contenido de tblCarrito: filas y columnas.
    private ArrayList<Producto> productosEnCombo = new ArrayList<>();   // Qué producto hay en cada
                                                 // posición del combo: la posición 2 es productosEnCombo.get(2).

    /**
     * Creates new form VentanaVenta
     */
    // =====================================================================
    // PARTE C: CONSTRUCTOR. Recibe el controlador (lo crea Aplicacion).
    // =====================================================================
    public VentanaVenta(VentaControlador controlador) {
        initComponents();                        // PRIMERO: crea los componentes del diseñador.
        this.controlador = controlador;          // Guarda el controlador recibido.

        // La tabla con sus columnas reales (reemplaza "Title 1... Title 4").
        modelo = new DefaultTableModel(new String[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0);
        tblCarrito.setModel(modelo);
        tblCarrito.setDefaultEditor(Object.class, null);    // Las celdas no se editan con doble clic.

        cargarProductos();                       // Llena cmbProducto con los productos de MySQL.
        mostrarVenta();                          // Muestra el cliente, el carrito vacío y el total.
        setLocationRelativeTo(null);             // Abre la ventana en el centro.
    }

    // =====================================================================
    // PARTE D: MÉTODOS AYUDANTES
    // No responden a un botón: ayudan a los botones.
    // =====================================================================

    // Llena el combo con los productos activos (nombre y precio).
    private void cargarProductos() {
        productosEnCombo = controlador.listarProductos();    // Recuerda los productos de cada posición.
        cmbProducto.removeAllItems();            // Quita "Item 1, Item 2..." que puso el diseñador.
        for (Producto p : productosEnCombo) {
            cmbProducto.addItem(p.getNombre() + " - $" + p.getPrecioVenta());   // "Arroz 1 kg - $4500.75"
        }
    }

    // Muestra la venta en curso: el cliente, el carrito y el total.
    // Se llama después de CADA cambio (agregar, quitar, cambiar cliente),
    // para que la pantalla siempre muestre lo mismo que tiene la venta.
    private void mostrarVenta() {
        Venta venta = controlador.getVenta();
        lblCliente.setText("Cliente: " + venta.getCliente().getNombre());
        modelo.setRowCount(0);                   // Borra las filas anteriores (si no, se duplicarían).
        for (DetalleVenta d : venta.getDetalles()) {
            modelo.addRow(new Object[]{          // Una fila por cada línea del carrito.
                d.getProducto().getNombre(),
                d.getCantidad(),
                String.format("%.2f", d.getPrecioUnitario()),   // 4500,75  Para que se vean iguales
                String.format("%.2f", d.calcularSubtotal())     // 9001,50
            });
        }
        lblTotal.setText(String.format("TOTAL: %.2f", venta.calcularTotal()));   // Dos decimales.
    }

    // Deja la pantalla lista para atender al siguiente cliente.
    private void empezarOtraVenta() {
        controlador.nuevaVenta();                // Carrito vacío a nombre del Consumidor final.
        cargarProductos();                       // Vuelve a leer MySQL: el stock cambió.
        txtDocumentoCliente.setText("");
        txtCantidad.setText("1");
        mostrarVenta();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtDocumentoCliente = new javax.swing.JTextField();
        btnBuscarCliente = new javax.swing.JButton();
        btnConsumidorFinal = new javax.swing.JButton();
        lblCliente = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbProducto = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        btnQuitar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCarrito = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        btnConfirmar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MiniStore - Nueva venta");
        setResizable(false);

        jLabel1.setText("Documento:");

        btnBuscarCliente.setText("Buscar");
        btnBuscarCliente.addActionListener(this::btnBuscarClienteActionPerformed);

        btnConsumidorFinal.setText("Consumidor final");
        btnConsumidorFinal.setToolTipText("");
        btnConsumidorFinal.addActionListener(this::btnConsumidorFinalActionPerformed);

        lblCliente.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCliente.setText("Cliente: Consumidor final");

        jLabel3.setText("Producto:");

        cmbProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Cantidad:");

        txtCantidad.setText("1");

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(this::btnAgregarActionPerformed);

        btnQuitar.setText("Quitar");
        btnQuitar.addActionListener(this::btnQuitarActionPerformed);

        tblCarrito.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblCarrito);

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotal.setText("TOTAL: 0");

        btnConfirmar.setText("Confirmar venta");
        btnConfirmar.addActionListener(this::btnConfirmarActionPerformed);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(btnConfirmar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addGap(156, 156, 156))
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblCliente)
                                    .addGap(65, 65, 65))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(18, 18, 18)
                                    .addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(txtDocumentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBuscarCliente)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(5, 5, 5)
                                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(btnConsumidorFinal))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(btnAgregar)
                                .addGap(18, 18, 18)
                                .addComponent(btnQuitar)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotal)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDocumentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCliente)
                    .addComponent(btnConsumidorFinal))
                .addGap(34, 34, 34)
                .addComponent(lblCliente)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar)
                    .addComponent(btnQuitar))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotal)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnConfirmar))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        // TODO add your handling code here:
        // La vista LEE el documento, se lo PASA al controlador y MUESTRA el resultado.
        String mensaje = controlador.buscarCliente(txtDocumentoCliente.getText());
        if (mensaje.startsWith("Cliente:")) {    // Lo encontró: lblCliente muestra el nuevo cliente.
            mostrarVenta();
        } else {                                 // Campo vacío o no existe: se avisa.
            JOptionPane.showMessageDialog(this, mensaje);
        }
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnConsumidorFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsumidorFinalActionPerformed
        // TODO add your handling code here:
        controlador.usarConsumidorFinal();       // Vuelve al cliente genérico (el carrito se conserva).
        txtDocumentoCliente.setText("");
        mostrarVenta();
    }//GEN-LAST:event_btnConsumidorFinalActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        // La POSICIÓN elegida en el combo dice QUÉ producto es (-1 = ninguno).
        int posicion = cmbProducto.getSelectedIndex();
        Producto producto = null;
        if (posicion != -1) {
            producto = productosEnCombo.get(posicion);
        }
        String mensaje = controlador.agregarProducto(producto, txtCantidad.getText());
        if (mensaje.equals("Producto agregado")) {
            mostrarVenta();                      // El carrito y el total se actualizan.
            txtCantidad.setText("1");            // Lista para el siguiente producto.
        } else {
            JOptionPane.showMessageDialog(this, mensaje);   // Cantidad inválida o sin stock.
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        // TODO add your handling code here:
        // La fila seleccionada en la tabla es la misma posición en el carrito (-1 = ninguna).
        String mensaje = controlador.quitarProducto(tblCarrito.getSelectedRow());
        if (mensaje.equals("Producto quitado")) {
            mostrarVenta();
        } else {
            JOptionPane.showMessageDialog(this, mensaje);
        }
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        // TODO add your handling code here:
        String mensaje = controlador.confirmarVenta();      // Guarda en MySQL (transacción).
        if (mensaje.startsWith("Venta N°")) {
            // Se guardó: muestra el comprobante. Como el diálogo es MODAL,
            // esta línea ESPERA hasta que el vendedor lo cierre...
            new DialogoComprobante(this, controlador.generarComprobante()).setVisible(true);
            empezarOtraVenta();                  // ...y después deja todo listo para otro cliente.
        } else {
            JOptionPane.showMessageDialog(this, mensaje);   // Carrito vacío o sin stock en MySQL.
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        // Si hay productos en el carrito, se pregunta antes de perderlos.
        if (!controlador.getVenta().getDetalles().isEmpty()) {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Cancelar la venta? Se perderán los productos del carrito",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta != JOptionPane.YES_OPTION) {
                return;                          // Dijo que No: sigue en la venta.
            }
        }
        dispose();                               // Cierra SOLO esta ventana (no la aplicación).
    }//GEN-LAST:event_btnCancelarActionPerformed

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
        // Para probar esta ventana sola: se crean los repositorios, el vendedor
        // y el controlador, y se le entregan a la ventana.
        // (En el programa completo lo hará Aplicacion, en abrirVenta).
        java.awt.EventQueue.invokeLater(() -> {
            Usuario carlos = new UsuarioRepositorio().buscarPorNombreUsuario("carlos");   // 1. El vendedor.
            VentaControlador controlador = new VentaControlador(                         // 2. El controlador,
                    new VentaRepositorio(), new ProductoRepositorio(),                   //    con los cuatro
                    new ClienteRepositorio(), new TiendaRepositorio(), carlos);          //    repositorios.
            new VentanaVenta(controlador).setVisible(true);                              // 3. La ventana.
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnConsumidorFinal;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JComboBox<String> cmbProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblCarrito;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDocumentoCliente;
    // End of variables declaration//GEN-END:variables
}
