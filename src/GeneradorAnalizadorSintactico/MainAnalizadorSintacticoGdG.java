/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneradorAnalizadorSintactico;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 */
public class MainAnalizadorSintacticoGdG {

    public static ArrayList<Regla> ArrReglas = new ArrayList();
    public static int numReglas = 0;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        AnalizadorSintacticoGdG analizador = new AnalizadorSintacticoGdG();
        
        ArrReglas = analizador.generarListaDeReglas("ExpresionesRegularesParaGdG4.txt");
        numReglas = ArrReglas.size();
        
        ArrayList<String> res;
        
        //res = First(ArrReglas.get(2).apNodo);
        res = Follow("F");
        
        System.out.print("\n\n\t{");
        for(String e: res)
            System.out.print(e+",");
        System.out.print("}\n");
        
    }
    
    
    public static ArrayList<String> First(Nodo l){
        
        ArrayList<String> R = new ArrayList();
        
        if(l.isTerminal() || l.getStrsimb().equals("Epsilon")){
            if(!l.getStrsimb().equals("")){
                R.add(l.getStrsimb());
                //System.out.println("Agregado Simbolo: "+l.getStrsimb());
            }
            return R;
        }
        
        for(int i=0;i<numReglas;i++){
            if(ArrReglas.get(i).getStrSimb().equals(l.getStrsimb())){
                if(l.getStrsimb().equals(ArrReglas.get(i).getApNodo().getStrsimb()))
                    continue;
                UnirElementos(R, First(ArrReglas.get(i).getApNodo()));
            }
        }
        
        if(R.contains("Epsilon") && !(l.getApNodo().getStrsimb().equals(""))){
            R.remove("Epsilon"); 
            UnirElementos(R, First(l.getApNodo()));
            return R;
        }

        return R;
    }
    
    public static ArrayList<String> Follow(String A){
        ArrayList<String> R = new ArrayList();
        Nodo nodo = new Nodo();
        
        //Si A es simbolo inicial de la Gramatica
        if(A.equals(ArrReglas.get(0).getStrSimb()))
            R.add("$");
        
        for(int i=0;i<numReglas;i++){
            nodo = BuscaSimbolo(A, i);
            
            if(nodo.getStrsimb().equals(""))
                continue;
            
            //Si el nodo n ya no apunta a otro
            if(nodo.getApNodo().getStrsimb().equals("")){
                if(A.equals(ArrReglas.get(i).getStrSimb()))
                    continue;
                UnirElementos(R, Follow(ArrReglas.get(i).getStrSimb()));
            }else{//Hay mas nodos
                ArrayList<String> aux = new ArrayList();
                aux = First(nodo.getApNodo());
                
                if(aux.contains("Epsilon")){
                    aux.remove("Epsilon");
                    UnirElementos(R, aux);
                    
                    if(!A.equals(ArrReglas.get(i).getStrSimb())){
                        UnirElementos(R, Follow(ArrReglas.get(i).getStrSimb()));
                    }
                }else{
                    UnirElementos(R, aux);
                }
            }
        }
        return R;
    
    }
    
    
    //Agrega los elementos de S que no estan en R, se agregan a R
    public static void UnirElementos(ArrayList<String> R, ArrayList<String> S){
        R.removeAll(S);
        R.addAll(S);
    }
    
    
    //Busca un simbolo en la lista de reglas y devuelve su referencia
    public static Nodo BuscaSimbolo(String A, int i){
        Regla R = ArrReglas.get(i);
        Nodo n;
        n = R.getApNodo();
        while(!n.getStrsimb().equals("")){
            if(n.getStrsimb().equals(A))
                return n;
            n = n.getApNodo();
        }
        return n;
    }

}
