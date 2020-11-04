/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorcompiladores;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author J.PEREZ
 */
public class AFN {    
    //ATRIBUTOS
    private Estado EdoInicial;
    private ArrayList<Character> Alfabeto;
    private ArrayList<Estado> EdosAceptacion;     
    private ArrayList<Estado> EstadosAFN;
    private int IdAFN;
    
    public AFN(){
        this.Alfabeto = new ArrayList();
        this.EdosAceptacion = new ArrayList();
        this.EstadosAFN = new ArrayList();
    }
    
    //constantes'ϵ'ñ
    static final char epsilon='ϵ';
    
    //METODOS 
    public void crearBasico(char c, int Id){
        this.IdAFN = Id;
        Alfabeto.add(c);
        Estado estado_ini = new Estado();
        estado_ini.setIdentificador(0);
        estado_ini.setEdoInicial(true);
        
        Estado estado_fin = new Estado();
        estado_fin.setIdentificador(1);
        estado_fin.setEdoFinal(true);
        
        estado_ini.setTransicion(estado_fin, c);
        
        this.EstadosAFN.add(estado_ini);
        this.EstadosAFN.add(estado_fin);
        this.EdosAceptacion.add(estado_fin);
    }
    
    public void unirAFN(AFN afn2){
        //creamos los nuevos estados iniciale sy finales
        Estado nuevo_edo_ini = new Estado();
        Estado nuevo_edo_fin = new Estado();
        
       this.EstadosAFN.add(nuevo_edo_ini);
       this.EstadosAFN.add(nuevo_edo_fin);
        //concatenar alfabetos
        afn2.Alfabeto.removeAll(this.Alfabeto);
        this.Alfabeto.addAll( afn2.Alfabeto);
        
        // el nuevo estado inicial apuntamos a los viejos estados finales
        nuevo_edo_ini.setTransicion(this.EdoInicial, epsilon);
        nuevo_edo_ini.setTransicion(afn2.EdoInicial, epsilon);
        
        //los viejos estados iniciales dejan de serlo 
        this.EdoInicial.setEdoInicial(false);
        afn2.EdoInicial.setEdoInicial(false);
        
        
        //hacemos el nuevo estado como inicial
        this.EdoInicial= nuevo_edo_ini;
        this.EdoInicial.setEdoInicial(true);
       
       // alos viejos estados creamso trancicion epsilos al nuevo estado final en este Automata
       for(Estado e  : this.EdosAceptacion ){
           e.setTransicion(nuevo_edo_fin, epsilon);
           e.setEdoFinal(false);
       }
       
       this.EdosAceptacion.clear(); // borramos sus estados de aceptacion
       
       // alos viejos estados creamso trancicion epsilos al nuevo estado final en AFN@ Automata
        for(Estado e  : afn2.EdosAceptacion ){
           e.setTransicion(nuevo_edo_fin, epsilon);
           e.setEdoFinal(false);
  
       }
       
       afn2.EdosAceptacion.clear(); // borramos sus  estados de acpetacion
       
       this.EstadosAFN.addAll(afn2.EstadosAFN);       
       
       //lo agregamos a la lista de aceptacion y lo seteamos como  final(clase de estado)
       this.EdosAceptacion.add(nuevo_edo_fin);
       nuevo_edo_fin.setEdoFinal(true);
       
       for( int i = 0; i< this.EstadosAFN.size(); i++ ){
           this.EstadosAFN.get(i).setIdentificador(i);
       }
       
       
       
       
       
       
       
        
        
        
        
        
        //this.EdosAceptacion.clear(); //borrar estados de aceptación? 
        //nuevo_edo_ini.setTransicion(new, epsilon);
        //AFN afn = new AFN();
       // return afn;
    }
    
    public AFN concatenarAFN(AFN afn2){     
        AFN nuevo = new AFN();
        nuevo = this;
        
        //Unir Alfabetos
        afn2.Alfabeto.remove(nuevo.Alfabeto);
        nuevo.Alfabeto.addAll(afn2.Alfabeto);
        
        //Crear transicion epsilon de cada edo de aceptacion del Afn1
        for(Estado e: nuevo.EdosAceptacion){
            e.setEdoFinal(false);
            e.setTransicion(afn2.getEstadoInicial(), epsilon);
        }
        afn2.getEstadoInicial().setEdoInicial(false);
        
        
        //Agregar estados del afn2 al afn nuevo
        nuevo.EstadosAFN.addAll(afn2.EstadosAFN);  
        
        //Susituir IDs
        int i =0;
        for(Estado e: nuevo.EstadosAFN){
            e.setIdentificador(i);
            i++;
        }
        
        nuevo.EdosAceptacion.clear();
        nuevo.EdosAceptacion.addAll(afn2.getEdosAceptacion());
        
        return nuevo;
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
    
    public int getIdAFN() {
        return IdAFN;
    }

    public void setIdAFN(int IdAFN) {
        this.IdAFN = IdAFN;
    }
    
    Estado getEstado(int id){
        return EstadosAFN.get(id);
    }
    
    Estado getEstadoInicial(){
        return EdoInicial;
    }
    
    ArrayList<Estado> getEdosAceptacion(){
        return EdosAceptacion;
    } 
    
}


