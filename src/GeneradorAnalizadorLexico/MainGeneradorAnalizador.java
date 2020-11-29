/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneradorAnalizadorLexico;

import AnalizadorCalculadora.*;
import Analizador2.*;
import clases.Numero;
import Analizador1.*;
import analizadores.AnalizadorLexico;
import clases.AFD;
import clases.AFN;
import static clases.AFN.epsilon;
import clases.Estado;
import clases.Resultado;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author J.PEREZ
 * 
 * CALCULADORA PARA NUMEROS FLOTANTES, EVALUADOR DE EXPRESIONES Y 
 * CONVERTIDOR A POSTFIJO.  
 *  
 * 
 *GRAMATICA:
       E -> E or T | T
       T -> T & C | C                 & es para concatenacion
       C -> C+ | C* | C? | F
       F -> (E) | [SIMB-SIMB] | SIMB
      
      GRAMATICA SIN RECURSION:
    
       E -> TE'
       E' -> or TE' | Epsilon
       T -> CT'
       T' -> & CT' | Epsilon
       C -> FC'
       C' -> +C' | *C' | ?C' | Epsilon
       F -> (E) | [SIMB-SIMB] | SIMB

* CLASES LEXICAS: Operadores para este analizador
        | Operacion OR
        & Concatenacion
        + Cerradura positiva
        * Cerradura de Kleene
        ? Cerradura opcional
        (
        )
        [
        ]
        -   Operador de RANGO
        \ ESCAPE
        SIMB
 * 
 */
public class MainGeneradorAnalizador {
    
    
    private static ArrayList<AFN> AFNBasicos = new ArrayList();
    private static ArrayList<AFN> AFNparaUnion = new ArrayList();
    
    
    public static void main (String[] args){   
        
        //Creacion de Automata Para Analizador Lexico
        crearAutomataBasicoAFN('|');//0  Posicion en el array BASICOS
        crearAutomataBasicoAFN('&');//1
        crearAutomataBasicoAFN('+');//2
        crearAutomataBasicoAFN('*');//3
        crearAutomataBasicoAFN('?');//4
        crearAutomataBasicoAFN('(');//5
        crearAutomataBasicoAFN(')');//6
        crearAutomataBasicoAFN('[');//7
        crearAutomataBasicoAFN(']');//8
        crearAutomataBasicoAFN('-');//9
        
        crearAutomataBasicoAFN((char)92);//10 ESCAPE (\)
        
        //Considerando que se pudiese tomar todos los caracteres del ASCII 
        //Se propone el rango de valores admitidos para un afn basico desde 
        //el simbolo 0 al 255 de la tabla ASCII de https://elcodigoascii.com.ar/
        //Excluyendo los caracteres OPERADORES
        
        crearAutomataBasicoAFN((char)0, (char)37);//11
        crearAutomataBasicoAFN((char)39);//12
        crearAutomataBasicoAFN((char)44);//13
        crearAutomataBasicoAFN((char)46, (char)62);//14
        crearAutomataBasicoAFN((char)64, (char)90);//15
        crearAutomataBasicoAFN((char)94, (char)123);//16
        crearAutomataBasicoAFN((char)125, (char)255);//17     
        
        //Operaciones
        AFN simb = new AFN();
        simb = AFNBasicos.get(11).Duplicar();
        simb.unirAFN(AFNBasicos.get(12));
        simb.unirAFN(AFNBasicos.get(13));
        simb.unirAFN(AFNBasicos.get(14));
        simb.unirAFN(AFNBasicos.get(15));
        simb.unirAFN(AFNBasicos.get(16));
        simb.unirAFN(AFNBasicos.get(17));
        
        
        //Anadir los AFN a la lista para UNIR
        AFNparaUnion.add(AFNBasicos.get(0));//0
        AFNparaUnion.add(AFNBasicos.get(1));//1
        AFNparaUnion.add(AFNBasicos.get(2));//2
        AFNparaUnion.add(AFNBasicos.get(3));//3
        AFNparaUnion.add(AFNBasicos.get(4));//4
        AFNparaUnion.add(AFNBasicos.get(5));//5
        AFNparaUnion.add(AFNBasicos.get(6));//6
        AFNparaUnion.add(AFNBasicos.get(7));//7
        AFNparaUnion.add(AFNBasicos.get(8));//8
        AFNparaUnion.add(AFNBasicos.get(9));//9
        AFNparaUnion.add(AFNBasicos.get(10));//10  
        AFNparaUnion.add(simb);//11
        
        //Asignasion de TOKENS
        AFNparaUnion.get(0).getEdosAceptacion().get(0).setToken(10);
        AFNparaUnion.get(1).getEdosAceptacion().get(0).setToken(20);
        AFNparaUnion.get(2).getEdosAceptacion().get(0).setToken(30);
        AFNparaUnion.get(3).getEdosAceptacion().get(0).setToken(40);
        AFNparaUnion.get(4).getEdosAceptacion().get(0).setToken(50);
        AFNparaUnion.get(5).getEdosAceptacion().get(0).setToken(60);
        AFNparaUnion.get(6).getEdosAceptacion().get(0).setToken(70);
        AFNparaUnion.get(7).getEdosAceptacion().get(0).setToken(80);
        AFNparaUnion.get(8).getEdosAceptacion().get(0).setToken(90);
        AFNparaUnion.get(9).getEdosAceptacion().get(0).setToken(100);
        AFNparaUnion.get(10).getEdosAceptacion().get(0).setToken(110);
        AFNparaUnion.get(11).getEdosAceptacion().get(0).setToken(120);
        
        AFN afn = new AFN();
        afn = unirAFNS(AFNparaUnion);

        AFD afd = afn.convertirAFN();        
        
//        System.out.println("ALFABETO AFD");
//        for(Character c: afd.getAlfabeto()){
//            System.out.println(" "+c);
//        }
//        
//        System.out.println("ESTADOS AFD");
//        for(int i=0; i<afd.getEstados().size(); i++){
//            System.out.println("");
//            System.out.println("Estado: "+afd.getEstados().get(i).getIdentificador());
//            System.out.println("Token: "+afd.getEstados().get(i).getToken());
//        }
//        
        ArrayList<ArrayList<Integer>> tablaAFD = afd.getTablaAFD();
        
//        System.out.println("CONTENIDO DE LA TABLA AFD");
//        for(int i=0; i<tablaAFD.size(); i++){
//            //
//            for(int j=0; j<tablaAFD.get(i).size(); j++){
//                System.out.print(" "+tablaAFD.get(i).get(j));
//            }
//            System.out.println("");
//        }
        
//        System.out.println("ALFABETO AFD");
//        for(Character c: afd.getAlfabeto()){
//            System.out.println(" "+c);
//        }
//        
//        System.out.println("ESTADOS AFD");
//        for(int i=0; i<afd.getEstados().size(); i++){
//            System.out.println("");
//            System.out.println("Estado: "+afd.getEstados().get(i).getIdentificador());
//            System.out.println("Token: "+afd.getEstados().get(i).getToken());
//        }
        
        //ANALIZADOR LEXICO
        String CadenaparaAnalizar = "(\\g|[A-Z])";
        GeneradorAnalizadorLexico Generador = new GeneradorAnalizadorLexico(tablaAFD, CadenaparaAnalizar);
 
        AFN res = new AFN();
        Generador.E(res);
        
        AFD resafd = res.convertirAFN();
        resafd.generarArchivoTabla("pruebalexico");
        
    }  
    
    
    
    //Crear el automata con transicion de un caracter
    private static void crearAutomataBasicoAFN(char c){
        AFN afn = new AFN();
        afn.crearBasico(c, AFNBasicos.size());    
        System.out.println("Automata basico con id:" +afn.getIdAFN()+" creado.");
        AFNBasicos.add(afn);
    }
    
    
    //Crear automata con transicion de un intervalo de caracteres
    private static void crearAutomataBasicoAFN(char c1, char c2){
        AFN afn = new AFN();
        afn.crearBasico(c1, c2, AFNBasicos.size());    
        System.out.println("Automata basico con id:" +afn.getIdAFN()+" creado.");
        AFNBasicos.add(afn);
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
