/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paneles;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import sun.jvm.hotspot.utilities.soql.SOQLException;

/**
 *
 * @author RojeruSan
 */
public class PnlAdministracion extends javax.swing.JPanel {
//Declarando las variables para la conexion a la base de datos

    Connection con;
    Statement sentenciasql;
    ResultSet rs;
    PreparedStatement pst;
//otros
    DefaultComboBoxModel combox1;
    DefaultComboBoxModel combox2;
    DefaultComboBoxModel combox3;
    DefaultComboBoxModel combox4;
    Integer IdCiudad = null;

    /**
     * Creates new form pnlHome
     */
    public PnlAdministracion() {
        initComponents();
        //conectando con la base de Datos
        con = Clases.conexion.conectar.getConnect();
        Llenarcombodepartamento();
        Llenarcombocargo();
        Llenarcombocaja();

    }

    private void Llenarcombocaja() {
        try {
            //Buscamos en la base de datos los datos que necesitamos
            String Consulta = "Select num_caja from caja order by num_caja asc";
            sentenciasql = con.createStatement();
            rs = sentenciasql.executeQuery(Consulta);
            combox4 = new DefaultComboBoxModel();
            combox4.addElement("Seleccione campo");
            while (rs.next()) {
                combox4.addElement(rs.getString("num_caja"));
            }
            cmbcajaselec.setModel(combox4);
            sentenciasql.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error = " + e.getMessage());
        }
    }

    private void Llenarcombocargo() {
        try {
            //Buscamos en la base de datos los datos que necesitamos
            String Consulta = "Select nom_cargo from cargo order by nom_cargo asc";
            sentenciasql = con.createStatement();
            rs = sentenciasql.executeQuery(Consulta);
            combox3 = new DefaultComboBoxModel();
            combox3.addElement("Seleccione campo");
            while (rs.next()) {
                combox3.addElement(rs.getString("nom_cargo"));
            }
            cmbcargo.setModel(combox3);
            sentenciasql.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error = " + e.getMessage());
        }
    }

    private void Llenarcombodepartamento() {
        try {
            //Buscamos en la base de datos los datos que necesitamos
            String Consulta = "Select nom_departamento from departamento order by nom_departamento asc";
            sentenciasql = con.createStatement();
            rs = sentenciasql.executeQuery(Consulta);
            combox1 = new DefaultComboBoxModel();
            combox1.addElement("Seleccione campo");
            while (rs.next()) {
                combox1.addElement(rs.getString("nom_departamento"));
            }
            cmbdept.setModel(combox1);
            sentenciasql.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error = " + e.getMessage());
        }

    }

    private void llenarCmbCiu(Integer iddepto) {
        try {
            //Buscamos en la base de datos los datos que necesitamos
            String Consulta = "Select * from ciudad where id_departamento = '" + iddepto + "' order by nom_ciudad asc";
            sentenciasql = con.createStatement();
            rs = sentenciasql.executeQuery(Consulta);
            combox2 = new DefaultComboBoxModel();
            combox2.addElement("Seleccione campo");
            while (rs.next()) {
                combox2.addElement(rs.getString("nom_ciudad"));
            }
            cmbciudad.setModel(combox2);
            sentenciasql.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error = " + e.getMessage());
        }
    }

    private void guardarextraccion() {
        String sentguardar = "insert into extraccion (num_caja, monto, motivo,fecha,hora ) values (?,?,?,?,?)";
        try {
            pst = con.prepareStatement(sentguardar);
            pst.setInt(1, Integer.valueOf(cmbcajaselec.getSelectedItem().toString()));
            pst.setInt(2, Integer.valueOf(txtretiro.getText()));
            pst.setString(3, txtmotivo.getText());
            java.util.Date ahora = new java.util.Date();
            SimpleDateFormat formateadora = new SimpleDateFormat("dd-MM-yyyy");
            String fecha = formateadora.format(ahora);
            pst.setDate(4, Date.valueOf(fecha));
            Calendar cal = Calendar.getInstance();
            String hora = cal.get(cal.HOUR_OF_DAY) + ":" + cal.get(cal.MINUTE) + ":" + cal.get(cal.SECOND);
            pst.setTime(5, Time.valueOf(hora));
            int n = pst.executeUpdate();
            pst.close();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se guardó correctamente");
                txtmontoactual.setText("");
                txtretiro.setText("");
                txtmotivo.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PnlAdministracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardarcategoria() {
        String sentguardar = "insert into categoria (nom_categoria, estado, ubicacion ) values (?,?,?)";
        try {
            pst = con.prepareStatement(sentguardar);
            pst.setString(1, txtcat.getText());
            pst.setString(3, txtubic.getText());
            pst.setBoolean(2, true);
            int n = pst.executeUpdate();
            pst.close();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se guardó correctamente");
                txtcat.setText("");
                txtubic.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PnlAdministracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer cogerIdDpto(String nombre) {
        System.out.println(nombre);
        Integer resultado = null;
        try {
            String consulta = "Select id_departamento from departamento where nom_departamento ='" + nombre + "'";
            sentenciasql = con.createStatement();
            rs = sentenciasql.executeQuery(consulta);
            rs.next();
            resultado = rs.getInt("id_departamento");
            System.out.println(resultado);
        } catch (SQLException ex) {

        }
        System.out.println("el resultado es" + resultado);
        return resultado;
    }

    private void guardarcaja() {
        String sentcaja = "insert into caja (num_caja, serie, estado, efectivo)values(?,?,?,?)";
        try {
            pst = con.prepareStatement(sentcaja);
            pst.setInt(1, Integer.valueOf(txtnumcaja.getText()));
            pst.setString(2, txtserie.getText());
            pst.setBoolean(3, true);
            pst.setInt(4, Integer.valueOf(txtefectivo.getText()));
            int n = pst.executeUpdate();
            pst.close();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se guardó correctamente");
                txtnumcaja.setText("");        // TODO add your handling code here:
                txtserie.setText("");        // TODO add your handling code here:
                txtefectivo.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PnlAdministracion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Integer IdCiudad(String id) {
        System.out.println("prueba para allar el id de la ciudad llega "+id);
        Integer valor = null;
        try {
            String consulta = "Select id_ciudad from ciudad where nom_ciudad = " + id;
            Statement sentencia = con.createStatement();
            ResultSet rs2 = sentencia.executeQuery(consulta);
            rs2.next();
            valor = rs2.getInt("id_ciudad");
            System.out.println("valor " + valor);
            rs2.close();
        } catch (SQLException e) {
            System.out.println("error" + e.getMessage().toUpperCase());
        }
        return valor;

    }

    private Integer IdCargo(String id) {
        System.out.println("prueba para allar el id de Cargo y entra "+id);
        Integer valor = null;
        try {
            String consulta = "Select id_cargo from cargo where nom_cargo like '" + id+"'";
            Statement sentencia = con.createStatement();
            ResultSet rs2 = sentencia.executeQuery(consulta);
            rs2.next();
            valor = rs2.getInt("id_cargo");
            System.out.println("valor " + valor);
            rs2.close();
        } catch (SQLException e) {
            System.out.println("error" + e.getMessage());
        }
        return valor;
    }

    public Integer cogerIdCiudad(String nombre) {
        System.out.println(nombre);
        Integer resultado = null;
        try {
            String consulta = "Select id_ciudad from ciudad where nom_ciudad ='" + nombre + "'";
            sentenciasql = con.createStatement();
            rs = sentenciasql.executeQuery(consulta);
            rs.next();
            resultado = rs.getInt(1);
            System.out.println(resultado);
        } catch (SQLException ex) {
        }
        System.out.println("el resultado es" + resultado);
        return resultado;
    }

    private void guardarproveedor() {
        String sentguardar = "insert into proveedor (nom_proveedor, direccion, nom_encargado, doc_tipo, num_documento, contacto, estado ) values (?,?,?,?,?,?,?)";
        try {
            pst = con.prepareStatement(sentguardar);
            pst.setString(1, txtnomprv.getText());
            pst.setString(2, txtdirprv.getText());
            pst.setString(3, txtencargadoprv.getText());
            pst.setString(4, cmbdocprv.getSelectedItem().toString());
            pst.setString(5, txtnumdocprv.getText());
            pst.setInt(6, Integer.valueOf(txtcontactoprv.getText()));
            
            pst.setBoolean(7, true);

            int n = pst.executeUpdate();
            pst.close();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se guardó correctamente");
                txtnomprv.setText("");        // TODO add your handling code here:
                txtnumdocprv.setText("");        // TODO add your handling code here:
                txtdirprv.setText("");        // TODO add your handling code here:
                txtencargadoprv.setText("");        // TODO add your handling code here:
                txtemailprv.setText("");        // TODO add your handling code here:
                txtcontactoprv.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PnlAdministracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardarusuario() {
        String sentguardar = "insert into usuario (nom_usuario, ape_usuario, num_documento, doc_tipo, genero, nick, pass, estado,id_cargo ) values (?,?,?,?,?,?,?,?,?)";
        try {
            pst = con.prepareStatement(sentguardar);
            pst.setString(1, txtnomusu.getText());
            pst.setString(2, txtapellidousu.getText());
            pst.setString(3, txtnumdocusu.getText());
            pst.setString(4, cmbtipocl.getSelectedItem().toString());
            pst.setString(5, cmbgenero.getSelectedItem().toString());
            pst.setString(6, txtnick.getText());
            pst.setString(7, txtpass.getText());
            pst.setBoolean(8, true);
            pst.setInt(9, IdCargo(cmbcargo.getSelectedItem().toString()));

            int n = pst.executeUpdate();
            pst.close();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se guardó correctamente");
                txtapecl.setText("");        // TODO add your handling code here:
                txtnomcl.setText("");        // TODO add your handling code here:
                txtnumdoccl.setText("");        // TODO add your handling code here:
                txtmailcl.setText("");        // TODO add your handling code here:
                txtcelcl.setText("");        // TODO add your handling code here:
                txtcallecl.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PnlAdministracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardarcliente() {
        String sentguardar = "insert into cliente (nom_cliente, ape_cliente, num_documento, doc_tipo, direccion, contacto, email, id_ciudad ) values (?,?,?,?,?,?,?,?)";
        try {
            pst = con.prepareStatement(sentguardar);
            pst.setString(1, txtnomcl.getText());
            pst.setString(2, txtapecl.getText());
            pst.setInt(3, Integer.valueOf(txtnumdoccl.getText()));
            pst.setString(4, cmbtipocl.getSelectedItem().toString());
            pst.setString(5, txtcallecl.getText());
            pst.setInt(6, Integer.valueOf(txtcelcl.getText()));
            pst.setString(7, txtmailcl.getText());
            
            pst.setInt(8, IdCiudad);

            int n = pst.executeUpdate();
            pst.close();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se guardó correctamente");
                txtapecl.setText("");        // TODO add your handling code here:
                txtnomcl.setText("");        // TODO add your handling code here:
                txtnumdoccl.setText("");        // TODO add your handling code here:
                txtmailcl.setText("");        // TODO add your handling code here:
                txtcelcl.setText("");        // TODO add your handling code here:
                txtcallecl.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PnlAdministracion.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbtipocl = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtnumdoccl = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtnomcl = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtapecl = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cmbdept = new javax.swing.JComboBox<>();
        cmbciudad = new javax.swing.JComboBox<>();
        txtcallecl = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtcelcl = new javax.swing.JTextField();
        txtmailcl = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnguardarcl = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cmbdocprv = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        txtnumdocprv = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtnomprv = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtdirprv = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        txtemailprv = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtencargadoprv = new javax.swing.JTextField();
        txtcontactoprv = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        btngprov = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtserie = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtnumcaja = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtefectivo = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        cmbestadocaja = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        btngcaja = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        cmbtipousu = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        txtnumdocusu = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txtnomusu = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        txtapellidousu = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        cmbcargo = new javax.swing.JComboBox<>();
        txtpass2 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        txtnick = new javax.swing.JTextField();
        txtpass = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        btnguardarusuario = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        cmbgenero = new javax.swing.JComboBox<>();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        txtubic = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        txtcat = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        cmbestado = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        btnGuardarCat = new javax.swing.JButton();
        jLabel66 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        txtretiro = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        txtmotivo = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        cmbcajaselec = new javax.swing.JComboBox<>();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel71 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        txtmontoactual = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setOpaque(false);
        setLayout(new javax.swing.OverlayLayout(this));

        jScrollPane1.setBorder(null);

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setForeground(new java.awt.Color(153, 51, 0));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 51, 0));
        jLabel1.setText("Nuevo Cliente");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("En el módulo CLIENTE podrá registrar en el sistema los datos de sus clientes más frecuentes para realizar ventas, además podrá realizar búsquedas de clientes, ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("actualizar datos de sus clientes o eliminarlos si así lo desea.");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Información Personal");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Información de Residencia");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Información de Contacto");

        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Numero de Documento");

        cmbtipocl.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CIN", "DNI", "PASAPORTE", "OTROS" }));
        cmbtipocl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbtipoclKeyPressed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Tipo de Documento");

        txtnumdoccl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtnumdoccl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnumdocclKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnumdocclKeyTyped(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Nombre");

        txtnomcl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtnomcl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnomclKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnomclKeyTyped(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Apellido");

        txtapecl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtapecl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtapeclActionPerformed(evt);
            }
        });
        txtapecl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtapeclKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtapeclKeyTyped(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Departamento");

        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("Ciudad");

        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Calle, finca, Locación");

        cmbdept.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Misiones", "Central", "Itapua", "Ñeembucu" }));
        cmbdept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbdeptActionPerformed(evt);
            }
        });

        cmbciudad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "San Juan Bautista", "San Ignacio", "San Miguel", "Santa Rosa" }));
        cmbciudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbciudadActionPerformed(evt);
            }
        });

        txtcallecl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtcallecl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcalleclKeyPressed(evt);
            }
        });

        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Teléfono, Celular");

        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Email");

        txtcelcl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtcelcl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcelclKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcelclKeyTyped(evt);
            }
        });

        txtmailcl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtmailcl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtmailclKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtmailclKeyTyped(evt);
            }
        });

        jButton1.setText("Limpiar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnguardarcl.setBackground(new java.awt.Color(0, 102, 102));
        btnguardarcl.setText("Guardar");
        btnguardarcl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarclActionPerformed(evt);
            }
        });

        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Los Campos marcados con * son Obligatorios");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11)
                                            .addComponent(cmbdept, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(50, 50, 50)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel12)
                                            .addComponent(cmbciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel9)
                                        .addComponent(cmbtipocl, 0, 267, Short.MAX_VALUE)
                                        .addComponent(txtnomcl))
                                    .addComponent(jLabel14)
                                    .addComponent(txtcelcl, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(46, 46, 46)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(txtmailcl, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel10)
                                        .addComponent(txtnumdoccl)
                                        .addComponent(txtapecl, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtcallecl, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(345, 345, 345)
                        .addComponent(jButton1)
                        .addGap(25, 25, 25)
                        .addComponent(btnguardarcl)))
                .addContainerGap(46, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbtipocl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnumdoccl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtnomcl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtapecl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(jLabel5)
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbdept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbciudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcallecl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(jLabel6)
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcelcl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtmailcl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnguardarcl))
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
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(1838, Short.MAX_VALUE))
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
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(194, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Nuevo Cliente", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(153, 51, 0));
        jLabel17.setText("Nuevo Proveedor");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("En el módulo PROVEEDORES usted podrá registrar los proveedores de productos a los cuales usted les compra productos o mercancía. Además, podrá actualizar");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setText("los datos de los proveedores, ver todos los proveedores registrados en el sistema, buscar proveedores en el sistema o eliminarlos si así lo desea.");

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setText("Datos Proveedor");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setText("Información de Contacto");

        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setText("Numero de Documento");

        cmbdocprv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CIN", "RUC", "DNI", "PASAPORTE", "OTROS" }));
        cmbdocprv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbdocprvKeyPressed(evt);
            }
        });

        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setText("Tipo de Documento");

        txtnumdocprv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtnumdocprv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnumdocprvKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnumdocprvKeyTyped(evt);
            }
        });

        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Nombre");

        txtnomprv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtnomprv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnomprvKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnomprvKeyTyped(evt);
            }
        });

        jLabel26.setForeground(new java.awt.Color(102, 102, 102));
        jLabel26.setText("Dirección");

        txtdirprv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtdirprv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdirprvActionPerformed(evt);
            }
        });
        txtdirprv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdirprvKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtdirprvKeyTyped(evt);
            }
        });

        jLabel27.setForeground(new java.awt.Color(102, 102, 102));
        jLabel27.setText("Estado");

        jLabel29.setForeground(new java.awt.Color(102, 102, 102));
        jLabel29.setText("Email");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Habilitado", "Desabilitado" }));
        jComboBox5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox5KeyPressed(evt);
            }
        });

        txtemailprv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtemailprv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtemailprvKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtemailprvKeyTyped(evt);
            }
        });

        jLabel30.setForeground(new java.awt.Color(102, 102, 102));
        jLabel30.setText("Encargado");

        jLabel31.setForeground(new java.awt.Color(102, 102, 102));
        jLabel31.setText("Teléfono, Celular");

        txtencargadoprv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtencargadoprv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtencargadoprvKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtencargadoprvKeyTyped(evt);
            }
        });

        txtcontactoprv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtcontactoprv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcontactoprvKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcontactoprvKeyTyped(evt);
            }
        });

        jButton3.setText("Limpiar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btngprov.setBackground(new java.awt.Color(0, 102, 102));
        btngprov.setText("Guardar");
        btngprov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngprovActionPerformed(evt);
            }
        });

        jLabel32.setForeground(new java.awt.Color(102, 102, 102));
        jLabel32.setText("Los Campos marcados con * son Obligatorios");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel27)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25)
                            .addComponent(cmbdocprv, 0, 267, Short.MAX_VALUE)
                            .addComponent(txtnomprv)
                            .addComponent(jLabel30)
                            .addComponent(jLabel22)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(txtencargadoprv)
                                .addGap(30, 30, 30)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(156, 156, 156)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel26)
                                    .addComponent(txtnumdocprv, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtdirprv, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addComponent(txtcontactoprv, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtemailprv, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(326, 326, 326)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jButton3)
                        .addGap(25, 25, 25)
                        .addComponent(btngprov))
                    .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel24)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbdocprv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnumdocprv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtnomprv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdirprv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel22)
                .addGap(15, 15, 15)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel30)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtencargadoprv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel31)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtcontactoprv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtemailprv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(btngprov))
                .addGap(10, 10, 10)
                .addComponent(jLabel32)
                .addGap(68, 68, 68))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel18)
                    .addComponent(jLabel17))
                .addContainerGap(1834, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel17)
                .addGap(30, 30, 30)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addGap(30, 30, 30)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(174, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Nuevo Proveedor", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(153, 51, 0));
        jLabel21.setText("Nueva Caja");

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setText("En el módulo CAJA usted podrá registrar cajas de ventas en el sistema para poder realizar ventas, además podrá actualizar los datos de las cajas de venta, realizar");

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel33.setText("búsquedas de cajas o eliminarlas si lo desea.");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setText("Información de la Caja");

        jLabel36.setForeground(new java.awt.Color(102, 102, 102));
        jLabel36.setText("Serie o Denominación");

        txtserie.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtserie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtserieKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtserieKeyTyped(evt);
            }
        });

        jLabel38.setForeground(new java.awt.Color(102, 102, 102));
        jLabel38.setText("Numero de Caja");

        txtnumcaja.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtnumcaja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnumcajaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnumcajaKeyTyped(evt);
            }
        });

        jLabel39.setForeground(new java.awt.Color(102, 102, 102));
        jLabel39.setText("Efectivo en Caja");

        txtefectivo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtefectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtefectivoActionPerformed(evt);
            }
        });
        txtefectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtefectivoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtefectivoKeyTyped(evt);
            }
        });

        jLabel40.setForeground(new java.awt.Color(102, 102, 102));
        jLabel40.setText("Estado");

        cmbestadocaja.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Habilitado", "Desabilitado" }));

        jButton5.setText("Limpiar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        btngcaja.setBackground(new java.awt.Color(0, 102, 102));
        btngcaja.setText("Guardar");
        btngcaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngcajaActionPerformed(evt);
            }
        });

        jLabel44.setForeground(new java.awt.Color(102, 102, 102));
        jLabel44.setText("Los Campos marcados con * son Obligatorios");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel40)
                                    .addComponent(cmbestadocaja, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel38)
                                    .addComponent(txtnumcaja, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(132, 132, 132)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel36)
                                    .addComponent(txtserie, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtefectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel34)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(326, 326, 326)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jButton5)
                                .addGap(25, 25, 25)
                                .addComponent(btngcaja))
                            .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel34)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtserie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnumcaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtefectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbestadocaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(btngcaja))
                .addGap(10, 10, 10)
                .addComponent(jLabel44)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel28)
                    .addComponent(jLabel21))
                .addContainerGap(1824, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel21)
                .addGap(30, 30, 30)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addGap(30, 30, 30)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(423, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Nueva Caja", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(153, 51, 0));
        jLabel35.setText("Nuevo Usuario");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel37.setText("En el módulo USUARIO podrá registrar nuevos usuarios en el sistema ya sea un administrador o un cajero, también podrá ver la lista de usuarios registrados, buscar");

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel41.setText("usuarios en el sistema, actualizar datos de otros usuarios y los suyos.");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel42.setText("Información Personal");

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setText("Información de Cuenta");

        jLabel46.setForeground(new java.awt.Color(102, 102, 102));
        jLabel46.setText("Numero de Documento");

        cmbtipousu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CIN", "DNI", "PASAPORTE", "OTROS" }));
        cmbtipousu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbtipousuKeyPressed(evt);
            }
        });

        jLabel47.setForeground(new java.awt.Color(102, 102, 102));
        jLabel47.setText("Tipo de Documento");

        txtnumdocusu.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtnumdocusu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnumdocusuKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnumdocusuKeyTyped(evt);
            }
        });

        jLabel48.setForeground(new java.awt.Color(102, 102, 102));
        jLabel48.setText("Nombre");

        txtnomusu.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtnomusu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnomusuKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnomusuKeyTyped(evt);
            }
        });

        jLabel49.setForeground(new java.awt.Color(102, 102, 102));
        jLabel49.setText("Apellido");

        txtapellidousu.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtapellidousu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtapellidousuActionPerformed(evt);
            }
        });
        txtapellidousu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtapellidousuKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtapellidousuKeyTyped(evt);
            }
        });

        jLabel50.setForeground(new java.awt.Color(102, 102, 102));
        jLabel50.setText("Cargo");

        jLabel52.setForeground(new java.awt.Color(102, 102, 102));
        jLabel52.setText("Género");

        cmbcargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Cajero" }));
        cmbcargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcargoActionPerformed(evt);
            }
        });
        cmbcargo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbcargoKeyPressed(evt);
            }
        });

        txtpass2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtpass2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpass2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtpass2KeyTyped(evt);
            }
        });

        jLabel53.setForeground(new java.awt.Color(102, 102, 102));
        jLabel53.setText("Nombre de Usuario");

        jLabel54.setForeground(new java.awt.Color(102, 102, 102));
        jLabel54.setText("Contraseña");

        txtnick.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtnick.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnickKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnickKeyTyped(evt);
            }
        });

        txtpass.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtpass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpassKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtpassKeyTyped(evt);
            }
        });

        jButton7.setText("Limpiar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        btnguardarusuario.setBackground(new java.awt.Color(0, 102, 102));
        btnguardarusuario.setText("Guardar");
        btnguardarusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarusuarioActionPerformed(evt);
            }
        });

        jLabel55.setForeground(new java.awt.Color(102, 102, 102));
        jLabel55.setText("Los Campos marcados con * son Obligatorios");

        cmbgenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Femenino" }));
        cmbgenero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbgeneroActionPerformed(evt);
            }
        });
        cmbgenero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbgeneroKeyPressed(evt);
            }
        });

        jLabel56.setForeground(new java.awt.Color(102, 102, 102));
        jLabel56.setText("Repetir Contraseña");

        jLabel57.setForeground(new java.awt.Color(102, 102, 102));
        jLabel57.setText("Estado de Cuenta");

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activa", "Inactiva" }));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel45)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtnick, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel53)
                                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(163, 163, 163)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtpass, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                                    .addComponent(jLabel54)
                                    .addComponent(jLabel56)
                                    .addComponent(txtpass2)))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel47)
                                        .addComponent(jLabel48)
                                        .addComponent(cmbtipousu, 0, 267, Short.MAX_VALUE)
                                        .addComponent(txtnomusu))
                                    .addComponent(jLabel50)
                                    .addComponent(cmbcargo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(156, 156, 156)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbgenero, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel46)
                                        .addComponent(jLabel49)
                                        .addComponent(txtnumdocusu)
                                        .addComponent(txtapellidousu, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(345, 345, 345)
                        .addComponent(jButton7)
                        .addGap(25, 25, 25)
                        .addComponent(btnguardarusuario)))
                .addContainerGap(98, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel55)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel47)
                            .addComponent(jLabel46))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbtipousu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnumdocusu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48)
                            .addComponent(jLabel49))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnomusu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtapellidousu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50)
                            .addComponent(jLabel52))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbcargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbgenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addComponent(jLabel45)
                        .addGap(15, 15, 15)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel53)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtnick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel54)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtpass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addComponent(jLabel56)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtpass2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(btnguardarusuario))
                .addGap(10, 10, 10)
                .addComponent(jLabel55)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel37)
                    .addComponent(jLabel35))
                .addContainerGap(1809, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel35)
                .addGap(30, 30, 30)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel41)
                .addGap(30, 30, 30)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(200, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Nuevo Usuario", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(153, 51, 0));
        jLabel58.setText("Nueva Categoría");

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel59.setText("En el módulo CATEGORÍA usted podrá registrar las categorías que servirán para agregar productos y también podrá ver los productos que pertenecen a una categoría");

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel60.setText("determinada. Además de lo antes mencionado, puede actualizar los datos de las categorías, realizar búsquedas de categorías o eliminarlas si así lo desea.");

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel61.setText("Información de la Categoría");

        jLabel62.setForeground(new java.awt.Color(102, 102, 102));
        jLabel62.setText("Pasillo o Ubicación");

        txtubic.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtubic.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtubicKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtubicKeyTyped(evt);
            }
        });

        jLabel63.setForeground(new java.awt.Color(102, 102, 102));
        jLabel63.setText("Nombre o Denominación");

        txtcat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtcat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcatKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcatKeyTyped(evt);
            }
        });

        jLabel65.setForeground(new java.awt.Color(102, 102, 102));
        jLabel65.setText("Estado");

        cmbestado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Habilitado", "Desabilitado" }));
        cmbestado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbestadoActionPerformed(evt);
            }
        });

        jButton9.setText("Limpiar");

        btnGuardarCat.setBackground(new java.awt.Color(0, 102, 102));
        btnGuardarCat.setText("Guardar");
        btnGuardarCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarCatActionPerformed(evt);
            }
        });

        jLabel66.setForeground(new java.awt.Color(102, 102, 102));
        jLabel66.setText("Los Campos marcados con * son Obligatorios");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel65)
                                    .addComponent(cmbestado, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel63)
                                    .addComponent(txtcat, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(132, 132, 132)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel62)
                                    .addComponent(txtubic, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel61)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(326, 326, 326)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jButton9)
                                .addGap(25, 25, 25)
                                .addComponent(btnGuardarCat))
                            .addComponent(jLabel66, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel61)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtubic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addComponent(jLabel65)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(btnGuardarCat))
                .addGap(10, 10, 10)
                .addComponent(jLabel66)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(jLabel59)
                    .addComponent(jLabel58))
                .addContainerGap(1800, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel58)
                .addGap(30, 30, 30)
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel60)
                .addGap(30, 30, 30)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(423, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Nueva Categoría", jPanel5);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel64.setText("Información de la Caja");

        jLabel67.setForeground(new java.awt.Color(102, 102, 102));
        jLabel67.setText("Monto a Retirar");

        txtretiro.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtretiro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtretiroKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtretiroKeyTyped(evt);
            }
        });

        jLabel69.setForeground(new java.awt.Color(102, 102, 102));
        jLabel69.setText("Motivo de la Extracción");

        txtmotivo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtmotivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmotivoActionPerformed(evt);
            }
        });
        txtmotivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtmotivoKeyTyped(evt);
            }
        });

        jLabel70.setForeground(new java.awt.Color(102, 102, 102));
        jLabel70.setText("Seleccione Caja");

        cmbcajaselec.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Habilitado", "Desabilitado" }));
        cmbcajaselec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcajaselecActionPerformed(evt);
            }
        });
        cmbcajaselec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbcajaselecKeyPressed(evt);
            }
        });

        jButton11.setText("Limpiar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(0, 102, 102));
        jButton12.setText("Guardar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel71.setForeground(new java.awt.Color(102, 102, 102));
        jLabel71.setText("Los Campos marcados con * son Obligatorios");

        jLabel68.setForeground(new java.awt.Color(102, 102, 102));
        jLabel68.setText("Monto Actual");

        txtmontoactual.setEditable(false);
        txtmontoactual.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtmontoactual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtmontoactualKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel70)
                                    .addComponent(cmbcajaselec, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel68)
                                    .addComponent(txtmontoactual, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(156, 156, 156)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtretiro, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtmotivo, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel67)
                                    .addComponent(jLabel69)))
                            .addComponent(jLabel64)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(326, 326, 326)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jButton11)
                                .addGap(25, 25, 25)
                                .addComponent(jButton12))
                            .addComponent(jLabel71, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel67)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtretiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel70)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbcajaselec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtmotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel68)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtmontoactual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jButton12))
                .addGap(10, 10, 10)
                .addComponent(jLabel71)
                .addGap(30, 30, 30))
        );

        jLabel72.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(153, 51, 0));
        jLabel72.setText("Extracción de Efectivo");

        jLabel73.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel73.setText("En el módulo EXTRACCION usted registra las extracciónes o retiro de dinero por cajas para evitar faltantes en momento de cierre de caja");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(187, 187, 187)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel73)
                            .addComponent(jLabel72))))
                .addContainerGap(1849, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel72)
                .addGap(30, 30, 30)
                .addComponent(jLabel73)
                .addGap(62, 62, 62)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(414, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Extracción", jPanel11);

        jScrollPane1.setViewportView(jTabbedPane1);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void txtapeclActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtapeclActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtapeclActionPerformed

    private void txtdirprvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdirprvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdirprvActionPerformed

    private void txtefectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtefectivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtefectivoActionPerformed

    private void txtapellidousuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtapellidousuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtapellidousuActionPerformed

    private void txtmotivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmotivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmotivoActionPerformed

    private void txtnumdocclKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumdocclKeyTyped
        if (txtnumdoccl.getText().length() >= 12) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtnumdocclKeyTyped

    private void txtnomclKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnomclKeyTyped
        if (txtnumdoccl.getText().length() >= 25) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtnomclKeyTyped

    private void txtapeclKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtapeclKeyTyped
        if (txtnumdoccl.getText().length() >= 25) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtapeclKeyTyped

    private void txtcelclKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcelclKeyTyped
        if (txtnumdoccl.getText().length() >= 20) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtcelclKeyTyped

    private void txtmailclKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmailclKeyTyped
        if (txtnumdoccl.getText().length() >= 50) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }       // TODO add your handling code here:
    }//GEN-LAST:event_txtmailclKeyTyped

    private void txtnumdocprvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumdocprvKeyTyped
        if (txtnumdoccl.getText().length() >= 12) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtnumdocprvKeyTyped

    private void txtnomprvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnomprvKeyTyped
        if (txtnumdoccl.getText().length() >= 25) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtnomprvKeyTyped

    private void txtdirprvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdirprvKeyTyped
        if (txtnumdoccl.getText().length() >= 25) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtdirprvKeyTyped

    private void txtencargadoprvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtencargadoprvKeyTyped
        if (txtnumdoccl.getText().length() >= 25) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtencargadoprvKeyTyped

    private void txtcontactoprvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcontactoprvKeyTyped
        if (txtnumdoccl.getText().length() >= 20) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtcontactoprvKeyTyped

    private void txtemailprvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtemailprvKeyTyped
        if (txtnumdoccl.getText().length() >= 50) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtemailprvKeyTyped

    private void txtnumcajaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumcajaKeyTyped
        if (txtnumdoccl.getText().length() >= 25) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtnumcajaKeyTyped

    private void txtserieKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtserieKeyTyped
        if (txtnumdoccl.getText().length() >= 40) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtserieKeyTyped

    private void txtefectivoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtefectivoKeyTyped
        if (txtnumdoccl.getText().length() >= 15) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtefectivoKeyTyped

    private void txtnumdocusuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumdocusuKeyTyped
        if (txtnumdoccl.getText().length() >= 12) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtnumdocusuKeyTyped

    private void txtnomusuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnomusuKeyTyped
        if (txtnumdoccl.getText().length() >= 25) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtnomusuKeyTyped

    private void txtapellidousuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtapellidousuKeyTyped
        if (txtnumdoccl.getText().length() >= 25) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtapellidousuKeyTyped

    private void txtnickKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnickKeyTyped
        if (txtnumdoccl.getText().length() >= 15) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtnickKeyTyped

    private void txtpassKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpassKeyTyped
        if (txtnumdoccl.getText().length() >= 15) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtpassKeyTyped

    private void txtpass2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpass2KeyTyped
        if (txtnumdoccl.getText().length() >= 15) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtpass2KeyTyped

    private void txtcatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcatKeyTyped
        if (txtnumdoccl.getText().length() >= 30) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtcatKeyTyped

    private void txtubicKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtubicKeyTyped
        if (txtnumdoccl.getText().length() >= 55) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtubicKeyTyped

    private void txtretiroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtretiroKeyTyped
        if (txtnumdoccl.getText().length() >= 15) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtretiroKeyTyped

    private void txtmontoactualKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmontoactualKeyTyped
        if (txtnumdoccl.getText().length() >= 15) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtmontoactualKeyTyped

    private void txtmotivoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmotivoKeyTyped
        if (txtnumdoccl.getText().length() >= 50) {
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtmotivoKeyTyped

    private void cmbtipoclKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbtipoclKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtnumdoccl.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_cmbtipoclKeyPressed

    private void txtnumdocclKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumdocclKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtnomcl.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtnumdocclKeyPressed

    private void txtnomclKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnomclKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtapecl.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtnomclKeyPressed

    private void txtapeclKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtapeclKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            cmbdept.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtapeclKeyPressed

    private void txtcalleclKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcalleclKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtcelcl.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtcalleclKeyPressed

    private void txtcelclKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcelclKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtmailcl.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtcelclKeyPressed

    private void txtnumdocprvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumdocprvKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtnomprv.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtnumdocprvKeyPressed

    private void cmbdocprvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbdocprvKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtnumdocprv.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_cmbdocprvKeyPressed

    private void txtnomprvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnomprvKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtdirprv.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtnomprvKeyPressed

    private void txtdirprvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdirprvKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            jComboBox5.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtdirprvKeyPressed

    private void jComboBox5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox5KeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtencargadoprv.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5KeyPressed

    private void txtencargadoprvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtencargadoprvKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtcontactoprv.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtencargadoprvKeyPressed

    private void txtcontactoprvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcontactoprvKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtemailprv.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtcontactoprvKeyPressed

    private void txtnumcajaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumcajaKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtserie.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtnumcajaKeyPressed

    private void txtserieKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtserieKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtefectivo.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtserieKeyPressed

    private void cmbtipousuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbtipousuKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtnumdocusu.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_cmbtipousuKeyPressed

    private void txtnumdocusuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumdocusuKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtnomusu.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtnumdocusuKeyPressed

    private void txtnomusuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnomusuKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtapellidousu.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtnomusuKeyPressed

    private void txtapellidousuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtapellidousuKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            cmbcargo.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtapellidousuKeyPressed

    private void cmbcargoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbcargoKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            cmbgenero.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_cmbcargoKeyPressed

    private void cmbgeneroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbgeneroKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtnomusu.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_cmbgeneroKeyPressed

    private void txtnickKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnickKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtpass.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtnickKeyPressed

    private void txtpassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpassKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtpass2.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtpassKeyPressed

    private void txtcatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcatKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtubic.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtcatKeyPressed

    private void txtubicKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtubicKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            cmbestado.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtubicKeyPressed

    private void cmbcajaselecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcajaselecActionPerformed
        String consulta = "Select efectivo from caja where num_caja = " + cmbcajaselec.getSelectedItem().toString();
        try {
            sentenciasql = con.createStatement();
            rs = sentenciasql.executeQuery(consulta);
            rs.next();
            txtmontoactual.setText(rs.getInt("efectivo") + "");

        } catch (Exception e) {
        }
// TODO add your handling code here:
    }//GEN-LAST:event_cmbcajaselecActionPerformed

    private void cmbcajaselecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbcajaselecKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtretiro.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_cmbcajaselecKeyPressed

    private void txtretiroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtretiroKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            txtmotivo.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtretiroKeyPressed

    private void btnGuardarCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarCatActionPerformed
        if (txtcat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            guardarcategoria();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarCatActionPerformed

    private void btnguardarusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarusuarioActionPerformed
        if (txtnomusu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            txtnomusu.requestFocus();
        } else // TODO add your handling code here:
        if (txtapellidousu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un apellido.", "Error", JOptionPane.ERROR_MESSAGE);
            txtapellidousu.requestFocus();
        } else // TODO add your handling code here:
        if (txtnumdocusu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un numero de documento.", "Error", JOptionPane.ERROR_MESSAGE);
            txtnumdocusu.requestFocus();
        } else if (txtnick.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un nick.", "Error", JOptionPane.ERROR_MESSAGE);
            txtnick.requestFocus();
        } else if (txtpass.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un codigo.", "Error", JOptionPane.ERROR_MESSAGE);
            txtpass.requestFocus();
        } else if (!txtpass.getText().equals(txtpass2.getText())) {
            JOptionPane.showMessageDialog(null, "repita el ingreso de las contraseñas.", "Error", JOptionPane.ERROR_MESSAGE);
            txtpass.setText("");
            txtpass2.setText("");
            txtpass.requestFocus();
        } else {
            guardarusuario();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnguardarusuarioActionPerformed

    private void btnguardarclActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarclActionPerformed
        if (txtnomcl.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            txtnomcl.requestFocus();
        } else // TODO add your handling code here:
        if (txtapecl.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un apellido.", "Error", JOptionPane.ERROR_MESSAGE);
            txtapecl.requestFocus();
        } else // TODO add your handling code here:
        if (txtnumdoccl.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un numero de documento.", "Error", JOptionPane.ERROR_MESSAGE);
            txtnumdoccl.requestFocus();
        } else {
            guardarcliente();
        }
// TODO add your handling code here:
    }//GEN-LAST:event_btnguardarclActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        txtapecl.setText("");        // TODO add your handling code here:
        txtnomcl.setText("");        // TODO add your handling code here:
        txtnumdoccl.setText("");        // TODO add your handling code here:
        txtmailcl.setText("");        // TODO add your handling code here:
        txtcelcl.setText("");        // TODO add your handling code here:
        txtcallecl.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btngprovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngprovActionPerformed
        if (txtnomprv.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            txtnomprv.requestFocus();
        } else // TODO add your handling code here:
        if (txtnumdocprv.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un numero de documento.", "Error", JOptionPane.ERROR_MESSAGE);
            txtnumdocprv.requestFocus();
        } else // TODO add your handling code here:
        if (txtencargadoprv.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un nombre de encargado.", "Error", JOptionPane.ERROR_MESSAGE);
            txtencargadoprv.requestFocus();
        } else if (txtcontactoprv.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un numero de contacto.", "Error", JOptionPane.ERROR_MESSAGE);
            txtcontactoprv.requestFocus();
        } else {
            guardarproveedor();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btngprovActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        txtnomprv.setText("");        // TODO add your handling code here:
        txtnumdocprv.setText("");        // TODO add your handling code here:
        txtdirprv.setText("");        // TODO add your handling code here:
        txtencargadoprv.setText("");        // TODO add your handling code here:
        txtemailprv.setText("");        // TODO add your handling code here:
        txtcontactoprv.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btngcajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngcajaActionPerformed
        if (txtnumcaja.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un numero de caja.", "Error", JOptionPane.ERROR_MESSAGE);
            txtnumcaja.requestFocus();
        } else if (txtserie.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un numero de serie.", "Error", JOptionPane.ERROR_MESSAGE);
            txtserie.requestFocus();
        } else if (txtefectivo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un monto inicial.", "Error", JOptionPane.ERROR_MESSAGE);
            txtefectivo.requestFocus();
        } else {
            guardarcaja();
        }
// TODO add your handling code here:
    }//GEN-LAST:event_btngcajaActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        txtnumcaja.setText("");        // TODO add your handling code here:
        txtserie.setText("");        // TODO add your handling code here:
        txtefectivo.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cmbdeptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbdeptActionPerformed
        Integer iddepto = cogerIdDpto(cmbdept.getSelectedItem().toString());
        llenarCmbCiu(iddepto);
        cmbciudad.setEnabled(true);        // TODO add your handling code here:
    }//GEN-LAST:event_cmbdeptActionPerformed

    private void cmbciudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbciudadActionPerformed
        IdCiudad = cogerIdCiudad(cmbciudad.getSelectedItem().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_cmbciudadActionPerformed

    private void cmbcargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbcargoActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if (txtretiro.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un monto a retirar.", "Error", JOptionPane.ERROR_MESSAGE);
            txtretiro.requestFocus();
        } else if (txtmotivo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe asignar un motivo de retiro.", "Error", JOptionPane.ERROR_MESSAGE);
            txtmotivo.requestFocus();
        } else {
            guardarextraccion();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        txtnomusu.setText("");        // TODO add your handling code here:
        txtapellidousu.setText("");        // TODO add your handling code here:
        txtnumdocusu.setText("");        // TODO add your handling code here:
        txtnick.setText("");        // TODO add your handling code here:
        txtpass.setText("");        // TODO add your handling code here:
        txtpass2.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        txtretiro.setText("");        // TODO add your handling code here:
        txtmotivo.setText("");        // TODO add your handling code here:
        txtmontoactual.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void txtmailclKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmailclKeyPressed
int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            btnguardarcl.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtmailclKeyPressed

    private void txtemailprvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtemailprvKeyPressed
int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            btngprov.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtemailprvKeyPressed

    private void txtefectivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtefectivoKeyPressed
int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            btngcaja.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtefectivoKeyPressed

    private void txtpass2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpass2KeyPressed
int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            btnguardarusuario.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtpass2KeyPressed

    private void cmbestadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbestadoActionPerformed
btnGuardarCat.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_cmbestadoActionPerformed

    private void cmbgeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbgeneroActionPerformed

            txtnick.requestFocus();
                // TODO add your handling code here:
    }//GEN-LAST:event_cmbgeneroActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarCat;
    private javax.swing.JButton btngcaja;
    private javax.swing.JButton btngprov;
    private javax.swing.JButton btnguardarcl;
    private javax.swing.JButton btnguardarusuario;
    private javax.swing.JComboBox<String> cmbcajaselec;
    private javax.swing.JComboBox<String> cmbcargo;
    private javax.swing.JComboBox<String> cmbciudad;
    private javax.swing.JComboBox<String> cmbdept;
    private javax.swing.JComboBox<String> cmbdocprv;
    private javax.swing.JComboBox<String> cmbestado;
    private javax.swing.JComboBox<String> cmbestadocaja;
    private javax.swing.JComboBox<String> cmbgenero;
    private javax.swing.JComboBox<String> cmbtipocl;
    private javax.swing.JComboBox<String> cmbtipousu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox5;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtapecl;
    private javax.swing.JTextField txtapellidousu;
    private javax.swing.JTextField txtcallecl;
    private javax.swing.JTextField txtcat;
    private javax.swing.JTextField txtcelcl;
    private javax.swing.JTextField txtcontactoprv;
    private javax.swing.JTextField txtdirprv;
    private javax.swing.JTextField txtefectivo;
    private javax.swing.JTextField txtemailprv;
    private javax.swing.JTextField txtencargadoprv;
    private javax.swing.JTextField txtmailcl;
    private javax.swing.JTextField txtmontoactual;
    private javax.swing.JTextField txtmotivo;
    private javax.swing.JTextField txtnick;
    private javax.swing.JTextField txtnomcl;
    private javax.swing.JTextField txtnomprv;
    private javax.swing.JTextField txtnomusu;
    private javax.swing.JTextField txtnumcaja;
    private javax.swing.JTextField txtnumdoccl;
    private javax.swing.JTextField txtnumdocprv;
    private javax.swing.JTextField txtnumdocusu;
    private javax.swing.JTextField txtpass;
    private javax.swing.JTextField txtpass2;
    private javax.swing.JTextField txtretiro;
    private javax.swing.JTextField txtserie;
    private javax.swing.JTextField txtubic;
    // End of variables declaration//GEN-END:variables

}
