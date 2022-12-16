/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paneles;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author RojeruSan
 */
public class PnlProductos extends javax.swing.JPanel {
//Declarando las variables para la conexion a la base de datos

    Connection con;
    Statement sentenciasql;
    ResultSet rs;
    PreparedStatement pst;
    /**
     * Creates new form pnlHome
     */
    //otros
    DefaultComboBoxModel combox1;
    DefaultTableModel tabla;

    public PnlProductos() {
        initComponents();
        con = Clases.conexion.conectar.getConnect();

        jRadioButton3.setSelected(true);
        Llenarcombocategoria();
        LlenartablaAlmacen("");
    }

    private String RadioSelection() {
        String respuesta = "";
        if (jRadioButton1.isSelected()) {
            respuesta = "5";
        } else if (jRadioButton2.isSelected()) {
            respuesta = "10";
        } else if (jRadioButton3.isSelected()) {
            respuesta = "0";
        }

        return respuesta;
    }
public Integer cogerIdCategoria(String nombre) {
        System.out.println(nombre);
        Integer resultado = null;
        try {
            String consulta = "Select id_categoria from categoria where nom_categoria ='" + nombre + "'";
            sentenciasql = con.createStatement();
            rs = sentenciasql.executeQuery(consulta);
            rs.next();
            resultado = rs.getInt("id_categoria");
            System.out.println(resultado);
        } catch (SQLException ex) {

        }
        System.out.println("el resultado es" + resultado);
        return resultado;
    }
    private void guardarproducto() {
        String consulta = "insert into producto (codigo, nom_producto, detalle, stock, stock_minimo, precio_compra, precio_venta, id_categoria,iva)values(?,?,?,?,?,?,?,?,?)";
        try {
            pst = con.prepareStatement(consulta);
            pst.setInt(1, Integer.valueOf(txtcodbarra.getText()));
            pst.setString(2, txtdenominacion.getText().toUpperCase());
            pst.setString(3, txtdetalle.getText());
            pst.setInt(4, Integer.valueOf(txtexistencia.getText()));
            pst.setInt(5, Integer.valueOf(txtminimo.getText()));
            pst.setInt(6, Integer.valueOf(txtpreciocompra.getText()));
            pst.setInt(7, Integer.valueOf(txtprecioventa.getText()));
            pst.setInt(8, cogerIdCategoria(cmbcategoria.getSelectedItem().toString()));
            pst.setString(9, RadioSelection());

            int n = pst.executeUpdate();
            pst.close();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se guardó correctamente");
                txtcodbarra.setText("");        // TODO add your handling code here:
                txtdenominacion.setText("");        // TODO add your handling code here:
                txtdetalle.setText("");        // TODO add your handling code here:
                txtexistencia.setText("");        // TODO add your handling code here:
                txtminimo.setText("");        // TODO add your handling code here:
                txtpreciocompra.setText("");        // TODO add your handling code here:
                txtprecioventa.setText("");        // TODO add your handling code here:
            }
        } catch (SQLException ex) {
            Logger.getLogger(PnlProductos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void Llenarcombocategoria() {
        try {
            System.out.println("entrando en combo categoria");
            //Buscamos en la base de datos los datos que necesitamos
            String Consulta = "Select nom_categoria from categoria order by nom_categoria asc";
            sentenciasql = con.createStatement();
            rs = sentenciasql.executeQuery(Consulta);
            combox1 = new DefaultComboBoxModel();
            combox1.addElement("Seleccione campo");
            while (rs.next()) {
                combox1.addElement(rs.getString("nom_categoria"));
            }
            cmbcategoria.setModel(combox1);
            sentenciasql.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error = " + e.getMessage());
        }
    }
void ActualizarDatos(){

int fila = TbAlmacen.getSelectedRow();
            try {
                //String[] titulos = {"Producto", "Detalle", "Codigo", "Precio Compra", "Precio Venta", "Stock", "iva"};
                String stguardar = "update producto set nom_producto=?,detalle=?,codigo=?,precio_compra=?,precio_venta=?,stock=?,iva=? where codigo=?";
                String datos = (String) TbAlmacen.getValueAt(fila, 2);
                PreparedStatement ps = con.prepareStatement(stguardar);

                ps.setString(1, (String)TbAlmacen.getValueAt(fila, 0));
                ps.setString(2, (String)TbAlmacen.getValueAt(fila, 1));
                ps.setInt(3, Integer.parseInt(datos));
                ps.setInt(4, Integer.valueOf((String)TbAlmacen.getValueAt(fila, 3)));
                ps.setInt(5,Integer.valueOf( (String)TbAlmacen.getValueAt(fila, 4)));
                ps.setInt(6, Integer.valueOf((String)TbAlmacen.getValueAt(fila, 5)));
                ps.setString(7, (String)TbAlmacen.getValueAt(fila, 6));
                ps.setInt(8, Integer.parseInt(datos));
                
                int n = ps.executeUpdate();
                ps.close();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se actualizó correctamente");
                }
                ps.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }



}
    private void LlenartablaAlmacen(String texto) {
        System.out.println("prueba1");
        try {
            String[] titulos = {"Producto", "Detalle", "Codigo", "Precio Compra", "Precio Venta", "Stock", "iva"};
            tabla = new DefaultTableModel(null, titulos);

            //la consulta whats
            String consultasql = "Select * FROM producto WHERE nom_producto LIKE '%" + texto + "%'";
            sentenciasql = con.createStatement();
            rs = sentenciasql.executeQuery(consultasql);
            String[] fila = new String[7];
            while (rs.next()) {
                fila[0] = rs.getString("nom_producto") + "";
                fila[1] = rs.getString("detalle");
                //        String valor = nombreDpto(rs.getInt("id_ciudad"));
                fila[2] = rs.getInt("codigo") + "";
                fila[3] = rs.getInt("precio_compra") + "";
                fila[4] = rs.getInt("precio_venta") + "";
                fila[5] = rs.getInt("stock") + "";
                fila[6] = rs.getString("iva");
                tabla.addRow(fila);
            }
            TbAlmacen.setModel(tabla);

        } catch (SQLException e) {
            System.out.println("mensaje" + e.getMessage());
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtcodbarra = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtdenominacion = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtminimo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cmbcategoria = new javax.swing.JComboBox<>();
        txtdetalle = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtpreciocompra = new javax.swing.JTextField();
        txtexistencia = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btngproducto = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtprecioventa = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jCTextField1 = new app.bolivia.swing.JCTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TbAlmacen = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jCTextField2 = new app.bolivia.swing.JCTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TbAlmacen1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jCTextField3 = new app.bolivia.swing.JCTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        TbAlmacen2 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jCTextField4 = new app.bolivia.swing.JCTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        TbAlmacen3 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new javax.swing.OverlayLayout(this));

        jScrollPane1.setBorder(null);

        jTabbedPane1.setForeground(new java.awt.Color(153, 51, 0));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 51, 0));
        jLabel1.setText("Nuevo Producto");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("En el módulo PRODUCTOS podrá agregar nuevos productos al sistema, actualizar datos de los productos, eliminar o actualizar la imagen de los productos, imprimir códigos");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("de barras de cada producto, buscar productos en el sistema, ver todos los productos en almacén, ver los productos más vendido y filtrar productos por categoría.");

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Información Personal");

        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Stock o Existencia");

        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Código de Barras");

        txtcodbarra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtcodbarra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcodbarraActionPerformed(evt);
            }
        });
        txtcodbarra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcodbarraKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcodbarraKeyTyped(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Nombre o Denominación");

        txtdenominacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtdenominacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdenominacionKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtdenominacionKeyTyped(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Stock Mínimo");

        txtminimo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtminimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtminimoActionPerformed(evt);
            }
        });
        txtminimo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtminimoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtminimoKeyTyped(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Categoría ");

        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Detalle");

        cmbcategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Lácteos" }));
        cmbcategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbcategoriaMouseClicked(evt);
            }
        });
        cmbcategoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbcategoriaKeyPressed(evt);
            }
        });

        txtdetalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtdetalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdetalleKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtdetalleKeyTyped(evt);
            }
        });

        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Precio Compra");

        txtpreciocompra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtpreciocompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpreciocompraKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtpreciocompraKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtpreciocompraKeyTyped(evt);
            }
        });

        txtexistencia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtexistencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtexistenciaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtexistenciaKeyTyped(evt);
            }
        });

        jButton1.setText("Limpiar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btngproducto.setBackground(new java.awt.Color(0, 102, 102));
        btngproducto.setText("Guardar");
        btngproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngproductoActionPerformed(evt);
            }
        });

        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Los Campos marcados con * son Obligatorios");

        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Precio Venta");

        txtprecioventa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtprecioventa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtprecioventaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtprecioventaKeyTyped(evt);
            }
        });

        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("IVA");

        jRadioButton1.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("5%");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jRadioButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jRadioButton1KeyPressed(evt);
            }
        });

        jRadioButton2.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("10%");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jRadioButton3.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Exentas");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(345, 345, 345)
                        .addComponent(jButton1)
                        .addGap(25, 25, 25)
                        .addComponent(btngproducto))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtcodbarra)
                                    .addComponent(txtdenominacion, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtdetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbcategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(97, 97, 97)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtexistencia, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(82, 82, 82)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtminimo, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel15)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtprecioventa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                        .addComponent(txtpreciocompra, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(jLabel18)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jRadioButton1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jRadioButton2)
                                        .addGap(18, 18, 18)
                                        .addComponent(jRadioButton3))))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(126, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbcategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2)
                            .addComponent(jRadioButton3)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtpreciocompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtprecioventa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtexistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtminimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtdenominacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtdetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(70, 70, 70)))
                .addGap(29, 29, 29)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btngproducto))
                .addGap(10, 10, 10)
                .addComponent(jLabel16)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(171, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(30, 30, 30)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Nuevo Producto", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 51, 0));
        jLabel5.setText("Productos en Almacen");

        jCTextField1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(153, 153, 153)));
        jCTextField1.setForeground(new java.awt.Color(102, 102, 102));
        jCTextField1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCTextField1.setPhColor(new java.awt.Color(153, 153, 153));
        jCTextField1.setPlaceholder("BUSCAR");
        jCTextField1.setSelectedTextColor(new java.awt.Color(102, 102, 102));
        jCTextField1.setSelectionColor(new java.awt.Color(153, 51, 0));
        jCTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jCTextField1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jCTextField1KeyTyped(evt);
            }
        });

        TbAlmacen.setForeground(new java.awt.Color(153, 153, 153));
        TbAlmacen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "PRECIO", "STOCK", "VENDIDOS", "ESTADO"
            }
        ));
        TbAlmacen.setGridColor(new java.awt.Color(204, 204, 204));
        TbAlmacen.setSelectionBackground(new java.awt.Color(153, 51, 0));
        TbAlmacen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TbAlmacenKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(TbAlmacen);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jCTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel5)
                .addGap(30, 30, 30)
                .addComponent(jCTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Almacen", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 51, 0));
        jLabel6.setText("Lo Más Vendido");

        jCTextField2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(153, 153, 153)));
        jCTextField2.setForeground(new java.awt.Color(102, 102, 102));
        jCTextField2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCTextField2.setPhColor(new java.awt.Color(153, 153, 153));
        jCTextField2.setPlaceholder("BUSCAR");
        jCTextField2.setSelectedTextColor(new java.awt.Color(102, 102, 102));
        jCTextField2.setSelectionColor(new java.awt.Color(0, 102, 102));
        jCTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jCTextField2KeyTyped(evt);
            }
        });

        TbAlmacen1.setForeground(new java.awt.Color(153, 153, 153));
        TbAlmacen1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "PRECIO", "STOCK", "VENDIDOS", "ESTADO"
            }
        ));
        TbAlmacen1.setGridColor(new java.awt.Color(204, 204, 204));
        TbAlmacen1.setSelectionBackground(new java.awt.Color(153, 51, 0));
        jScrollPane3.setViewportView(TbAlmacen1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jCTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel6)
                .addGap(30, 30, 30)
                .addComponent(jCTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Lo mas Vendido", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 51, 0));
        jLabel12.setText("Producto por Categoría");

        jCTextField3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(153, 153, 153)));
        jCTextField3.setForeground(new java.awt.Color(102, 102, 102));
        jCTextField3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCTextField3.setPhColor(new java.awt.Color(153, 153, 153));
        jCTextField3.setPlaceholder("BUSCAR");
        jCTextField3.setSelectedTextColor(new java.awt.Color(102, 102, 102));
        jCTextField3.setSelectionColor(new java.awt.Color(0, 102, 102));
        jCTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jCTextField3KeyTyped(evt);
            }
        });

        TbAlmacen2.setForeground(new java.awt.Color(153, 153, 153));
        TbAlmacen2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "PRECIO", "STOCK", "VENDIDOS", "ESTADO"
            }
        ));
        TbAlmacen2.setGridColor(new java.awt.Color(204, 204, 204));
        TbAlmacen2.setSelectionBackground(new java.awt.Color(153, 51, 0));
        jScrollPane4.setViewportView(TbAlmacen2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jCTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel12)
                .addGap(30, 30, 30)
                .addComponent(jCTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Por Categoría", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(153, 51, 0));
        jLabel17.setText("Stock Mínimo");

        jCTextField4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(153, 153, 153)));
        jCTextField4.setForeground(new java.awt.Color(102, 102, 102));
        jCTextField4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCTextField4.setPhColor(new java.awt.Color(153, 153, 153));
        jCTextField4.setPlaceholder("BUSCAR");
        jCTextField4.setSelectedTextColor(new java.awt.Color(102, 102, 102));
        jCTextField4.setSelectionColor(new java.awt.Color(153, 51, 0));
        jCTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jCTextField4KeyTyped(evt);
            }
        });

        TbAlmacen3.setForeground(new java.awt.Color(153, 153, 153));
        TbAlmacen3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "PRECIO", "STOCK", "VENDIDOS", "ESTADO"
            }
        ));
        TbAlmacen3.setGridColor(new java.awt.Color(204, 204, 204));
        TbAlmacen3.setSelectionBackground(new java.awt.Color(153, 51, 0));
        jScrollPane5.setViewportView(TbAlmacen3);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jCTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel17)
                .addGap(30, 30, 30)
                .addComponent(jCTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Stock Minimo", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1316, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 697, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Devolución", jPanel6);

        jScrollPane1.setViewportView(jTabbedPane1);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void txtminimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtminimoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtminimoActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void txtexistenciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtexistenciaKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            getToolkit().beep();
            evt.consume();
            System.out.println("Ingrese solo numeros");
        }
        if (txtexistencia.getText().length() >= 9) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtexistenciaKeyTyped

    private void txtminimoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtminimoKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            getToolkit().beep();
            evt.consume();
            System.out.println("Ingrese solo numeros");
        }
        if (txtminimo.getText().length() >= 9) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtminimoKeyTyped

    private void txtdenominacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdenominacionKeyTyped
        if (txtdenominacion.getText().length() >= 100) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtdenominacionKeyTyped

    private void txtpreciocompraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpreciocompraKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            getToolkit().beep();
            evt.consume();
            System.out.println("Ingrese solo numeros");
        }
        if (txtpreciocompra.getText().length() >= 10) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }
// TODO add your handling code here:
    }//GEN-LAST:event_txtpreciocompraKeyTyped

    private void txtprecioventaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtprecioventaKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            getToolkit().beep();
            evt.consume();
            System.out.println("Ingrese solo numeros");
        }
        if (txtprecioventa.getText().length() >= 10) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtprecioventaKeyTyped

    private void txtdetalleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdetalleKeyTyped
        if (txtdetalle.getText().length() >= 150) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtdetalleKeyTyped

    private void jCTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCTextField1KeyTyped
        if (txtcodbarra.getText().length() >= 35) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField1KeyTyped

    private void jCTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCTextField2KeyTyped
        if (txtdenominacion.getText().length() >= 35) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField2KeyTyped

    private void jCTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCTextField3KeyTyped
        if (jCTextField3.getText().length() >= 35) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField3KeyTyped

    private void jCTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCTextField4KeyTyped
        if (jCTextField4.getText().length() >= 35) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField4KeyTyped

    private void txtcodbarraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcodbarraKeyTyped

// TODO add your handling code here:
    }//GEN-LAST:event_txtcodbarraKeyTyped

    private void txtcodbarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcodbarraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcodbarraActionPerformed

    private void txtcodbarraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcodbarraKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtdenominacion.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtcodbarraKeyPressed

    private void txtdenominacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdenominacionKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtdetalle.requestFocus();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtdenominacionKeyPressed

    private void txtdetalleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdetalleKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            cmbcategoria.requestFocus();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtdetalleKeyPressed

    private void cmbcategoriaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbcategoriaKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtexistencia.requestFocus();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_cmbcategoriaKeyPressed

    private void cmbcategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbcategoriaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbcategoriaMouseClicked

    private void txtexistenciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtexistenciaKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtminimo.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtexistenciaKeyPressed

    private void txtminimoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtminimoKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtpreciocompra.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtminimoKeyPressed

    private void txtpreciocompraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpreciocompraKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtprecioventa.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtpreciocompraKeyPressed

    private void txtprecioventaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtprecioventaKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            jRadioButton1.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtprecioventaKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        txtcodbarra.setText("");        // TODO add your handling code here:
        txtdenominacion.setText("");        // TODO add your handling code here:
        txtdetalle.setText("");        // TODO add your handling code here:
        txtexistencia.setText("");        // TODO add your handling code here:
        txtminimo.setText("");        // TODO add your handling code here:
        txtpreciocompra.setText("");        // TODO add your handling code here:
        txtprecioventa.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btngproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngproductoActionPerformed
        System.out.println("entando a guardar");
        if (txtcodbarra.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un codigo de producto.", "Error", JOptionPane.ERROR_MESSAGE);
            txtcodbarra.requestFocus();
        } else // TODO add your handling code here:
        if (txtdenominacion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un nombre al producto.", "Error", JOptionPane.ERROR_MESSAGE);
            txtdenominacion.requestFocus();
        } else // TODO add your handling code here:
        if (txtexistencia.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar la cantidad a registrarse.", "Error", JOptionPane.ERROR_MESSAGE);
            txtexistencia.requestFocus();
        } else if (txtminimo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar la cantidad minima para avisar.", "Error", JOptionPane.ERROR_MESSAGE);
            txtminimo.requestFocus();
        } else if (txtpreciocompra.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar El precio de compra del producto.", "Error", JOptionPane.ERROR_MESSAGE);
            txtpreciocompra.requestFocus();
        } else if (txtprecioventa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar El precio de venta del producto.", "Error", JOptionPane.ERROR_MESSAGE);
            txtprecioventa.requestFocus();
        } else {
            guardarproducto();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btngproductoActionPerformed

    private void txtpreciocompraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpreciocompraKeyReleased
        try {
            String x = (Integer.valueOf(txtpreciocompra.getText()) * 0.3) + "";
            System.out.println("" + x);
            double x2 = (Double.parseDouble(x) + Integer.valueOf(txtpreciocompra.getText()));
            System.out.println("" + x2);
            txtprecioventa.setText(Math.round(x2) + "");
        } catch (Exception e) {

        }// TODO add your handling code here:
    }//GEN-LAST:event_txtpreciocompraKeyReleased

    private void jRadioButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jRadioButton1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1KeyPressed

    private void jCTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCTextField1KeyReleased
        LlenartablaAlmacen(jCTextField1.getText().toUpperCase());
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField1KeyReleased

    private void TbAlmacenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TbAlmacenKeyTyped
int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            int opcion = JOptionPane.showConfirmDialog(null, "Está seguro que desea modificar el registro?", "confirmacion", JOptionPane.YES_NO_OPTION);
             if (opcion == JOptionPane.YES_OPTION){
             ActualizarDatos();
             }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_TbAlmacenKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TbAlmacen;
    private javax.swing.JTable TbAlmacen1;
    private javax.swing.JTable TbAlmacen2;
    private javax.swing.JTable TbAlmacen3;
    private javax.swing.JButton btngproducto;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbcategoria;
    private javax.swing.JButton jButton1;
    private app.bolivia.swing.JCTextField jCTextField1;
    private app.bolivia.swing.JCTextField jCTextField2;
    private app.bolivia.swing.JCTextField jCTextField3;
    private app.bolivia.swing.JCTextField jCTextField4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtcodbarra;
    private javax.swing.JTextField txtdenominacion;
    private javax.swing.JTextField txtdetalle;
    private javax.swing.JTextField txtexistencia;
    private javax.swing.JTextField txtminimo;
    private javax.swing.JTextField txtpreciocompra;
    private javax.swing.JTextField txtprecioventa;
    // End of variables declaration//GEN-END:variables
}
