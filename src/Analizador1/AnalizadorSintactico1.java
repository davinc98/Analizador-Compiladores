/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador1;

import analizadores.AnalizadorLexico;
import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 * 
 * ANALIZADOR SINTACTICO PARA LA GRAMATICA SIN RECURSION IZQ
 *      E -> TE'
 *      E' -> +TE' | -TE' | Epsilon
 *      T -> FT'
 *      T' -> *FT' | /FT' | Epsilon
 *      F -> (E) | num 
 * 
 *      
 */
public class AnalizadorSintactico1 {
    
    /*CLASES LEXICAS PARA LA GRAMATICA SIN RECURSION IZQ*/
    int MAS = 10;
    int MENOS = 20;
    int PROD = 30;
    int DIV = 40;
    int PAR_IZQ = 50;
    int PAR_DER = 60;
    int NUM = 70;
    
    AnalizadorLexico lexic;
    
    public AnalizadorSintactico1(ArrayList<ArrayList<Integer>> tablaAutomata, String cadena){
        this.lexic = new AnalizadorLexico(tablaAutomata, cadena);
    }
    
 
    
    //Funcio INICIAL
    public Boolean E(){
        if(T())
            if(Ep())
                return true;          
        return false;
    }
    
    public Boolean Ep(){
        int token = lexic.yylex();
        if(token == MAS || token==MENOS){
            if(T())
                if(Ep())
                    return true;
            return false;
        }
        //Considerar a epsilon
        lexic.regresarToken();
        return true;
    }
    
    public Boolean T(){
        if(F())
            if(Tp())
                return true;
        return false;
    }
    
    public Boolean Tp(){
        int token = lexic.yylex();
        if(token == PROD || token == DIV){//token==PROD || token==DIV
            if(F())
                if(Tp())
                    return true;
            return false;
        }
        //Considerar a Epsilon
        lexic.regresarToken();
        return true;
    }
        
    public Boolean F(){
        int token = lexic.yylex();
        switch(token){
            case 50: //token==PAR_IZQ
                if(E()){
                    token=lexic.yylex();
                    if(token==60)//token==PAR_DER
                        return true;
                }
                return false;  
            case 70: //token==NUM
                return true;
        }
        return false;
    }
}
