package analizadorcompiladores;

import static analizadorcompiladores.AFN.epsilon;
import java.util.ArrayList;
import com.google.gson.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import static java.lang.System.exit;
import javax.swing.JOptionPane;
import ventanas.*;

public class Principal {
    
    public static ArrayList<AFN> listaAFN = new ArrayList();
    public static ArrayList<AFD> listaAFD = new ArrayList();
    public static int opcionMenu = -1;
    
    public static void main (String[] args){        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        AFN union = new AFN();
        
        //Interfaces
        Menu menu = new Menu();
        CrearBasico basico;
        TablaAFN tablaAFN;
        TablaAFD tablaAFD;
        Cerradura cerr;
        Token tok;
        AnalizarCadenaAFN cadenaAFN;
        AFNaAFD afn_afd;
        
        //Menu
        while(true){
            while(opcionMenu==-1){ System.out.print(""); }
            switch(opcionMenu){
                case 1: //crear AFN
                    menu.setVisible(false);
                    basico = new CrearBasico();
                    
                    while(basico.c1()=='-' || basico.c2()=='-' ){
                        System.out.print("");
                        if(basico.volver) break;
                    }
                    if(!basico.volver) crearAutomataBasicoAFN(basico.c1(),basico.c2());
                    
                    basico.setVisible(false);
                    menu.setVisible(true);
                    break;
                case 2: //Unir AFN
                    menu.setVisible(false);
                    tablaAFN = new TablaAFN(0);

                    while(tablaAFN.V1()==-1 || tablaAFN.V2()==-1){
                        System.out.print("");
                        if(tablaAFN.volver) break;
                    }
                    if(!tablaAFN.volver) unirAutomatas(tablaAFN.V1(), tablaAFN.V2());
                    
                    tablaAFN.setVisible(false);
                    menu.setVisible(true);
                    break;
                case 3: //concatenar AFN
                    menu.setVisible(false);
                    tablaAFN = new TablaAFN(1);
                    
                    while(tablaAFN.V1()==-1 || tablaAFN.V2()==-1){
                        System.out.print("");
                        if(tablaAFN.volver) break;
                    }
                    if(!tablaAFN.volver) concatenarAutomatas(tablaAFN.V1(), tablaAFN.V2());
                    
                    tablaAFN.setVisible(false);
                    menu.setVisible(true);
                    break;
                case 4: //Cerradura Transitiva
                    menu.setVisible(false);
                    cerr = new Cerradura(0);
                    
                    while(cerr.id()==-1){
                        System.out.print("");
                        if(cerr.volver) break;
                    }
                    if(!cerr.volver) cerraduraTransitiva(cerr.id());
                    
                    cerr.setVisible(false);
                    menu.setVisible(true);
                    break;
                case 5: //Cerradura de Kleene
                    menu.setVisible(false);
                    cerr = new Cerradura(1);
                    
                    while(cerr.id()==-1){
                        System.out.print("");
                        if(cerr.volver) break;
                    }
                    if(!cerr.volver) cerraduraDeKleene(cerr.id());
                    
                    cerr.setVisible(false);
                    menu.setVisible(true);
                    break;
                case 6: //Cerradura
                    System.out.println("Probando cerradura epsilon");
                    crearAutomataBasicoAFN('c');
                    crearAutomataBasicoAFN('d');
                    AFN auf3 =listaAFN.get(listaAFN.size()-1 );
                    auf3.unirAFN(listaAFN.get(0));
                    
                    cerraduraEpsilon(listaAFN.size()-1);
                    /*
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
                case 7: //Token
                    menu.setVisible(false);
                    tok = new Token();
                    
                    while(tok.id()==-1 || tok.token()==-1){
                        System.out.print("");
                        if(tok.volver) break;
                    }
                    if(!tok.volver) asignarToken(tok.id(),tok.token());
                    
                    tok.setVisible(false);
                    menu.setVisible(true);
                    break;  
                case 8: //Unir todos los AFN
                    int op = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas unir todos los AFN?");
                    if(op==JOptionPane.YES_OPTION){
                        union = unirAFNS(listaAFN);
                        listaAFN.clear();
                        listaAFN.add(union);
                        JOptionPane.showMessageDialog(null, "¡AFN unidos!");
                    } else  JOptionPane.showMessageDialog(null, "No se unieron los AFN");
                    break;
                case 9: //Analizar cadena usando AFN
                    menu.setVisible(false);
                    cadenaAFN = new AnalizarCadenaAFN();
                    
                    while(cadenaAFN.id()==-1 || cadenaAFN.cadena()==null){
                        System.out.print("");
                        if(cadenaAFN.volver) break;
                    }
                    if(!cadenaAFN.volver) {
                        if(analizarCadenaAFN(cadenaAFN.id(),cadenaAFN.cadena()) != 0){
                            JOptionPane.showMessageDialog(null, "¡Cadena Aceptada!");
                        } else JOptionPane.showMessageDialog(null, "Cadena no válida");
                    }
                    
                    cadenaAFN.setVisible(false);
                    menu.setVisible(true);
                    break;    
                case 10: //Convertir AFN a AFD
                    menu.setVisible(false);
                    afn_afd = new AFNaAFD();
                    
                    while(afn_afd.id()==-1){
                        System.out.print("");
                        if(afn_afd.volver) break;
                    }
                    if(!afn_afd.volver) convertirAFNaAFD(afn_afd.id());
                    
                    afn_afd.setVisible(false);
                    menu.setVisible(true);
                    break;
                case 11: //Analizador Léxico
                    break;
                case 12: //Validar cadena usando AFD
                    menu.setVisible(false);
                    tablaAFD = new TablaAFD();

                    while(tablaAFD.cadena().equals("") || tablaAFD.id()==-1){
                        System.out.print("");
                        if(tablaAFD.volver) break;
                    }
                    if(!tablaAFD.volver){
                        JOptionPane.showMessageDialog(null, "Falta agregar el Analizador Léxico xD");
                    }
                    
                    tablaAFD.setVisible(false);
                    menu.setVisible(true);
                    break;   
                case 13: //Crear AFD usando ER
                    break;
                case 14: //copiar array list AFN  json en el portapapeles     
                    StringSelection ss = new StringSelection(gson.toJson(listaAFN));
                    Toolkit tool = Toolkit.getDefaultToolkit();
                    Clipboard clip = tool.getSystemClipboard();
                    clip.setContents(ss, null);
                    JOptionPane.showMessageDialog(null, "Gson ya está en portapapeles");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Hasta Luego!");
                    menu.setVisible(false); exit(0);
            }
            opcionMenu=-1;
        }
    }  

    //////////////////////////////////////////////////// Metodos Pepe
    //Crear el automata con transicion de un caracter
    private static void crearAutomataBasicoAFN(char c){
        AFN afn = new AFN();
        afn.crearBasico(c, listaAFN.size());    
        System.out.println("Automata ["+c+"] basico con id:" +afn.getIdAFN()+" creado.");
        listaAFN.add(afn);
    }
    //Crear automata con transicion de un intervalo de caracteres
    private static void crearAutomataBasicoAFN(char c1, char c2){
        AFN afn = new AFN();
        afn.crearBasico(c1, c2, listaAFN.size());    
        System.out.println("Automata ["+c1+"-"+c2+"] basico con id:" +afn.getIdAFN()+" creado.");
        listaAFN.add(afn);
    }
    public static void unirAutomatas(int Id1, int Id2) {
        listaAFN.get(Id1).unirAFN(listaAFN.get(Id2));
        listaAFN.remove(Id2);
    }
    public static void concatenarAutomatas(int Id1, int Id2) {
        listaAFN.get(Id1).concatenarAFN(listaAFN.get(Id2));
        listaAFN.remove(Id2);
    }
    public static void cerraduraTransitiva(int id){
        listaAFN.get(id).cerraduraTransitiva();
    }
    public static void cerraduraDeKleene(int id){
        listaAFN.get(id).cerraduradeKleene();
    }
    public static void asignarToken(int id,int token){
        listaAFN.get(id).getEdosAceptacion().get(0).setToken(token);
    }
    public static int analizarCadenaAFN(int id,String cadena){
        return listaAFN.get(id).analizarCadena(cadena);
    }
    public static void convertirAFNaAFD(int id){
        AFD afdnuevo = listaAFN.get(id).convertirAFN();
        listaAFD.add(afdnuevo);
    }
    public static AFN unirAFNS(ArrayList<AFN> AFNS){//Union de AFN sin estado final comun
        AFN Final = new AFN();//Contendra todos los afn unidos
        
        //Unir Alfabeto
        Final.getAlfabeto().addAll(AFNS.get(0).getAlfabeto());
        for(int i = 0; i<AFNS.size();i++){
            AFNS.get(i).getAlfabeto().removeAll(Final.getAlfabeto());
            Final.getAlfabeto().addAll(AFNS.get(i).getAlfabeto());
        }
        
        //Eliminar Duplicados del Alfabeto
        int size = Final.getAlfabeto().size();
        int duplicates = 0;
        for (int i = 0; i < size - 1; i++) {
           for (int j = i + 1; j < size; j++) {
               if (!Final.getAlfabeto().get(j).equals(Final.getAlfabeto().get(i)))
                   continue;
               duplicates++;
               Final.getAlfabeto().remove(j);
               j--;
               size--;
           }
        } 
        //Se crea un nuevo estado inicial
        Estado nuevo_edo_ini = new Estado(); 
        
        //Trancision del nuevo estado inicial a los estados iniciales de los AFN   
        for(int i=0; i< AFNS.size();i++){
            nuevo_edo_ini.setTransicion(AFNS.get(i).getEdoInicial(), epsilon);
            AFNS.get(i).getEdoInicial().setEdoInicial(false);
        }
                                                  
        //hacemos el nuevo estado como inicial
        nuevo_edo_ini.setEdoInicial(true);
        Final.setEdoInicial(nuevo_edo_ini);
        Final.getEstadosAFN().add(0, nuevo_edo_ini);//Se agrega al inicio del arreglo
        
        //Agregamos los estados de aceptacion de los AFNS y los estados de cada AFN
        for(int i=0; i<AFNS.size(); i++){
            Final.getEdosAceptacion().addAll(AFNS.get(i).getEdosAceptacion());
            Final.getEstadosAFN().addAll(AFNS.get(i).getEstadosAFN());
        }
      
        //Se reasignan identificadores
        for( int i = 0; i< Final.getEstadosAFN().size(); i++ ){
            Final.getEstadosAFN().get(i).setIdentificador(i);
        }
        return Final;
    }
    
    /////////////////////////////////////////////////////// No sé si funciona
    public static void cerraduraEpsilon(int id){
        AFN automataAnalizar =  listaAFN.get( id );
        AFN automataGuardar = new AFN();
                    
        ArrayList<Estado> listaEstadosAnalizar = new ArrayList();
        listaEstadosAnalizar.add(automataAnalizar.getEdoInicial());
                    
        ArrayList <Estado> listaCE = automataAnalizar.irA( listaEstadosAnalizar,'c');
        automataGuardar.setEstadosAFN(listaCE);
        System.out.println("Numero de elementos c_e: "+  listaCE.size() );                    
        listaAFN.add(automataGuardar);
    }
}
