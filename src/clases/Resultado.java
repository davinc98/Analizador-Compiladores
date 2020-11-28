/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author J.PEREZ
 * 
 * CLASE PARA RETORNAR EL RESULTADO DE EVALUAR UNA EXPRESION EN LA
 * CALCULADORA ASI COMO LA MISMA EXPRESION EN POSTFIJO
 */
public class Resultado {
    
    private double valor = 0;//Almacena el resultado de la evaluxacion
    private String cadena = "";//Almacena la expresion en POSTFIJO
    boolean valido = false;

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }
    
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }   
    
    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }    
}
