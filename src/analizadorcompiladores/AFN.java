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

public class AFN{  
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
        
        //Reemplazar esatdo final de afn1 por el inicial de afn2 
        for(Estado e: this.EdosAceptacion){
            e.setTransiciones(afn2.getEdoInicial().getTransiciones());
            e.setEdoFinal(false);
            afn2.getEstadosAFN().remove(afn2.getEdoInicial());
        }        
      
        //Agregar estados del afn2 al afn1
        this.EstadosAFN.addAll(afn2.EstadosAFN);  
        
        //Reasignar identificadores
        for( int i = 0; i< this.EstadosAFN.size(); i++ ){
            this.EstadosAFN.get(i).setIdentificador(i);
        }
        
        this.EdosAceptacion.clear();
        this.EdosAceptacion.addAll(afn2.getEdosAceptacion());
    }
    
    public void cerraduraTransitiva(){//+
        Estado nuevo_edo_ini = new Estado(); 
        nuevo_edo_ini.setEdoInicial(true);
        nuevo_edo_ini.setTransicion(EdoInicial, epsilon);
        
        Estado nuevo_edo_fin = new Estado(); 
        nuevo_edo_fin.setEdoFinal(true);
        
        EstadosAFN.add(0,nuevo_edo_ini);//Agregar el nuevo estado inicial en 0
                
        //Crear una transicion epsilon del estado final al inicial de AFN
        for(Estado e: EdosAceptacion){
 e.setTransicion(EdoInicial, epsilon);
            e.setTransicion(nuevo_edo_fin, epsilon);
            e.setEdoFinal(false);
        }
        EdoInicial.setEdoInicial(false);//Se asigna el nuevo estado inicial
        EdoInicial = nuevo_edo_ini;
        
        EdosAceptacion.clear();//Borrar los estados de aceptacion anteriores
        EdosAceptacion.add(nuevo_edo_fin);
        EstadosAFN.add(nuevo_edo_fin);  
        
        //Reasignacion de identificadores
        for(int i=0;i<EstadosAFN.size();i++){
            EstadosAFN.get(i).setIdentificador(i);
        }
        System.out.println("Todo OK Transitiva");
    }
    
    public void cerraduradeKleene(){//*
        Estado nuevo_edo_ini = new Estado(); 
        nuevo_edo_ini.setEdoInicial(true);
        nuevo_edo_ini.setTransicion(EdoInicial, epsilon);
        
        Estado nuevo_edo_fin = new Estado(); 
        nuevo_edo_fin.setEdoFinal(true);
        
        EstadosAFN.add(0,nuevo_edo_ini);//Agregar el nuevo estado inicoal en 0
                
        //Crear una transicion epsilon del estado final al inicial de AFN
        for(Estado e: EdosAceptacion){
 e.setTransicion(EdoInicial, epsilon);
            e.setTransicion(nuevo_edo_fin, epsilon);
            e.setEdoFinal(false);
        }
        EdoInicial.setEdoInicial(false);//Se asigna el nuevo estado inicial
        EdoInicial = nuevo_edo_ini;
        
        EdosAceptacion.clear();//Borrar los estados de aceptacion anteriores
        EdosAceptacion.add(nuevo_edo_fin);
        EstadosAFN.add(nuevo_edo_fin);  
        
        //Agregamos un transicion epsilon del nuevo edoIni al nuevo edoFin
        EdoInicial.setTransicion(EdosAceptacion.get(0), epsilon);
        
        //Reasignacion de identificadores
        for(int i=0;i<EstadosAFN.size();i++){
            EstadosAFN.get(i).setIdentificador(i);
        }
        System.out.println("Todo OK Kleene");
    }
    
    //OPERACIONES   
     ArrayList<Estado> cerraduraEpsilon(Estado e){        
        Stack<Estado> s = new Stack();
        ArrayList<Estado>  R= new ArrayList<Estado>();
        Estado p = new Estado();
        s.push(e);
        
        //System.out.println(s.size());
        while( !s.empty()){
            p= s.pop();
            R.add(p);
            for(Transicion trans: p.Transiciones){
                if( trans.getSimbolo() == epsilon  ){
                    // for para cada uno de los  estados destinos
                   for(Estado edoDestino:  trans.getEdosDestino()){
                        if(!R.contains(edoDestino))
                            s.push(edoDestino);
                   } 
                   
                }
            }
        }
        R = Ordenar(R);
        return R;
    }
    
     ArrayList <Estado> cerraduraEpsilon(ArrayList<Estado> c){
        ArrayList<Estado > R = new ArrayList<Estado>();
        for(Estado e:c){
            R.addAll( cerraduraEpsilon(e));
        }
        
        //Ordenar los estados por su Identificador
        R = Ordenar(R);
        return R;  
    }
    
    ArrayList<Estado> mover( Estado e, char c){
        ArrayList<Estado> R = new ArrayList<Estado>();
        for(Transicion t: e.Transiciones){
            if(t.getSimbolo()== c){
                R.addAll(t.getEdosDestino());   //Repeticiones de mismos estados
            }
        }    
        R = Ordenar(R);
        return R;
    }
    
    ArrayList<Estado> mover( ArrayList<Estado> E, char c){
        ArrayList<Estado> R = new ArrayList<Estado>();
        for(Estado e:E){
            R.addAll(mover(e, c));//Podrian existir repeticiones de mismos estados
        }
        
        //Ordenar los estados de forma Ascendente
        R = Ordenar(R);
        
        return R;
    }    
    
    //Analiza un conjunto de estados y devuelve los estados a los que se llega con c
    ArrayList<Estado> irA(ArrayList<Estado> E, char c){
         ArrayList<Estado> R = new ArrayList<Estado>();         
         R = cerraduraEpsilon(mover(E,c));
         
         //Eliminar Estados Duplicados en R
        int size = R.size();
        int duplicates = 0;
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
               if (R.get(j).getIdentificador() != R.get(i).getIdentificador())
                   continue;
               duplicates++;
               R.remove(j);
               j--;
               size--;
            }
        }
         
        //Ordenar los estados de forma Ascendente
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
    
    //Metodos
    public ArrayList<Subconjunto> construirSubconjuntos(){
        ArrayList<Subconjunto> R = new ArrayList<Subconjunto>();
        Subconjunto S = new Subconjunto();
        
        //Nuevo subconjunto S0
        S.setId(0);
        S.setMarca(false);//No se ha analizado
        S.agregarEstados(cerraduraEpsilon(EdoInicial));//Agregamos los estados  
                        
        R.add(S); //Agregar el primer Subconjunto  
        
        
        Boolean faltan = true;
        while(faltan){
            for(int i=0; i<R.size();i++){  
                Subconjunto Sn = R.get(i);
                if(Sn.getMarca() == false){//Si NO ha sido analizado
                    for(char c: Alfabeto){
                        
                        ArrayList<Estado> Cn = irA(Sn.getEstados(), c); 
                        
                        if(Cn.size()>0){//Revisar si no es el conjunto VACIO
                            
                            //Si R NO contiene a un Subconjunto con los mismos estados
                            if(!contiene(R, Cn)){                          
                                Subconjunto Snuevo = new Subconjunto();
                                Snuevo.setId(R.size());//ID igual al tamano del arreglo R de subconjuntos -1
                                Snuevo.agregarEstados(Cn);
                                Snuevo.setMarca(false);

                                R.add(Snuevo);

//                                System.out.println("");
//                                System.out.println("Subconjunto: "+Snuevo.getId());
//                                for(int l=0;l<Snuevo.getEstados().size();l++){                
//                                    System.out.println(" Estado:"+Snuevo.getEstados().get(l).getIdentificador());
//                                }

                            }
                        }
                        
                        
                    }
                    Sn.setMarca(true);//Se marca que ya se analizo.
                }            
            }  
            //Escanear el arreglo para verificar que todos los subconj ya se analizaron
            for(Subconjunto Sn: R){
                if(Sn.getMarca()==false)
                    faltan = true;
                else
                    faltan = false;
            }
        }
        return R;        
    }
    
    public AFD convertirAFN(){
        AFD afd = new AFD();
        ArrayList<Subconjunto> R = construirSubconjuntos();//Obtener subconjuntos. 
        
        //Agregar Alfabeto
        afd.setAlfabeto(Alfabeto);
                                   
        for(Subconjunto S: R){
            System.out.println("");
            System.out.println("Subconjunto: "+S.getId());
            for(int l=0;l<S.getEstados().size();l++){                
                System.out.println(" Estado:"+S.getEstados().get(l).getIdentificador());
            }
        }
        
        //Creamos una Estado por cada Subconjunto
        for(Subconjunto S: R){
            Estado E = new Estado();
            E.setIdentificador(afd.getEstados().size());
            
            //TOKEN
            int Token = TokenInterseccion(S.getEstados(), EdosAceptacion);            
            E.setToken(Token);
            
            afd.getEstados().add(E);
        }
        
        //Colocar ESTADO INICIAL
        afd.getEstados().get(0).setEdoInicial(true);
        afd.setEdoInicial(afd.getEstados().get(0));
        
        //Colocar TRANSICIONES a cada Estado
        for(int i=0; i<R.size();i++){
            ArrayList<Estado> Estados = new ArrayList<Estado>(); 
            for(char c: Alfabeto){
                 Estados = irA(R.get(i).getEstados(),c);
                 if(Estados.size()>0){//Revisar si es VACIO
                     
                     //Crear transicion por cada estado
                     for(Estado E: Estados){
                         afd.getEstados().get(i).setTransicion(E, c);
                     }
                }
            }
        }
        
        return afd;
    }
    
    
    //Verifica si hay algun estado del Conjunto S, en los Estados Finales
    //Y regresa el token de la primera interseccion
    int TokenInterseccion(ArrayList<Estado> S, ArrayList<Estado> F){
        
        for(int i=0; i<F.size();i++){//Recorrido sobre F
            for(int j=0;j<S.size();j++){
                if(F.get(i).getIdentificador()==S.get(j).getIdentificador()){
                    //Hay un estado como interseccion
                    return F.get(i).getToken();
                }
            }
        }
        return 0;
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


