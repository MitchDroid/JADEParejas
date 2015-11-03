/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 *
 */
package controladores;

import basedatos.ControladorConeccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ControladorBaseDatos {

    private ControladorConeccion controladorConeccion;
    private Connection connection = null;
    private static ControladorBaseDatos controladorBD;

    public static ControladorBaseDatos getInstance() {
        if (controladorBD == null) {
            controladorBD = new ControladorBaseDatos();
        }
        return controladorBD;
    }

    /**
     * Esta clase es la interfaz entre la base de datos y las vistas
     */
    public ControladorBaseDatos() {
        controladorConeccion = new ControladorConeccion();
        connection = controladorConeccion.createConnection();

    }

    /**
     * Método para recuperar  la lista de los records.
     *
     * @return Vector con la lista Formateada
     */
    public Vector<Vector<String>> obtenerRecordsDeLaBD() {

        Vector<Vector<String>> doublevector = new Vector<Vector<String>>();

        if (connection != null) {
            try {
                String SQL_SELECT_QUERY = "SELECT * FROM tb_records ORDER BY record ASC";
                PreparedStatement pst = connection.prepareStatement(SQL_SELECT_QUERY);
                ResultSet rs = pst.executeQuery();

                while (rs.next() == true) {
                    Vector<String> singlevector = new Vector<String>();
                    singlevector.add(rs.getString(2));
                    singlevector.add(rs.getString(3));
                    singlevector.add(rs.getString(4));
                    doublevector.add(singlevector);

                }

                pst.close();

            } catch (SQLException e) {
                e.printStackTrace();

            }

        }

        return doublevector;
    }

    /**
     * Método para insertar nuevos registros de los records en la base de datos.
     *
     * @return 1 si el registro fue insertado correctamente
     */
    public int insertarRegistroQuery(final String record, final String nombre, final String tipoMatriz) {

        int queryResult = 0;
        if (connection != null) {
            try {
                String SQL_INSERT_QUERY = "INSERT INTO tb_records (record, nombre,tipo_matriz) VALUES (?,?,?)";
                PreparedStatement pst = connection.prepareStatement(SQL_INSERT_QUERY);

                pst.setString(1, record);
                pst.setString(2, nombre);
                pst.setString(3, tipoMatriz);

                /**
                 * Execute QUERY*
                 */
                queryResult = pst.executeUpdate();

                if (pst != null) {
                    pst.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
        return queryResult;
    }

}
