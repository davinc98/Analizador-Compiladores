package analizadorcompiladores;

import java.util.ArrayList;
import java.util.Stack;

public class AnalizadorLexico {
    
    private  ArrayList<ArrayList<Integer>>  tablaAutomata;
    private int  caracterActual=0;
    private int InicioLExema=0;
    private int finLexema=-1;
    private boolean ultimoEdoAcept=false;
    private int token=0;
    private int  edoActual=0;
    private Stack<Integer> pila = new Stack();

    private String cadena2Analizar;
    
    
    public AnalizadorLexico(){ }
    
    //Comentario de prueba
    public AnalizadorLexico(AFD automata, String cadena  ){
        this.setTablaAutomata( automata.getArrayTabla() ); //obtiene la tabla de transiciones  del automata
        this.setCadena2Analizar(cadena);
 
    }
    
    
     public int getEdoActual() {
        return edoActual;
    }

    public void setEdoActual(int edoActual) {
        this.edoActual = edoActual;
    }
    
    

    public String getCadena2Analizar() {
        return cadena2Analizar;
    }

    public void setCadena2Analizar(String cadena2Analizar) {
        this.cadena2Analizar = cadena2Analizar;
    }
    private String yyText;

    public ArrayList<ArrayList<Integer>> getTablaAutomata() {
        return tablaAutomata;
    }

    public void setTablaAutomata(ArrayList<ArrayList<Integer>> tablaAutomata) {
        this.tablaAutomata = tablaAutomata;
    }

    public int getCaracterActual() {
        return caracterActual;
    }

    public void setCaracterActual(int caracterActual) {
        this.caracterActual = caracterActual;
    }

    public int getInicioLExema() {
        return InicioLExema;
    }

    public void setInicioLExema(int InicioLExema) {
        this.InicioLExema = InicioLExema;
    }

    public int getFinLexema() {
        return finLexema;
    }

    public void setFinLexema(int finLexema) {
        this.finLexema = finLexema;
    }

    public boolean isUltimoEdoAcept() {
        return ultimoEdoAcept;
    }

    public void setUltimoEdoAcept(boolean ultimoEdoAcept) {
        this.ultimoEdoAcept = ultimoEdoAcept;
    }

    private int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getYyText() {
        return yyText;
    }

    private void setYyText(String yyText) {
        this.yyText = yyText;
    }
    
    
    
    //devuelve al estado qeu se manda en un estado con un simbolo   d(edo, sym) 
   
    public int EvaluarSimbolo(int edo, int  simbolo){
         //en el estado actual a que estado vamos con simbolo
         
         char caracterAnalizar =  this.getCadena2Analizar().charAt(simbolo); //el caracter del string a analizar en la posicion  indicada
        
         //System.out.println("El caracter a analizar es : "+ caracterAnalizar );
         
         int posSimbolo = -1; //posiSion del SImbolo en el SAlfabeto
         ArrayList<Integer> Alfabeto = this.getTablaAutomata().get( 0  ); //se pone el alfabeto en el primer elemento
         
         // System.out.println("Alfabeto  es : "+ Alfabeto.toString() );
        // System.out.println("Tamaño de alfabeto  es : "+ Alfabeto.size() );
         
         
         for(int i=0;i< Alfabeto.size() ; i++ ){ // recorrer el alfabeto
             char symActual =  Character.toChars(Alfabeto.get(i)  )[0]; //el caracter en ela posicion i del alfabeto
           //  System.out.println("Simbolo actual es : "+ symActual );
             
             if(symActual ==caracterAnalizar ){ 
                 posSimbolo =i;
                 break;
             }
         }
         
         
        // System.out.println("La posicion del simbolo  es : "+ posSimbolo );
         
         
         
        if(posSimbolo>=0){ // si se encontreo el simbolo en el alfabeto
          ArrayList<Integer> Si = this.getTablaAutomata().get( edo+1  ); // del Si solicitado 
          
          return Si.get( posSimbolo );
         
        } 
         
        return -1;
    }
    
    
    public int getToken4Edo(int edo){ //devuelve el token de un estado
         ArrayList<Integer> Si = this.getTablaAutomata().get( edo+1  ); // del Si solicitado 
        return Si.get(Si.size()-1);  // devolver el ulrimo elemento de si (token)
    }
    
    //regresa a una lllamada previa del yylex y setea los apuntadores para ponerlos y regresen para llmar nuevamente yylex
    //regresa en la posicion donde se quedo, si no hay posicion previa regresa -1
    public int regresarToken(){
        if(!pila.isEmpty()){
        
            int indiceAct = pila.pop();

            this.setCaracterActual( indiceAct  );
            this.setInicioLExema( this.getCaracterActual()  );
            this.setFinLexema(-1);
            this.setEdoActual( 0  ); //configuramos el estado actual
            this.setUltimoEdoAcept(false);


            return indiceAct;
        }else{
            System.out.println("no hay mas elementos en la pila");
            return -1;
        }
    }
    
    public int yylex(){
  //      si caracterActual  es el ultimo 
       if( this.getCaracterActual() >= this.getCadena2Analizar().length()    ){
           System.out.println("fin analisis");
           return -1;
       }
       
       
       pila.add(this.getCaracterActual());  //agregamos la posicion a la pila
 
       while( this.getCaracterActual() < (this.getCadena2Analizar().length() )   ){  //mientras no sea el fin de cadena
            int edoPosible =this.EvaluarSimbolo( this.getEdoActual(),  this.getCaracterActual()   );
           //  System.out.println("Estado actual "+this.getEdoActual()+"  "+this.getCaracterActual() );
            
            //si estado actual tiene transicion
           if( edoPosible >= 0  ){// mientras regrese un estado mayor a -1 hay transiciones
                
                  //si estado actual es de aceptacion
                  int tokenEdoAct = this.getToken4Edo( edoPosible);
                  
                 //  System.out.println("Estado actual "+edoPosible+" token " +tokenEdoAct );
                if( tokenEdoAct  >0  ){ 
                    this.setUltimoEdoAcept(true); //guardamos que pasamos por un estado de aceptacion
                    this.setFinLexema( this.getCaracterActual()  ); //marcamos fin del lexema
                    this.setToken(  tokenEdoAct  ); // guardar el token del estado actual
                    // recordar tambien la accion asociada a ese estado (?)
                }
                
               //  System.out.println("Lexema  Inicio  "+this.getInicioLExema()+" fin "+this.getFinLexema()+ " Caracter Actual "+this.getCaracterActual() );
                 
                 this.setEdoActual( edoPosible  ); //configuramos el estado actual
                this.caracterActual++;//pasamos al proximo caracter

           }else{ //si no hay transicion  
               
                 //System.out.println("Estado actual "+edoPosible+" no hay tranciciones" );
                if(  this.isUltimoEdoAcept() ){
                    
                   // System.out.println("hubo transicion");
                   
                    // cual era la cadena en ese estado de aceptacion
                    String subCadena = this.getCadena2Analizar().substring(this.getInicioLExema(), this.getFinLexema()+1);
                   //  System.out.println("Lexema  Inicio  "+this.getInicioLExema()+" fin "+this.getFinLexema()+ " Subcadena "+subCadena);
                    this.setYyText(  subCadena   );
                    //acomodo el nuevo inicio para la siguiente llamada yylex
                    this.setCaracterActual( this.getFinLexema()+1  );
                    this.setInicioLExema( this.getCaracterActual()  );
                    this.setFinLexema(-1);
                     this.setEdoActual( 0  ); //configuramos el estado actual
                    this.setUltimoEdoAcept(false);
                    //recuerdo el estaoo de aceptacion
                    //regresar token respectivo
                    return this.getToken();
                     
                
                }else{//si no  pasamos por un estado de aceptacion
                    //descartar el lexema actual 
                    //estadoActual =0   
                    //error
                    
                    
                    this.setCaracterActual( this.getInicioLExema()+1  );
                    this.setInicioLExema( this.getCaracterActual()  );
                    this.setFinLexema(-1);
                    this.setEdoActual( 0  ); //configuramos el estado actual
                    this.setUltimoEdoAcept(false);
                    return -1;
                }                      
           }                                        
       }
       
       
       
      //termino la cadena con un estado de aceptacion
          if(  this.isUltimoEdoAcept() ){                   
                    // cual era la cadena en ese estado de aceptacion
                    String subCadena = this.getCadena2Analizar().substring(this.getInicioLExema(), this.getFinLexema()+1);
                    this.setYyText(  subCadena   );
                   return this.getToken();
        }
       
        
        return -1; 
    }
    

}
