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
    private int Identificador;
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
    
    
    public void setTransicion(Estado e, char c){
        
        for(Transicion t: this.Transiciones){
            if(t.getSimbolo()  ==c){
                t.addEdoDestino(e);
                return;
            }
        }
        
        //Si no encontro una transicion con el simbolo c entonces,
        Transicion t = new Transicion();
        t.setSimbolo(c);
        t.addEdoDestino(e);
        this.Transiciones.add(t);
    }
    
    public Transicion getTransicion(){
        return Transiciones.get(0);
    }
    
}
