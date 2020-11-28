/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadores;

import clases.AFN;
import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 */


public class GeneradorAnalizadoresLexicos {
    /*GRAMATICA:
       E -> EorT | T
       T -> T&C | C                 & es para concatenacion
       C -> C+ | C* | C? | F
       F -> (E) | s
      
      Gramatica sin recursion
       E -> TE'
       E' -> orTE' | Epsilon
       T -> CT'
       T' -> &CT' | Epsilon
       C -> FC'
       C' -> +C' | *C' | ?C' | Epsilon
       F -> (E) | [SIMB, SIMB] | \SIMB | SIMB
    
        \ Se utiliza para diferenciar de los operadores +, * y ? 
        para que sea considerado como otro simbolo
    
        Expresiones Regulares
                * Cerradura de Kleene
                + Cerradura positiva
                ? Cerradura opcional
        Operadores Aritmeticos
    
    Ejemplo: Input 
        (a|b)+  10
        (1|\*)? 20
                
    */
    
    public final static int OR =10; // | union
    public final static int CONC=20; // . concatenacion
    public final static int CERR_POS=30; // +  cerradura positiva
    public final static int CERR_KLEEN=40; //* cerradura cleen
    public final static int OPC=50;   //? opcional
    public final static int PAR_IZQ=60; // ( parentecis izquierdo
    public final static int PAR_DER=70; // ) parentesis derecho
    public final static int CORCH_IZQ=80; // [ corchete apertura para lso rangos
    public final static int CORCH_DER=90; // ] corchete que cierra para los rangos
    public final static int GUION=100; // -  guion para los tangos
    public final static int SIMB=110; // simbolo apto para automatas
    public int contadorId= 0; //Contador para los id de los AFN
    
    AnalizadorLexico lexic = new AnalizadorLexico();
    
    
    public GeneradorAnalizadoresLexicos(ArrayList<ArrayList<Integer>> tablaAutomata, String cadena){
        this.lexic = new AnalizadorLexico(tablaAutomata, cadena);
    }
    
    public Boolean Ini(){
        int token;
        
//        if(E()){
//            token = lexic.yylex();
//            if(token == -10)//-10:FIN
//                return true;
//        }
        return false;
    }
    
    

    //Funciones
    public Boolean E(AFN f){
        if(T(f)){
            if(Ep(f)){
                return true;
            }
        }
        return false;
    }
    
    public Boolean Ep(AFN f){
        int token = lexic.yylex();
        AFN f2 = new AFN();
        if(token == OR)
            if(T(f2)){
                f.unirAFN(f2);//Accion semantica
                if(Ep(f))
                    return true;
                return false;
            }
            lexic.regresarToken();
            return true;
        
    }
    
    public Boolean T(AFN f){
        if(C(f))
            if(Tp(f))
                return true;
        return false;
    }
    
    public Boolean Tp(AFN f){
        int token = lexic.yylex();
        AFN f2 = new AFN();
        if(token == CONC){
            if(C(f2)){
                f.concatenarAFN(f2);//Accion semantica
                if(Tp(f))
                    return true;
            }
            return false;
        }
        lexic.regresarToken();
        return true;
    }
    
    public Boolean C(AFN f){
        if(F(f))
            if(Cp(f))
                return true;
        return false;
        
    }
    
    public Boolean Cp(AFN f){
        int token = lexic.yylex();
        switch(token){
            case CERR_POS:
                f.cerraduraTransitiva();
                if(Cp(f))
                    return true;
                return false;
                //break;
            case CERR_KLEEN:
                f.cerraduradeKleene();
                if(Cp(f))
                    return true;
                return false;
            case OPC:
                f.cerraduraOpcional();
                if(Cp(f))
                    return true;
                return false;
            default:
               break; 
        }
        lexic.regresarToken();
        return true;
    }
    
    public Boolean F(AFN f){
        int token1, token2, token3;
        token1 = lexic.yylex();
        String lexema1, lexema2;
        switch(token1){
            case PAR_IZQ:
                if(E(f)){
                    token1 = lexic.yylex();
                    if(token1==PAR_DER)
                        return true;
                }
                return false;
                
            case CORCH_IZQ:
                token1 = lexic.yylex();
                if(token1==SIMB){
                    token2 = lexic.yylex();
                    if(token2 == GUION){
                        lexema1 = lexic.getYyText();
                        token2 = lexic.yylex();                        
                        if(token2 == SIMB){
                            lexema2 = lexic.getYyText();
                            token3 = lexic.yylex();
                            if(token3 == CORCH_DER){
                                //Accion semantica
                                Character Ci = lexema1.charAt(0);
                                Character Cf = lexema2.charAt(0);
                                f.crearBasico(Ci, Cf, contadorId);
                                contadorId++;
                                return true;
                            }    
                        }
                    }
                }
                return false;
            case SIMB:
                Character Ci = lexic.getYyText().charAt(0);
                f.crearBasico(Ci, contadorId);
                return true;
        }
        return false;
    }
}
