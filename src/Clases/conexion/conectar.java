package Clases.conexion;

//import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class conectar {
    //se declaran variables para definir parametros de conexión

    private static String db = "controlstock";//"infochat";
    private static String user = "postgres";//"postgres"; // postgres
    private static String pass = "1324";//"12345"; // secreto
    private static String url = "jdbc:postgresql://127.0.0.1:5432/" + db;//"jdbc:postgresql://127.0.0.1:5432/" + db;
    private static Connection conex;

    //metodo para realizar la conexion
    public static Connection getConnect() {
        try {
            //acceso al driver mysql
            Class.forName("org.postgresql.Driver");
            //acceso al driver postgresql
            //Class.forName("org.postgresql.Driver");
            conex = DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
        }

        return conex;

    }

//    public static Connection Conectar() {
//        try {
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            conex = DriverManager.getConnection(url, user, pass);
//            System.out.println("CONEXION EXITOSA");
//        } catch (Exception e) {
//            System.out.println("ERROR DE CONEXION: " + e.getMessage());
//        }
//        return conex;
//    }
}