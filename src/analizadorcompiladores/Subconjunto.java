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
 * Sirve para agregar los nuevos conjusntos que se obtienen
 * al aplicar IrA(S, c)
 * La MARCA nos permitira saber si ya fue analizado para ver transiciones
 * y el ID para enierar los subconjuntos que salen.
 */
public class Subconjunto {
    
    private ArrayList<Estado> Estados;//Estado resultantes al aplicar IrA
    private int id;//ID del subconjunto
    private boolean marca;//True si ya fue utilizado para generar 
                            //otros posibles subconjuntos.

    public ArrayList<Estado> getEstados() {
        return Estados;
    }

    public void agregarEstados(ArrayList<Estado> Estados) {
        this.Estados.addAll(Estados);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getMarca() {
        return marca;
    }

    public void setMarca(boolean marca) {
        this.marca = marca;
    }
    
    
    
    
}
