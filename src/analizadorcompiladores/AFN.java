/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorcompiladores;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author J.PEREZ
 */

public class AFN implements Cloneable{  
    //Constante 'ϵ'
    static final char epsilon='ϵ';
    //ATRIBUTOS
    private Estado EdoInicial;
    private ArrayList<Character> Alfabeto = new ArrayList();
    private ArrayList<Estado> EdosAceptacion = new ArrayList();  
    private ArrayList<Estado> EstadosAFN = new ArrayList();
    private int IdAFN;
    
    public AFN(){}
    public AFN Duplicar(){
         AFN nuevo = new AFN();
         nuevo.setEdoInicial(EdoInicial);
         nuevo.setAlfabeto(Alfabeto);
         nuevo.setEstadosAFN(EstadosAFN);
         nuevo.setEdosAceptacion(EdosAceptacion);
         nuevo.setIdAFN(IdAFN);         
         return nuevo;
    }
    
    //METODOS 
    public void crearBasico(char c, int Id){
        this.IdAFN = Id;
        Alfabeto.add(c);
        
        Estado estado_ini = new Estado();
        estado_ini.setIdentificador(0);
        estado_ini.setEdoInicial(true);
        
        this.EdoInicial=estado_ini;
        
        Estado estado_fin = new Estado();
        estado_fin.setIdentificador(1);
        estado_fin.setEdoFinal(true);
        
        estado_ini.setTransicion(estado_fin, c);
        
        this.EstadosAFN.add(estado_ini);
        this.EstadosAFN.add(estado_fin);
        this.EdosAceptacion.add(estado_fin);
    }
    
    public void unirAFN(AFN afn2){
        //concatenar alfabetos
        afn2.Alfabeto.removeAll(this.Alfabeto);
        this.Alfabeto.addAll( afn2.Alfabeto);
        
        //Se crea un nuevo estado inicial
        Estado nuevo_edo_ini = new Estado();            
        //El nuevo estado inicial apuntam a los estados finales de ambos afn        
        nuevo_edo_ini.setTransicion(this.EdoInicial, epsilon);
        nuevo_edo_ini.setTransicion(afn2.EdoInicial, epsilon);        
                                                                
        
        //los viejos estados iniciales dejan de serlo
         this.EdoInicial.setEdoInicial(false);
         afn2.EdoInicial.setEdoInicial(false);
        
        //hacemos el nuevo estado como inicial
        nuevo_edo_ini.setEdoInicial(true);
        this.EdoInicial= nuevo_edo_ini;
        
        //Se crea un nuevo estado final
        Estado nuevo_edo_fin = new Estado();
        nuevo_edo_fin.setEdoFinal(true);
        //A los viejos estados del afn1 trancicion epsilon al nuevo estado final
        for(Estado e  : this.EdosAceptacion ){
            e.setTransicion(nuevo_edo_fin, epsilon);
            e.setEdoFinal(false);
        }
       
        //A los viejos estados del afn2 trancicion epsilos al nuevo estado final
        for(Estado e  : afn2.EdosAceptacion ){
            e.setTransicion(nuevo_edo_fin, epsilon);
            e.setEdoFinal(false);
        }
        
        this.EdosAceptacion.clear(); //Borramos los estados de aceptacion del afn1
        this.EdosAceptacion.add(nuevo_edo_fin);//Y se agrega el nuevo edo_fin
        afn2.EdosAceptacion.clear(); //borramos sus  estados de acpetacion del afn2   
        
        this.EstadosAFN.add(0, nuevo_edo_ini);//Se agrega al inicio
        this.EstadosAFN.addAll(afn2.EstadosAFN);//Se agregan los estados del afn2      
        this.EstadosAFN.add(nuevo_edo_fin);//Se agrega el nuevo fin

        //Se reasignan identificadores
        for( int i = 0; i< this.EstadosAFN.size(); i++ ){
            this.EstadosAFN.get(i).setIdentificador(i);
        }
    }
    
    public void concatenarAFN(AFN afn2){           
        //Unir Alfabetos
        afn2.Alfabeto.remove(this.Alfabeto);
        this.Alfabeto.addAll(afn2.Alfabeto);
        
        //Crear transicion epsilon de cada edo de aceptacion del Afn1
        //al edo_inicio del afn2
        for(Estado e: this.EdosAceptacion){
            e.setEdoFinal(false);
            e.setTransicion(afn2.getEstadoInicial(), epsilon);
        }        
        
        afn2.getEdoInicial().setEdoInicial(false);
        //Agregar estados del afn2 al afn1
        this.EstadosAFN.addAll(afn2.EstadosAFN);  
        
        //Reasignar identificadores
        for( int i = 0; i< this.EstadosAFN.size(); i++ ){
            this.EstadosAFN.get(i).setIdentificador(i);
        }
        
        this.EdosAceptacion.clear();
        this.EdosAceptacion.addAll(afn2.getEdosAceptacion());
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
    private ArrayList<Subconjunto> construirSubconjuntos(){
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
    
    public AFD convertirAFN(){
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
    
    public int getIdAFN() {
        return IdAFN;
    }

    public void setIdAFN(int IdAFN) {
        this.IdAFN = IdAFN;
    }
    
    Estado getEstado(int id){
        return EstadosAFN.get(id);
    }

    public ArrayList<Estado> getEstadosAFN() {
        return EstadosAFN;
    }

    public void setEstadosAFN(ArrayList<Estado> EstadosAFN) {
//        //remover estados repetidos agregar los demas
//       EstadosAFN.removeAll(this.EstadosAFN);
//        this.EstadosAFN.addAll( EstadosAFN);
//        
        this.EstadosAFN = EstadosAFN;
    }
    
    public void addEstado(Estado  edo) {
        if( ! this.EstadosAFN.contains(edo) ){ //agregarlo si no esta ya
              this.EstadosAFN.add(edo);
        }
        
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
    Estado getEstadoInicial(){
        return EdoInicial;
    }
    ArrayList<Estado> getEdosAceptacion(){
        return EdosAceptacion;
    } 
    public void setEdosAceptacion(ArrayList<Estado> EdosAceptacion) {
        this.EdosAceptacion = EdosAceptacion;
    }
}


