package analizadorcompiladores;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.*;

public class Pruebas {    

    private static ArrayList<AFN> listaAFN = new ArrayList();
    private static ArrayList<AFD> listaAFD = new ArrayList();
    
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
        crearAutomataBasicoAFN('T');//10
        
        //Acciones
        AFN afn = unirAutomatasAFN(ListaAFNBasicos.get(1),ListaAFNBasicos.get(2));
        
        afn.cerraduradeKleene();
        
        System.out.println(gson.toJson(afn.getEstadosAFN()));
        
//        AFN afncerradura = afn.Duplicar();
//        System.out.println(gson.toJson(afncerradura));
        System.out.println("Estados: "+afn.getEstadosAFN().size());
        
    }  
    
    private static void crearAutomataBasicoAFN(char c){
        AFN afn = new AFN();
        afn.crearBasico(c, ListaAFNBasicos.size());    
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

    private static void concatenarAutomatasAFN(int Id1, int Id2) {
        AFN aux1 = new AFN();
        AFN aux2 = new AFN();
        
        aux1 = ListaAFNBasicos.get(Id1).Duplicar();
        aux2 = ListaAFNBasicos.get(Id2).Duplicar();
             
        aux1.concatenarAFN(aux2);
        //listaAFNUnidos.add(aux1);
        
    }

    private static void convertirAFNaAFD(int Id) {
        AFN aux1 = new AFN();        
        aux1 = listaAFN.get(Id);
        aux1.convertirAFN();
    }

}