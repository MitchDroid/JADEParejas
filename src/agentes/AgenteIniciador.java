/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package agentes;

import jade.core.Agent;
import jadeparejas.SeleccionarTamañoMatriz;


public class AgenteIniciador extends Agent{
    
    private SeleccionarTamañoMatriz mTamMatriz;

    @Override
    protected void setup() {
        mTamMatriz = new SeleccionarTamañoMatriz(this);
        mTamMatriz.setVisible(true);      
             
        System.out.println("Hola soy el Agente encargado de iniciar la Interfaz de Agentes.");
        System.out.println("Mi nombre es " + getLocalName());
    }
    
    
    
    
}
