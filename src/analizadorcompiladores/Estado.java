/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 */
public class Estado {
    
    private int Identificador;
    ArrayList<Transicion> Transiciones;
    Boolean EdoInicial;
    Boolean EdoFinal;
    int Token;

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
        
        for(Transicion t: Transiciones){
            if(t.getSimbolo()==c){
                t.setEdoDestino(e);
                return;
            }
        }
        
        //Si no encontro una transicion con el simbolo c entonces,
        Transicion t = new Transicion();
        t.setSimbolo(c);
        t.setEdoDestino(e);
        this.Transiciones.add(t);
    }
    
    public void setTransiciones(ArrayList<Estado> e, char c){
        //Verificar primero si ya existe una transicion con el simbolo c
        Transicion t = new Transicion();
        t.setSimbolo(c);
        for(Estado E: e){
             t.setEdoDestino(E);
        }        
        this.Transiciones.add(t);
    }
    
    
}
