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
public class Transicion {
    private char SimboloI;//Simbolo Inicial 
    private char SimboloF;//Simolo Final
    private ArrayList<Estado> EdosDestino;
    
    public Transicion(){
        this.EdosDestino =  new ArrayList();
    }
    

    public char getSimboloI() {
        return SimboloI;
    }
    
    public char getSimboloF() {
        return SimboloF;
    }

    public void setSimboloI(char Simbolo) {
        this.SimboloI = Simbolo;
    }
    public void setSimboloF(char Simbolo) {
        this.SimboloF = Simbolo;
    }

    public ArrayList<Estado> getEdosDestino() {
        return EdosDestino;
    }

    public void setEdosDestino(ArrayList<Estado> EdosDestino) {
        this.EdosDestino = EdosDestino;
    }
    
    public void addEdoDestino(Estado EdoDestino) {
        this.EdosDestino.add(EdoDestino);
    }
    
    public void setEdoDestino(Estado e) {
        this.EdosDestino.add(e);
    }
    
    
}
