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
public class AFD {//Faltam metodos y atributos
    
    private Estado EdoInicial;
    private ArrayList<Character> Alfabeto = new ArrayList();
    ArrayList<Estado> Estados = new ArrayList<Estado>();
    
    
    AFD(){}

    public Estado getEdoInicial() {
        return EdoInicial;
    }

    public void setEdoInicial(Estado EdoInicial) {
        this.EdoInicial = EdoInicial;
    }

    public ArrayList<Character> getAlfabeto() {
        return Alfabeto;
    }

    public void setAlfabeto(ArrayList<Character> Alfabeto) {
        this.Alfabeto = Alfabeto;
    }

    public ArrayList<Estado> getEstados() {
        return Estados;
    }

    public void setEstados(ArrayList<Estado> Estados) {
        this.Estados = Estados;
    }
    
}

