/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorcompiladores;

import static analizadorcompiladores.AFN.epsilon;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

/**
 *
 * @author J.PEREZ
 */
public class AFD {//Faltam metodos y atributos
    
    private Estado EdoInicial;
    private ArrayList<Character> Alfabeto = new ArrayList();
    ArrayList<Estado> Estados = new ArrayList<Estado>();
    
    
    AFD(){}
    
    public boolean generarArchivoTabla(String nombretabla){
        String nombre = "AFD-"+nombretabla+".txt";
        File f = new File(nombre);
        try{
  
            PrintWriter pw = new PrintWriter(f);
            f.createNewFile();
            
            //Colocer encabezado de archivo con el alfabeto
            String encabezado = "";
            for(int i=0;i<Alfabeto.size();i++) {
                char c = Alfabeto.get(i);
                encabezado = encabezado+" "+ c;
            }            
            pw.println("#"+encabezado+" Token");
            
            String subcadena;
            ArrayList<Estado> Si = new ArrayList<Estado>(); 
            for(int i=0; i<Estados.size();i++){//Recorrer cada estado
                subcadena = ""+i;
                System.out.println("Estado a analizar: "+Estados.get(i).getIdentificador());
                for(char c:  Alfabeto){
                    Si = mover(Estados.get(i), c);
                    if(Si.size()==0){//Si el conjunto esta vacio
                        subcadena = subcadena + " -1";
                    }else{
                        System.out.println("--->"+c+"="+Si.get(0).getIdentificador());
                        //Vamos a suponer que SIEMPRE regresara solo un estado
                        subcadena = subcadena + " "+Si.get(0).getIdentificador();
                    }
                }
                subcadena = subcadena+" "+Estados.get(i).getToken();
                pw.println(subcadena);
            }
            
            pw.close();
        }catch(IOException e){
            System.out.println("Ocurrio un error al generar la tabla.");
            return false;
        }
        System.out.println("Archivo Generado.");
        return true;
    }
    
    
    //OPERACIONES   
    
    ArrayList<Estado> mover( Estado e, char c){
        ArrayList<Estado> R = new ArrayList<Estado>();
        
//        System.out.println("Transiciones desde: "+e.getIdentificador());
        for(Transicion t: e.Transiciones){
            
            //Recuperar el intervalo de caracteres
            int C1 = (int)t.getSimboloI();
            int C2 = (int)t.getSimboloF();
            
            int C = (int)c;
            
            if(C>=C1 && C<=C2){//Se compara si el caracter esta dentro del intervalo
                R.addAll(t.getEdosDestino());
//                System.out.println("Transicion: "+c+"->"+R.get(0).getIdentificador());
            }
        }    
        R = Ordenar(R);
        return R;
    }
       
    
    //Ordena un conjunto de estados de forma ascendente por Identificador
    private ArrayList<Estado> Ordenar( ArrayList<Estado> R){
        ArrayList<Estado> Ordenado = new ArrayList<Estado>(); 
        Estado auxiliar = new Estado();
        
        for(int i = 1; i < R.size(); i++)
        {
          for(int j = 0;j < R.size()-i;j++)
          {
            if(R.get(j).getIdentificador()> R.get(j+1).getIdentificador())
            {
              auxiliar = R.get(j);
              R.set(j, R.get(j+1));
              R.set(j+1, auxiliar);
            }   
          }
        }
        Ordenado = R;
        return Ordenado;
    }
    

    
    //Verifica si el conjunto de estados Cn ya esta en algun subconjunto 
    //de los subconjuntos de R, y regresa el identificador del subconjunto
    //True si el conjuto de estados ya ha sido agregado
    Boolean contiene(ArrayList<Subconjunto> R, ArrayList<Estado> Cn){
        
        Boolean agregado = false;
        
        for(Subconjunto S: R){            
            ArrayList<Estado> C = S.getEstados();            
            if(C.size() == Cn.size()){
                boolean equiv = true;
                for(int i=0;i<Cn.size();i++){
                    if(C.get(i).getIdentificador() != Cn.get(i).getIdentificador()){
                        equiv = false;
                        break;
                    }                    
                }
                if(equiv == true){
                    agregado = true;
                    break;
                }
            }    
        }        
        return agregado;//False si no hay coincidencias
    }

    //Regresa la tabla AFD en un arreglo bidimencional
    public ArrayList<ArrayList<Integer>> getArrayTabla() {
        ArrayList<ArrayList<Integer>> tablaAFD = new ArrayList<ArrayList<Integer>>();
        //La tabla tiene la sifuiente forma
        /*
        -El primer array list sera el alfabeto en su valor entero del ascii
        
        Encabezado:     L   D   .   ... T
        Est Ini 0       1   2   -1      6               0
        Est 1           7   8   -1      -1              10
        Est 2
        ...
        Est n           
        
        -El ultimo elemento de cada arraylist de un estado sera el valor del token
         */

        //El alfabeto se agrega a la tabla con su valor entero en ASCII
        ArrayList<Integer> Encabezado = new ArrayList<Integer>();
        for (Character c : Alfabeto) {
            Encabezado.add((int) c);
        }

        tablaAFD.add(Encabezado);

        ArrayList<Estado> Si = new ArrayList<Estado>();
        for (int i = 0; i < Estados.size(); i++) {//Recorrer cada estado
            ArrayList<Integer> Estadoi = new ArrayList<Integer>();

            System.out.println("Estado a analizar: " + Estados.get(i).getIdentificador());
            for (char c : Alfabeto) {
                Si = mover(Estados.get(i), c);
                if (Si.size() == 0) {//Si el conjunto esta vacio
                    Estadoi.add(-1);
                } else {
                    System.out.println("--->" + c + "=" + Si.get(0).getIdentificador());
                    //Vamos a suponer que SIEMPRE regresara solo un estado
                    Estadoi.add(Si.get(0).getIdentificador());
                }
            }
            Estadoi.add(Estados.get(i).getToken());

            //Agregar estado a la tablaAFD
            tablaAFD.add(Estadoi);
        }

        return tablaAFD;
    }
    
    
    

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

