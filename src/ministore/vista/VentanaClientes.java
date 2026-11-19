/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ministore.vista;
// PARTE A: los import
import java.util.ArrayList;                          // Para la lista de clientes de la tabla.
import javax.swing.JOptionPane;                      // Para los mensajes.
import javax.swing.table.DefaultTableModel;          // El "contenido" de la tabla.
import ministore.controlador.ClienteControlador;     // El controlador que recibe la ventana.
import ministore.modelo.Cliente;
import ministore.repositorio.ClienteRepositorio;     // Lo usa el main para probar la ventana.
/**
 *
 * @author Mauricio
 */
public class VentanaClientes extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaClientes.class.getName());
    
     // =====================================================================
    // PARTE B: ATRIBUTOS QUE NO GENERA EL DISEÑADOR (se escriben a mano)
    // =====================================================================
    private ClienteControlador controlador;      // Quien valida y guarda: la ventana solo lo usa.
    private DefaultTableModel modelo;            // El contenido de la tabla: filas y columnas.
    private ArrayList<Cliente> clientesEnTabla = new ArrayList<>();   // Qué cliente hay en cada fila.
    private int idSeleccionado = -1;             // El cliente elegido en la tabla (-1 = ninguno).

    /**
     * Creates new form VentanaClientes
     */
    // =====================================================================
    // PARTE C: CONSTRUCTOR. Ahora RECIBE el controlador.
    // La ventana no crea su propio controlador: usa el que le entregan,
    // para que todas las ventanas compartan los mismos datos.
    // =====================================================================
    public VentanaClientes(ClienteControlador controlador) {
        initComponents();                        // PRIMERO: crea los componentes del diseñador.
                                                 // Antes de esta línea, tblClientes no existe.
        this.controlador = controlador;          // Guarda el controlador recibido.

        // La tabla con sus columnas reales (reemplaza "Title 1... Title 4").
        // El 0 significa: empieza sin filas.
        modelo = new DefaultTableModel(new String[]{"Documento", "Nombre", "Teléfono"}, 0);
        tblClientes.setModel(modelo);
        tblClientes.setDefaultEditor(Object.class, null);    // Las celdas no se editan con doble clic.

        cargarTabla(controlador.listarClientes());           // Muestra los clientes de MySQL.
        setLocationRelativeTo(null);                         // Abre la ventana en el centro.
    }
    // =====================================================================
    // PARTE D: MÉTODOS AYUDANTES
    // No responden a un botón: ayudan a los botones.
    // =====================================================================

    // Llena la tabla: una fila por cada cliente de la lista que recibe.
    private void cargarTabla(ArrayList<Cliente> lista) {
        clientesEnTabla = lista;                 // Recuerda los clientes: la fila 2 es clientesEnTabla.get(2).
        modelo.setRowCount(0);                   // Borra las filas anteriores (si no, se duplicarían).
        for (Cliente c : lista) {
            modelo.addRow(new Object[]{c.getDocumento(), c.getNombre(), c.getTelefono()});
        }
    }

    // Muestra el mensaje del controlador. Si fue exitoso ("Cliente guardado",
    // "Cliente actualizado" o "Cliente eliminado"), limpia y recarga la tabla.
    // La usan Guardar, Actualizar y Eliminar: se escribe UNA vez.
    private void mostrarResultado(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
        if (mensaje.startsWith("Cliente ")) {    // Los éxitos empiezan por "Cliente"; los errores no.
            limpiar();
            cargarTabla(controlador.listarClientes());
        }
    }
    // Deja el formulario vacío y sin cliente seleccionado.
    private void limpiar() {
        idSeleccionado = -1;                     // -1 = ningún cliente elegido.
        txtDocumento.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        tblClientes.clearSelection();            // Quita la selección de la tabla.
    }
    // Pasa los datos de la fila seleccionada al formulario.
    private void mostrarSeleccionado() {
        int fila = tblClientes.getSelectedRow(); // La fila elegida (-1 si no hay ninguna).
        if (fila == -1) {
            return;
        }
        Cliente c = clientesEnTabla.get(fila);   // El cliente de esa fila.
        idSeleccionado = c.getId();              // Lo recuerda para Actualizar y Eliminar.
        txtDocumento.setText(c.getDocumento());
        txtNombre.setText(c.getNombre());
        txtTelefono.setText(c.getTelefono());
        txtCorreo.setText(c.getCorreo());
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDocumento = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        txtBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        btnBuscar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MiniStore - Clientes");

        jLabel1.setText("Documento:");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Teléfono:");

        jLabel4.setText("Correo:");

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
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
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                                    .addComponent(txtDocumento)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(168, 168, 168)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(btnBuscar)
                        .addGap(154, 154, 154))))
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(btnGuardar)
                .addGap(95, 95, 95)
                .addComponent(btnActualizar)
                .addGap(94, 94, 94)
                .addComponent(btnEliminar)
                .addGap(92, 92, 92)
                .addComponent(btnLimpiar)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnBuscar)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnActualizar)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar))
                .addGap(102, 102, 102))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        // La vista solo LEE lo que escribió el usuario (getText),
        // se lo PASA al controlador y MUESTRA la respuesta.
        // No hay ni un if de validación aquí: eso lo hace el controlador.
        String mensaje = controlador.guardarCliente(
                txtDocumento.getText(),
                txtNombre.getText(),
                txtTelefono.getText(),
                txtCorreo.getText());
        mostrarResultado(mensaje);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // TODO add your handling code here:  Lo deje como referencia
        // Al hacer clic en una fila, sus datos pasan al formulario.
        mostrarSeleccionado();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here: Se deja como referencia
        // Igual que Guardar, pero además envía el id del cliente elegido en la tabla.
        // Si no hay ninguno, idSeleccionado vale -1 y el controlador responde
        // "Seleccione un cliente de la tabla".
        String mensaje = controlador.actualizarCliente(
                idSeleccionado,                  // A QUÉ cliente se le aplican los cambios.
                txtDocumento.getText(),
                txtNombre.getText(),
                txtTelefono.getText(),
                txtCorreo.getText());
        mostrarResultado(mensaje);
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        // 1. ¿Hay un cliente elegido? Si no, se avisa y el método termina.
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla");
            return;
        }
        // 2. Se pide confirmación: showConfirmDialog muestra Sí y No.
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el cliente seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        // 3. Solo si dijo que Sí. Si es el Consumidor final, el controlador lo impide.
        if (respuesta == JOptionPane.YES_OPTION) {
            mostrarResultado(controlador.desactivarCliente(idSeleccionado));
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        // Formulario vacío y la tabla completa otra vez (por si se había buscado).
        txtBuscar.setText("");                   // También borra el texto de la búsqueda.
        limpiar();
        cargarTabla(controlador.listarClientes());
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        // Con el campo vacío: se muestran todos los clientes.
        if (txtBuscar.getText().trim().isEmpty()) {
            cargarTabla(controlador.listarClientes());
            return;
        }
        // buscarCliente devuelve UN cliente (o null), y cargarTabla necesita una LISTA:
        // el cliente encontrado se mete en una lista de un solo elemento.
        Cliente c = controlador.buscarCliente(txtBuscar.getText());
        ArrayList<Cliente> resultado = new ArrayList<>();
        if (c != null) {                        // Siempre revisar null antes de usarlo.
            resultado.add(c);
        } else {
            JOptionPane.showMessageDialog(this, "No existe un cliente con ese documento");
        }
        cargarTabla(resultado);
    }//GEN-LAST:event_btnBuscarActionPerformed

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
        // java.awt.EventQueue.invokeLater(() -> new VentanaClientes().setVisible(true));  // borrar
        // Para probar esta ventana sola: se crean el repositorio y el controlador,
        // y se le entregan a la ventana. (Más adelante lo hará el main del proyecto,
        // para que TODAS las ventanas compartan los mismos repositorios).
        java.awt.EventQueue.invokeLater(() -> {
            ClienteRepositorio clientes = new ClienteRepositorio();                  // 1. La bodega de clientes.
            ClienteControlador controlador = new ClienteControlador(clientes);       // 2. El controlador, con la bodega.
            new VentanaClientes(controlador).setVisible(true);                       // 3. La ventana, con el controlador.
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
