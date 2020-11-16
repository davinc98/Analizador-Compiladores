/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorcompiladores;

import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 */
public class Estado implements Comparable<Estado>{
    private int Identificador = -1;
    ArrayList<Transicion> Transiciones = new ArrayList();
    Boolean EdoInicial = false;
    Boolean EdoFinal = false;
    int Token = 0;
    Boolean Marca = false; //Ayudara para evitar ciclos infinitos
    
    public Estado() {}
    
    @Override
    public int compareTo(Estado o) {
            if (Identificador < o.Identificador) {
                return -1;
            }
            if (Identificador > o.Identificador) {
                return 1;
            }
            return 0;
    }

    public int getIdentificador() {
        return Identificador;
    }

    public void setIdentificador(int Identificador) {
        this.Identificador = Identificador;
    }

    public ArrayList<Transicion> getTransiciones() {
        return Transiciones;
    }

    public void setTransiciones(ArrayList<Transicion> Transiciones) {
        this.Transiciones = Transiciones;
    }
    
   


    public Boolean isEdoInicial() {
        return EdoInicial;
    }

    public void setEdoInicial(Boolean EdoInicial) {
        this.EdoInicial = EdoInicial;
    }

    public Boolean isEdoFinal() {
        return EdoFinal;
    }

    public void setEdoFinal(Boolean EdoFinal) {
        this.EdoFinal = EdoFinal;
    }

    public int getToken() {
        return Token;
    }

    public void setToken(int Token) {
        this.Token = Token;
    }
    
    
    //Funcion para crear una transicion de un solo caracter
    public void setTransicion(Estado e, char c){
        int C = (int)c;
        
        for(Transicion t: this.Transiciones){
            //Recuperar el rango de caracteres de cada transicion
            int CI = (int)t.getSimboloI();
            int CF = (int)t.getSimboloF();
            
            if(CI == CF){//La transicion solo es con un caracter
                if(C == CI){
                    t.addEdoDestino(e);
                    return;
                }
            }
        }
        
        //Si no encontro una transicion con el simbolo c entonces,
        Transicion t = new Transicion();
        t.setSimboloI(c);
        t.setSimboloF(c);
        t.addEdoDestino(e);
        this.Transiciones.add(t);
    }
    
    //Funcion para crear una transicion un intervalo caracter
    //Recibe el intervalo de caracteres y el estado destino
    public void setTransicion(Estado e, char c1, char c2){
        int C1 = (int)c1;
        int C2 = (int)c2;
        
        for(Transicion t: this.Transiciones){
            //Recuperar el rango de caracteres de cada transicion
            int CI = (int)t.getSimboloI();
            int CF = (int)t.getSimboloF();
            
            if(CI != CF){//La transicion es un intervalo
                if(C1==CI && C2==CF){
                    t.addEdoDestino(e);
                    return;
                }
            }
        }
        
        //Si no encontro una transicion con el simbolo c entonces,
        Transicion t = new Transicion();
        t.setSimboloI(c1);
        t.setSimboloF(c2);
        t.addEdoDestino(e);
        this.Transiciones.add(t);
    }
    
   
    //Devuleve la primera transicion del estado
    public Transicion getTransicion(){
        return Transiciones.get(0);
    }
    
}
