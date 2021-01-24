/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneradorAnalizadorSintactico;

import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 */
public class Regla {
    String StrSimb = "";
    Boolean terminal = false;
    Nodo apNodo;

    public Nodo getApNodo() {
        return apNodo;
    }

    public void setApNodo(Nodo apNodo) {
        this.apNodo = apNodo;
    }

    public String getStrSimb() {
        return StrSimb;
    }

    public void setStrSimb(String StrSimb) {
        this.StrSimb = StrSimb;
    }

    public Boolean getTerminal() {
        return terminal;
    }

    public void setTerminal(Boolean terminal) {
        this.terminal = terminal;
    }

        
}
