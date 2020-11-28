/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador2;

import clases.Numero;
import Analizador1.*;
import analizadores.AnalizadorLexico;
import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 * 
 * ANALIZADOR SINTACTICO PARA LA GRAMATICA SIN RECURSION IZQ
 * PARA UNA CALCULADORA
 * 
 *      E -> TE'
 *      E' -> +TE' | -TE' | Epsilon
 *      T -> FT'
 *      T' -> *FT' | /FT' | Epsilon
 *      F -> (E) | num 
 * 
 *      
 */
public class SintacCalculadora {
    
    /*CLASES LEXICAS PARA LA GRAMATICA SIN RECURSION IZQ*/
    int MAS = 10;
    int MENOS = 20;
    int PROD = 30;
    int DIV = 40;
    int PAR_IZQ = 50;
    int PAR_DER = 60;
    int NUM = 70;
    
    AnalizadorLexico lexic;
    
    public SintacCalculadora(ArrayList<ArrayList<Integer>> tablaAutomata, String cadena){
        this.lexic = new AnalizadorLexico(tablaAutomata, cadena);
    }
    
    public double Ini(){
        int token;
        Numero V = new Numero();
        if(E(V)){
            token  = lexic.yylex();
            if(token == -10)
                return V.getValor();
        }
        return V.getValor();
    }
     
    //Funciones
    public Boolean E(Numero V){
        if(T(V))
            if(Ep(V))
                return true;          
        return false;
    }
    
    public Boolean Ep(Numero V){
        Numero W = new Numero();
        int token = lexic.yylex();
        if(token == MAS || token==MENOS){
            if(T(W)){
                if(token==MAS)
                    V.setValor(V.getValor() + W.getValor());
                else
                    V.setValor(V.getValor() - W.getValor());
                if(Ep(V))
                    return true;
            }
            return false;
        }
        //Considerar a epsilon
        lexic.regresarToken();
        return true;
    }
    
    public Boolean T(Numero V){
        if(F(V))
            if(Tp(V))
                return true;
        return false;
    }
    
    public Boolean Tp(Numero V){
        Numero W = new Numero();
        int token = lexic.yylex();
        if(token == PROD || token == DIV){//token==PROD || token==DIV
            if(F(W)){
                if(token==PROD)
                    V.setValor(V.getValor() * W.getValor());
                else
                    V.setValor(V.getValor() / W.getValor());
                if(Tp(V))
                    return true;
            }
            return false;
        }
        //Considerar a Epsilon
        lexic.regresarToken();
        return true;
    }
        
    public Boolean F(Numero V){
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
                V.setValor(atof(lexic.getYyText()));
                return true;
        }
        return false;
    }
    
    
    
    public static double atof(String s) {
        int i, numMinuses = 0, numDots = 0;

        s = s.trim();
        for (i = 0; i < s.length()
                && (s.charAt(i) == '-' || s.charAt(i) == '.' || Character.isDigit(s.charAt(i))); i++)
            if (s.charAt(i) == '-')
                numMinuses++;
            else if (s.charAt(i) == '.')
                numDots++;

        if (i != 0 && numMinuses < 2 && numDots < 2)
            return Double.parseDouble(s.substring(0, i));

        return 0.0;
    }
}
