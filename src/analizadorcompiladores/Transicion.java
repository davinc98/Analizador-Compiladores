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
public class Transicion {
    private char Simbolo;
    private ArrayList<Estado> EdosDestino;

    public char getSimbolo() {
        return Simbolo;
    }

    public void setSimbolo(char Simbolo) {
        this.Simbolo = Simbolo;
    }

    public ArrayList<Estado> getEdosDestino() {
        return EdosDestino;
    }

    public void setEdosDestino(ArrayList<Estado> EdosDestino) {
        this.EdosDestino = EdosDestino;
    }
    
    public void setEdoDestino(Estado e) {
        this.EdosDestino.add(e);
    }
    
    
}
