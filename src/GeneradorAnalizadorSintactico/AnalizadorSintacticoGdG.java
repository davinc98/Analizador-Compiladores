/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneradorAnalizadorSintactico;

import GeneradorAnalizadorLexico.MainGeneradorAnalizadorLexico;
import analizadores.AnalizadorLexico;
import clases.AFD;
import clases.AFN;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author J.PEREZ
 */

/*
        DESCENSO RECURSIVO PARA LA GRAMATICA DE GRAMATICAS
        
        G -> Reglas
        Reglas -> Reglas Regla; | Regla;
        Regla -> LadoIzq FLECLA LadosDerechos
        LadoIzq -> SIMBOLO
        LadosDerechos -> LadosDerechos OR LadoDerecho
        LadoDerecho -> ListaSimbolos
        ListaSimbolos -> ListaSimbolos SIMBOLO | SIMBOLO
        
        Eliminando la recursion por la izquierda:
        
        G -> Reglas
        Reglas -> Regla; | Reglas'
        Reglas' -> Regla; Reglas' | Epsilon
        Regla -> LadoIzq FLECHA LadosDerechos
        LadoIzq -> SIMBOLO
        LadosDerechos -> LadoDerecho LadosDerechos'
        LadosDerechos' -> OR LadoDerecho LadosDerechos' | Epsilon
        LadoDerecho -> ListaSimbolos
        ListaSimbolos -> SIMBOLO ListaSimbolos'
        ListaSimbolos' -> SIMBOLO ListaSimbolos' | Epsilon
    
        El proposito del programa es que si se tiene una entrada como:
            E -> TE';
            E' -> +TE' | -TE' | Epsilon;
            T -> FT';
            T' -> *FT' | /FT' | Epsilon;
            F -> (E) | num;
        Deberiamos obtener al final un arreglo de reglas, con apuntadores nodos. 
     */
public class AnalizadorSintacticoGdG {

    
    
    //CLASES LEXICAS PARA ESTE ANALIZADOR
    public final static int PUNTOYCOMA = 10;
    public final static int FLECHA = 20;
    public final static int OR = 30;
    public final static int SIMBOLO = 40;
    public final static int GUION = 50;

    public static int numReglas = 0;
    public static ArrayList<String> VN = new ArrayList(); //Arreglo de simbolos NO TERMINALES
    public static ArrayList<Regla> ArrReglas = new ArrayList();

    //Corregir analizador
    public static AnalizadorLexico lexic;
    ArrayList<ArrayList<Integer>> tablageneradorAFD;

    public AnalizadorSintacticoGdG() throws FileNotFoundException, IOException {
       
        //Creacion del AFN para Gramatica de Gramaticas
        MainGeneradorAnalizadorLexico generador = new MainGeneradorAnalizadorLexico();
        AFN afn = generador.generarAFN("ClasesLexicasGdG.txt");
        AFD afd = afn.convertirAFN();
        tablageneradorAFD = afd.getTablaAFD();
        
        System.out.println("CONTENIDO DE LA TABLA AFD");
        for (int i = 0; i < tablageneradorAFD.size(); i++) {
            //
            for (int j = 0; j < tablageneradorAFD.get(i).size(); j++) {
                System.out.print(" " + tablageneradorAFD.get(i).get(j));
            }
            System.out.println("");
        }
    }
    
    public ArrayList<Regla> generarListaDeReglas(String nombreFichero) throws FileNotFoundException, IOException{
        
        //String nombreFichero = "ExpresionesRegularesParaGdG.txt";
        BufferedReader br = null;
        br = new BufferedReader(new FileReader(nombreFichero));
        String linea = br.readLine();
        while (linea != null) {
            //System.out.println("Cadena entrada: "+linea);
            lexic = new AnalizadorLexico(tablageneradorAFD, linea);
            G();
            System.out.println(linea);
            linea = br.readLine();
        }

        //Recorrer Tabla y  Asignar Simbolos Terminales
        System.out.println("\n\nNumReglas: " + numReglas);
        for (Regla r : ArrReglas) {
            System.out.print("\nRegla: [" + r.getStrSimb() + "] -> ");

            Nodo sig = r.getApNodo();
            while (sig != null) {
                if (sig != null && sig.getStrsimb()!="") {
                    
                    for(Regla reg: ArrReglas){
                        if(sig.getStrsimb().equals(reg.getStrSimb()))
                            sig.setTerminal(false);
                    }
                    if(sig.isTerminal())
                        System.out.print(":");
                    System.out.print("[" + sig.getStrsimb() + "] -> ");
                }else{
                    System.out.print("NULL");
                }
                sig = sig.getApNodo();
            }
        }
        System.out.println("\nFin asignacion de simbolos no terminales.");
        return ArrReglas;
    }

    public static boolean G() {
        if (Reglas()) {
            return true;
        }
        return false;
    }

    public static boolean Reglas() {
        int t;
        if (Regla()) {
            t = lexic.yylex();
            if (t == PUNTOYCOMA) {
                System.out.println("\t\tPUNTO Y COMA");
                if (ReglasP()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean ReglasP() {
        lexic.guardaEstadoActual();
        int t;
        if (Regla()) {
            t = lexic.yylex();
            if (t == PUNTOYCOMA) {
                System.out.println("\t\tPUNTO Y COMA");
                if (ReglasP()) {
                    return true;
                }
                return false;
            }
        }
        lexic.recuperaEstadoGuardado();
        return true;
    }

    private static boolean Regla() {
        int t;
        SimbCadena SimbLadoIzq = new SimbCadena();
        if (LadoIzq(SimbLadoIzq)) {
            //System.out.println("\t\tSIMBOLO ENCONTRADO ASIGNADO: " + SimbLadoIzq.getSimb());
            t = lexic.yylex();
            if (t == FLECHA) {
                System.out.println("\t\tFLECHA");
                if (LadosDerechos(SimbLadoIzq)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean LadoIzq(SimbCadena S) {
        int t = lexic.yylex();
        if (t == SIMBOLO) {
            S.setSimb(lexic.getYyText());
            System.out.println("\t\tSIMBOLO IZQ ENCONTRADO: " + S.getSimb());
            return true;
        }
        return false;
    }

    private static boolean LadosDerechos(SimbCadena SimbLadoIzq) {
        Nodo nodo = new Nodo();
        if (LadoDerecho(nodo)) {
            //System.out.println("\t\tSIMBOLO AGREGADO A VN: " + nodo.getStrsimb());
            VN.add(SimbLadoIzq.getSimb());

            Regla r = new Regla();
            r.setStrSimb(SimbLadoIzq.getSimb());
            r.setApNodo(nodo);
            ArrReglas.add(r);
            numReglas++;

            System.out.println("\t\t\tREGLA AGREGADA: " + r.getApNodo().getStrsimb());

            if (LadosDerechosP(SimbLadoIzq)) {
                return true;
            }
        }
        return false;
    }

    private static boolean LadosDerechosP(SimbCadena S) {
        int t = lexic.yylex();
        Nodo nodo = new Nodo();
        if (t == OR) {
            System.out.println("\t\tOR");
            if (LadoDerecho(nodo)) {
                System.out.println("\t\t\tNODO RECUPERADO: " + nodo.getStrsimb());

                Regla r = new Regla();
                r.setStrSimb(S.getSimb());
                r.setApNodo(nodo);
                ArrReglas.add(r);
                numReglas++;

                if (LadosDerechosP(S)) {
                    return true;
                }
            }
            return false;
        }
        lexic.regresarToken();
        return true;
    }

    private static boolean LadoDerecho(Nodo nodoSimb) {
        Nodo nodoaux = new Nodo();
        int t = lexic.yylex();
        if (t == GUION) {
            t = lexic.yylex();
            //System.out.println("\t\tGUION ENCONTRADO");
            if (t == SIMBOLO) {
                String s = lexic.getYyText();
                //System.out.println("\t\tSIMBOLO ENCONTRADO: " + s);
                nodoSimb.setStrsimb(s);
                System.out.println("\t\tNODO CREADO==>: " + nodoSimb.getStrsimb());
                if (ListaSimbolosP(nodoaux)) {
                    nodoSimb.setApNodo(nodoaux);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private static boolean ListaSimbolosP(Nodo nodoSimb) {
        Nodo nodoaux = new Nodo();
        int t = lexic.yylex();
        if (t == GUION) {
            t = lexic.yylex();
            //System.out.println("\t\tGUION ENCONTRADO");
            if (t == SIMBOLO) {
                String s = lexic.getYyText();
                System.out.println("\t\tSIMBOLO ENCONTRADO: " + s);
                nodoSimb.setStrsimb(s);
                System.out.println("\t\tNODO CREADO==>: " + nodoSimb.getStrsimb());
                if (ListaSimbolosP(nodoaux)) {
                    nodoSimb.setApNodo(nodoaux);
                    return true;
                }
                return false;
            }
            //return false;
        }
        nodoSimb = null;
        lexic.regresarToken();
        return true;
    }
}
