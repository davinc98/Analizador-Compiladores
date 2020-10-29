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
public class AFD {//Faltam metodos y atributos
    
    private Estado EdoInicial;
    private char[] Alfabeto;
    ArrayList<Estado> Estados;
    

    
    AFD(){}
    AFD(Estado inicial, char[] alfabeto){
        this.EdoInicial = inicial;
        this.Alfabeto = alfabeto;
    }

    public Estado getEdoInicial() {
        return EdoInicial;
    }

    public void setEdoInicial(Estado EdoInicial) {
        this.EdoInicial = EdoInicial;
    }

    public char[] getAlfabeto() {
        return Alfabeto;
    }

    public void setAlfabeto(char[] Alfabeto) {
        this.Alfabeto = Alfabeto;
    }

    public ArrayList<Estado> getEstados() {
        return Estados;
    }

    public void setEstados(ArrayList<Estado> Estados) {
        this.Estados = Estados;
    }
    
}
