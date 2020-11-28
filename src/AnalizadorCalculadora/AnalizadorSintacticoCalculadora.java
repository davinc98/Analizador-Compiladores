/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorCalculadora;

import Analizador2.*;
import clases.Numero;
import Analizador1.*;
import analizadores.AnalizadorLexico;
import clases.Cadena;
import clases.Resultado;
import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 * 
 * ANALIZADOR SINTACTICO PARA UNA CALCULADORA      
 */


public class AnalizadorSintacticoCalculadora {
    
    /*CLASES LEXICAS PARA LA GRAMATICA CALCULADORA*/
    static final int MAS = 10;
    static final int MENOS = 20;
    static final int PROD = 30;
    static final int DIV = 40;
    static final int POT = 50;
    static final int PAR_IZQ = 60;
    static final int PAR_DER = 70;
    static final int SIN = 80;
    static final int COS = 90;
    static final int TAN = 100;
    static final int LN = 110;
    static final int LOG = 120;
    static final int NUM = 130;
    
    AnalizadorLexico lexic;
    
    public AnalizadorSintacticoCalculadora(ArrayList<ArrayList<Integer>> tablaAutomata, String cadena){
        this.lexic = new AnalizadorLexico(tablaAutomata, cadena);
    }
    
    
    public Resultado Iniciar(){
        int token;
        Resultado V = new Resultado();
        if(E(V)){
            token  = lexic.yylex();
            if(token == -10){
                V.setValido(true);
                return V;
            }
        }
        V.setValido(false);
        return V;
    }
     
    //Funciones
    public Boolean E(Resultado V){
        if(T(V))
            if(Ep(V)){
                V.setValido(true);
                return true;
            }
        return false;
    }
    
    public Boolean Ep(Resultado V){
        Resultado W = new Resultado();
        int token = lexic.yylex();
        if(token == MAS || token==MENOS){
            if(T(W)){
                //Accion Semantica
                if(token==MAS){
                    V.setValor(V.getValor() + W.getValor());
                    V.setCadena(V.getCadena()+" "+W.getCadena()+" +");
                }else{
                    V.setValor(V.getValor() - W.getValor());
                    V.setCadena(V.getCadena()+" "+W.getCadena()+" -");
                }
                if(Ep(V))
                    return true;
            }
            return false;
        }
        //Considerar a epsilon
        lexic.regresarToken();
        return true;
    }
    
    public Boolean T(Resultado V){
        if(P(V))
            if(Tp(V))
                return true;
        return false;
    }
    
    public Boolean Tp(Resultado V){
        Resultado W = new Resultado();
        int token = lexic.yylex();
        
        if(token == PROD || token == DIV){//token==PROD || token==DIV
            if(P(W)){
                //Accion Semantica
                if(token==PROD){
                    V.setValor(V.getValor() * W.getValor());
                    V.setCadena(V.getCadena()+" "+W.getCadena()+" *");
                }else{
                    V.setValor(V.getValor() / W.getValor());
                    V.setCadena(V.getCadena()+" "+W.getCadena()+" /");
                }
                if(Tp(V))
                    return true;
            }
            return false;
        }
        //Considerar a Epsilon
        lexic.regresarToken();
        return true;
    }
    
    public Boolean P(Resultado V){
        if(F(V))
            if(Pp(V))
                return true;
        return false;
    }
    
    public Boolean Pp(Resultado V){
        Resultado W = new Resultado();
        int token = lexic.yylex();
        if(token==POT){
            if(F(W)){
                //Accion semantica
                V.setValor(Math.pow(V.getValor(), W.getValor()));
                V.setCadena(V.getCadena()+" "+W.getCadena()+" ^");
                if(Pp(V))
                    return true;
            }
            return false;
        }
        lexic.regresarToken();        
        return true;    
    }
        
    public Boolean F(Resultado V){
        int token = lexic.yylex();
        switch(token){
            
            case PAR_IZQ: //token==PAR_IZQ
                if(E(V)){
                    token=lexic.yylex();
                    if(token==PAR_DER)//token==PAR_DER
                        return true;
                }
                return false;  
            
            case SIN:
                token = lexic.yylex();
                if(token==PAR_IZQ){
                    if(E(V)){
                        token=lexic.yylex();
                        if(token==PAR_DER){//token==PAR_DER
                            V.setCadena(V.getCadena()+" SIN");
                            V.setValor(Math.sin(V.getValor()));
                            return true;
                        }
                    }
                }
                return false;
                
            case COS:
                token = lexic.yylex();
                if(token==PAR_IZQ){
                    if(E(V)){
                        token=lexic.yylex();
                        if(token==PAR_DER){//token==PAR_DER
                            V.setCadena(V.getCadena()+" COS");
                            V.setValor(Math.cos(V.getValor()));
                            return true;
                        }
                    }
                }
                return false;
            case TAN:
                token = lexic.yylex();
                if(token==PAR_IZQ){
                    if(E(V)){
                        token=lexic.yylex();
                        if(token==PAR_DER){//token==PAR_DER
                            V.setCadena(V.getCadena()+" TAN");
                            V.setValor(Math.tan(V.getValor()));
                            return true;
                        }
                    }
                }
                return false;
            case LN:
                token = lexic.yylex();
                if(token==PAR_IZQ){
                    if(E(V)){
                        token=lexic.yylex();
                        if(token==PAR_DER){//token==PAR_DER
                            V.setCadena(V.getCadena()+" LN");
                            V.setValor(Math.log(V.getValor()));
                            return true;
                        }
                    }
                }
                return false;
            case LOG:
                token = lexic.yylex();
                if(token==PAR_IZQ){
                    if(E(V)){
                        token=lexic.yylex();
                        if(token==PAR_DER){//token==PAR_DER
                            V.setCadena(V.getCadena()+" LOG");
                            V.setValor(Math.log10(V.getValor()));
                            return true;
                        }
                    }
                }
                return false;
            
            case NUM: //token==NUM
                V.setValor(atof(lexic.getYyText()));
                V.setCadena(lexic.getYyText());
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
