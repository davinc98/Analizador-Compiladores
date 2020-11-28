/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador1;

import analizadores.AnalizadorLexico;
import clases.AFD;
import clases.AFN;
import static clases.AFN.epsilon;
import clases.Estado;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author J.PEREZ
 * 
 * ESTA CLASE ES PARA PROBAR UN ANALIZADOR SINTACTICO
 * DEL EJERCICIO CON GRAMATICA SIN RECURSION SIGUIENTE:
 *          E -> TE'
 *          E' -> +TE' | -TE' | Epsilon
 *          T -> FT'
 *          T' -> *FT' | /FT' | Epsilon
 *          F -> (E) | num
 * 
 *      Con VN = {E, E', T, T', F}
 *          VT = {+, -, *, /, (, ), num}   CLASES LEXICAS
 * 
 * TOMANDO POR COMODIDAD LA CLASE LEXICA num como S
 * 
 */
public class MainAnalizador1 {
    
    
    private static ArrayList<AFN> ListaAFN = new ArrayList();
    private static ArrayList<AFD> ListaAFD = new ArrayList();
    
    private static ArrayList<AFN> ListaAFNBasicos = new ArrayList();
    
    public static void main (String[] args){        
        int opcionMenu;
        Scanner leer = new Scanner(System.in);
        boolean salir=false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        //Creacion de Automata Para Analizador Lexico
        crearAutomataBasicoAFN('+');//0  Posicion en el array
        crearAutomataBasicoAFN('-');//1
        crearAutomataBasicoAFN('*');//2
        crearAutomataBasicoAFN('/');//3
        crearAutomataBasicoAFN('(');//4
        crearAutomataBasicoAFN(')');//5
        crearAutomataBasicoAFN('S');//6//num
        
        //Acciones      
        ListaAFNBasicos.get(0).getEdosAceptacion().get(0).setToken(10);
        ListaAFNBasicos.get(1).getEdosAceptacion().get(0).setToken(20);
        ListaAFNBasicos.get(2).getEdosAceptacion().get(0).setToken(30);
        ListaAFNBasicos.get(3).getEdosAceptacion().get(0).setToken(40);
        ListaAFNBasicos.get(4).getEdosAceptacion().get(0).setToken(50);
        ListaAFNBasicos.get(5).getEdosAceptacion().get(0).setToken(60);
        ListaAFNBasicos.get(6).getEdosAceptacion().get(0).setToken(70);
        
        AFN afn = new AFN();
        afn = unirAFNS(ListaAFNBasicos);

        AFD afd = afn.convertirAFN();        
        
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
        
        ArrayList<ArrayList<Integer>> tablaAFD = afd.getTablaAFD();
        
        System.out.println("CONTENIDO DE LA TABLA AFD");
        for(int i=0; i<tablaAFD.size(); i++){
            //
            for(int j=0; j<tablaAFD.get(i).size(); j++){
                System.out.print(" "+tablaAFD.get(i).get(j));
            }
            System.out.println("");
        }
        
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
        
        //ANALIZADOR LEXICO
        System.out.println("\n\nANALIZADOR LEXICO");

        //num+num+num
        String CadenaparaAnalizar = "S+S*S+S+S+S/S/S";
        AnalizadorSintactico1 sintact = new AnalizadorSintactico1(tablaAFD, CadenaparaAnalizar);                   
        if(sintact.E())
            System.out.println("\n\nCadena valida.");
        else
            System.out.println("\n\nCadena no valida.");
 
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
