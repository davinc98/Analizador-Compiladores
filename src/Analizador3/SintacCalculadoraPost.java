/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador3;

import Analizador2.*;
import clases.Numero;
import Analizador1.*;
import analizadores.AnalizadorLexico;
import clases.Cadena;
import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 * 
 * ANALIZADOR SINTACTICO PARA LA GRAMATICA SIN RECURSION IZQ
 * PARA UNA CALCULADORA QUE CONVIERTE A POSTFIJO UNA CADENA
 * 
 *      E -> TE'
 *      E' -> +TE' | -TE' | Epsilon
 *      T -> FT'
 *      T' -> *FT' | /FT' | Epsilon
 *      F -> (E) | num 
 * 
 *      
 */
public class SintacCalculadoraPost {
    
    /*CLASES LEXICAS PARA LA GRAMATICA SIN RECURSION IZQ*/
    int MAS = 10;
    int MENOS = 20;
    int PROD = 30;
    int DIV = 40;
    int PAR_IZQ = 50;
    int PAR_DER = 60;
    int NUM = 70;
    
    AnalizadorLexico lexic;
    
    public SintacCalculadoraPost(ArrayList<ArrayList<Integer>> tablaAutomata, String cadena){
        this.lexic = new AnalizadorLexico(tablaAutomata, cadena);
    }
    
    public String Ini(){
        int token;
        Cadena V = new Cadena();
        if(E(V)){
            token  = lexic.yylex();
            if(token == -10)
                return V.getCadena();
        }
        return V.getCadena();
    }
     
    //Funciones
    public Boolean E(Cadena V){
        if(T(V))
            if(Ep(V))
                return true;          
        return false;
    }
    
    public Boolean Ep(Cadena V){
        Cadena W = new Cadena();
        int token = lexic.yylex();
        if(token == MAS || token==MENOS){
            if(T(W)){
                if(token==MAS)
                    V.setCadena(V.getCadena()+" "+W.getCadena()+"+");
                else
                    V.setCadena(V.getCadena()+" "+W.getCadena()+"-");
                if(Ep(V))
                    return true;
            }
            return false;
        }
        //Considerar a epsilon
        lexic.regresarToken();
        return true;
    }
    
    public Boolean T(Cadena V){
        if(F(V))
            if(Tp(V))
                return true;
        return false;
    }
    
    public Boolean Tp(Cadena V){
        Cadena W = new Cadena();
        int token = lexic.yylex();
        if(token == PROD || token == DIV){//token==PROD || token==DIV
            if(F(W)){
                if(token==PROD)
                    V.setCadena(V.getCadena()+" "+W.getCadena()+"*");
                else
                    V.setCadena(V.getCadena()+" "+W.getCadena()+"/");
                if(Tp(V))
                    return true;
            }
            return false;
        }
        //Considerar a Epsilon
        lexic.regresarToken();
        return true;
    }
        
    public Boolean F(Cadena V){
        int token = lexic.yylex();
        switch(token){
            case 50: //token==PAR_IZQ
                if(E(V)){
                    token=lexic.yylex();
                    if(token==60)//token==PAR_DER
                        return true;
                }
                return false;  
            case 70: //token==NUM
                V.setCadena(lexic.getYyText());
                return true;
        }
        return false;
    }
}
