package analizadorcompiladores;
import static analizadorcompiladores.AFN.epsilon;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;


public class Pruebas {    

    private static ArrayList<AFN> ListaAFN = new ArrayList();
    private static ArrayList<AFD> ListaAFD = new ArrayList();
    
    private static ArrayList<AFN> ListaAFNBasicos = new ArrayList();
    
    public static void main (String[] args){        
        int opcionMenu;
        Scanner leer = new Scanner(System.in);
        boolean salir=false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        //Creacion de Automata Para Analizador Lexico
        crearAutomataBasicoAFN('L');//0  Posicion en el array
        crearAutomataBasicoAFN('L');//1
        crearAutomataBasicoAFN('D');//2
        crearAutomataBasicoAFN('D');//3
        crearAutomataBasicoAFN('D');//4
        crearAutomataBasicoAFN('.');//5
        crearAutomataBasicoAFN('D');//6
        crearAutomataBasicoAFN('M');//7
        crearAutomataBasicoAFN('P');//8
        crearAutomataBasicoAFN('E');//9
        crearAutomataBasicoAFN('T','V');//10
        
        //Acciones
        
        //Union de 1 y 2
        AFN afn = unirAutomatasAFN(ListaAFNBasicos.get(1),ListaAFNBasicos.get(2));
        //Aplicar  cerradura de Kleene
        afn.cerraduradeKleene();
        //Concatenar 0 con la cerradura de 1 y 2
        afn = concatenarAutomatasAFN(ListaAFNBasicos.get(0), afn);
        
        afn.getEdosAceptacion().get(0).setToken(10);
        ListaAFN.add(afn);
        
        //Cerradura Transitiva de 3
        AFN afn3 = new AFN();
        afn3 = ListaAFNBasicos.get(3).Duplicar();
        afn3.cerraduraTransitiva();
        
        afn3.getEdosAceptacion().get(0).setToken(20);
        ListaAFN.add(afn3);
        
        //Concatenacion de 4+ 5 y 6+
        AFN afn4 = new AFN();
        afn4 = ListaAFNBasicos.get(4).Duplicar();
        afn4.cerraduraTransitiva();
        
        afn4 = concatenarAutomatasAFN(afn4, ListaAFNBasicos.get(5));
        
        AFN afn6 = new AFN();
        afn6 = ListaAFNBasicos.get(6).Duplicar();
        afn6.cerraduraTransitiva();
        
        afn6 = concatenarAutomatasAFN(afn4, afn6);
        
        afn6.getEdosAceptacion().get(0).setToken(30);
        ListaAFN.add(afn6);
        
        //7 y 8 se quedan igual
        ListaAFNBasicos.get(7).getEdosAceptacion().get(0).setToken(40);
        ListaAFN.add(ListaAFNBasicos.get(7));
        ListaAFNBasicos.get(8).getEdosAceptacion().get(0).setToken(50);
        ListaAFN.add(ListaAFNBasicos.get(8));
        
        //Union de 9 Y 10 con cerradura de kleene
        AFN afn7 = unirAutomatasAFN(ListaAFNBasicos.get(9),ListaAFNBasicos.get(10));
        afn7.cerraduraTransitiva();
        
        afn7.getEdosAceptacion().get(0).setToken(60);
        ListaAFN.add(afn7);
        
        AFN aefen = new AFN();
        aefen = unirAFNS(ListaAFN);
        
        System.out.println("Analizar cadena en AFN: LLLLD");
        System.out.println("Resultado: "+aefen.analizarCadena("LLLLD"));
        System.out.println("Analizar cadena en AFN: LDTTLS");
        System.out.println("Resultado: "+aefen.analizarCadena("LDTTLS"));
        
        AFD afd = aefen.convertirAFN();
        
        
        System.out.println("ALFABETO AFD");
        for(Character c: afd.getAlfabeto()){
            System.out.println(" "+c);
        }
        
        System.out.println("ESTADOS AFD");
        for(int i=0; i<afd.getEstados().size(); i++){
            System.out.println("");
            System.out.println("Estado: "+afd.getEstados().get(i).getIdentificador());
            System.out.println("Token: "+afd.getEstados().get(i).getToken());
        }
        
        //Generar Archivo con la tabla AFD
        afd.generarArchivoTabla("Prueba1");

        ArrayList<ArrayList<Integer>> tablaAFD = afd.getArrayTabla();
        
        System.out.println("CONTENIDO DEL ARRAY DE LA TABLA AFD");
        for(int i=0; i<tablaAFD.size(); i++){
            //
            for(int j=0; j<tablaAFD.get(i).size(); j++){
                System.out.print(" "+tablaAFD.get(i).get(j));
            }
            System.out.println("");
        }
        
        //Prueba Analizador Léxico Aacini
        System.out.println("Analizador léxico para afd con DD.DDTTLLDEMEEP");
        AnalizadorLexico lexico = new AnalizadorLexico(afd,"DD.DDTTLLDEMEEP" );                   
        int r;
        while( (r = lexico.yylex() ) != -1   ){
            System.out.println("El token es "+r );
            System.out.println("El  lexema es: "+lexico.getYyText() );
        } 
        while( (r = lexico.regresarToken() ) != 0   ){
            System.out.println("Regresando apuntador actual  a  "+r );  
        }
        System.out.println("Regresando apuntador actual  a  "+r );
        
        lexico.regresarToken();
        
        while( (r = lexico.yylex() ) != -1   ){
            System.out.println("El token es "+r );
            System.out.println("El  lexema es: "+lexico.getYyText() );
            
        }
 
    }  
    
    //Crear el automata con transicion de un caracter
    private static void crearAutomataBasicoAFN(char c){
        AFN afn = new AFN();
        afn.crearBasico(c, ListaAFNBasicos.size());    
        System.out.println("Automata basico con id:" +afn.getIdAFN()+" creado.");
        ListaAFNBasicos.add(afn);
    }
    
    //Crear automata con transicion de un intervalo de caracteres
    private static void crearAutomataBasicoAFN(char c1, char c2){
        AFN afn = new AFN();
        afn.crearBasico(c1, c2, ListaAFNBasicos.size());    
        System.out.println("Automata basico con id:" +afn.getIdAFN()+" creado.");
        ListaAFNBasicos.add(afn);
    }

    private static AFN unirAutomatasAFN(AFN afn1, AFN afn2) {
        AFN aux1 = new AFN();
        AFN aux2 = new AFN();
            
        aux1 = afn1.Duplicar();
        aux2 = afn2.Duplicar();
            
        aux1.unirAFN(aux2);
        return aux1;
    }

    private static AFN concatenarAutomatasAFN(AFN afn1, AFN afn2) {
        AFN aux1 = new AFN();
        AFN aux2 = new AFN();
        
        aux1 = afn1.Duplicar();
        aux2 = afn2.Duplicar();
             
        aux1.concatenarAFN(aux2);
        return aux1;
        
    }
    
    public static AFN unirAFNS(ArrayList<AFN> AFNS){//Union de AFN sin estado final comun
        //Unir Alfabeto
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
    
  
}
