/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.Agent;
import jade.core.MicroRuntime;
import jade.core.NotFoundException;
import jadeparejas.ResultadosRecords;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AgenteMostrarRecords extends Agent {

    @Override
    protected void setup() {
        System.out.println("Hola soy el Agente encargado de Mostrar la lista de los records.");
        System.out.println("Mi nombre es " + getLocalName());
        
        obtenerResultadosDeLaBD();
        killAgent();
    }

    private void obtenerResultadosDeLaBD() {
        ResultadosRecords resultadosRecords = new ResultadosRecords();
        resultadosRecords.setVisible(true);
    }

    
     /**
     * Método para detener el Agente después de cada proceso
     * 
     */
    public void killAgent() {
        try {
            MicroRuntime.killAgent("AgenteResultadosRecords");
        } catch (NotFoundException ex) {
            Logger.getLogger(AgenteRecords.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
