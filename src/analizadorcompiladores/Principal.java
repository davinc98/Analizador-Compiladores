package analizadorcompiladores;
import java.util.ArrayList;
import java.util.Scanner;
import com.google.gson.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.JOptionPane;
import ventanas.*;

public class Principal {
    
    public static ArrayList<AFN> listaAFN = new ArrayList();
    public static ArrayList<AFD> listaAFD = new ArrayList();
    public static int opcionMenu = -1;
    
    public static void main (String[] args){        
        //Scanner leer = new Scanner(System.in);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Menu menu = new Menu();
        TablaAFN tablaAFN;
        
        while(true){
            while(opcionMenu==-1){
                System.out.print("");
            }
            /*
            System.out.println("-------MENU-------");
            System.out.println("1. Crear AFN básico."); //In: un caracter, y ponerle ID (para cerradura, etc)
            System.out.println("2. Unir 2 AFN."); //In: 2do AFN, ID para AFN unión, Out: AFN unión
            System.out.println("3. Concatenar 2 AFN.");
            System.out.println("4. Cerradura Transitiva o de Kleene");
            System.out.println("5. Unión para Analizador Léxico."); //qué automata quiero unir (mostrar tabla con automatas)
            System.out.println("6. Convertir AFN a AFD."); //cualquier AFN, hasta el de la opcion 6. In: ID del AFN
            System.out.println("7. Validar cadena usando un AFN o un AFD."); //seleccionar AFN o AFD de los ya ingresados
            System.out.println("8. Opcional.");
            System.out.println("9. Salir");
            System.out.println("10. Guardar Gson a portapapeles");
            System.out.print("Selecciona el número de la opción deseada: ");
            opcionMenu = leer.nextInt();
            */
            switch(opcionMenu){
                case 1: //crear AFN
                    String c = JOptionPane.showInputDialog("Ingresa el caracter");
                    if(c != null){
                        System.out.println("Se recibió: "+c.charAt(0));
                        crearAutomataBasico(c.charAt(0));
                    } else opcionMenu=-1;
                    break;
                case 2: //Unir AFN
                    menu.setVisible(false);
                    tablaAFN = new TablaAFN();
                    
                    tablaAFN.inicializar(0);
                    while(tablaAFN.V1()==-1 && tablaAFN.V2()==-1){
                        System.out.print("");
                    }
                    unirAutomatas(tablaAFN.V1(), tablaAFN.V2());
                    
                    tablaAFN.setVisible(false);
                    menu.setVisible(true);
                    /*
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    unirAutomatas(0, 1);
                    */
                    opcionMenu=-1;
                    break;
                    
                case 3: //concatenar AFN
                    
                    //tablaAFN.imprimirAccionActual("Concatenar AFN");
                    System.out.println("Ingresa el identificador del primer automata: ");
                    System.out.println("Ingresa el identificador del segundo automata: ");
                    
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    concatenarAutomatas(0, 1);           
                    //System.out.println(gson.toJson(listaAFN.get(1)));
                    //System.out.println(gson.toJson(listaAFNUnidos.get(0).getEstadosAFN()));
                    opcionMenu=-1;
                    break;
                case 4: //Cerradura
                    
                    System.out.println("Probando cerradura epsilon ultimo automata creado");
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    
                    
                    AFN auf3 =listaAFN.get(listaAFN.size()-1 );
                    
                    
                    
                    auf3.unirAFN(listaAFN.get(0));
                    
                    
                    AFN  automataAnalizar =  listaAFN.get( listaAFN.size()-1 );
                    AFN  automataGuardar = new AFN();
                    
                    
                    ArrayList<Estado> listaEstadosAnalizar = new ArrayList();
                    listaEstadosAnalizar.add(automataAnalizar.getEstadoInicial());
                    
                    ArrayList <Estado> listaCE =automataAnalizar.irA( listaEstadosAnalizar,'c');
                    automataGuardar.setEstadosAFN(listaCE);
                    System.out.println("numero de elementos c_e"+  listaCE.size() );
                    
                    listaAFN.add(automataGuardar);
                    
                    
                    opcionMenu=-1;
                    break;
                case 5: //Analizador Léxico
                    opcionMenu=-1;
                    break;
                case 6: //Convertir AFN a AFD
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    concatenarAutomatas(0, 1);  
                    
                    convertirAFNaAFD(0);
                    opcionMenu=-1;
                    break;
                case 7: //Validar cadena
                    opcionMenu=-1;
                    break;
                case 8: //Opcional
                    opcionMenu=-1;
                    break;
                    
                case 10: //copiar array list AFN  json en el portapapeles     
                    System.out.println( "Copiando json al portapapeles");
                    StringSelection ss = new StringSelection(gson.toJson(listaAFN));
                    Toolkit tool = Toolkit.getDefaultToolkit();
                    Clipboard clip = tool.getSystemClipboard();
                    clip.setContents(ss, null);
                    opcionMenu=-1;
                    break;
                     
                default:
                    System.out.println("Hasta luego!");
                    break;
            }
        }
    }  
    
    private static void crearAutomataBasico(char c){
        AFN afn = new AFN();
        afn.crearBasico(c, listaAFN.size());    
        System.out.println("Automata basico con id:" +afn.getIdAFN()+" creado.");
        listaAFN.add(afn);
    }

    private static void unirAutomatas(int Id1, int Id2) {
        AFN aux1 = new AFN();
        AFN aux2 = new AFN();
        
        aux1 = listaAFN.get(Id1).Duplicar();
        aux2 = listaAFN.get(Id2).Duplicar();
            
        aux1.unirAFN(aux2);
        listaAFN.add(aux1);
    }

    private static void concatenarAutomatas(int Id1, int Id2) {
        AFN aux1 = new AFN();
        AFN aux2 = new AFN();
        
        aux1 = listaAFN.get(Id1).Duplicar();
        aux2 = listaAFN.get(Id2).Duplicar();
             
        aux1.concatenarAFN(aux2);
        listaAFN.remove(Id2);
        //listaAFN.add(aux1);
        
    }

    private static void convertirAFNaAFD(int Id) {
        AFN aux1 = new AFN();        
        aux1 = listaAFN.get(Id);
        aux1.convertirAFN();
    }

}
