package analizadorcompiladores;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;


public class Principal {
    

    private static ArrayList<AFN> listaAFN = new ArrayList();
    private static ArrayList<AFD> listaAFD = new ArrayList();
    
    private static ArrayList<AFN> listaAFNUnidos = new ArrayList();
    
    public static void main (String[] args){        
        int opcionMenu;
        Scanner leer = new Scanner(System.in);
        boolean salir=false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        while(salir==false){
            System.out.println("-------MENU-------");
            System.out.println("1. Crear AFN básico."); //In: un caracter, y ponerle ID (para cerradura, etc)
            System.out.println("2. Unir 2 AFN."); //In: 2do AFN, ID para AFN unión, Out: AFN unión
            System.out.println("3. Concatenar 2 AFN.");
            System.out.println("4. Cerradura Transitiva o de Kleene");
            System.out.println("5. Unión para Analizador Léxico."); //qué automata quiero unir (mostrar tabla con automatas)
            System.out.println("6. Convertir AFN a AFD."); //cualquier AFN, hasta el de la opcion 6. In: ID del AFN
            System.out.println("7. Validar cadena usando un AFN o un AFD."); //seleccionar AFN o AFD de los ya ingresados
            System.out.println("8. Opcional.");
            System.out.println("9. Salir");
            System.out.print("Selecciona el número de la opción deseada: ");
            opcionMenu = leer.nextInt();

            switch(opcionMenu){
                case 1: //crear AFN
                    System.out.print("Ingresa el caracter para la transición del AFN: ");
                    char c = leer.next().charAt(0);
                    System.out.println("C: "+c);
                    crearAutomataBasico(c);
                    break;
                case 2: //Unir AFN
                    System.out.print("Ingresa el identificador del primer automata: ");
                    System.out.print("Ingresa el identificador del segundo automata: ");
                    
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    
                    unirAutomatas(0, 1);           
                    //System.out.println(gson.toJson(listaAFN.get(1)));
                    System.out.println(gson.toJson(listaAFNUnidos.get(0).getEstadosAFN()));
                    break;
                    
                case 3: //concatenar AFN
                    System.out.print("Ingresa el identificador del primer automata: ");
                    System.out.print("Ingresa el identificador del segundo automata: ");
                    
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    concatenarAutomatas(0, 1);           
                    //System.out.println(gson.toJson(listaAFN.get(1)));
                    System.out.println(gson.toJson(listaAFNUnidos.get(0).getEstadosAFN()));
                    break;
                case 4: //Cerradura
                    break;
                case 5: //Analizador Léxico
                    break;
                case 6: //Convertir AFN a AFD
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    concatenarAutomatas(0, 1);  
                    
                    convertirAFNaAFD(0);
                    break;
                case 7: //Validar cadena
                    break;
                case 8: //Opcional                    
                    break;
                    
                 case 9: //copiar array list AFN  json en el portapapeles     
                   //  Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    // System.out.println(gson.toJson(listaAFN));


                      System.out.println( "Copiando json al portapapeles");


                      StringSelection ss = new StringSelection(gson.toJson(listaAFN));
                      Toolkit tool = Toolkit.getDefaultToolkit();
                      Clipboard clip = tool.getSystemClipboard();
                      clip.setContents(ss, null);
                     
                    break;
                     
                    
                default:
                    salir=true;
                    System.out.println("Hasta luego!");
                    break;
            }
        }
    }  
    
    private static void crearAutomataBasico(char c){
        AFN afn = new AFN();
        afn.crearBasico(c, listaAFN.size());    
        System.out.println("Automata basico con id:" +afn.getIdAFN()+" creado.");
        listaAFN.add(afn);
    }

    private static void unirAutomatas(int Id1, int Id2) {
        AFN aux1 = new AFN();
        AFN aux2 = new AFN();
        
        aux1 = listaAFN.get(Id1).Duplicar();
        aux2 = listaAFN.get(Id2).Duplicar();
            
        aux1.unirAFN(aux2);
        listaAFNUnidos.add(aux1);
    }

    private static void concatenarAutomatas(int Id1, int Id2) {
        AFN aux1 = new AFN();
        AFN aux2 = new AFN();
        
        aux1 = listaAFN.get(Id1).Duplicar();
        aux2 = listaAFN.get(Id2).Duplicar();
             
        aux1.concatenarAFN(aux2);
        listaAFNUnidos.add(aux1);
        
    }

    private static void convertirAFNaAFD(int Id) {
        AFN aux1 = new AFN();        
        aux1 = listaAFN.get(Id);
        aux1.convertirAFN();
    }

}
