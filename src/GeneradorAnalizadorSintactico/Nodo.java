/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneradorAnalizadorSintactico;

/**
 *
 * @author J.PEREZ
 */
public class Nodo {
    private String strsimb="";
    private boolean terminal = true;
    Nodo apNodo = null;
    
    public void Nodo(String simb){
        strsimb = simb;
    }

    public String getStrsimb() {
        return strsimb;
    }

    public void setStrsimb(String strsimb) {
        this.strsimb = strsimb;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public Nodo getApNodo() {
        return apNodo;
    }

    public void setApNodo(Nodo apNodo) {
        this.apNodo = apNodo;
    }
    
}
