package jadeparejas;

import agentes.AgenteMostrarRecords;
import agentes.AgenteRecords;
import basedatos.ControladorConeccion;
import jade.core.MicroRuntime;
import jade.util.leap.Properties;
import java.awt.*;
import java.awt.event.*;
import java.net.Inet4Address;
import java.sql.Connection;
import javax.swing.*;

public class MostrarMatriz extends JFrame implements Runnable, ActionListener {

    private Matriz t;
    private JButton[][] botones;
    private ImageIcon[] imagenes;
    private static int sw;
    private static int a, b, ii, jj;
    private JLabel tiempo;
    private JPanel A;

    Thread hilo;
    boolean cronometroActivo;
    public static int onoff = 0;
    private int d;

    private ControladorConeccion controladorConeccion;
    private Connection coneccion = null;

    private AgenteRecords agenteRecords;
    private ResultadosRecords resultadosRecords;
    private static String JADE_PUERTO_DE_PLATAFORMA = "1099";
    private static String JADE_IP_LOCAL_HOST = "";
    private String nombre = "";
    private JButton btn;

    private int tamano;

    private void iniciarControladorBD() {
        controladorConeccion = new ControladorConeccion();
        coneccion = controladorConeccion.createConnection();

    }

    public MostrarMatriz(final int tam, final String nombre) {
        try {

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MostrarMatriz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MostrarMatriz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MostrarMatriz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MostrarMatriz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        this.nombre = nombre;
        this.tamano = tam;
        sw = 0;
        t = new Matriz(tamano);
        t.genAleatorio();
        initComponents();
        configurarPanel();
    }

    public void configurarPanel() {
        setTitle("JADE Parejas| Julian y Diego");
        setSize(450, 500);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void initComponents() {

        iniciarControladorBD();

        agenteRecords = new AgenteRecords();

        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));

        //Etiqueta donde se colocara el tiempo 
        tiempo = new JLabel("00:00:000");
        tiempo.setFont(new Font(Font.SERIF, Font.BOLD, 50));
        tiempo.setForeground(Color.BLUE);
        tiempo.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tiempo.setOpaque(true);

        //Boton iniciar
        btn = new JButton("Iniciar");
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (onoff == 0) {
                    onoff = 1;
                    A.setVisible(true);
                    iniciarCronometro();

                }
            }
        });

        d = t.getDim();
        imagenes = new ImageIcon[d * d / 2 + 1];
        imagenes[0] = null;
        //ruta de las imagenes
        for (int i = 1; i <= d * d / 2; i++) {
            imagenes[i] = new ImageIcon(getClass().getResource("/jadeparejas/res/" + i + ".jpg"));
        }

        GridLayout gridLayout = new GridLayout(d, d);
        A = new JPanel(gridLayout);
        botones = new JButton[d][d];
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                botones[i][j] = new JButton();
                botones[i][j].addActionListener(this);
                A.add(botones[i][j]);
            }
        }

        listPane.add(A);
        listPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        listPane.add(Box.createHorizontalGlue());
        listPane.add(tiempo, BorderLayout.WEST);
        listPane.add(Box.createHorizontalGlue());
        listPane.add(btn, BorderLayout.WEST);

        this.add(listPane);
        this.setLocationRelativeTo(null);

    }

    public void accion(int x, int y) {
        switch (sw) {
            case 0:
                if (!t.esClic(x, y)) {
                    t.clic(x, y);
                    botones[x][y].setIcon(imagenes[t.getPos(x, y)]);
                    sw = 1;
                    a = x;
                    b = y;
                }
                break;
            case 1:
                if (!t.esClic(x, y)) {
                    t.clic(x, y);
                    botones[x][y].setIcon(imagenes[t.getPos(x, y)]);
                    ii = x;
                    jj = y;
                    if (t.getPos(a, b) != t.getPos(ii, jj)) {
                        sw = 2;
                    } else {
                        sw = 0;
                    }
                }
                break;
            case 2:
                botones[a][b].setIcon(null);
                botones[ii][jj].setIcon(null);

                t.clic(a, b);
                t.clic(ii, jj);
                sw = 0;
                break;
        }
    }

    public void actionPerformed(ActionEvent ae) {
        int d = t.getDim();
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                if (botones[i][j] == ae.getSource()) {
                    accion(i, j);
                    if (t.esCompleto()) {
                        if (onoff == 1) {
                            onoff = 0;
                            pararCronometro();
                        }
                        JOptionPane.showMessageDialog(this, "Felicidades " + nombre
                                + ", ahora intenta mejorar y hacerlo en el menor tiempo posible.",
                                "Bien Hecho!!", JOptionPane.INFORMATION_MESSAGE, null);
                                              
                        mostrarResultados();
                        
                    }
                    return;
                }
            }
        }
    }
    
 

    private String obtenerTipoDeMatriz() {
        String tipoMatriz = "";
        switch (tamano) {
            case 2:
                tipoMatriz = "2 x 2";
                break;
            case 4:
                tipoMatriz = "4 x 4";
                break;
            case 6:
                tipoMatriz = "6 x 6";
                break;
            default:
                tipoMatriz = "2 x 2";

        }

        return tipoMatriz;
    }

    private void mostrarResultados() {

        iniciarAgenteResultadosRecords(null);
    }

    /**
     * Método que guarda en un String array los datos obtenidos y notifica al
     * agente para iniciar el juego
     *
     * @param record tiempo / resultado de la partida
     *
     *
     */
    public void guardarRecords(JLabel record) {
        System.out.println(record.getText());
        String[] params = new String[3];
        String timeRecord = record.getText().toString();
        String rnombre = nombre;
        String tipoMatriz = obtenerTipoDeMatriz();
        params[0] = timeRecord;
        params[1] = rnombre;
        params[2] = tipoMatriz;

        iniciarAgenteRecords(params);
    }

    /**
     * Método que inicia el agente que muestra los resultados de los Records de
     * las partidas
     *
     * @param params arreglo de strings con los parámetros de los records
     *
     *
     */
    public void iniciarAgenteResultadosRecords(String[] params) {
        try {
            String packaClassName = AgenteMostrarRecords.class.getName();
            Properties pp = new jade.util.leap.Properties();
            JADE_IP_LOCAL_HOST = Inet4Address.getLocalHost().getHostAddress();
            pp.setProperty(MicroRuntime.HOST_KEY, JADE_IP_LOCAL_HOST);
            pp.setProperty(MicroRuntime.PORT_KEY, JADE_PUERTO_DE_PLATAFORMA);

            MicroRuntime.startJADE(pp, new Runnable() {
                public void run() {
                }
            });

            if (MicroRuntime.isRunning()) {
                MicroRuntime.startAgent("AgenteResultadosRecords", packaClassName, params);
            } else {
                System.err.println("Error iniciando JADE Agente Resultados");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método que inicia el agente que guarda los Records de las partidas
     *
     * @param params arreglo de strings con los parámetros de los records
     *
     *
     */
    public void iniciarAgenteRecords(String[] params) {
        try {
            String packaClassName = AgenteRecords.class.getName();
            Properties pp = new jade.util.leap.Properties();
            JADE_IP_LOCAL_HOST = Inet4Address.getLocalHost().getHostAddress();
            pp.setProperty(MicroRuntime.HOST_KEY, JADE_IP_LOCAL_HOST);
            pp.setProperty(MicroRuntime.PORT_KEY, JADE_PUERTO_DE_PLATAFORMA);

            MicroRuntime.startJADE(pp, new Runnable() {
                public void run() {
                }
            });

            if (MicroRuntime.isRunning()) {
                MicroRuntime.startAgent("AgenteGuardaRecords", packaClassName, params);
            } else {
                System.err.println("Error iniciando JADE Agente Records");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Iniciar el cronometro poniendo cronometroActivo 
    //en verdadero para que entre en el while
    public void iniciarCronometro() {
        btn.setEnabled(false);
        cronometroActivo = true;
        hilo = new Thread(this);
        hilo.start();
    }

    //Esto es para parar el cronometro
    public void pararCronometro() {
        cronometroActivo = false;
        btn.setEnabled(true);
    }

    /**
     * Hilo principal que inicia el cronometro
     */
    public void run() {
        Integer minutos = 0, segundos = 0, milesimas = 0;
        //min es minutos, seg es segundos y mil es milesimas de segundo
        String min = "", seg = "", mil = "";
        try {
            //Mientras cronometroActivo sea verdadero entonces seguira
            //aumentando el tiempo
            while (cronometroActivo) {
                Thread.sleep(4);
                //Incrementamos 4 milesimas de segundo
                milesimas += 4;

                //Cuando llega a 1000 osea 1 segundo aumenta 1 segundo
                //y las milesimas de segundo de nuevo a 0
                if (milesimas == 1000) {
                    milesimas = 0;
                    segundos += 1;
                    //Si los segundos llegan a 60 entonces aumenta 1 los minutos
                    //y los segundos vuelven a 0
                    if (segundos == 60) {
                        segundos = 0;
                        minutos++;
                    }
                }

                //Esto solamente es estetica para que siempre este en formato
                //00:00:000
                if (minutos < 10) {
                    min = "0" + minutos;
                } else {
                    min = minutos.toString();
                }
                if (segundos < 10) {
                    seg = "0" + segundos;
                } else {
                    seg = segundos.toString();
                }

                if (milesimas < 10) {
                    mil = "00" + milesimas;
                } else if (milesimas < 100) {
                    mil = "0" + milesimas;
                } else {
                    mil = milesimas.toString();
                }

                //Colocamos en la etiqueta la informacion
                tiempo.setText(min + ":" + seg + ":" + mil);
            }
        } catch (Exception e) {
        }
        //Cuando se reincie se coloca nuevamente en 00:00:000
        //tiempo.setText("00:00:000");
        guardarRecords(tiempo);
    }

}
