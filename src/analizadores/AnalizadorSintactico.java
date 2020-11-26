/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadores;

import clases.*;
import java.util.ArrayList;
/**
 *
 * @author J.PEREZ
 */
public class AnalizadorSintactico {
    
    /*
      PRUEBA PARA LA GRAMATICA
       E->EorT | T
       T->TC | C
       C-> C+ | C* | C? | F
       F-> (E) | s
      
      Gramatica sin recursion
       E -> TE'
       E' -> orTE' | Epsilon
       T -> CT'
       T' -> CT' | Epsilon
       C -> FC'
       C' -> +C' | *C' | ?C' | Epsilon
       F -> (E) | s                                      */
    
    public final static int UNION =10; // | union
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
    
    private AnalizadorLexico lex;
    private ArrayList<ArrayList<Integer>> TablaAutomataER;
    
    
    //SETEAR LA TABLA 
    //SETEAR EL NAALIZADOR LEXICO
    // SETEAR LA CADENA DE ENTRADA 
    
    AnalizadorSintactico(){
    
    
    }
    

    //Funciones
    //E->TE'
    public Boolean E(AFN f){
        
        if ( T(f))
            if(Ep(f))
                return true;
        return false;
        
    }
    //E' -> orTE' | Epsilon
    public Boolean Ep(AFN f){
        int tok = lex.yylex();
        if( tok== AnalizadorSintactico.UNION ){
            if( T(  f )  ){
                
                if( Ep(f) ){
                    return true;
                }
            }
            return false;
        }
        lex.regresarToken();
        return true;
    }
    
    
    //T -> CT'
    public Boolean T(AFN f){
        if ( C(f))
            if(Tp(f))
                return true;
        return false;
    }
    
    
    //T' -> &CT' | Epsilon
    public Boolean Tp(AFN f){
       AFN f2 = new AFN();
       int tok =  lex.yylex();
       if(tok == AnalizadorSintactico.CONC ){ //&
           if( C( f2) ){
               if(Tp( f ) )
                    return true;
           } 
           return false;
       } 
       //epsilon
       lex.regresarToken();
       return true;
    }
    
    public Boolean C(AFN f){
        return true;
    }
    
    public Boolean Cp(AFN f){
        return true;
    }
    
    public Boolean F(AFN f){
        return true;
    }
}
