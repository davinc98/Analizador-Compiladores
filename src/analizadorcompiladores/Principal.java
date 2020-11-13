package analizadorcompiladores;
import java.util.ArrayList;
import com.google.gson.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import static java.lang.System.exit;
import ventanas.*;

public class Principal {
    
    public static ArrayList<AFN> listaAFN = new ArrayList();
    public static ArrayList<AFD> listaAFD = new ArrayList();
    public static int opcionMenu = -1;
    
    public static void main (String[] args){        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Menu menu = new Menu();
        TablaAFN tablaAFN;
        TablaAFD tablaAFD;
        CrearBasico basico;
        
        while(true){
            while(opcionMenu==-1){
                System.out.print("");
            }
            switch(opcionMenu){
                case 1: //crear AFN
                    menu.setVisible(false);
                    basico = new CrearBasico();
                    basico.inicializar();
                    while(basico.c1()=='-' || basico.c2()=='-' ){
                        System.out.print("");
                        if(basico.volver) break;
                    }
                    if(!basico.volver){
                        System.out.println("c1: "+basico.c1()+" c2:"+basico.c2());
                        crearAutomataBasico(basico.c1()); //actualizar para 2 char
                    }
                    basico.setVisible(false);
                    menu.setVisible(true);
                    opcionMenu = -1;
                    break;
                case 2: //Unir AFN
                    menu.setVisible(false);
                    tablaAFN = new TablaAFN();
                    tablaAFN.inicializar(0);
                    while(tablaAFN.V1()==-1 && tablaAFN.V2()==-1){
                        System.out.print("");
                        if(tablaAFN.volver) break;
                    }
                    if(!tablaAFN.volver)
                        unirAutomatas(tablaAFN.V1(), tablaAFN.V2());
                    
                    tablaAFN.setVisible(false);
                    menu.setVisible(true);
                    opcionMenu=-1;
                    break;
                    
                case 3: //concatenar AFN
                    menu.setVisible(false);
                    tablaAFN = new TablaAFN();
                    
                    tablaAFN.inicializar(1);
                    while(tablaAFN.V1()==-1 && tablaAFN.V2()==-1){
                        System.out.print("");
                        if(tablaAFN.volver) break;
                    }
                    if(!tablaAFN.volver)
                        concatenarAutomatas(tablaAFN.V1(), tablaAFN.V2());
                    
                    tablaAFN.setVisible(false);
                    menu.setVisible(true);
                    opcionMenu=-1;
                    break;
                case 4: //Cerradura
                    System.out.println("Probando cerradura epsilon ultimo automata creado");
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    
                    AFN auf3 =listaAFN.get(listaAFN.size()-1 );
                    auf3.unirAFN(listaAFN.get(0));
                    
                    cerraduraEpsilon(listaAFN.size()-1);
                    /*
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    
                    AFN auf3 =listaAFN.get(listaAFN.size()-1 );
                    auf3.unirAFN(listaAFN.get(0));
                    System.out.println("Probando cerradura epsilon ultimo automata creado");
                    AFN automataAnalizar =  listaAFN.get( listaAFN.size()-1 );
                    AFN automataGuardar = new AFN();
                    
                    ArrayList<Estado> listaEstadosAnalizar = new ArrayList();
                    listaEstadosAnalizar.add(automataAnalizar.getEstadoInicial());
                    
                    ArrayList <Estado> listaCE = automataAnalizar.irA( listaEstadosAnalizar,'c');
                    automataGuardar.setEstadosAFN(listaCE);
                    System.out.println("numero de elementos c_e"+  listaCE.size() );
                    
                    listaAFN.add(automataGuardar);*/
                    opcionMenu=-1;
                    break;
                case 5: //Analizador Léxico
                    opcionMenu=-1;
                    break;
                case 6: //Convertir AFN a AFD
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    concatenarAutomatas(0, 1);  
                    
                    
                    opcionMenu=-1;
                    break;
                case 7: //Validar cadena
                    menu.setVisible(false);
                    tablaAFD = new TablaAFD();
                    tablaAFD.inicializar();
                    while(tablaAFD.cadena.equals("") || tablaAFD.numAFD==-1){
                        System.out.print("");
                        if(tablaAFD.volver) break;
                    }
                    if(!tablaAFD.volver){
                        System.out.println("Cadena:"+tablaAFD.cadena+" num:"+tablaAFD.numAFD);
                        
                        //Codigo para validar cadena
                        
                    }
                    tablaAFD.setVisible(false);
                    menu.setVisible(true);
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
                    menu.setVisible(false);
                    exit(0);
            }
        }
    }  
    
    public static void crearAutomataBasico(char c){
        AFN afn = new AFN();
        afn.crearBasico(c, listaAFN.size());    
        System.out.println("Automata basico con id:" +afn.getIdAFN()+" creado.");
        listaAFN.add(afn);
    }

    public static void unirAutomatas(int Id1, int Id2) {
        AFN aux1 = new AFN();
        AFN aux2 = new AFN();
        
        aux1 = listaAFN.get(Id1).Duplicar();
        aux2 = listaAFN.get(Id2).Duplicar();
            
        aux1.unirAFN(aux2);
        listaAFN.add(aux1);
    }

    public static void concatenarAutomatas(int Id1, int Id2) {
        AFN aux1 = new AFN();
        AFN aux2 = new AFN();
        
        aux1 = listaAFN.get(Id1).Duplicar();
        aux2 = listaAFN.get(Id2).Duplicar();
             
        aux1.concatenarAFN(aux2);
        listaAFN.remove(Id2);
        //listaAFN.add(aux1);
    }
    public static void cerraduraEpsilon(int id){
        AFN automataAnalizar =  listaAFN.get( id );
        AFN automataGuardar = new AFN();
                    
        ArrayList<Estado> listaEstadosAnalizar = new ArrayList();
        listaEstadosAnalizar.add(automataAnalizar.getEstadoInicial());
                    
        ArrayList <Estado> listaCE = automataAnalizar.irA( listaEstadosAnalizar,'c');
        automataGuardar.setEstadosAFN(listaCE);
        System.out.println("numero de elementos c_e"+  listaCE.size() );
                    
        listaAFN.add(automataGuardar);
    }
}
