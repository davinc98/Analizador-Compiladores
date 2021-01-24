/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneradorAnalizadorLexico;

import clases.AFD;
import clases.AFN;
import static clases.AFN.epsilon;
import clases.Estado;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 * 
 * CALCULADORA PARA NUMEROS FLOTANTES, EVALUADOR DE EXPRESIONES Y 
 * CONVERTIDOR A POSTFIJO.  
 *  
 * 
 *GRAMATICA PARA EL GENERADOR DE ANALIZADORES LEXICOS:
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
public class MainGeneradorAnalizadorLexico {
    
    private static ArrayList<AFN> AFNBasicosGenerador = new ArrayList();
    private static ArrayList<AFN> AFNFinalesGenerador = new ArrayList();
    ArrayList<ArrayList<Integer>> tablageneradorAFD = new ArrayList();
    
    private static ArrayList<AFN> AFNGenerados = new ArrayList();
    
    public MainGeneradorAnalizadorLexico(){ 
        //Creacion de Automata Para Generador de Analizadores Lexicos
        
        //OPERADORES
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
                                //38    &
        crearAutomataBasicoAFN((char)39);//12
                                //40    (
                                //41    )
        crearAutomataBasicoAFN((char)44);//13
                                //45    -
        crearAutomataBasicoAFN((char)46, (char)62);//14
                                //63    ?
        crearAutomataBasicoAFN((char)64, (char)90);//15
                                //91    [
                                //92    \
                                //93    ]
        crearAutomataBasicoAFN((char)94, (char)123);//16
                                //124   |
        crearAutomataBasicoAFN((char)125, (char)255);//17     
        
        //Operaciones
        AFN simb = new AFN();
        simb = AFNBasicosGenerador.get(11).Duplicar();
        simb.unirAFN(AFNBasicosGenerador.get(12));
        simb.unirAFN(AFNBasicosGenerador.get(13));
        simb.unirAFN(AFNBasicosGenerador.get(14));
        simb.unirAFN(AFNBasicosGenerador.get(15));
        simb.unirAFN(AFNBasicosGenerador.get(16));
        simb.unirAFN(AFNBasicosGenerador.get(17));
        
        
        //Anadir los AFN a la lista para UNIR
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(0));//0
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(1));//1
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(2));//2
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(3));//3
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(4));//4
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(5));//5
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(6));//6
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(7));//7
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(8));//8
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(9));//9
        AFNFinalesGenerador.add(AFNBasicosGenerador.get(10));//10  
        AFNFinalesGenerador.add(simb);//11
        
        //Asignasion de TOKENS
        AFNFinalesGenerador.get(0).getEdosAceptacion().get(0).setToken(10);
        AFNFinalesGenerador.get(1).getEdosAceptacion().get(0).setToken(20);
        AFNFinalesGenerador.get(2).getEdosAceptacion().get(0).setToken(30);
        AFNFinalesGenerador.get(3).getEdosAceptacion().get(0).setToken(40);
        AFNFinalesGenerador.get(4).getEdosAceptacion().get(0).setToken(50);
        AFNFinalesGenerador.get(5).getEdosAceptacion().get(0).setToken(60);
        AFNFinalesGenerador.get(6).getEdosAceptacion().get(0).setToken(70);
        AFNFinalesGenerador.get(7).getEdosAceptacion().get(0).setToken(80);
        AFNFinalesGenerador.get(8).getEdosAceptacion().get(0).setToken(90);
        AFNFinalesGenerador.get(9).getEdosAceptacion().get(0).setToken(100);
        AFNFinalesGenerador.get(10).getEdosAceptacion().get(0).setToken(110);
        AFNFinalesGenerador.get(11).getEdosAceptacion().get(0).setToken(120);
        
        AFN afn = new AFN();
        afn = unirAFNS(AFNFinalesGenerador);
        AFD afd = afn.convertirAFN();    
        tablageneradorAFD = afd.getTablaAFD();
    }  
    
    //Recibe el nombre del archivo(con ext) con las ER y los tokens
    public AFN generarAFN(String nombreFichero){
        BufferedReader br = null; 
        
        try {
            br = new BufferedReader(new FileReader(nombreFichero));
            String linea = br.readLine();
            while (linea != null) {
                String []cad = linea.split(" ");
                String expreg = cad[0];int token = Integer.parseInt(cad[1]);
                
                generarAFN(expreg, token);
                //System.out.println(linea);
                
                linea = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Fichero no encontrado");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de lectura del fichero");
            System.out.println(e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.out.println("Error al cerrar el fichero");
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Fin transformacion de ERs.");
        return unirAFNS(AFNGenerados);
    }
    
    
    //Recibe solo la expresion regular y el token
    public void generarAFN(String expreg, int token){        
        //ANALIZADOR LEXICO
        GeneradorAnalizadorLexico  Generador = new GeneradorAnalizadorLexico(tablageneradorAFD, expreg);
        AFN res = new AFN();
        Generador.E(res);
        res.getEdosAceptacion().get(0).setToken(token);
        AFNGenerados.add(res);
    }
    
    public AFN getAFNFinal(){
        return unirAFNS(AFNGenerados);
    }
    
    
    
    //Crear el automata con transicion de un caracter
    private static void crearAutomataBasicoAFN(char c){
        AFN afn = new AFN();
        afn.crearBasico(c, AFNBasicosGenerador.size());    
        System.out.println("Automata basico con id:" +afn.getIdAFN()+" creado.");
        AFNBasicosGenerador.add(afn);
    }
    
    
    //Crear automata con transicion de un intervalo de caracteres
    private static void crearAutomataBasicoAFN(char c1, char c2){
        AFN afn = new AFN();
        afn.crearBasico(c1, c2, AFNBasicosGenerador.size());    
        System.out.println("Automata basico con id:" +afn.getIdAFN()+" creado.");
        AFNBasicosGenerador.add(afn);
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
    
    //Union de AFN sin estado final comun
    public static AFN unirAFNS(ArrayList<AFN> AFNS){
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
