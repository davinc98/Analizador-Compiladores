package analizadorcompiladores;

public class AnalizadorLexico {
    
    private AFN tablaAutomata;
    private int  caracterActual=0;
    private int InicioLExema=0;
    private int finLexema=-1;
    private boolean ultimoEdoAcept=false;
    private int token=0;
    private int  edoActual=0;

    private String cadena2Analizar;
    
    
    public AnalizadorLexico(){ }
    
    //Comentario de prueba
    public AnalizadorLexico(AFN tabla, String cadena  ){
        this.setTablaAutomata(tabla);
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

    public AFN getTablaAutomata() {
        return tablaAutomata;
    }

    public void setTablaAutomata(AFN tablaAutomata) {
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

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getYyText() {
        return yyText;
    }

    public void setYyText(String yyText) {
        this.yyText = yyText;
    }
    
    
    
    //evalua un simbolo en un estado, regresando a que estado manda 
   
    private int EvaluarSimbolo(int edo, int  simbolo){
         //en el estado actual a que estado vamos con simbolo
         
        
        return 0;
    }
    
    
    private int getToken4Edo(int Edo){ //devuelve el token de un estado
    
        return 0;
    }
    
    
    public int yylex(){
       // si caracterActual  es el ultimo 
       if( this.getCaracterActual() == (this.getCadena2Analizar().length()+1 )  ){
           return -1;
       }
        

       while( this.getCaracterActual() != (this.getCadena2Analizar().length()+1 )   ){  //mientras no sea el fin de cadena
            int edoPosible =this.EvaluarSimbolo( this.getEdoActual(),  this.getCaracterActual()   );
            //si estado actual tiene transicion
           if( edoPosible >= 0  ){// mientras regrese un estado mayor a -1 hay transiciones
                this.setEdoActual( edoPosible  ); //configuramos el estado actual
                this.caracterActual++;//pasamos al proximo caracter

                  //si estado actual es de aceptacion
                  int tokenEdoAct = this.getToken4Edo( this.getEdoActual());
                if( tokenEdoAct  >0  ){ 
                    this.setUltimoEdoAcept(true); //guardamos que pasamos por un estado de aceptacion
                    this.setFinLexema( this.getEdoActual() ); //marcamos fin del lexema
                    this.setToken(  tokenEdoAct  ); // guardar el token del estado actual
                    // recordar tambien la accion asociada a ese estado (?)
                }
           
           }else{ //si no hay transicion  
               
                
                if(  this.isUltimoEdoAcept() ){
                   
                    // cual era la cadena en ese estado de aceptacion
                    String subCadena = this.getCadena2Analizar().substring(this.getInicioLExema(), this.getFinLexema());
                    this.setYyText(  subCadena   );
                    //acomodo el nuevo inicio para la siguiente llamada yylex
                    this.setCaracterActual( this.getFinLexema()+1  );
                    this.setInicioLExema( this.getCaracterActual()  );
                    this.setFinLexema(-1);
                    this.setUltimoEdoAcept(false);
                    return this.getToken();
                     //recuerdo el estaoo de aceptacion
                    //regresar token respectivo
                
                }else{//si no  pasamos por un estado de aceptacion
                    //descartar el lexema actual 
                    //estadoActual =0   
                    //error
                    return -1;
                }
               
               
           
           
           
           
           }
           
           
           
           
       
       }
       
        
        return -1; 
    }
    

}
