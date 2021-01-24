package analizadores;

import clases.AFD;
import java.util.ArrayList;
import java.util.Stack;
import java.lang.Integer;

public class AnalizadorLexico {

    private ArrayList<ArrayList<Integer>> tablaAutomata; //Tabla del AFD
    private int posCaracterActual = 0;
    private int inicioLexema = 0;
    private int finLexema = -1;//Si no se ha pasado por edo_acep vale -1
    private boolean edoAceptPrevio = false;//Bandera que nos indica si ya se paso por un edo_acep
    private int token = 0;//Almacena el token del estado actual
    private int edoActual = 0;//Almacena el contador del caracter actual 
    private Stack<Integer> pila = new Stack();

    private String cadenaAnalizar;
    private String yyText;//Subcadena entre inicioLexema y finLexema
    
    
    //VARIABLES RESPALDO PARA GUARDAR ESTADO DEL ANALIZADOR
    private int posCaracterActualResp = 0;
    private int inicioLexemaResp = 0;
    private int finLexemaResp = -1;
    private boolean edoAceptPrevioResp = false;
    private int tokenResp = 0;
    private int edoActualResp = 0;
    private Stack<Integer> pilaResp = new Stack();
    private String yyTextResp;
    

    public AnalizadorLexico() {
    }
    
    
    public void guardaEstadoActual(){
        posCaracterActualResp = posCaracterActual;
        inicioLexemaResp = inicioLexema;
        finLexemaResp = finLexema;
        edoAceptPrevioResp = edoAceptPrevio;
        tokenResp = token;
        edoActualResp = edoActual;
        
        //Respaldar Pila
        int c = pila.size();
        Stack<Integer> aux = new Stack();
        while(c != 0){
            Integer t = pila.pop();
            pilaResp.add(t);
            aux.add(t);
            c = pila.size();
        }
        //Volver a agreagar valores a la pila
        c = aux.size();
        while(c != 0){
            Integer t = aux.pop();
            pila.add(t);
            c = aux.size();
        }
        yyTextResp = yyText;
    }
    
    public void recuperaEstadoGuardado(){
        posCaracterActual = posCaracterActualResp;
        inicioLexema = inicioLexemaResp;
        finLexema = finLexemaResp;
        edoAceptPrevio = edoAceptPrevioResp;
        token = tokenResp;
        edoActual = edoActualResp;
        
        //Vaciar pila
        int c = pila.size();
        while(c != 0){
            Integer t = pila.pop();
            c = pila.size();
        }

        c = pilaResp.size();
        while(c != 0){
            Integer t = pilaResp.pop();
            pila.add(t);
            c = pilaResp.size();
        }
        
        yyText = yyTextResp;
    }
    
    
    //Recibe la tabla del automata y la cadena a analizar
    public AnalizadorLexico(ArrayList<ArrayList<Integer>> tablaAutomata, String cadena) {
        //Se recupera la la tabla de transiciones  del automata
        this.setTablaAutomata(tablaAutomata);
        this.setCadenaAnalizar(cadena);
        
        System.out.println("\n[AnalizadorLexico] Cadena Recibida: "+cadena+"\n");
    }

    //Al crear el analizador recibe un AFD y la cadena que se analizara
    public AnalizadorLexico(AFD automata, String cadena) {
        //Se recupera la la tabla de transiciones  del automata
        this.setTablaAutomata(automata.getTablaAFD());
        this.setCadenaAnalizar(cadena);
        
        System.out.println("Cadena Recibida: "+cadena+"\n");
    }

    //Devuelve al Estado(int) que se manda en un estado con una posicion del caracter   d(edo, sym) 
    public int EvaluarSimbolo(int edo, int posicion) {
        //en el estado actual a que estado vamos con posicion

        //Recuperar el caracter del string a analizar en la posicion  indicada
        char caracterAnalizar = this.getCadenaAnalizar().charAt(posicion);

        //System.out.println("El caracter a analizar es : "+ caracterAnalizar );
        int posSimbolo = -1; //posiSion del SImbolo en el SAlfabeto

        //Recuperar el alfabeto del la tabla del Automata
        ArrayList<Integer> Alfabeto = this.getTablaAutomata().get(0);

        // System.out.println("Alfabeto  es : "+ Alfabeto.toString() );
        // System.out.println("Tamaño de alfabeto  es : "+ Alfabeto.size() );         
        for (int i = 0; i < Alfabeto.size(); i++) { // recorrer el alfabeto
            //el caracter en ela posicion i del alfabeto
            char symActual = Character.toChars(Alfabeto.get(i))[0];
            //  System.out.println("Simbolo actual es : "+ symActual );
            if (symActual == caracterAnalizar) {
                posSimbolo = i;
                break;
            }
        }

        // System.out.println("La posicion del posicion  es : "+ posSimbolo );
        if (posSimbolo >= 0) { // si se encontreo el posicion en el alfabeto
            ArrayList<Integer> Si = this.getTablaAutomata().get(edo + 1); // del Si solicitado 
            return Si.get(posSimbolo);
        }
        return -1;
    }

    public int getToken4Edo(int edo) { //devuelve el token de un estado
        ArrayList<Integer> Si = this.getTablaAutomata().get(edo + 1); // del Si solicitado 
        return Si.get(Si.size() - 1);  // devolver el ulrimo elemento de si (token)
    }

    //regresa a una lllamada previa del yylex y setea los apuntadores para ponerlos y regresen para llmar nuevamente yylex
    //regresa en la posicion donde se quedo, si no hay posicion previa regresa -1
    public int regresarToken() {
        if (!pila.isEmpty()) {

            int indiceAct = pila.pop();

            this.setPosCaracterActual(indiceAct);
            this.setInicioLexema(this.getPosCaracterActual());
            this.setFinLexema(-1);
            this.setEdoActual(0); //configuramos el estado actual
            this.setEdoAceptPrevio(false);

            return indiceAct;
        } else {
            System.out.println("No hay mas elementos en la pila\n\n");
            return -1;
        }
    }

    //El analizador lexico continuara con el analisis aun con los errores
    //Regresara los tokens conforme se <<bayan>> pidiendo.
    public int yylex() {

        //      si posCaracterActual  es el ultimo 
        if (this.getPosCaracterActual() >= this.getCadenaAnalizar().length()) {
            //System.out.println("fin analisis");
            return -10; //-10 es el token para FIN
        }

        pila.add(this.getPosCaracterActual());  //agregamos la posicion a la pila

        while (this.getPosCaracterActual() < (this.getCadenaAnalizar().length())) {  //mientras no sea el fin de cadena

            //Recuperar el estado_posible con una transicion del estado actual con el caracter actual
            int edoPosible = this.EvaluarSimbolo(this.getEdoActual(), this.getPosCaracterActual());
            //  System.out.println("Estado actual "+this.getEdoActual()+"  "+this.getPosCaracterActual() );

            //si estado actual tiene transicion con carcater actual
            if (edoPosible >= 0) {

                //si estado actual es de aceptacion
                int tokenEdoAct = this.getToken4Edo(edoPosible);

                //  System.out.println("Estado actual "+edoPosible+" token " +tokenEdoAct );
                if (tokenEdoAct > 0) {
                    this.setEdoAceptPrevio(true); //guardamos que pasamos por un estado de aceptacion
                    this.setFinLexema(this.getPosCaracterActual()); //marcamos fin del lexema
                    this.setToken(tokenEdoAct); // guardar el token del estado actual
                    
                    // recordar tambien la accion asociada a ese estado (?)
                }

                //  System.out.println("Lexema  Inicio  "+this.getInicioLexema()+" fin "+this.getFinLexema()+ " Caracter Actual "+this.getPosCaracterActual() );
                this.setEdoActual(edoPosible); //configuramos el estado actual
                this.posCaracterActual++;//pasamos al proximo caracter

            } else { //si no hay transicion  

                //System.out.println("Estado actual "+edoPosible+" no hay tranciciones" );
                if (this.isEdoAceptPrevio()) {//Si ya se paso por un edo_acep

                    // System.out.println("hubo transicion");
                    // cual era la cadena en ese estado de aceptacion
                    String subCadena = this.getCadenaAnalizar().substring(this.getInicioLexema(), this.getFinLexema() + 1);
                    //  System.out.println("Lexema  Inicio  "+this.getInicioLexema()+" fin "+this.getFinLexema()+ " Subcadena "+subCadena);
                    
                    this.setYyText(subCadena);
                    
                    //acomodo el nuevo inicio para la siguiente llamada yylex
                    this.setPosCaracterActual(this.getFinLexema() + 1);//Movemos al siguiente
                    this.setInicioLexema(this.getPosCaracterActual());
                    this.setFinLexema(-1);
                    this.setEdoActual(0); //configuramos el estado actual
                    this.setEdoAceptPrevio(false);
                    //recuerdo el estado de aceptacion
                    
                    //regresar token respectivo
                    return this.getToken();

                } else {//si no  pasamos por un estado de aceptacion
                    //descartar el lexema actual 
                    //estadoActual =0   
                    //error

                    System.out.println("ERROR encontrado. [Posicion "+inicioLexema+"]");
                    
                    this.setPosCaracterActual(this.getInicioLexema() + 1);
                    this.setInicioLexema(this.getPosCaracterActual());
                    this.setFinLexema(-1);
                    this.setEdoActual(0); //configuramos el estado actual
                    this.setEdoAceptPrevio(false);
                    
                    return -1;//VALOR DEL ERROR ES -1
                    //System.out.println("Ignorando error. [Continua en posicion "+posCaracterActual+"]\n");
                }
            }
        }

        //termino la cadena con un estado de aceptacion
        if (this.isEdoAceptPrevio()) {
            // cual era la cadena en ese estado de aceptacion
            String subCadena = this.getCadenaAnalizar().substring(this.getInicioLexema(), this.getFinLexema() + 1);
            this.setYyText(subCadena);
            return this.getToken();
        }

        return -1;
    }
    
    //Inicializa todos los valores para iniciar analisis desde el principio
    public void Reset() {
        posCaracterActual = 0;
        inicioLexema = 0;
        finLexema = -1;
        edoAceptPrevio = false;
        token = 0;
        edoActual = 0;
        pila.removeAllElements();
    }

    public int getEdoActual() {
        return edoActual;
    }

    public void setEdoActual(int edoActual) {
        this.edoActual = edoActual;
    }

    public String getCadenaAnalizar() {
        return cadenaAnalizar;
    }

    public void setCadenaAnalizar(String cadena2Analizar) {
        this.cadenaAnalizar = cadena2Analizar;
    }

    public ArrayList<ArrayList<Integer>> getTablaAutomata() {
        return tablaAutomata;
    }

    public void setTablaAutomata(ArrayList<ArrayList<Integer>> tablaAutomata) {
        this.tablaAutomata = tablaAutomata;
    }

    public int getPosCaracterActual() {
        return posCaracterActual;
    }

    public void setPosCaracterActual(int caracterActual) {
        this.posCaracterActual = caracterActual;
    }

    public int getInicioLexema() {
        return inicioLexema;
    }

    public void setInicioLexema(int InicioLExema) {
        this.inicioLexema = InicioLExema;
    }

    public int getFinLexema() {
        return finLexema;
    }

    public void setFinLexema(int finLexema) {
        this.finLexema = finLexema;
    }

    public boolean isEdoAceptPrevio() {
        return edoAceptPrevio;
    }

    public void setEdoAceptPrevio(boolean ultimoEdoAcept) {
        this.edoAceptPrevio = ultimoEdoAcept;
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

}
