/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorCalculadora;

import clases.AFD;
import clases.AFN;
import static clases.AFN.epsilon;
import clases.Estado;
import clases.Resultado;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author J.PEREZ
 * 
 * CALCULADORA PARA NUMEROS FLOTANTES, EVALUADOR DE EXPRESIONES Y 
 * CONVERTIDOR A POSTFIJO.  
 *  
 * 
 * GRAMATICA PARA LA CALCULADORA:
 * 
 *          E -> E+T | E-T | T
 *          T -> T*P | T/P | P
 *          P -> P^F | F
 *          F -> (E) | SIN(E) | COS(E) | TAN(E) | LN(E) | LOG(E) | NUM
 * 
 * ELIMINANDO LA RECURSION POR LA IZQ:
 * 
 *          E -> TE'
 *          E' -> +TE' | -TE' | Epsilon
 *          T -> PT'
 *          T' -> *PT' | /PT' | Epsilon
 *          P -> FP'
 *          P' -> ^FP' | Epsilon
 *          F -> (E) | SIN(E) | COS(E) | TAN(E) | LN(E) | LOG(E) | NUM
 * 
 *      Con VN = {E, E', T, T', P, P', F}
 *          VT = {+, -, *, /, ^, (, ), SIN, COS, TAN, LN, LOG, NUM}   CLASES LEXICAS
 * 
 * TOMANDO POR NUM A LA EXPRESION: D+(.D+)?
 * 
 */
public class MainCalculadora {
    
    
    private static ArrayList<AFN> AFNBasicos = new ArrayList();
    private static ArrayList<AFN> AFNparaUnion = new ArrayList();
    
    
    public static void main (String[] args){   
        
        //Creacion de Automata Para Analizador Lexico
        crearAutomataBasicoAFN('+');//0  Posicion en el array BASICOS
        crearAutomataBasicoAFN('-');//1
        crearAutomataBasicoAFN('*');//2
        crearAutomataBasicoAFN('/');//3
        crearAutomataBasicoAFN('^');//4
        crearAutomataBasicoAFN('(');//5
        crearAutomataBasicoAFN(')');//6
        
        //Automata para SIN
        crearAutomataBasicoAFN('S');//7
        crearAutomataBasicoAFN('I');//8
        crearAutomataBasicoAFN('N');//9
        
        //Automata para COS
        crearAutomataBasicoAFN('C');//10
        crearAutomataBasicoAFN('O');//11
        crearAutomataBasicoAFN('S');//12        
        
        //Automata para TAN
        crearAutomataBasicoAFN('T');//13
        crearAutomataBasicoAFN('A');//14
        crearAutomataBasicoAFN('N');//15
        
        //Automata para LN
        crearAutomataBasicoAFN('L');//16
        crearAutomataBasicoAFN('N');//17
        
        //Automata para LOG
        crearAutomataBasicoAFN('L');//18
        crearAutomataBasicoAFN('O');//19
        crearAutomataBasicoAFN('G');//20
        
        //Automata para NUM:  D+(.D+)?
        crearAutomataBasicoAFN('0','9');//21
        crearAutomataBasicoAFN('.');//22
        crearAutomataBasicoAFN('0','9');//23
        
        
        //Acciones
        AFN aux = concatenarAutomatasAFN(AFNBasicos.get(7), AFNBasicos.get(8));
        AFN afnSIN = concatenarAutomatasAFN(aux, AFNBasicos.get(9));
        
        aux = concatenarAutomatasAFN(AFNBasicos.get(10), AFNBasicos.get(11));
        AFN afnCOS = concatenarAutomatasAFN(aux, AFNBasicos.get(12));
        
        aux = concatenarAutomatasAFN(AFNBasicos.get(13), AFNBasicos.get(14));
        AFN afnTAN = concatenarAutomatasAFN(aux, AFNBasicos.get(15));
        
        AFN afnLN = concatenarAutomatasAFN(AFNBasicos.get(16), AFNBasicos.get(17));
        
        aux = concatenarAutomatasAFN(AFNBasicos.get(18), AFNBasicos.get(19));
        AFN afnLOG = concatenarAutomatasAFN(aux, AFNBasicos.get(20));
        
        AFN auxD1 = AFNBasicos.get(23).Duplicar();
                auxD1.cerraduraTransitiva();//D+
        AFN auxP = concatenarAutomatasAFN(AFNBasicos.get(22), auxD1);//.D+
            auxP.cerraduraOpcional();//(.D+)?        
        AFN auxD2 = AFNBasicos.get(21).Duplicar();
            auxD2.cerraduraTransitiva();//D+        
        AFN afnNUM = concatenarAutomatasAFN(auxD2, auxP);//D+(.D+)?
        
        //Anadir los AFN a la lista para UNIR
        AFNparaUnion.add(AFNBasicos.get(0));//0
        AFNparaUnion.add(AFNBasicos.get(1));//1
        AFNparaUnion.add(AFNBasicos.get(2));//2
        AFNparaUnion.add(AFNBasicos.get(3));//3
        AFNparaUnion.add(AFNBasicos.get(4));//4
        AFNparaUnion.add(AFNBasicos.get(5));//5
        AFNparaUnion.add(AFNBasicos.get(6));//6
        
        AFNparaUnion.add(afnSIN);//7
        AFNparaUnion.add(afnCOS);//8
        AFNparaUnion.add(afnTAN);//9
        AFNparaUnion.add(afnLN);//10
        AFNparaUnion.add(afnLOG);//11
        AFNparaUnion.add(afnNUM);//12    
        
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
        AFNparaUnion.get(12).getEdosAceptacion().get(0).setToken(130);
        
        AFN afn = new AFN();
        afn = unirAFNS(AFNparaUnion);

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
        //String CadenaparaAnalizar = "SIN(90)";
        /*String CadenaparaAnalizar = "20.8+7^(9-5)/4";
        
        

        AnalizadorSintacticoCalculadora calculadora = new AnalizadorSintacticoCalculadora(tablaAFD, CadenaparaAnalizar);                   

        Resultado res = new Resultado();
        calculadora.E(res);
        
        if(res.isValido()){
            System.out.println("\n\nCadena Aceptada! ");
            System.out.println("\tPostfijo:  "+res.getCadena());
            System.out.println("\tResultado: "+res.getValor());
        }else{
            System.out.println("\n\nCadena no valida.");
        }*/
        while(true){
                String CadenaparaAnalizar = JOptionPane.showInputDialog("Ingrese la cadena");
               // String CadenaparaAnalizar = "20.8+7^(9-5)/4";

                AnalizadorSintacticoCalculadora calculadora = new AnalizadorSintacticoCalculadora(tablaAFD, CadenaparaAnalizar);                   

                Resultado res = new Resultado();
                calculadora.E(res);

                if(res.isValido()){
                    System.out.println("\n\nCadena Aceptada! ");
                    System.out.println("\tPostfijo:  "+res.getCadena());
                    System.out.println("\tResultado: "+res.getValor());
                    JOptionPane.showMessageDialog(null,"\tResultado: "+res.getValor()+"\n\tPostFijo: "+res.getCadena(),
                            "El resultado de"+CadenaparaAnalizar,JOptionPane.INFORMATION_MESSAGE);
                }else{
                    System.out.println("\n\nCadena no valida.");
                }
         }
 
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
