/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadores;

/**
 *
 * @author J.PEREZ
 */
public class AnalizadorSintactico {
    
    /*
      PRUEBA PARA LA GRAMATICA
       E->EorT | T
       T->TC | C
       C-> C+ | C* | C? | F
       F-> (E) | s
      
      Gramatica sin recursion
       E -> TE'
       E' -> orTE' | Epsilon
       T -> CT'
       T' -> CT' | Epsilon
       C -> FC'
       C' -> +C' | *C' | ?C' | Epsilon
       F -> (E) | s                                      */
    
    
    

    //Funciones
    public Boolean E(){
        return true;
    }
    
    public Boolean Ep(){
        return true;
    }
    
    public Boolean T(){
        return true;
    }
    
    public Boolean Tp(){
        return true;
    }
    
    public Boolean C(){
        return true;
    }
    
    public Boolean Cp(){
        return true;
    }
    
    public Boolean F(){
        return true;
    }
}
