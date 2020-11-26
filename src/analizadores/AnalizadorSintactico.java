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
    //C -> FC'
    public Boolean C(AFN f){
         if ( F(f))
            if(Cp(f))
                return true;
        return false;
    }
    
    
    
    //  C' -> +C' | *C' | ?C' | Epsilon
    public Boolean Cp(AFN f){
        int tok = lex.yylex();
       
        switch(tok)
                {
                   // declaración case
                   // los valores deben ser del mismo tipo de la expresión
                   case AnalizadorSintactico.CERR_POS : //+
                       f.cerraduraTransitiva(); 
                       if( Cp(f))
                           return true;
                       return false;
 
                   case AnalizadorSintactico.CERR_KLEEN : //*
                      f.cerraduradeKleene();
                       if( Cp(f))
                           return true;
                       return false;
                       
                    case AnalizadorSintactico.OPC : // ?
                        f.cerraduraOpcional();
                         if( Cp(f))
                           return true;
                       return false;
                        
                        
                   default ://epsilon 
                       lex.regresarToken();
                       return true;
                }
    }
    
    
    
    // F -> (E) | s
    public Boolean F(AFN f){
        String lexema1, lexema2;
        int tok = lex.yylex();
        
        switch(tok)
        {
           case AnalizadorSintactico.PAR_IZQ :// (
               if( E(f)){
                   tok= lex.yylex();
                   if(tok== AnalizadorSintactico.PAR_DER ) //  )
                       return true;
               
               }

           case AnalizadorSintactico.CORCH_IZQ :
              // Declaraciones
              break; // break es opcional

           // Podemos tener cualquier número de declaraciones de casos o case
           // debajo se encuentra la declaración predeterminada, que se usa cuando ninguno de los casos es verdadero.
           // No se necesita descanso en el case default
           default : 
              // Declaraciones
        }
                
        
        
    
    }
}
