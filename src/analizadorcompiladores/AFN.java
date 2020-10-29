/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author J.PEREZ
 */
public class AFN {
    
    //ATRIBUTOS
    private Estado EdoInicial;
    private char [] Alfabeto;
    private ArrayList<Estado> EdosAceptacion;     
    private ArrayList<Estado> EstadosAFN;
    private int IdAFN;
    
    //constantes'ϵ'ñ
    static final char epsilon='ϵ';
    
    //METODOS
    public AFN crearBasico(char c){
        
        AFN afn = new AFN();
        return afn;
    }
    
    public AFN unirAFN(AFN afn2){
        
        AFN afn = new AFN();
        return afn;
    }
    
    public AFN concatenarAFN(AFN afn2){
        
        AFN afn = new AFN();
        return afn;
    }
    
    public void cerrTrans(){
    }
    
    public void cerrKleen(){
    }
    
    //OPERACIONES   
    ArrayList<Estado> cerraduraEpsilon(Estado e){        
        Stack<Estado> s = new Stack();
        ArrayList<Estado>  R= new ArrayList<Estado>();
        Estado p;
        s.push(e);
        while( !s.empty() ){
            p= s.pop();
            R.add(p);
            for(Transicion trans: p.Transiciones){
                if( trans.getSimbolo() == epsilon  ){
                   if(! R.contains(p)  )
                       s.push(p);
                }
            }
        }
        return R;
    }
    
    ArrayList <Estado> cerraduraEpsilon(ArrayList<Estado> c){
        ArrayList<Estado > R = new ArrayList<Estado>();
        for(Estado e:c){
            R.addAll( cerraduraEpsilon(e) );
        }
        return R;
    }
    
    ArrayList<Estado> mover( Estado e, char c){
        ArrayList<Estado> R = new ArrayList<Estado>();
        for(Transicion t: e.Transiciones){
            if(t.getSimbolo()== c){
                R.addAll(t.getEdosDestino());   //Repeticiones de mismos estados
            }
        }    
        return R;
    }
    
    ArrayList<Estado> mover( ArrayList<Estado> E, char c){
        ArrayList<Estado> R = new ArrayList<Estado>();
        for(Estado e:E){
            R.addAll(mover(e, c));//Podrian existir repeticiones de mismos estados
        }
        return R;
    }    
    
    ArrayList<Estado> irA(ArrayList<Estado> E, char c){
         ArrayList<Estado> R = new ArrayList<Estado>();         
         R = cerraduraEpsilon(mover(E,c));
         return R;        
    }
    
    
    //Verifica si el conjunto de estados Cn ya esta en algun subconjunto 
    //de los subconjuntos de R, y regresa el identificador del subconjunto
    int contiene(ArrayList<Subconjunto> R, ArrayList<Estado> Cn){
        
        for(Subconjunto S: R){            
            ArrayList C = S.getEstados();
            //IMPLEMENTAR METODO QUE COMPARE Cn y C
            
            
            
            
            
            
        }        
        return -1;//Si no se encuentran coincidencias
    }
    
    //Metodos
    ArrayList<Subconjunto> construirSubconjuntos(){
        ArrayList<Subconjunto> R = new ArrayList<Subconjunto>();
        Subconjunto S = new Subconjunto();
        
        S.setId(0);
        S.setMarca(false);//No se ha analizado
        S.agregarEstados(cerraduraEpsilon(EdoInicial));//Agregamos los estados      
        
        R.add(S);
        
        //**REVISAR SI NO TERMINA EN LA PRIMERA ITERACION        
            for(Subconjunto Sn: R){                
                if(Sn.getMarca() == false){//Si NO ha sido analizado
                    for(char c: Alfabeto){
                        
                        ArrayList<Estado> Cn = irA(Sn.getEstados(), c);                    
                        
                        //Si R NO contiene a un Subconjunto con los mismos estados
                        if(contiene(R, Cn) == -1){                          
                            Subconjunto Snuevo = new Subconjunto();
                            Snuevo.setId(R.size()-1);//ID igual al tamano del arreglo R de subconjuntos -1
                            Snuevo.setMarca(false);
                            
                            R.add(Snuevo);
                        }
                    }
                    Sn.setMarca(true);//Se marca que ya se analizo.
                }            
            }            
   
        return R;        
    }
    
    AFD convertirAFN(){
        AFD afd = new AFD();
        ArrayList<Subconjunto> R = construirSubconjuntos();//Obtener subconjuntos.     
        
        
        //Crear ESTADO INICIAL
        Estado E = new Estado();
        E.setEdoInicial(true);
        E.setIdentificador(R.get(0).getId());//Establecer el estado inicial tomando el primer Subconjunto
        
        
        Subconjunto S = R.get(0);
        
        //Obtener TRANSICIONES
        for(char c: Alfabeto){
             ArrayList<Estado> Cn = new ArrayList<Estado>();                
             Cn = irA(R.get(0).getEstados(),c);
             
             if(Cn.size()>0){
                 Estado En = new Estado();
                 int IdDestino = contiene(R, Cn);
                 
                 En.setIdentificador(IdDestino);
                 
                 E.setTransicion(En, c);
             }
        }
        
        for(Subconjunto Si: R){
            
            for(char c:Alfabeto){
                
                ArrayList<Estado> Cn = new ArrayList<Estado>();                
                Cn = irA(Si.getEstados(),c); 
                
                if(!Cn.isEmpty()){//Si no es vacio
                    
                    //Revisamos si el conjunto ya esta en 
//                    int Destino = R.indexOf(N);
//                    E.setTransicion();
                }
            }
        }
        
        
        
        
        
        
        
        return afd;
    }
    
    Boolean analizarCadena(String s){
        ArrayList<Estado> E = new ArrayList<Estado>();  
        E = cerraduraEpsilon(EdoInicial);
        
        //FALTA
        
        return true;
    }
    
    Estado getEstado(int id){
        return EstadosAFN.get(id);
    }
    
}


