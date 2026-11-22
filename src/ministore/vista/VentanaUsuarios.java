/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ministore.vista;
// PARTE A: los import
import java.util.ArrayList;                          // Para la lista de usuarios de la tabla.
import javax.swing.JOptionPane;                      // Para los mensajes.
import javax.swing.table.DefaultTableModel;          // El "contenido" de la tabla.
import ministore.controlador.UsuarioControlador;     // El controlador que recibe la ventana.
import ministore.modelo.Rol;                         // Los roles del combo.
import ministore.modelo.Usuario;                     // Los usuarios de la tabla.
import ministore.repositorio.RolRepositorio;         // Los repositorios los usa el main
import ministore.repositorio.UsuarioRepositorio;     // para probar la ventana sola.
/**
 *
 * @author Mauricio
 */
public class VentanaUsuarios extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaUsuarios.class.getName());
    // =====================================================================
    // PARTE B: ATRIBUTOS QUE NO GENERA EL DISEÑADOR (se escriben a mano)
    // =====================================================================
    private UsuarioControlador controlador;      // Quien valida y guarda: la ventana solo lo usa.
    private DefaultTableModel modelo;            // El contenido de tblUsuarios: filas y columnas.
    private ArrayList<Usuario> usuariosEnTabla = new ArrayList<>();   // Qué usuario hay en cada fila: // la fila 2 es usuariosEnTabla.get(2).
    /**
     * Creates new form VentanaUsuarios
     */
    // =====================================================================
    // PARTE C: CONSTRUCTOR. Recibe el controlador (lo crea Aplicacion).
    // =====================================================================
    public VentanaUsuarios(UsuarioControlador controlador) {
        initComponents();                        // PRIMERO: crea los componentes del diseñador.
        this.controlador = controlador;          // Guarda el controlador recibido.

        // La tabla con sus columnas reales (reemplaza "Title 1... Title 4").
        // No hay columna de contraseña: el usuario solo guarda su hash, y no se muestra.
        modelo = new DefaultTableModel(new String[]{"Usuario", "Nombre completo", "Rol"}, 0);
        tblUsuarios.setModel(modelo);
        tblUsuarios.setDefaultEditor(Object.class, null);   // Las celdas no se editan con doble clic.

        cargarRoles();                           // Llena cmbRol con los roles de MySQL.
        cargarTabla();                           // Muestra los usuarios activos.
        setLocationRelativeTo(null);             // Abre la ventana en el centro.
    }

    // =====================================================================
    // PARTE D: MÉTODOS AYUDANTES
    // No responden a un botón: ayudan a los botones.
    // =====================================================================

    // Llena el combo con los roles, en el orden de MySQL: 0 = Administrador, 1 = Vendedor.
    // El controlador recibe la POSICIÓN elegida y la convierte en el objeto Rol.
    private void cargarRoles() {
        cmbRol.removeAllItems();                 // Quita "Item 1, Item 2..." que puso el diseñador.
        for (Rol r : controlador.listarRoles()) {
            cmbRol.addItem(r.getNombre());
        }
        cmbRol.setSelectedIndex(-1);             // Ninguno elegido: el administrador debe escoger.
    }

    // Llena la tabla con los usuarios ACTIVOS (los desactivados no se muestran:
    // para quien usa el programa, desactivar es eliminar).
    private void cargarTabla() {
        usuariosEnTabla = controlador.listarUsuarios();     // Recuerda el usuario de cada fila.
        modelo.setRowCount(0);                   // Borra las filas anteriores (si no, se duplicarían).
        for (Usuario u : usuariosEnTabla) {
            modelo.addRow(new Object[]{u.getNombreUsuario(), u.getNombreCompleto(), u.getRol().getNombre()});
        }
    }

    // Deja el formulario vacío y sin usuario seleccionado.
    private void limpiar() {
        txtNombreUsuario.setText("");
        txtContrasena.setText("");
        txtNombreCompleto.setText("");
        cmbRol.setSelectedIndex(-1);
        tblUsuarios.clearSelection();            // Quita la selección de la tabla.
    }

    // Muestra el mensaje del controlador. Si fue exitoso ("Usuario guardado" o
    // "Usuario desactivado"), limpia el formulario y recarga la tabla.
    private void mostrarResultado(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
        if (mensaje.startsWith("Usuario ")) {    // Los éxitos empiezan por "Usuario "; los errores no.
            limpiar();
            cargarTabla();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtNombreUsuario = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombreCompleto = new javax.swing.JTextField();
        cmbRol = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        btnGuardar = new javax.swing.JButton();
        btnDesactivar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        txtContrasena = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MiniStore - Usuarios");
        setResizable(false);

        jLabel1.setText("Usuario:");

        jLabel2.setText("Contraseña:");

        jLabel3.setText("Nombre:");

        jLabel4.setText("Rol:");

        cmbRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblUsuarios);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnDesactivar.setText("Desactivar");
        btnDesactivar.addActionListener(this::btnDesactivarActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(20, 20, 20)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreCompleto)
                    .addComponent(cmbRol, 0, 144, Short.MAX_VALUE)
                    .addComponent(txtNombreUsuario)
                    .addComponent(txtContrasena))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDesactivar)
                .addGap(121, 121, 121)
                .addComponent(btnLimpiar)
                .addGap(129, 129, 129))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNombreCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmbRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDesactivar)
                    .addComponent(btnGuardar)
                    .addComponent(btnLimpiar))
                .addGap(74, 74, 74))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        // getPassword() devuelve los caracteres (char[]), no un String:
        // new String(...) los convierte en texto, igual que en el Login.
        String contrasena = new String(txtContrasena.getPassword());
        String mensaje = controlador.guardarUsuario(
                txtNombreUsuario.getText(),
                contrasena,
                txtNombreCompleto.getText(),
                cmbRol.getSelectedIndex());      // La POSICIÓN elegida (-1 = ninguna).
        mostrarResultado(mensaje);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        // TODO add your handling code here:
        // 1. ¿Hay un usuario elegido? Si no, se avisa y el método termina.
        int fila = tblUsuarios.getSelectedRow(); // La fila elegida (-1 si no hay ninguna).
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla");
            return;
        }
        Usuario u = usuariosEnTabla.get(fila);   // El usuario de esa fila.
        // 2. Se pide confirmación: showConfirmDialog muestra Sí y No.
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Desactivar a " + u.getNombreUsuario() + "? Ya no podrá iniciar sesión",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        // 3. Solo si dijo que Sí. Si es él mismo, el controlador lo impide.
        if (respuesta == JOptionPane.YES_OPTION) {
            mostrarResultado(controlador.desactivarUsuario(u.getId()));
        }
    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        limpiar();                               // Formulario vacío y sin selección.
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
        // Para probar esta ventana sola: se crean los repositorios, el administrador
        // y el controlador, y se le entregan a la ventana.
        // (En el programa completo lo hará Aplicacion, en abrirUsuarios).
        java.awt.EventQueue.invokeLater(() -> {
            UsuarioRepositorio usuarios = new UsuarioRepositorio();
            Usuario admin = usuarios.buscarPorNombreUsuario("admin");                   // 1. El administrador.
            UsuarioControlador controlador = new UsuarioControlador(                   // 2. El controlador,
                    usuarios, new RolRepositorio(), admin);                            //    con los repositorios.
            new VentanaUsuarios(controlador).setVisible(true);                         // 3. La ventana.
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<String> cmbRol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JPasswordField txtContrasena;
    private javax.swing.JTextField txtNombreCompleto;
    private javax.swing.JTextField txtNombreUsuario;
    // End of variables declaration//GEN-END:variables
}
