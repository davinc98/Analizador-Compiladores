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
 * @author Aacini++
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
    private int token=0;
    
    
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
               return false;

           case AnalizadorSintactico.CORCH_IZQ :// [ 
               tok= lex.yylex();
               if(tok==AnalizadorSintactico.SIMB){ 
                   lexema1 = lex.getYyText();
                   tok= lex.yylex();
                   if(tok==AnalizadorSintactico.GUION){//-
                       tok= lex.yylex();
                       if(  tok== AnalizadorSintactico.SIMB ){ //SIMB
                           lexema2 = lex.getYyText();
                           tok= lex.yylex();
                           if(tok== AnalizadorSintactico.CORCH_DER){ // ]
                               char car1 =  lexema1.charAt(0);
                               char car2 =  lexema2.charAt(0);
                               f.crearBasico(car1, car2, this.token);  //crea el basico con interbalos       
                               return true;
                           }
                       }   
                   }   
               }
              return false;
             case AnalizadorSintactico.SIMB:// SIMB
                lexema1= lex.getYyText();
                char car1 =  lexema1.charAt(0);
                f.crearBasico(car1, this.token);  //crea el basico con interbalos       
                return true;                 
        }
        return false;  
    }
}
