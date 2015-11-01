/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import controladores.ControladorBaseDatos;
import jade.core.Agent;
import jade.core.MicroRuntime;
import jade.core.NotFoundException;
import jade.domain.JADEAgentManagement.KillAgent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AgenteRecords extends Agent {

    private String record = "";
    private String nombre = "";
    private String tipoMatriz = "";
    private ControladorBaseDatos controladorBD;

    @Override
    protected void setup() {
        System.out.println("Hola soy el agente Encargado de guardar los Records ");
        System.out.println("Mi nombre es " + getLocalName());

        controladorBD = ControladorBaseDatos.getInstance();
        Object[] arg = getArguments();
        if (arg != null) {
            record = arg[0].toString();
            nombre = arg[1].toString();
            tipoMatriz = arg[2].toString();

        }

        guardarRecordsEnBD(record, nombre, tipoMatriz);
    }
    
    /**
     * Método para invocar el query para insertar datos en la BD
     * 
     */
    private void guardarRecordsEnBD(final String record, final String fnombre, final String tipoMatriz){
        int resultadoQuery = controladorBD.insertarRegistroQuery(record, fnombre, tipoMatriz);
        
         if (resultadoQuery == 1) {
            JOptionPane.showConfirmDialog(null, "Reporte Agente: " + getLocalName() + "!! - Registro insertado correctamente!", "Mensaje", JOptionPane.OK_OPTION);
        } else {
            JOptionPane.showConfirmDialog(null, "Reporte Agente: " + getLocalName() + "!! - Error Insertando Registro!", "Mensaje", JOptionPane.OK_OPTION);
        }
         
         killAgent();
    }
    
    /**
     * Método para detener el Agente después de cada proceso
     * 
     */
    public void killAgent() {
        try {
            MicroRuntime.killAgent("AgenteGuardaRecords");
        } catch (NotFoundException ex) {
            Logger.getLogger(AgenteRecords.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
